package hiiragi283.ragium.api.material

import hiiragi283.lib.collection.Table
import hiiragi283.lib.item.HTItemInstanceLike
import hiiragi283.lib.registry.HTSimpleDeferredBlock
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.HTSimpleBlockItemWithKey
import hiiragi283.lib.resource.HTSimpleKeyOrValue
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

/**
 * 素材に紐づいたコンテンツを管理するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
interface HTMaterialContents<R : HTPart, out V : Any> : Table<R, RagiumMaterial, V> {
    fun getOrThrow(row: R, column: RagiumMaterial): V = get(row, column) ?: error(getErrorMessage(row, column))

    /**
     * 対応する値がない場合のエラーメッセージを作成します。
     */
    fun getErrorMessage(row: R, column: RagiumMaterial): String

    //    Provider    //

    /**
     * 部品と素材に対応するブロックやアイテムを管理するクラスです。
     * @param blocks 素材ブロックの一覧
     * @param items 素材アイテムの一覧
     * @author Hiiragi Tsubasa
     * @since 26.1.2
     */
    data class Provider(
        val blocks: HTMaterialContents<HTBlockPart, BlockEntry>,
        val items: HTMaterialContents<HTItemPart, ItemEntry>
    ) {
        /**
         * 指定した部品と素材に対応する素材アイテムを取得します。
         * @return 対応するアイテムがない場合は`null`
         */
        fun getBlockOrItem(part: HTPart, key: RagiumMaterial): ItemEntry? = when (part) {
            is HTBlockPart -> blocks[part, key]?.asItemEntry()
            is HTItemPart -> items[part, key]
        }
    }

    //    BlockEntry    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.2
     */
    data class BlockEntry(
        override val block: HTSimpleDeferredBlock,
        override val item: HTSimpleDeferredItem,
        val isBuiltIn: Boolean
    ) : HTSimpleBlockItemWithKey,
        ItemLike by item,
        HTItemInstanceLike by item {
        constructor(
            blockItem: HTSimpleDeferredBlockAndItem,
            isBuiltIn: Boolean
        ) : this(blockItem.block, blockItem.item, isBuiltIn)

        fun asItemEntry(): ItemEntry = ItemEntry(item, isBuiltIn)
    }

    //    ItemEntry    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.2
     */
    data class ItemEntry(val item: HTSimpleDeferredItem, val isBuiltIn: Boolean) :
        HTSimpleKeyOrValue<Item> by item,
        ItemLike by item,
        HTItemInstanceLike by item
}
