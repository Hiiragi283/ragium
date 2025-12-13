package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.ingredient.HTFluidIngredientCreator
import hiiragi283.ragium.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.ragium.api.math.div
import hiiragi283.ragium.api.math.fraction
import hiiragi283.ragium.api.math.minus
import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.recipe.HTAbstractRecipe
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeFinder
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.input.HTRecipeInput.Builder
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.upgrade.HTUpgradeKeys
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.inventory.container.HTContainerMenu
import hiiragi283.ragium.common.inventory.slot.HTIntSyncSlot
import hiiragi283.ragium.common.recipe.HTFinderRecipeCache
import hiiragi283.ragium.common.storage.energy.battery.HTMachineEnergyBattery
import hiiragi283.ragium.common.storage.holder.HTBasicEnergyBatteryHolder
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import org.apache.commons.lang3.math.Fraction

/**
 * レシピの処理を行う機械に使用される[HTMachineBlockEntity]の拡張クラス
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 */
abstract class HTProcessorBlockEntity<INPUT : Any, RECIPE : Any>(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(blockHolder, pos, state) {
    lateinit var battery: HTMachineEnergyBattery.Processor
        protected set

    final override fun initializeEnergyBattery(builder: HTBasicEnergyBatteryHolder.Builder, listener: HTContentListener) {
        battery = builder.addSlot(HTSlotInfo.INPUT, HTMachineEnergyBattery.input(listener, this))
    }

    val itemCreator: HTItemIngredientCreator get() = RagiumPlatform.INSTANCE.itemCreator()
    val fluidCreator: HTFluidIngredientCreator get() = RagiumPlatform.INSTANCE.fluidCreator()
    val resultHelper: HTResultHelper = HTResultHelper

    //    Ticking    //

    protected var requiredEnergy: Int = 0
    protected var usedEnergy: Int = 0

    override fun addMenuTrackers(menu: HTContainerMenu) {
        super.addMenuTrackers(menu)
        // Progress
        menu.track(HTIntSyncSlot.create(::usedEnergy))
        menu.track(HTIntSyncSlot.create(::requiredEnergy))
    }

    fun getProgress(): Fraction {
        val totalTick: Int = usedEnergy
        val maxTicks: Int = requiredEnergy
        if (maxTicks <= 0) return Fraction.ZERO
        val rawFraction: Fraction = fraction(totalTick, maxTicks)
        return rawFraction - rawFraction.properWhole
    }

    final override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // アウトプットが埋まっていないか判定する
        if (!shouldCheckRecipe(level, pos)) return false
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
            // アウトプットに完成品を搬出できるか判定する
            canProgressRecipe(level, input, recipe) -> {
                usedEnergy -= requiredEnergy
                // レシピを実行する
                completeRecipe(level, pos, state, input, recipe)
                true
            }

            else -> false
        }
    }

    /**
     * 指定された引数から，レシピチェックを行うかどうかを判定します。
     * @return レシピチェックを行う場合は`true`, それ以外の場合は`false`
     */
    protected abstract fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean

    /**
     * 指定された引数から，入力を取得します。
     * @return 入力を生成できない場合は`null`
     */
    protected abstract fun createRecipeInput(level: ServerLevel, pos: BlockPos): INPUT?

    /**
     * 指定された[input]と[level]に一致するレシピを取得します。
     * @return 一致するレシピがない場合は`null`
     */
    protected abstract fun getMatchedRecipe(input: INPUT, level: ServerLevel): RECIPE?

    /**
     * 指定された[recipe]から，レシピに必要なエネルギー量を取得します。
     */
    protected fun getRequiredEnergy(recipe: RECIPE): Int {
        if (isCreative()) return 0
        battery.currentEnergyPerTick = modifyValue(HTUpgradeKeys.ENERGY_EFFICIENCY) { battery.baseEnergyPerTick / it }
        val time: Int = modifyValue(HTUpgradeKeys.SPEED) { getRecipeTime(recipe) / (it * getBaseMultiplier()) }
        return battery.currentEnergyPerTick * time
    }

    protected open fun getRecipeTime(recipe: RECIPE): Int = 20 * 10

    /**
     * 指定された引数から，レシピ処理を完了できるかどうか判定します。
     * @return 完了できる場合は`true`, それ以外の場合は`false`
     */
    protected abstract fun canProgressRecipe(level: ServerLevel, input: INPUT, recipe: RECIPE): Boolean

    /**
     * 指定された引数から，レシピ処理を実行します。
     */
    protected abstract fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: INPUT,
        recipe: RECIPE,
    )

    //    RecipeBased    //

    abstract class RecipeBased<RECIPE : HTAbstractRecipe>(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
        HTProcessorBlockEntity<HTRecipeInput, RECIPE>(blockHolder, pos, state) {
        override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTRecipeInput? = HTRecipeInput.create(null, ::buildRecipeInput)

        protected abstract fun buildRecipeInput(builder: Builder)
    }

    //    Cached    //

    /**
     * レシピのキャッシュを保持する[HTProcessorBlockEntity]の拡張クラス
     */
    abstract class Cached<RECIPE : HTAbstractRecipe>(
        private val recipeCache: HTRecipeCache<HTRecipeInput, RECIPE>,
        blockHolder: Holder<Block>,
        pos: BlockPos,
        state: BlockState,
    ) : RecipeBased<RECIPE>(blockHolder, pos, state) {
        constructor(
            finder: HTRecipeFinder<HTRecipeInput, RECIPE>,
            blockHolder: Holder<Block>,
            pos: BlockPos,
            state: BlockState,
        ) : this(HTFinderRecipeCache(finder), blockHolder, pos, state)

        final override fun getMatchedRecipe(input: HTRecipeInput, level: ServerLevel): RECIPE? = recipeCache.getFirstRecipe(input, level)
    }
}
