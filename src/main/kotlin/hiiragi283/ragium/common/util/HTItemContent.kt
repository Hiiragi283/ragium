package hiiragi283.ragium.common.util

import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.util.Identifier

interface HTItemContent :
    HTTranslationFormatter.Material,
    ItemConvertible {
    val item: Item
    val id: Identifier

    override fun asItem(): Item = item
}
