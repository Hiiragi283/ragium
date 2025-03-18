package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item

class HTMaterialItem(val prefix: HTTagPrefix, val key: HTMaterialKey, properties: Properties) : Item(properties) {
    override fun getDescription(): Component = prefix.createText(key)
}
