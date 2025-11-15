package hiiragi283.ragium.common.item.block

import hiiragi283.ragium.api.block.attribute.getAttributeTier
import hiiragi283.ragium.api.item.HTDescriptionBlockItem
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.capability.HTFluidCapabilities
import hiiragi283.ragium.api.storage.fluid.HTFluidView
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.block.storage.HTDrumBlock
import hiiragi283.ragium.common.tier.HTDrumTier
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class HTDrumBlockItem(block: HTDrumBlock, properties: Properties) : HTDescriptionBlockItem<HTDrumBlock>(block, properties) {
    override fun getTier(): HTDrumTier? = block.getAttributeTier<HTDrumTier>()

    /**
     * @see mekanism.common.item.block.machine.ItemBlockFluidTank.addStats
     */
    override fun addStats(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        val tier: HTDrumTier? = getTier()
        val view: HTFluidView = HTFluidCapabilities.getFluidView(stack, 0) ?: return
        // Fluid Name
        val stack: ImmutableFluidStack? = view.getStack()
        when {
            stack == null -> RagiumTranslation.EMPTY.translateColored(ChatFormatting.DARK_RED)
            tier == HTDrumTier.CREATIVE -> RagiumTranslation.STORED.translateColored(
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
        when (tier) {
            HTDrumTier.CREATIVE -> RagiumTranslation.CAPACITY.translateColored(
                ChatFormatting.BLUE,
                ChatFormatting.GRAY,
                RagiumTranslation.INFINITE,
            )
            else -> RagiumTranslation.CAPACITY_MB.translateColored(
                ChatFormatting.BLUE,
                ChatFormatting.GRAY,
                view.getCapacity(),
            )
        }.let(tooltips::add)
    }
}
