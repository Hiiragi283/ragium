package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.material.HTMaterialProvider
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible

interface HTBlockContent :
    HTContent.Delegated<Block>,
    ItemConvertible {
    override fun asItem(): Item = get().asItem()

    interface Material :
        HTBlockContent,
        HTMaterialProvider
}
