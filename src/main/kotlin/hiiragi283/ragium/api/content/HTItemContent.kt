package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.material.HTMaterialProvider
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.Registries

interface HTItemContent :
    HTContent<Item>,
    ItemConvertible {
    override fun get(): Item = Registries.ITEM.getOrThrow(key)

    override fun asItem(): Item = get()

    interface Material :
        HTItemContent,
        HTMaterialProvider
}
