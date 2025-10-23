package hiiragi283.ragium.api.variant

import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import net.minecraft.world.item.Item
import net.minecraft.world.item.Tier

/**
 * ツール系アイテム向けの[HTVariantKey]の拡張インターフェース
 */
interface HTToolVariant : HTVariantKey.WithTag<Item> {
    /**
     * 指定した引数からアイテムを登録します。
     * @param register アイテムのレジストリ
     * @param material 道具の素材
     * @param tier 道具のデータ
     * @return 登録されたアイテムの[HTDeferredItem]
     */
    fun registerItem(register: HTDeferredItemRegister, material: HTMaterialType, tier: Tier): HTDeferredItem<*>
}
