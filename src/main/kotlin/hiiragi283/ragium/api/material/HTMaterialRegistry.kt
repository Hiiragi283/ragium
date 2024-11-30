package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.util.collection.HTTable
import net.minecraft.item.Item

class HTMaterialRegistry(
    private val types: Map<HTMaterialKey, HTMaterialKey.Type>,
    private val items: HTTable<HTTagPrefix, HTMaterialKey, out Set<Item>>,
    private val properties: Map<HTMaterialKey, HTPropertyHolder>,
) {
    val keys: Set<HTMaterialKey>
        get() = types.keys

    val entryMap: Map<HTMaterialKey, Entry>
        get() = types.keys.associateWith(::getEntry)

    operator fun contains(key: HTMaterialKey): Boolean = key in types

    fun getItems(prefix: HTTagPrefix, key: HTMaterialKey): Set<Item> = items.get(prefix, key) ?: setOf()

    fun getEntry(key: HTMaterialKey): Entry = Entry(
        key,
        checkNotNull(types[key]) { "Invalid material key; $key!" },
        items.column(key),
        properties.getOrDefault(key, HTPropertyHolder.Empty),
    )

    //    Entry    //

    data class Entry(
        val key: HTMaterialKey,
        val type: HTMaterialKey.Type,
        val itemMap: Map<HTTagPrefix, Set<Item>>,
        private val property: HTPropertyHolder,
    ) : HTPropertyHolder by property {
        fun getItems(prefix: HTTagPrefix): Set<Item> = itemMap.getOrDefault(prefix, setOf())

        fun getFirstItem(prefix: HTTagPrefix): Item? = getItems(prefix).firstOrNull()

        fun getFirstItemOrThrow(prefix: HTTagPrefix): Item = getItems(prefix).first()
    }
}
