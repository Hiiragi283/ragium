package hiiragi283.ragium.api.content

import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible

interface HTItemContent :
    HTContent.Delegated<Item>,
    ItemConvertible {
    override fun asItem(): Item = get()
}
