package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.mutableMultiMapOf
import hiiragi283.ragium.api.extension.mutableTableOf
import hiiragi283.ragium.api.extension.toDataResult
import hiiragi283.ragium.api.material.*
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.api.util.collection.HTMultiMap
import hiiragi283.ragium.api.util.collection.HTTable
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModLoader
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent
import org.slf4j.Logger

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
internal object HTMaterialRegistryImpl : HTMaterialRegistry {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    //    Init    //

    override lateinit var keys: Set<HTMaterialKey>
    private lateinit var typeMap: Map<HTMaterialKey, HTMaterialType>
    private lateinit var propertyMap: Map<HTMaterialKey, HTPropertyHolder>
    override lateinit var entryMap: Map<HTMaterialKey, HTMaterialRegistry.Entry>

    private var definitionCache: HTMultiMap.Mutable<Item, HTMaterialDefinition> = mutableMultiMapOf()

    fun initRegistry() {
        registerMaterials()
        modifyProperties()

        entryMap = typeMap.mapValues { (key: HTMaterialKey, type: HTMaterialType) ->
            val property: HTPropertyHolder = propertyMap[key] ?: HTPropertyHolder.Empty
            Entry(key, type, property)
        }
        LOGGER.info("Loaded material registry!")
    }

    private fun registerMaterials() {
        LOGGER.info("Invoke material events...")
        val typeCache: MutableMap<HTMaterialKey, HTMaterialType> = mutableMapOf()

        ModLoader.postEvent(
            HTRegisterMaterialEvent { key: HTMaterialKey, type: HTMaterialType ->
                check(typeCache.put(key, type) == null) { "Duplicated material registration: ${key.name}" }
            },
        )

        this.typeMap = typeCache
        this.keys = this.typeMap.keys
        LOGGER.info("Registered new materials!")
    }

    private fun modifyProperties() {
        val propertyCache: MutableMap<HTMaterialKey, HTPropertyHolderBuilder> = mutableMapOf()
        ModLoader.postEvent(HTModifyPropertyEvent { propertyCache.computeIfAbsent(it) { HTPropertyHolderBuilder() } })
        this.propertyMap = propertyCache.mapValues { (_, builder: HTPropertyHolderBuilder) -> builder.build() }
        LOGGER.info("Modified material properties!")
    }

    //    HTMaterialRegistry    //

    private var tagItemCache: HTTable.Mutable<HTTagPrefix, HTMaterialKey, MutableList<Holder<Item>>> = mutableTableOf()

    @SubscribeEvent
    fun onDataMapUpdated(event: DataMapsUpdatedEvent) {
        if (event.cause != DataMapsUpdatedEvent.UpdateCause.SERVER_RELOAD) return
        if (event.registryKey != Registries.ITEM) return
        // Reload material items
        tagItemCache.clear()
        val lookup: HolderLookup.RegistryLookup<Item> = event.registries.lookupOrThrow(Registries.ITEM)
        lookup.listElements().forEach { holder: Holder.Reference<Item> ->
            val definition: HTMaterialDefinition = holder.getData(HTMaterialDefinition.DATA_MAP_TYPE) ?: return@forEach
            tagItemCache
                .computeIfAbsent(
                    definition.tagPrefix,
                    definition.material,
                ) { _: HTTagPrefix, _: HTMaterialKey -> mutableListOf() }
                .add(holder)
            check(definitionCache.put(holder.value(), definition)) { "Item: ${holder.key} already has material data!" }
        }

        LOGGER.info("Reloaded material items!")
    }

    override fun getItems(prefix: HTTagPrefix, key: HTMaterialKey): List<Holder<Item>> = tagItemCache.get(prefix, key) ?: listOf()

    override fun getDefinitions(item: ItemLike): List<HTMaterialDefinition> = definitionCache[item.asItem()].toList()

    override fun getEntryData(key: HTMaterialKey): DataResult<HTMaterialRegistry.Entry> =
        entryMap[key].toDataResult { "Unknown material key: $key" }

    //    Entry    //

    class Entry(override val key: HTMaterialKey, override val type: HTMaterialType, property: HTPropertyHolder) :
        HTMaterialRegistry.Entry,
        HTPropertyHolder by property {
        override fun getItems(prefix: HTTagPrefix): List<Holder<Item>> = tagItemCache.column(key)[prefix] ?: listOf()
    }
}
