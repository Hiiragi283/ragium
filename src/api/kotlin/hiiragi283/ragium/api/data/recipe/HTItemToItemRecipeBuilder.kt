package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.HTItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTExtractingRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe

class HTItemToItemRecipeBuilder<R : HTItemToItemRecipe>(
    prefix: String,
    private val factory: Factory<R>,
    val ingredient: HTItemIngredient,
    val result: HTItemResult,
) : HTRecipeBuilder.Prefixed(prefix) {
    companion object {
        @JvmStatic
        fun extracting(ingredient: HTItemIngredient, result: HTItemResult): HTItemToItemRecipeBuilder<HTExtractingRecipe> =
            HTItemToItemRecipeBuilder(RagiumConst.EXTRACTING, ::HTExtractingRecipe, ingredient, result)
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): Recipe<*> = factory.create(ingredient, result)

    fun interface Factory<R : HTItemToItemRecipe> {
        fun create(ingredient: HTItemIngredient, result: HTItemResult): R
    }
}
