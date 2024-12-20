package hiiragi283.ragium.api.material

import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.tag.TagKey

interface HTMaterialProvider : ItemConvertible {
    val material: HTMaterialKey
    val tagPrefix: HTTagPrefix

    val definition: HTMaterialDefinition
        get() = HTMaterialDefinition(tagPrefix, material)

    val prefixedTagKey: TagKey<Item>
        get() = tagPrefix.createTag(material)

    companion object {
        @JvmStatic
        fun ofWrapped(prefix: HTTagPrefix, key: HTMaterialKey, item: ItemConvertible): HTMaterialProvider = object : HTMaterialProvider {
            override val material: HTMaterialKey = key
            override val tagPrefix: HTTagPrefix = prefix

            override fun asItem(): Item = item.asItem()
        }
    }
}
