package hiiragi283.ragium.common.item.block

import hiiragi283.ragium.api.capability.HTFluidCapabilities
import hiiragi283.ragium.api.item.HTSubCreativeTabContents
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.fluid.HTFluidView
import hiiragi283.ragium.api.text.RagiumTranslation
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
            stack == null -> RagiumTranslation.EMPTY.translateColored(ChatFormatting.DARK_RED)
            isCreative -> RagiumTranslation.STORED.translateColored(
                ChatFormatting.LIGHT_PURPLE,
                stack,
                ChatFormatting.GRAY,
                RagiumTranslation.INFINITE,
            )
            else -> RagiumTranslation.STORED_MB.translateColored(
                ChatFormatting.LIGHT_PURPLE,
                stack,
                ChatFormatting.GRAY,
                stack.amount(),
            )
        }.let(tooltips::add)
        // Tank Capacity
        when (isCreative) {
            true -> RagiumTranslation.CAPACITY.translateColored(
                ChatFormatting.BLUE,
                ChatFormatting.GRAY,
                RagiumTranslation.INFINITE,
            )
            false -> RagiumTranslation.CAPACITY_MB.translateColored(
                ChatFormatting.BLUE,
                ChatFormatting.GRAY,
                view.getCapacity(),
            )
        }.let(tooltips::add)
    }
}
