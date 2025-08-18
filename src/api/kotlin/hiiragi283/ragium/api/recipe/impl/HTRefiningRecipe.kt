package hiiragi283.ragium.api.recipe.impl

import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTFluidWithCatalystToFluidRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import java.util.Optional

class HTRefiningRecipe(ingredient: HTFluidIngredient, catalyst: Optional<HTItemIngredient>, result: HTFluidResult) :
    HTFluidWithCatalystToFluidRecipe(
        ingredient,
        catalyst,
        result,
    ) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.REFINING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.REFINING.get()
}
