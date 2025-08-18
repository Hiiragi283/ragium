package hiiragi283.ragium.api.data.recipe.impl

import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.base.HTCombineItemToObjRecipe
import hiiragi283.ragium.api.recipe.impl.HTAlloyingRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe

class HTCombineItemToObjRecipeBuilder<R1 : HTRecipeResult<ItemStack>, R2 : HTCombineItemToObjRecipe<R1>>(
    prefix: String,
    private val factory: Factory<R1, R2>,
    private val ingredients: List<HTItemIngredient>,
    private val result: R1,
) : HTRecipeBuilder.Prefixed(prefix) {
    companion object {
        @JvmStatic
        fun alloying(
            result: HTItemResult,
            vararg ingredients: HTItemIngredient,
        ): HTCombineItemToObjRecipeBuilder<HTItemResult, HTAlloyingRecipe> =
            HTCombineItemToObjRecipeBuilder(RagiumConst.ALLOYING, ::HTAlloyingRecipe, listOf(*ingredients), result)
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): Recipe<*> = factory.create(ingredients, result)

    fun interface Factory<R1 : HTRecipeResult<ItemStack>, R2 : HTCombineItemToObjRecipe<R1>> {
        fun create(ingredients: List<HTItemIngredient>, result: R1): R2
    }
}
