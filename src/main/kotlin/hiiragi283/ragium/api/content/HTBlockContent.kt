package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.material.HTMaterialProvider
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.Registries

interface HTBlockContent :
    HTContent<Block>,
    ItemConvertible {
    override fun get(): Block = Registries.BLOCK.getOrThrow(key)

    override fun asItem(): Item = get().asItem()

    interface Material :
        HTBlockContent,
        HTMaterialProvider
}
