package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.item.HTConsumableItem
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class HTMaterialItem(private val prefix: HTTagPrefix, private val key: HTMaterialKey, properties: Properties) :
    HTConsumableItem(properties) {
    override fun getDescription(): Component = prefix.createText(key)

    override fun getName(stack: ItemStack): Component = prefix.createText(key)
}
