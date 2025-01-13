package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.material.HTMaterialProvider
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

interface HTBlockContent :
    HTContent<Block>,
    ItemLike {
    override fun get(): Block = BuiltInRegistries.BLOCK.getValueOrThrow(key)

    override fun asItem(): Item = get().asItem()

    interface Material :
        HTBlockContent,
        HTMaterialProvider
}
