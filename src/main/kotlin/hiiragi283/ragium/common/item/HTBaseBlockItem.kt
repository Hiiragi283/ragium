package hiiragi283.ragium.common.item

import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text

class HTBaseBlockItem(block: Block, settings: Settings) : BlockItem(block, settings) {
    override fun appendTooltip(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Text>,
        type: TooltipType,
    ) {
        stack.get(RagiumComponentTypes.TOOLTIPS)?.appendTooltip(context, tooltip::add, type)
    }
}
