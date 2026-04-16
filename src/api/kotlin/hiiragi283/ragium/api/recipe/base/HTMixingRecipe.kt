package hiiragi283.ragium.api.recipe.base

import hiiragi283.core.api.recipe.base.HTMultiOutputRecipe
import hiiragi283.ragium.api.recipe.input.HTMixingRecipeInput
import net.minecraft.core.HolderLookup
import net.neoforged.neoforge.fluids.FluidStack

interface HTMixingRecipe : HTMultiOutputRecipe<HTMixingRecipeInput> {
    fun assembleFluids(input: HTMixingRecipeInput, registries: HolderLookup.Provider): List<FluidStack>

    interface Serializable :
        HTMixingRecipe,
        HTMultiOutputRecipe.Serializable<HTMixingRecipeInput>
}
