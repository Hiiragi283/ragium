package hiiragi283.ragium.common.item.upgrade

import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import hiiragi283.ragium.api.tier.HTBaseTier
import hiiragi283.ragium.common.item.HTTierBasedItem
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class HTCreativeUpgradeItem(properties: Properties) : HTTierBasedItem(HTBaseTier.CREATIVE, properties) {
    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        for (key: HTMachineUpgrade.Key in HTMachineUpgrade.Key.entries) {
            tooltips.add(key.translateColored(ChatFormatting.LIGHT_PURPLE, key.creativeValue))
        }
    }
}
