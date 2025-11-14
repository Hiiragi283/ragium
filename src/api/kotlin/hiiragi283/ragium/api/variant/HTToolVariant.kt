package hiiragi283.ragium.api.variant

import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * ツール系アイテム向けの[HTVariantKey]の拡張インターフェース
 */
interface HTToolVariant : HTVariantKey {
    val tagKeys: Iterable<TagKey<Item>>

    /**
     * 指定した引数からアイテムを登録します。
     * @param register アイテムのレジストリ
     * @param material 道具の素材
     * @param name アイテムのID
     * @return 登録されたアイテムの[HTDeferredItem]
     */
    fun registerItem(
        register: HTDeferredItemRegister,
        material: HTEquipmentMaterial,
        name: String = "${material.asMaterialName()}_${variantName()}",
    ): HTDeferredItem<*>
}
