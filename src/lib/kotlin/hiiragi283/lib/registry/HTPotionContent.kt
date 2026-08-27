package hiiragi283.lib.registry

import hiiragi283.lib.resource.SimpleSupplierWithKey
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.level.ItemLike

/**
 * ポーションに関する要素を束ねたクラスです。
 * @param baseHolder 基本のポーションの[HTDeferredHolder]
 * @param longHolder 延長されたポーションの[HTDeferredHolder]
 * @param strongHolder 強化されたポーションの[HTDeferredHolder]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data class HTPotionContent(
    val baseHolder: HTSimpleDeferredHolder<Potion>,
    val longHolder: HTSimpleDeferredHolder<Potion>,
    val strongHolder: HTSimpleDeferredHolder<Potion>?,
) : SimpleSupplierWithKey<Potion> by baseHolder {

    /**
     * 醸造レシピを登録します。
     * @param builder 醸造レシピの登録先
     * @param ingredient 醸造の起点となるアイテム
     */
    fun registerMix(builder: PotionBrewing.Builder, ingredient: ItemLike) {
        builder.addStartMix(ingredient.asItem(), baseHolder)
        builder.addMix(baseHolder, Items.REDSTONE, longHolder)
        strongHolder?.let { builder.addMix(baseHolder, Items.GLOWSTONE_DUST, it) }
    }
}
