package hiiragi283.ragium.common.item

import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class HTPotionDropItem(properties: Properties) : Item(properties) {
    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        stack
            .get(DataComponents.POTION_CONTENTS)
            ?.addPotionTooltip(tooltips::add, 1f, context.tickRate())
    }
}
