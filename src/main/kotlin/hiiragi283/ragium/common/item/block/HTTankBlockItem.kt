package hiiragi283.ragium.common.item.block

import hiiragi283.core.api.capability.HTFluidCapabilities
import hiiragi283.core.api.item.HTSubCreativeTabContents
import hiiragi283.core.api.stack.ImmutableFluidStack
import hiiragi283.core.api.storage.fluid.HTFluidView
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.common.block.storage.HTTankBlock
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class HTTankBlockItem(block: HTTankBlock, properties: Properties) :
    HTStorageBlockItem<HTTankBlock>(block, properties),
    HTSubCreativeTabContents {
    /**
     * @see mekanism.common.item.block.machine.ItemBlockFluidTank.addStats
     */
    override fun addStats(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        val isCreative: Boolean = HTUpgradeHelper.isCreative(stack)
        val view: HTFluidView = HTFluidCapabilities.getFluidView(stack, 0) ?: return
        // Fluid Name
        val stack: ImmutableFluidStack? = view.getStack()
        when {
            stack == null -> HTCommonTranslation.EMPTY.translateColored(ChatFormatting.DARK_RED)
            isCreative -> HTCommonTranslation.STORED.translateColored(
                ChatFormatting.LIGHT_PURPLE,
                stack,
                ChatFormatting.GRAY,
                HTCommonTranslation.INFINITE,
            )
            else -> HTCommonTranslation.STORED_MB.translateColored(
                ChatFormatting.LIGHT_PURPLE,
                stack,
                ChatFormatting.GRAY,
                stack.amount(),
            )
        }.let(tooltips::add)
        // Tank Capacity
        when (isCreative) {
            true -> HTCommonTranslation.CAPACITY.translateColored(
                ChatFormatting.BLUE,
                ChatFormatting.GRAY,
                HTCommonTranslation.INFINITE,
            )
            false -> HTCommonTranslation.CAPACITY_MB.translateColored(
                ChatFormatting.BLUE,
                ChatFormatting.GRAY,
                view.getCapacity(),
            )
        }.let(tooltips::add)
    }
}
