package hiiragi283.ragium.common.item

import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text

open class HTBaseItem(settings: Settings) : Item(settings) {
    override fun appendTooltip(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Text>,
        type: TooltipType,
    ) {
        stack.get(RagiumComponentTypes.TOOLTIPS)?.appendTooltip(context, tooltip::add, type)
    }
}
