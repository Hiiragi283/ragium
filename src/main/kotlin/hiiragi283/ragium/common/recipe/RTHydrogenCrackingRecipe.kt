package hiiragi283.ragium.common.recipe

import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.input.HTFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemAndFluidResult
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.api.recipe.RTReactingRecipe
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.fluid.RagiumFluids
import net.minecraft.util.Unit
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

data object RTHydrogenCrackingRecipe : RTReactingRecipe, HTProgressRecipe.Simple<HTFluidRecipeInput> {
    @JvmStatic
    fun setCracked(stack: FluidStack) {
        stack[RagiumDataComponents.HYDROGEN_CRACKED] = Unit.INSTANCE
    }

    //    RTReactingRecipe    //

    override val progressData: HTProgressData = HTProgressData.time(200)

    override fun test(first: FluidInstance, second: FluidInstance): Boolean = when {
        !first.`is`(RagiumTags.Fluids.HYDROGEN_CRACKING) || first.amount() < FluidType.BUCKET_VOLUME -> false
        else -> second.`is`(RagiumFluids.HYDROGEN.fluidTag) && second.amount() >= FluidType.BUCKET_VOLUME
    }

    override fun apply(first: FluidInstance, second: FluidInstance): HTItemAndFluidResult {
        val result: FluidStack = HTIngredientHelper.unwrap(first)
        setCracked(result)
        return HTItemAndFluidResult(result)
    }

    override fun getRequiredAmount(first: FluidInstance, second: FluidInstance): Pair<Int, Int> = when {
        test(first, second) -> FluidType.BUCKET_VOLUME to FluidType.BUCKET_VOLUME
        else -> 0 to 0
    }
}
