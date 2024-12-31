package hiiragi283.ragium.api.material

import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.tag.TagKey

/**
 * [HTMaterialKey]と[HTTagPrefix]を保持するインターフェース
 */
interface HTMaterialProvider : ItemConvertible {
    val material: HTMaterialKey
    val tagPrefix: HTTagPrefix

    val prefixedTagKey: TagKey<Item>
        get() = tagPrefix.createTag(material)
}
