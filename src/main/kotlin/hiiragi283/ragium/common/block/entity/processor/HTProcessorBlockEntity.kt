package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeFinder
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.recipe.manager.HTFinderRecipeCache
import hiiragi283.ragium.common.storage.energy.battery.HTMachineEnergyBattery
import hiiragi283.ragium.common.storage.holder.HTBasicEnergyBatteryHolder
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

/**
 * レシピの処理を行う機械に使用される[HTMachineBlockEntity.Energized]の拡張クラス
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 */
abstract class HTProcessorBlockEntity<INPUT : Any, RECIPE : Any>(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity.Energized(blockHolder, pos, state) {
    final override fun createBattery(builder: HTBasicEnergyBatteryHolder.Builder, listener: HTContentListener): HTMachineEnergyBattery<*> =
        builder.addSlot(HTSlotInfo.INPUT, HTMachineEnergyBattery.input(listener, this))

    //    Ticking    //

    protected var requiredEnergy: Int = 0
    protected var usedEnergy: Int = 0

    val progress: Float
        get() {
            val totalTick: Int = usedEnergy
            val maxTicks: Int = requiredEnergy
            if (maxTicks <= 0) return 0f
            val fixedTotalTicks: Int = totalTick % maxTicks
            return Mth.clamp(fixedTotalTicks / maxTicks.toFloat(), 0f, 1f)
        }

    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // インプットに一致するレシピを探索する
        val input: INPUT = createRecipeInput(level, pos) ?: return false
        val recipe: RECIPE = getMatchedRecipe(input, level) ?: return false
        val recipeEnergy: Int = getRequiredEnergy(recipe)
        // レシピの進行度を確認する
        if (this.requiredEnergy != recipeEnergy) {
            this.requiredEnergy = recipeEnergy
        }
        // エネルギーを消費する
        if (usedEnergy < requiredEnergy) {
            usedEnergy += battery.consume()
        }
        return when {
            usedEnergy < requiredEnergy -> false
            // レシピを正常に扱えるか判定する
            canProgressRecipe(level, input, recipe) -> {
                usedEnergy -= requiredEnergy
                // レシピを実行する
                completeRecipe(level, pos, state, input, recipe)
                true
            }

            else -> false
        }
    }

    protected abstract fun createRecipeInput(level: ServerLevel, pos: BlockPos): INPUT?

    protected abstract fun getMatchedRecipe(input: INPUT, level: ServerLevel): RECIPE?

    protected fun getRequiredEnergy(recipe: RECIPE): Int = getModifiedEnergy(battery.currentEnergyPerTick * getRecipeTime(recipe))

    protected open fun getRecipeTime(recipe: RECIPE): Int = 20 * 10

    protected abstract fun canProgressRecipe(level: ServerLevel, input: INPUT, recipe: RECIPE): Boolean

    protected abstract fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: INPUT,
        recipe: RECIPE,
    )

    //    Slot    //

    val containerData: ContainerData = object : ContainerData {
        override fun get(index: Int): Int = when (index) {
            0 -> usedEnergy
            1 -> requiredEnergy
            else -> -1
        }

        override fun set(index: Int, value: Int) {
            when (index) {
                0 -> usedEnergy = value
                1 -> requiredEnergy = value
            }
        }

        override fun getCount(): Int = 2
    }

    //    Cached    //

    /**
     * レシピのキャッシュを保持する[HTProcessorBlockEntity]の拡張クラス
     */
    abstract class Cached<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(
        private val recipeCache: HTRecipeCache<INPUT, RECIPE>,
        blockHolder: Holder<Block>,
        pos: BlockPos,
        state: BlockState,
    ) : HTProcessorBlockEntity<INPUT, RECIPE>(blockHolder, pos, state) {
        constructor(
            finder: HTRecipeFinder<INPUT, RECIPE>,
            blockHolder: Holder<Block>,
            pos: BlockPos,
            state: BlockState,
        ) : this(HTFinderRecipeCache(finder), blockHolder, pos, state)

        final override fun getMatchedRecipe(input: INPUT, level: ServerLevel): RECIPE? = recipeCache.getFirstRecipe(input, level)
    }
}
