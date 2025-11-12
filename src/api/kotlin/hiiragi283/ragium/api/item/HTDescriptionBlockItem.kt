package hiiragi283.ragium.api.item

import hiiragi283.ragium.api.block.HTBlockWithDescription
import hiiragi283.ragium.api.text.RagiumTranslation
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.block.Block

/**
 * @see mekanism.common.item.block.ItemBlockTooltip
 */
open class HTDescriptionBlockItem<BLOCK>(block: BLOCK, private val hasDetails: Boolean, properties: Properties) :
    HTBlockItem<BLOCK>(block, properties) where BLOCK : Block, BLOCK : HTBlockWithDescription {
    constructor(block: BLOCK, properties: Properties) : this(block, false, properties)

    final override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        if (flag.hasShiftDown()) {
            tooltips.add(block.getDescription().translate())
        } else if (flag.hasControlDown()) {
            addDetails(stack, context, tooltips, flag)
        } else {
            addStats(stack, context, tooltips, flag)
            if (hasDetails) {
                tooltips.add(RagiumTranslation.TOOLTIP_SHOW_DETAILS.translateColored(ChatFormatting.AQUA))
            }
            tooltips.add(RagiumTranslation.TOOLTIP_SHOW_DESCRIPTION.translateColored(ChatFormatting.YELLOW))
        }
    }

    protected open fun addStats(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {}

    protected open fun addDetails(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {}
}
