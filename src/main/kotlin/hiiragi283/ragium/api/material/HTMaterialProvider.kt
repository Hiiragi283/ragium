package hiiragi283.ragium.api.material

import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

/**
 * [HTMaterialKey]と[HTTagPrefix]を保持するインターフェース
 */
interface HTMaterialProvider : ItemLike {
    val material: HTMaterialKey
    val tagPrefix: HTTagPrefix

    val prefixedTagKey: TagKey<Item>
        get() = tagPrefix.createTag(material)
}
