package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer
import java.util.*

class HTSolidifyingRecipe(ingredient: HTFluidIngredient, catalyst: Optional<HTItemIngredient>, result: HTItemResult) :
    HTFluidWithCatalystToItemRecipe(
        RagiumRecipeTypes.SOLIDIFYING.get(),
        ingredient,
        catalyst,
        result,
    ) {
    override fun isIncomplete(): Boolean {
        val bool1: Boolean = ingredient.hasNoMatchingStacks()
        val bool2: Boolean = catalyst.map(HTItemIngredient::hasNoMatchingStacks).orElse(false)
        return bool1 || bool2
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.SOLIDIFYING.get()
}
