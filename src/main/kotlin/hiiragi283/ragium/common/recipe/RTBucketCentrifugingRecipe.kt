package hiiragi283.ragium.common.recipe

import hiiragi283.lib.capability.HTFluidCapabilities
import hiiragi283.lib.item.createOrEmpty
import hiiragi283.lib.recipe.base.HTItemSeparatingRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.ingredient.HTIngredient
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import net.minecraft.core.TypedInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.transfer.fluid.FluidUtil

data object RTBucketCentrifugingRecipe : HTItemSeparatingRecipe, HTProgressRecipe.Holder {
    override val ingredient: HTIngredient<Item, ItemInstance> = object : HTIngredient<Item, ItemInstance> {
        override fun test(instance: ItemInstance): Boolean = testOnlyType(instance) && instance.count() > 0

        override fun testOnlyType(instance: TypedInstance<Item>): Boolean {
            val input: ItemStack = HTIngredientHelper.unwrapAsStack(instance)
            return when {
                input.craftingRemainder?.`is`(Tags.Items.BUCKETS_EMPTY) ?: false -> input.let(HTFluidCapabilities::getCapability)?.size() == 1
                else -> false
            }
        }

        override fun getMatchingStack(instance: ItemInstance): ItemInstance = when (test(instance)) {
            true -> HTIngredientHelper.copyWithCount(instance, 1)
            false -> ItemStack.EMPTY
        }
    }

    override fun apply(input: ItemInstance): HTItemSeparatingRecipe.Output = HTItemSeparatingRecipe.Output(
        input.craftingRemainder.createOrEmpty(),
        HTIngredientHelper.unwrapAsStack(input).let(FluidUtil::getFirstStackContained),
    )

    override fun getMatchingStack(input: ItemInstance): ItemInstance = when {
        test(input) -> HTIngredientHelper.copyWithCount(input, 1)
        else -> ItemStack.EMPTY
    }

    override fun getProgressData(input: ItemInstance): HTProgressData = progressData

    override val progressData: HTProgressData = HTProgressData.time(60)
}
