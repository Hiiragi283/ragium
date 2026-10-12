package hiiragi283.ragium.common.recipe

import hiiragi283.lib.capability.HTFluidCapabilities
import hiiragi283.lib.item.createOrEmpty
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.ragium.api.recipe.RTCentrifugingRecipe
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.transfer.fluid.FluidUtil

data object RTBucketCentrifugingRecipe : RTCentrifugingRecipe, HTProgressRecipe.Simple<SingleRecipeInput> {
    override val progressData: HTProgressData = HTProgressData.time(60)

    override fun test(input: ItemInstance): Boolean = when {
        input.craftingRemainder?.`is`(Tags.Items.BUCKETS_EMPTY) ?: false ->
            HTIngredientHelper.unwrap(input).let(HTFluidCapabilities::getCapability)?.size() == 1

        else -> false
    }

    override fun apply(input: ItemInstance): RTCentrifugingRecipe.Output = RTCentrifugingRecipe.Output(
        input.craftingRemainder.createOrEmpty(),
        HTIngredientHelper.unwrap(input).let(FluidUtil::getFirstStackContained)
    )

    override fun getMatchingStack(input: ItemInstance): ItemInstance = when {
        test(input) -> HTIngredientHelper.copyWithCount(input, 1)
        else -> ItemStack.EMPTY
    }
}
