package hiiragi283.ragium.common.recipe

import hiiragi283.lib.recipe.base.HTItemAndFluidToFluidRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.api.data.recipe.HTOreSlurryDataHelper
import hiiragi283.ragium.common.fluid.RagiumFluids
import net.minecraft.world.item.ItemInstance
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

data object RTOreSolvingRecipe : HTItemAndFluidToFluidRecipe, HTProgressRecipe.Simple<HTItemAndFluidRecipeInput> {
    override val progressData: HTProgressData = HTProgressData.time(200)

    override fun test(first: ItemInstance, second: FluidInstance): Boolean {
        // Sulfuric Acid
        if (!second.`is`(RagiumFluids.SULFURIC_ACID.fluidTag) || second.amount() < FluidType.BUCKET_VOLUME) return false
        // Ore
        return first.has(RagiumDataComponents.ORE_SLURRY_DATA)
    }

    override fun apply(first: ItemInstance, second: FluidInstance): FluidStack =
        HTOreSlurryDataHelper.createFluid(first) ?: FluidStack.EMPTY

    override fun getRequiredAmount(first: ItemInstance, second: FluidInstance): Pair<Int, Int> = when {
        test(first, second) -> 1 to FluidType.BUCKET_VOLUME
        else -> 0 to 0
    }
}
