package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.text.translatableText
import hiiragi283.ragium.api.tier.HTTierProvider
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * @see mekanism.common.item.ItemAlloy
 */
class HTTierBasedItem(provider: HTTierProvider, properties: Properties) :
    Item(properties),
    HTTierProvider by provider {
    override fun getName(stack: ItemStack): Component = translatableText(getDescriptionId(stack)).withStyle(getBaseTier().color)
}
