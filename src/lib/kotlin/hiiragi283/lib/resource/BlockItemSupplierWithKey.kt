package hiiragi283.lib.resource

import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
typealias SimpleBlockItemSupplierWithKey = BlockItemSupplierWithKey<Block, Item>

/**
 * ブロックとアイテム向けの[SupplierWithKey]の拡張インターフェースです。
 * @param BLOCK 提供するブロックのクラス
 * @param ITEM 提供するアイテムのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
interface BlockItemSupplierWithKey<out BLOCK : Block, out ITEM : Item> {
    val block: SupplierWithKey<Block, BLOCK>
    val item: SupplierWithKey<Item, ITEM>
}
