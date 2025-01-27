package hiiragi283.ragium.api.material

import net.minecraft.network.chat.MutableComponent
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

/**
 * [HTMaterialKey]と[HTTagPrefix]を保持するインターフェース
 */
interface HTMaterialProvider : ItemLike {
    val material: HTMaterialKey
    val tagPrefix: HTTagPrefix

    /**
     * [tagPrefix]で[material]を修飾した[TagKey]
     */
    val prefixedTagKey: TagKey<Item> get() = tagPrefix.createTag(material)

    /**
     * [tagPrefix]で[material]を修飾した[MutableComponent]
     */
    val prefixedText: MutableComponent get() = tagPrefix.createText(material)

    /**
     * 元となる[HTTagPrefix]
     */
    val parentPrefix: HTTagPrefix? get() = null
}
