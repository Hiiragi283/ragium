package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.item.HTConsumableItem
import hiiragi283.ragium.api.material.HTMaterialItemLike
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

class HTMaterialItem(override val prefix: HTTagPrefix, override val key: HTMaterialKey, properties: Properties) :
    HTConsumableItem(properties),
    HTMaterialItemLike {
    override val id: ResourceLocation get() = asItemHolder().idOrThrow

    override fun getDescription(): Component = prefix.createText(key)

    override fun getName(stack: ItemStack): Component = prefix.createText(key)
}
