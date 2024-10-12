package hiiragi283.ragium.common.util

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible

interface HTBlockContent :
    HTEntryDelegated<Block>,
    HTTranslationFormatter.Material,
    ItemConvertible {
    override fun asItem(): Item = value.asItem()
}
