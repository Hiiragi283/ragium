package hiiragi283.ragium.api.recipe.base

import hiiragi283.core.api.recipe.base.HTMultiOutputRecipe
import hiiragi283.ragium.api.recipe.input.HTMixingRecipeInput
import net.minecraft.core.HolderLookup
import net.neoforged.neoforge.fluids.FluidStack

interface HTMixingRecipe : HTMultiOutputRecipe<HTMixingRecipeInput> {
    fun assembleFluids(input: HTMixingRecipeInput, registries: HolderLookup.Provider): List<FluidStack>

    fun getRequiredAmounts(input: HTMixingRecipeInput): RequiredAmounts

    @JvmRecord
    data class RequiredAmounts(
        val firstItem: Int,
        val secondItem: Int,
        val firstFluid: Int,
        val secondFluid: Int,
    ) {
        companion object {
            @JvmField
            val EMPTY = RequiredAmounts(0, 0, 0, 0)
        }

        constructor(amounts: IntArray) : this(amounts[0], amounts[1], amounts[2], amounts[3])
    }

    interface Serializable :
        HTMixingRecipe,
        HTMultiOutputRecipe.Serializable<HTMixingRecipeInput>
}
