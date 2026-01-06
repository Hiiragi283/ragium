package hiiragi283.ragium.common.item

import hiiragi283.core.api.item.HTColoredNameItem
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class HTUpgradeItem(private val color: ChatFormatting, properties: Properties) : HTColoredNameItem(properties) {
    override fun getNameColor(stack: ItemStack): ChatFormatting = color

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        RagiumDataMapTypes.getUpgradeData(stack)?.appendTooltips(tooltips::add)
    }
}
