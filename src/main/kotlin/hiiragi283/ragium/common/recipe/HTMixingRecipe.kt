package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.base.HTFluidRecipe
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.Ior
import hiiragi283.core.util.HTShapelessRecipeHelper
import hiiragi283.ragium.common.recipe.input.HTChemicalRecipeInput
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack

class HTMixingRecipe(
    val itemIngredients: List<HTItemIngredient>,
    val fluidIngredients: List<HTFluidIngredient>,
    val result: Ior<HTItemResult, HTFluidResult>,
    override val time: Int,
) : HTProcessingRecipe.Serializable<HTChemicalRecipeInput>,
    HTFluidRecipe<HTChemicalRecipeInput> {
    companion object {
        const val MAX_FLUID_INPUT = 2
        const val MAX_ITEM_INPUT = 2
    }

    override fun test(input: HTChemicalRecipeInput): Boolean {
        val bool1: Boolean = itemIngredients.isEmpty() || HTShapelessRecipeHelper.shapelessMatch(itemIngredients, input.items).isNotEmpty()
        val bool2: Boolean = HTShapelessRecipeHelper.shapelessMatch(fluidIngredients, input.fluids).isNotEmpty()
        return bool1 && bool2
    }

    override fun assemble(input: HTChemicalRecipeInput, registries: HolderLookup.Provider): ItemStack =
        result.getLeft()?.getStackResult(registries)?.value() ?: ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MIXING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MIXING.get()

    override fun assembleFluid(input: HTChemicalRecipeInput, registries: HolderLookup.Provider): FluidStack =
        result.getRight()?.getStackResult(registries)?.value() ?: FluidStack.EMPTY
}
