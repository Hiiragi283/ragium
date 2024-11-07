package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.tag.TagKey

interface HTContent<T : ItemConvertible> :
    HTRegistryContent<T>,
    ItemConvertible {
    val commonTagKey: TagKey<Item>?
        get() = null

    override fun asItem(): Item = value.asItem()

    //    Material    //

    interface Material<T : ItemConvertible> : HTContent<T> {
        val material: HTMaterialKey

        val tagPrefix: HTTagPrefix

        val prefixedTagKey: TagKey<Item>
            get() = tagPrefix.createTag(material)

        override val commonTagKey: TagKey<Item>
            get() = tagPrefix.commonTagKey
    }

    //    Tier    //

    interface Tier<T : ItemConvertible> : HTContent<T> {
        val tier: HTMachineTier
    }
}
