package hiiragi283.ragium.common.block.entity.generator.base

import hiiragi283.ragium.api.block.attribute.getAttributeFront
import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeFinder
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.common.block.entity.generator.HTGeneratorBlockEntity
import hiiragi283.ragium.common.recipe.HTFinderRecipeCache
import hiiragi283.ragium.common.util.HTEnergyHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

/**
 * 電力を生産する設備に使用される[HTGeneratorBlockEntity]の拡張クラス
 */
abstract class HTProgressGeneratorBlockEntity<INPUT : Any, RECIPE : Any>(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTGeneratorBlockEntity(blockHolder, pos, state) {
    //    Ticking    //

    protected var energyToGenerate: Int = 0
    protected var generatedEnergy: Int = 0

    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // バッテリー内の電力を正面に自動搬出させる
        val frontBattery: HTEnergyBattery? = energyCache.getBattery(level, pos, state.getAttributeFront())
        HTEnergyHelper.moveEnergy(this.battery, frontBattery, this.battery::onContentsChanged)
        if (energyToGenerate == 0) {
            // アウトプットが埋まっていないか判定する
            if (!shouldCheckRecipe(level, pos)) return false
            // インプットに一致するレシピを探索する
            val input: INPUT = createRecipeInput(level, pos) ?: return false
            val recipe: RECIPE = getMatchedRecipe(input, level) ?: return false
            val energyToGenerate: Int = getEnergyToGenerate(recipe)
            // 発電の進行度を確認する
            if (this.energyToGenerate != energyToGenerate) {
                this.energyToGenerate = energyToGenerate
                onGenerationUpdated(level, pos, state, input, recipe)
            }
        }
        // エネルギーを生産する
        if (generatedEnergy < energyToGenerate) {
            battery.currentEnergyPerTick = modifyValue(HTMachineUpgrade.Key.ENERGY_GENERATION) { battery.baseEnergyPerTick * it }
            generatedEnergy += battery.generate()
        }
        return when {
            generatedEnergy < energyToGenerate -> false
            else -> {
                generatedEnergy -= energyToGenerate
                energyToGenerate = 0
                false
            }
        }
    }

    /**
     * 指定された引数から，レシピチェックを行うかどうかを判定します。
     * @return レシピチェックを行う場合は`true`, それ以外の場合は`false`
     */
    protected open fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean = battery.getNeeded() > 0

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
    protected abstract fun getEnergyToGenerate(recipe: RECIPE): Int

    protected abstract fun onGenerationUpdated(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: INPUT,
        recipe: RECIPE,
    )

    //    Cached    //

    /**
     * レシピのキャッシュを保持する[HTProgressGeneratorBlockEntity]の拡張クラス
     */
    abstract class Cached<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(
        private val recipeCache: HTRecipeCache<INPUT, RECIPE>,
        blockHolder: Holder<Block>,
        pos: BlockPos,
        state: BlockState,
    ) : HTProgressGeneratorBlockEntity<INPUT, RECIPE>(blockHolder, pos, state) {
        constructor(
            finder: HTRecipeFinder<INPUT, RECIPE>,
            blockHolder: Holder<Block>,
            pos: BlockPos,
            state: BlockState,
        ) : this(HTFinderRecipeCache(finder), blockHolder, pos, state)

        final override fun getMatchedRecipe(input: INPUT, level: ServerLevel): RECIPE? = recipeCache.getFirstRecipe(input, level)
    }
}
