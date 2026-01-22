package hiiragi283.ragium.common.item

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.item.HTColoredNameItem
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class HTUpgradeItem(private val color: TextColor, properties: Properties) : HTColoredNameItem(properties) {
    constructor(color: HTDefaultColor, properties: Properties) : this(color.textColor, properties)

    override fun getNameColor(stack: ItemStack): TextColor = color

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        RagiumDataMapTypes.getUpgradeData(stack)?.appendTooltips(tooltips::add)
    }
}
