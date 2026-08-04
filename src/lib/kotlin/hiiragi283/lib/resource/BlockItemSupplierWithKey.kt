package hiiragi283.lib.resource

import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
typealias SimpleBlockItemSupplierWithKey = BlockItemSupplierWithKey<Block, Item>

/**
 * ブロックとアイテム向けの[SupplierWithId]の拡張インターフェースです。
 * @param BLOCK 提供するブロックのクラス
 * @param ITEM 提供するアイテムのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
interface BlockItemSupplierWithKey<out BLOCK : Block, out ITEM : Item> : SupplierWithKey<Block, BLOCK> {
    /**
     * 保持しているアイテムを[SupplierWithKey]として取得します。
     */
    fun getItemSupplier(): SupplierWithKey<Item, ITEM>
}
