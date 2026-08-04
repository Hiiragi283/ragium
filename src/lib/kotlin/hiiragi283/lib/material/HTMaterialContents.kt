package hiiragi283.lib.material

import hiiragi283.lib.collection.Table
import hiiragi283.lib.collection.forEach
import hiiragi283.lib.item.HTSimpleItemLike
import hiiragi283.lib.item.ItemStack
import hiiragi283.lib.material.part.HTPart
import hiiragi283.lib.material.part.HTPartKey
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.resource.SimpleBlockItemSupplierWithKey
import hiiragi283.lib.resource.SimpleSupplierWithKey
import hiiragi283.lib.text.Text
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.toTextResult
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.block.Block

/**
 * 素材に紐づいたコンテンツを管理するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
interface HTMaterialContents<R : Any, out V> : Table<R, HTMaterialKey, V> {
    /**
     * 指定した[row]と[key]から対応する値を返します。
     * @since 21.1.0
     */
    fun getResult(row: R, key: HTMaterialKey): HTTextResult<V> = get(row, key).toTextResult { getErrorMessage(row, key) }

    /**
     * 対応する値がない場合のエラーメッセージを作成します。
     */
    fun getErrorMessage(row: R, key: HTMaterialKey): String

    class BlockEntry(delegate: SimpleBlockItemSupplierWithKey, val isBuiltIn: Boolean) :
        SimpleSupplierWithKey<Block> by delegate.block,
        SimpleBlockItemSupplierWithKey by delegate,
        HTIdLike.Translatable,
        HTSimpleItemLike {
        override val translationKey: String get() = get().descriptionId

        override fun getText(): Text = get().name

        override fun asItem(): Item = item.get()

        override fun toStack(count: Int, patch: DataComponentPatch): ItemStack = ItemStack(this, count, patch)

        override fun toTemplate(count: Int, patch: DataComponentPatch): ItemStackTemplate? = runCatching { ItemStackTemplate(asItem(), count, patch) }.getOrNull()

        operator fun component1(): ResourceKey<Block> = getKey()

        operator fun component2(): Block = get()

        operator fun component3(): Boolean = isBuiltIn

        override fun toString(): String = "BlockEntry(id=${getId()},isBuiltIn=$isBuiltIn)"
    }

    class ItemEntry(delegate: SimpleSupplierWithKey<Item>, val isBuiltIn: Boolean) :
        SimpleSupplierWithKey<Item> by delegate,
        HTIdLike.Translatable,
        HTSimpleItemLike {
        override val translationKey: String get() = get().descriptionId

        override fun getText(): Text = toStack().itemName

        override fun asItem(): Item = get()

        override fun toStack(count: Int, patch: DataComponentPatch): ItemStack = ItemStack(this, count, patch)

        override fun toTemplate(count: Int, patch: DataComponentPatch): ItemStackTemplate? = runCatching { ItemStackTemplate(asItem(), count, patch) }.getOrNull()

        operator fun component1(): ResourceKey<Item> = getKey()

        operator fun component2(): Item = get()

        operator fun component3(): Boolean = isBuiltIn

        override fun toString(): String = "ItemEntry(id=${getId()},isBuiltIn=$isBuiltIn)"
    }
}

//    Extensions    //

fun <V> HTMaterialContents<HTPartKey, V>.columnPart(key: HTMaterialKey): Sequence<Pair<HTPart, V>> = this.column(key)
    .asSequence()
    .mapNotNull { (partKey: HTPartKey, entry: V) ->
        val part: HTPart = HTPart.getManager()[partKey] ?: return@mapNotNull null
        part to entry
    }

inline fun <V> HTMaterialContents<HTPartKey, V>.forEachPart(action: (part: HTPart, material: HTMaterial, entry: V) -> Unit) {
    this.forEach { (partKey: HTPartKey, materialKey: HTMaterialKey, entry: V) ->
        val part: HTPart = HTPart.getManager()[partKey] ?: return@forEach
        val material: HTMaterial = HTMaterial.getManager()[materialKey] ?: return@forEach
        action(part, material, entry)
    }
}
