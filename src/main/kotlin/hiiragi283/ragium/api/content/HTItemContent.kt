package hiiragi283.ragium.api.content

import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible

interface HTItemContent :
    HTEntryDelegated<Item>,
    HTTranslationFormatter.Material,
    ItemConvertible {
    override fun asItem(): Item = value
}
