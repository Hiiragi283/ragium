package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.base.HTMixingRecipe
import hiiragi283.ragium.api.recipe.input.HTMixingRecipeInput
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

@JvmRecord
data class HTFluidMixingRecipe(
    val itemIngredient: Optional<HTItemIngredient>,
    val fluidIngredients: List<HTFluidIngredient>,
    val results: List<HTFluidResult>,
    override val time: Int,
) : HTMixingRecipe.Serializable {
    override fun assembleFluids(input: HTMixingRecipeInput): List<FluidStack> =
        results.map(HTFluidResult::getOrEmpty).filterNot(FluidStack::isEmpty)

    override fun assembleItems(input: HTMixingRecipeInput, preview: Boolean): List<ItemStack> = emptyList()

    override fun getRequiredAmounts(input: HTMixingRecipeInput): HTMixingRecipe.RequiredAmounts {
        val (firstItem: ItemStack, _, firstFluid: FluidStack, secondFluid: FluidStack) = input
        val amounts = IntArray(4) { 0 }
        // first item
        if (itemIngredient.isPresent && !itemIngredient.get().test(firstItem)) {
            amounts[0] = itemIngredient.get().amount
        }
        // fluids
        val firstFluidIng: HTFluidIngredient = fluidIngredients[0]
        val secondFluidIng: HTFluidIngredient? = fluidIngredients.getOrNull(1)
        if (firstFluidIng.test(firstFluid)) {
            amounts[2] = firstFluidIng.amount
            if (secondFluidIng != null && secondFluidIng.test(secondFluid)) {
                amounts[3] = secondFluid.amount
            }
        } else if (firstFluidIng.test(secondFluid)) {
            amounts[3] = firstFluidIng.amount
            if (secondFluidIng != null && secondFluidIng.test(secondFluid)) {
                amounts[2] = secondFluid.amount
            }
        }
        return HTMixingRecipe.RequiredAmounts(amounts)
    }

    override fun test(input: HTMixingRecipeInput): Boolean {
        val (firstItem: ItemStack, _, firstFluid: FluidStack, secondFluid: FluidStack) = input
        if (!fluidIngredients[0].test(firstFluid) && !fluidIngredients[1].test(secondFluid)) return false
        if (itemIngredient.isPresent && !itemIngredient.get().test(firstItem)) return false
        val ingredient: HTFluidIngredient = fluidIngredients.getOrNull(1) ?: return true
        return ingredient.test(secondFluid) || ingredient.test(firstFluid)
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.FLUID_MIXING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MIXING.get()
}
