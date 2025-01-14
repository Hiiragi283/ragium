package hiiragi283.ragium.api.material

import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * [HTMaterialKey]と[HTTagPrefix]を保持するインターフェース
 */
interface HTMaterialProvider {
    val material: HTMaterialKey
    val tagPrefix: HTTagPrefix

    val prefixedTagKey: TagKey<Item>
        get() = tagPrefix.createTag(material)
}
