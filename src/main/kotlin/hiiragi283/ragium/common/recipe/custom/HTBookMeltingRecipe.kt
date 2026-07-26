package hiiragi283.ragium.common.recipe.custom

import hiiragi283.core.api.recipe.base.HTItemToFluidRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.util.HTExperienceHelper
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.neoforged.neoforge.fluids.FluidStack

data object HTBookMeltingRecipe : HTItemToFluidRecipe {
    private fun getStoredEnchantments(input: ItemStack): ItemEnchantments = input.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)

    override fun test(input: ItemStack): Boolean = !getStoredEnchantments(input).isEmpty

    override fun getMatchingStack(input: ItemStack): ItemStack = when {
        test(input) -> input.copyWithCount(1)
        else -> ItemStack.EMPTY
    }

    override fun assemble(input: ItemStack): FluidStack = getStoredEnchantments(input)
        .let(HTExperienceHelper::getTotalMinCost)
        .let(HTExperienceHelper::fluidAmountFromExp)
        .let(HCFluids.EXPERIENCE::toStack)

    override fun getProgressData(input: SingleRecipeInput): HTProgressData = HTProgressData.time(
        input
            .item()
            .let(::getStoredEnchantments)
            .keySet()
            .size * 100,
    )

    override fun isIncomplete(): Boolean = false
}
