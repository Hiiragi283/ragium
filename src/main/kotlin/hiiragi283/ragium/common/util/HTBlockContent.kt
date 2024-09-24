package hiiragi283.ragium.common.util

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible

interface HTBlockContent :
    HTTranslationFormatter.Material,
    ItemConvertible {
    val block: Block

    override fun asItem(): Item = block.asItem()
}
