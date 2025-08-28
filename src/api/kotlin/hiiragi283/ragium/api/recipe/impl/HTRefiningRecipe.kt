package hiiragi283.ragium.api.recipe.impl

import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.base.HTFluidTransformingRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer
import java.util.*

class HTRefiningRecipe(
    fluidIngredient: HTFluidIngredient,
    itemIngredient: Optional<HTItemIngredient>,
    itemResult: Optional<HTItemResult>,
    fluidResult: Optional<HTFluidResult>,
) : HTFluidTransformingRecipe(fluidIngredient, itemIngredient, itemResult, fluidResult) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.FLUID_TRANSFORM.get()
}
