package hiiragi283.ragium.common.util

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.util.Identifier

interface HTBlockContent :
    HTTranslationFormatter.Material,
    ItemConvertible {
    val block: Block
    val id: Identifier

    override fun asItem(): Item = block.asItem()
}
