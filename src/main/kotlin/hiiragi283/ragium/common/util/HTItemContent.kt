package hiiragi283.ragium.common.util

import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible

interface HTItemContent :
    HTTranslationFormatter.Material,
    ItemConvertible {
    val item: Item

    override fun asItem(): Item = item
}
