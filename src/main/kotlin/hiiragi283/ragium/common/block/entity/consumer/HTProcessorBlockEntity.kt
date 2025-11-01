package hiiragi283.ragium.common.block.entity.consumer

import hiiragi283.ragium.api.recipe.manager.HTRecipeCache
import hiiragi283.ragium.api.recipe.manager.HTRecipeFinder
import hiiragi283.ragium.api.recipe.manager.createCache
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

/**
 * レシピの処理を行う機械に使用される[HTConsumerBlockEntity]の拡張クラス
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 */
abstract class HTProcessorBlockEntity<INPUT : Any, RECIPE : Any>(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTConsumerBlockEntity(blockHolder, pos, state) {
    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // インプットに一致するレシピを探索する
        val input: INPUT = createRecipeInput(level, pos)
        val recipe: RECIPE = getMatchedRecipe(input, level) ?: return false
        val recipeEnergy: Int = getRequiredEnergy(recipe)
        // レシピの進行度を確認する
        if (this.requiredEnergy != recipeEnergy) {
            this.requiredEnergy = recipeEnergy
        }
        // エネルギーを消費する
        if (usedEnergy < requiredEnergy) {
            usedEnergy += battery.extract(energyUsage, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
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

    protected abstract fun createRecipeInput(level: ServerLevel, pos: BlockPos): INPUT

    protected abstract fun getMatchedRecipe(input: INPUT, level: ServerLevel): RECIPE?

    protected fun getRequiredEnergy(recipe: RECIPE): Int = getModifiedEnergy(energyUsage * getRecipeTime(recipe))

    protected open fun getRecipeTime(recipe: RECIPE): Int = 20 * 10

    protected abstract fun canProgressRecipe(level: ServerLevel, input: INPUT, recipe: RECIPE): Boolean

    protected abstract fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: INPUT,
        recipe: RECIPE,
    )

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
        ) : this(finder.createCache(), blockHolder, pos, state)

        final override fun getMatchedRecipe(input: INPUT, level: ServerLevel): RECIPE? = recipeCache.getFirstRecipe(input, level)
    }
}
