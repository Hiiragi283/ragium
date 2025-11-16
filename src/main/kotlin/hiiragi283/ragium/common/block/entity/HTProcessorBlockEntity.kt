package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.common.inventory.container.HTContainerMenu
import hiiragi283.ragium.common.inventory.slot.HTIntSyncSlot
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

/**
 * レシピの処理を行う機械に使用される[HTMachineBlockEntity]の拡張クラス
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 */
abstract class HTProcessorBlockEntity<INPUT : Any, RECIPE : Any>(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(blockHolder, pos, state) {
    //    Ticking    //

    protected var requiredEnergy: Int = 0
    protected var usedEnergy: Int = 0

    override fun addMenuTrackers(menu: HTContainerMenu) {
        super.addMenuTrackers(menu)
        // Progress
        menu.track(HTIntSyncSlot(::usedEnergy))
        menu.track(HTIntSyncSlot(::requiredEnergy))
    }

    val progress: Float
        get() {
            val totalTick: Int = usedEnergy
            val maxTicks: Int = requiredEnergy
            if (maxTicks <= 0) return 0f
            val fixedTotalTicks: Int = totalTick % maxTicks
            return Mth.clamp(fixedTotalTicks / maxTicks.toFloat(), 0f, 1f)
        }

    final override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
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
            usedEnergy += gatherEnergy(level, pos)
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

    protected abstract fun getRequiredEnergy(recipe: RECIPE): Int

    protected open fun getRecipeTime(recipe: RECIPE): Int = 20 * 10

    protected abstract fun gatherEnergy(level: ServerLevel, pos: BlockPos): Int

    protected abstract fun canProgressRecipe(level: ServerLevel, input: INPUT, recipe: RECIPE): Boolean

    protected abstract fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: INPUT,
        recipe: RECIPE,
    )
}
