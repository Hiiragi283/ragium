package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTFluidToObjRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer
import java.util.*

class HTRefiningRecipe(ingredient: HTFluidIngredient, itemResult: Optional<HTItemResult>, fluidResults: List<HTFluidResult>) :
    HTFluidToObjRecipe(
        RagiumRecipeTypes.REFINING.get(),
        ingredient,
        itemResult,
        fluidResults,
    ) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.REFINING.get()
}
