package hiiragi283.ragium.common.recipe

import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.recipe.base.HTItemAndFluidToFluidRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import net.minecraft.world.item.ItemInstance
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

data object RTSplashBrewingRecipe : HTItemAndFluidToFluidRecipe {
    override fun test(first: ItemInstance, second: FluidInstance): Boolean = when {
        !first.`is`(Tags.Items.GUNPOWDERS) -> false
        HTPotionHelper.getContents(second)?.bottleType != HTBottleType.DEFAULT -> false
        else -> second.amount() >= FluidType.BUCKET_VOLUME
    }

    override fun apply(first: ItemInstance, second: FluidInstance): FluidStack =
        HTPotionHelper.getContents(second)?.copy(bottleType = HTBottleType.SPLASH)?.toFluidStack() ?: FluidStack.EMPTY

    override fun getRequiredAmount(first: ItemInstance, second: FluidInstance): Pair<Int, Int> =
        when (test(first, second)) {
            true -> 1 to FluidType.BUCKET_VOLUME
            false -> 0 to 0
        }

    override fun getProgressData(input: HTItemAndFluidRecipeInput): HTProgressData = HTProgressData.time(200)
}
