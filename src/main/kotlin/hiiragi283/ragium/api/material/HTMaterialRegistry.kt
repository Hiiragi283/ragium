package hiiragi283.ragium.api.material

import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Keyable
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.util.collection.HTTable
import net.minecraft.item.Item
import java.util.stream.Stream

/**
 * [HTMaterialKey]のレジストリ
 * @see hiiragi283.ragium.api.RagiumAPI.materialRegistry
 */
class HTMaterialRegistry(
    private val types: Map<HTMaterialKey, HTMaterialType>,
    private val items: HTTable<HTTagPrefix, HTMaterialKey, out Set<Item>>,
    private val properties: Map<HTMaterialKey, HTPropertyHolder>,
) : Keyable {
    /**
     * 登録された[HTMaterialKey]の一覧
     */
    val keys: Set<HTMaterialKey>
        get() = types.keys

    /**
     * 登録された[HTMaterialKey]とその[Entry]のマップ
     */
    val entryMap: Map<HTMaterialKey, Entry>
        get() = types.keys.associateWith(::createEntry)

    /**
     * 指定された[key]が登録されているか判定します。
     */
    operator fun contains(key: HTMaterialKey): Boolean = key in types

    /**
     * 指定された[prefix]と[key]に紐づいたアイテムの一覧を返します。
     *
     * @return 値がない場合は[emptySet]
     */
    fun getItems(prefix: HTTagPrefix, key: HTMaterialKey): Set<Item> = items.get(prefix, key) ?: setOf()

    private fun createEntry(key: HTMaterialKey): Entry = Entry(
        types[key] ?: error("Unknown material key: $key"),
        items.column(key),
        properties.getOrDefault(key, HTPropertyHolder.Empty),
    )

    /**
     * 指定された[key]に紐づいた[Entry]を返します。
     * @throws IllegalStateException [key]が登録されていない場合
     */
    fun getEntry(key: HTMaterialKey): Entry = getEntryOrNull(key) ?: error("Unknown material key: $key")

    /**
     * 指定された[key]に紐づいた[Entry]を返します。
     * @return [key]が登録されていない場合はnull
     */
    fun getEntryOrNull(key: HTMaterialKey): Entry? = entryMap[key]

    //    Keyable    //

    override fun <T : Any> keys(ops: DynamicOps<T>): Stream<T> = keys
        .stream()
        .map(HTMaterialKey::name)
        .map(ops::createString)

    //    Entry    //

    /**
     * 素材の情報をまとめたクラス
     */
    data class Entry(val type: HTMaterialType, val itemMap: Map<HTTagPrefix, Set<Item>>, private val property: HTPropertyHolder) :
        HTPropertyHolder by property {
        fun getItems(prefix: HTTagPrefix): Set<Item> = itemMap.getOrDefault(prefix, setOf())

        fun getFirstItemOrNull(prefix: HTTagPrefix): Item? = getItems(prefix).firstOrNull()

        fun getFirstItem(prefix: HTTagPrefix): Item = getItems(prefix).first()
    }
}
