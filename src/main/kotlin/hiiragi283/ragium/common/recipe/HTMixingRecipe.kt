package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.common.recipe.base.HTChemicalIngredient
import hiiragi283.ragium.common.recipe.base.HTChemicalRecipe
import hiiragi283.ragium.common.recipe.base.HTChemicalResult
import hiiragi283.ragium.common.recipe.input.HTChemicalRecipeInput
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class HTMixingRecipe(ingredients: HTChemicalIngredient, val results: HTChemicalResult, parameters: SubParameters) :
    HTChemicalRecipe(ingredients, parameters) {
    companion object {
        const val MAX_FLUID_INPUT = 3
        const val MAX_FLUID_OUTPUT = 2
        const val MAX_ITEM_INPUT = 3
        const val MAX_ITEM_OUTPUT = 1
    }

    override fun matches(input: HTChemicalRecipeInput, level: Level): Boolean = matchIngredients(input)

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = results
        .getLeft()
        ?.firstOrNull()
        ?.getStackResult(registries)
        ?.value() ?: ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MIXING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MIXING.get()
}
