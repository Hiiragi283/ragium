package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import net.minecraft.world.item.crafting.RecipeType

interface HTFluidTransformRecipe :
    HTFluidRecipe<HTItemWithFluidRecipeInput>,
    HTItemIngredient.CountGetter,
    HTFluidIngredient.CountGetter {
    override fun getType(): RecipeType<*> = RagiumRecipeTypes.FLUID_TRANSFORM.get()
}
