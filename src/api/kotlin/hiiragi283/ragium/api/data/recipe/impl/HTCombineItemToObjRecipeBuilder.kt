package hiiragi283.ragium.api.data.recipe.impl

import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.impl.HTAlloyingRecipe
import hiiragi283.ragium.api.recipe.impl.HTEnchantingRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe

class HTCombineItemToObjRecipeBuilder<R : HTCombineItemToItemRecipe>(
    prefix: String,
    private val factory: Factory<R>,
    private val ingredients: List<HTItemIngredient>,
    private val result: HTItemResult,
) : HTRecipeBuilder.Prefixed(prefix) {
    companion object {
        @JvmStatic
        fun alloying(result: HTItemResult, vararg ingredients: HTItemIngredient): HTCombineItemToObjRecipeBuilder<HTAlloyingRecipe> =
            HTCombineItemToObjRecipeBuilder(RagiumConst.ALLOYING, ::HTAlloyingRecipe, listOf(*ingredients), result)

        @JvmStatic
        fun enchanting(result: HTItemResult, vararg ingredients: HTItemIngredient): HTCombineItemToObjRecipeBuilder<HTEnchantingRecipe> =
            HTCombineItemToObjRecipeBuilder(RagiumConst.ENCHANTING, ::HTEnchantingRecipe, listOf(*ingredients), result)
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): Recipe<*> = factory.create(ingredients, result)

    fun interface Factory<R : HTCombineItemToItemRecipe> {
        fun create(ingredients: List<HTItemIngredient>, result: HTItemResult): R
    }
}
