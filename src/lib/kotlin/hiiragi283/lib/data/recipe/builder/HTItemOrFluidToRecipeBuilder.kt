package hiiragi283.lib.data.recipe.builder

import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTRecipeResult
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.HTDelegates
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe

/**
 * 1種類のアイテムと液体から1種類の完成品を作成するレシピ向けの[HTProgressRecipeBuilder]の実装クラスです。
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
abstract class HTItemOrFluidToRecipeBuilder<RESULT : HTRecipeResult<*>, out RECIPE : Recipe<*>>(
    prefix: String,
    private val factory: Factory<RESULT, RECIPE>,
) : HTProgressRecipeBuilder<RECIPE>(prefix),
    HTItemOrFluidIngredientAcceptor {
    final override fun getRecipeId(): Identifier? = result.getId()

    final override fun createRecipe(): RECIPE = factory.create(ingredient, result, progressData)

    // Ingredients
    @PublishedApi internal var ingredient: Either<HTItemIngredient, HTFluidIngredient> by HTDelegates.onceInitialize()

    override operator fun HTItemIngredient.unaryPlus() {
        ingredient = Either.Left(this)
    }

    override operator fun HTFluidIngredient.unaryPlus() {
        ingredient = Either.Right(this)
    }

    // Result
    var result: RESULT by HTDelegates.onceInitialize()

    operator fun RESULT.unaryPlus() {
        result = this
    }

    //    Factory    //

    /**
     * @param RESULT レシピの完成品となるクラス
     * @param RECIPE 生成するレシピのクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.12
     */
    fun interface Factory<RESULT : HTRecipeResult<*>, out RECIPE : Any> {
        fun create(ingredient: Either<HTItemIngredient, HTFluidIngredient>, result: RESULT, progressData: HTProgressData): RECIPE
    }
}
