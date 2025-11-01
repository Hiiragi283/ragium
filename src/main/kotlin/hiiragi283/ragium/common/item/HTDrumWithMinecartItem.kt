package hiiragi283.ragium.common.item

import hiiragi283.ragium.common.tier.HTDrumTier
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class HTDrumWithMinecartItem(private val tier: HTDrumTier, properties: Properties) :
    HTMinecartItem(tier.getMinecartFactory(), properties) {
    override fun getName(stack: ItemStack): Component = Component.translatable(getDescriptionId(stack)).withStyle(tier.getBaseTier().color)
}
