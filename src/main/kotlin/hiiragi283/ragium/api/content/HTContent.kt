package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.TagKey

interface HTContent<T : ItemConvertible> :
    HTRegistryContent<T>,
    ItemConvertible {
    override fun asItem(): Item = value.asItem()

    //    Material    //

    interface Material<T : ItemConvertible> : HTContent<T> {
        companion object {
            @JvmStatic
            fun wrapped(prefix: HTTagPrefix, key: HTMaterialKey, item: ItemConvertible): Material<Item> = object : Material<Item> {
                override val material: HTMaterialKey = key
                override val tagPrefix: HTTagPrefix = prefix
                override val registry: Registry<Item> = Registries.ITEM
                override val key: RegistryKey<Item>
                    get() = registry.getKey(item.asItem()).orElseThrow()
            }
        }

        val material: HTMaterialKey

        val tagPrefix: HTTagPrefix

        val prefixedTagKey: TagKey<Item>
            get() = tagPrefix.createTag(material)
    }

    //    Tier    //

    interface Tier<T : ItemConvertible> : HTContent<T> {
        val tier: HTMachineTier
    }
}
