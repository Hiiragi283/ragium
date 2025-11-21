package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.multi.HTItemWithFluidInputRecipe
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.impl.recipe.HTEnchantingRecipe
import net.minecraft.resources.ResourceLocation

class HTItemWithFluidRecipeBuilder(
    prefix: String,
    private val factory: Factory<*>,
    private val itemIngredient: HTItemIngredient,
    private val fluidIngredient: HTFluidIngredient,
    private val result: HTItemResult,
) : HTRecipeBuilder<HTItemWithFluidRecipeBuilder>(prefix) {
    companion object {
        @JvmStatic
        fun enchanting(
            itemIngredient: HTItemIngredient,
            fluidIngredient: HTFluidIngredient,
            result: HTItemResult,
        ): HTItemWithFluidRecipeBuilder =
            HTItemWithFluidRecipeBuilder(RagiumConst.ENCHANTING, ::HTEnchantingRecipe, itemIngredient, fluidIngredient, result)
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): HTItemWithFluidInputRecipe = factory.create(itemIngredient, fluidIngredient, result)

    fun interface Factory<RECIPE : HTItemWithFluidInputRecipe> {
        fun create(itemIngredient: HTItemIngredient, fluidIngredient: HTFluidIngredient, result: HTItemResult): RECIPE
    }
}
