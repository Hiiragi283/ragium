package hiiragi283.lib.data.recipe.builder

import hiiragi283.lib.data.recipe.result.HTItemOrFluidResultBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.HTDelegates
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe

/**
 * 1種類のアイテムと液体から1種類の完成品を作成するレシピ向けの[HTProgressRecipeBuilder]の実装クラスです。
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
class HTItemOrFluidRecipeBuilder<out RECIPE : Recipe<*>>(
    prefix: String,
    private val factory: HTItemOrFluidToRecipeBuilder.Factory<HTItemOrFluidResult, RECIPE>,
) : HTProgressRecipeBuilder<RECIPE>(prefix),
    HTItemOrFluidIngredientAcceptor,
    HTItemOrFluidResultBuilder.Holder {
    override fun getRecipeId(): Identifier? = result.getId()

    override fun createRecipe(): RECIPE = factory.create(ingredient, result, progressData)

    // Ingredients
    @PublishedApi
    internal var ingredient: Either<HTItemIngredient, HTFluidIngredient> by HTDelegates.onceInitialize()

    override operator fun HTItemIngredient.unaryPlus() {
        ingredient = Either.Left(this)
    }

    override operator fun HTFluidIngredient.unaryPlus() {
        ingredient = Either.Right(this)
    }

    // Results
    override val builder: HTItemOrFluidResultBuilder = HTItemOrFluidResultBuilder()
}
