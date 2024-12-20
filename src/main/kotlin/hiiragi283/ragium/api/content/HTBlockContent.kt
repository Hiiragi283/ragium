package hiiragi283.ragium.api.content

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible

interface HTBlockContent :
    HTContent.Delegated<Block>,
    ItemConvertible {
    override fun asItem(): Item = get().asItem()
}
