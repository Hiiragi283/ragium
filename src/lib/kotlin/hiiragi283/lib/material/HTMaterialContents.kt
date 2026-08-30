package hiiragi283.lib.material

import hiiragi283.lib.collection.Table
import hiiragi283.lib.collection.forEach
import hiiragi283.lib.item.HTItemInstanceLike
import hiiragi283.lib.material.part.HTPart
import hiiragi283.lib.material.part.HTPartKey
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.resource.HTKeyLike
import hiiragi283.lib.resource.SimpleBlockItemSupplierWithKey
import hiiragi283.lib.resource.SimpleSupplierWithKey
import hiiragi283.lib.text.Text
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

/**
 * 素材に紐づいたコンテンツを管理するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
interface HTMaterialContents<out V : Any> : Table<HTPartKey, HTMaterialKey, V> {
    fun getOrThrow(row: HTPartKey, column: HTMaterialKey): V = get(row, column) ?: error(getErrorMessage(row, column))

    /**
     * 対応する値がない場合のエラーメッセージを作成します。
     */
    fun getErrorMessage(row: HTPartKey, column: HTMaterialKey): String

    //    Provider    //

    /**
     * 部品と素材に対応するブロックやアイテムを管理するクラスです。
     * @param blocks 素材ブロックの一覧
     * @param items 素材アイテムの一覧
     * @author Hiiragi Tsubasa
     * @since 26.1.2
     */
    data class Provider(val blocks: HTMaterialContents<BlockEntry>, val items: HTMaterialContents<ItemEntry>) {
        /**
         * 指定した部品と素材に対応する素材アイテムを取得します。
         * @return 対応するアイテムがない場合は`null`
         */
        fun getBlockOrItem(part: HTPartKey, key: HTMaterialKey): ItemEntry? = blocks[part, key]?.asItemEntry() ?: items[part, key]
    }

    //    BlockEntry    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.2
     */
    data class BlockEntry(override val block: HTSimpleDeferredBlockAndItem, val isBuiltIn: Boolean) :
        SimpleSupplierWithKey<Block>,
        SimpleBlockItemSupplierWithKey,
        HTIdLike.Translatable by block,
        ItemLike by block,
        HTItemInstanceLike by block {
        override val item: HTSimpleDeferredItem = block.item

        fun asItemEntry(): ItemEntry = ItemEntry(item, isBuiltIn)

        override fun get(): Block = block.get()

        override fun getKey(): ResourceKey<Block> = block.getKey()
    }

    //    ItemEntry    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.2
     */
    data class ItemEntry(val item: HTSimpleDeferredItem, val isBuiltIn: Boolean) :
        SimpleSupplierWithKey<Item> by item,
        HTKeyLike.Translatable<Item>,
        ItemLike by item,
        HTItemInstanceLike by item {
        override val translationKey: String get() = item.translationKey

        override fun getText(): Text = item.getText()

        override fun getId(): Identifier = item.id
    }
}

//    Extensions    //

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun <V : Any> HTMaterialContents<V>.columnPart(key: HTMaterialKey): Sequence<Pair<HTPart, V>> = this.column(key)
    .asSequence()
    .mapNotNull { (partKey: HTPartKey, entry: V) ->
        val part: HTPart = HTPart.getManager()[partKey] ?: return@mapNotNull null
        part to entry
    }

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
inline fun <V : Any> HTMaterialContents<V>.forEachPart(action: (part: HTPart, material: HTMaterial, entry: V) -> Unit) {
    this.forEach { (partKey: HTPartKey, materialKey: HTMaterialKey, entry: V) ->
        val part: HTPart = HTPart.getManager()[partKey] ?: return@forEach
        val material: HTMaterial = HTMaterial.getManager()[materialKey] ?: return@forEach
        action(part, material, entry)
    }
}
