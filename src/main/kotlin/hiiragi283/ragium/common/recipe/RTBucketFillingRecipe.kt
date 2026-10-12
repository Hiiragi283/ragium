package hiiragi283.ragium.common.recipe

import hiiragi283.lib.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

data object RTBucketFillingRecipe : HTItemAndFluidToItemRecipe, HTProgressRecipe.Simple<HTItemAndFluidRecipeInput> {
    private fun getBucket(instance: FluidInstance): ItemStack =
        HTIngredientHelper.unwrap(instance).let(instance.fluidType::getBucket)

    override fun test(first: ItemInstance, second: FluidInstance): Boolean {
        val filled: ItemStack = getBucket(second)
        return when {
            filled.isEmpty -> false
            ItemStack.isSameItemSameComponents(HTIngredientHelper.unwrap(first), filled.craftingRemainder) -> false
            else -> second.amount() >= FluidType.BUCKET_VOLUME
        }
    }

    override fun apply(first: ItemInstance, second: FluidInstance): ItemStack = getBucket(second)

    override fun getMatchingStack(first: ItemInstance, second: FluidInstance): Pair<ItemInstance, FluidInstance> =
        when {
            test(first, second) -> Pair(
                HTIngredientHelper.copyWithCount(first, 1),
                HTIngredientHelper.copyWithAmount(second, FluidType.BUCKET_VOLUME)
            )

            else -> ItemStack.EMPTY to FluidStack.EMPTY
        }

    override val progressData: HTProgressData = HTProgressData.time(60)
}
