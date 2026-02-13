package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.common.recipe.base.HTChemicalIngredient
import hiiragi283.ragium.common.recipe.base.HTChemicalRecipe
import hiiragi283.ragium.common.recipe.base.HTChemicalResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTMixingRecipe(ingredients: HTChemicalIngredient, results: HTChemicalResult, parameters: SubParameters) :
    HTChemicalRecipe(ingredients, results, parameters) {
    companion object {
        const val MAX_FLUID_INPUT = 3
        const val MAX_FLUID_OUTPUT = 2
        const val MAX_ITEM_INPUT = 3
        const val MAX_ITEM_OUTPUT = 1
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MIXING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MIXING.get()
}
