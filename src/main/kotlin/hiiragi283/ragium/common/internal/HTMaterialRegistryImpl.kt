package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumIMC
import hiiragi283.ragium.api.extension.asHolder
import hiiragi283.ragium.api.extension.mutableMultiMapOf
import hiiragi283.ragium.api.extension.mutableTableOf
import hiiragi283.ragium.api.material.*
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.api.util.collection.HTMultiMap
import hiiragi283.ragium.api.util.collection.HTTable
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.InterModComms
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.InterModProcessEvent
import net.neoforged.neoforge.event.TagsUpdatedEvent
import org.slf4j.Logger
import java.util.function.Consumer
import kotlin.jvm.optionals.getOrNull

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
internal object HTMaterialRegistryImpl : HTMaterialRegistry {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    //    Init    //

    override lateinit var keys: Set<HTMaterialKey>
    private lateinit var typeMap: Map<HTMaterialKey, HTMaterialType>
    private lateinit var propertyMap: Map<HTMaterialKey, HTPropertyHolder>
    override val entryMap: MutableMap<HTMaterialKey, HTMaterialRegistry.Entry> = mutableMapOf()

    private lateinit var messageItemCache: HTTable<HTTagPrefix, HTMaterialKey, out List<Holder<Item>>>
    private lateinit var definitionCache: HTMultiMap<Item, HTMaterialDefinition>

    fun processMessage(event: InterModProcessEvent) {
        LOGGER.info("Loading IMCMessages...")
        val typeCache: MutableMap<HTMaterialKey, HTMaterialType> = mutableMapOf()

        event.getIMCStream(RagiumIMC.REGISTER_MATERIAL::equals).forEach { message: InterModComms.IMCMessage ->
            val value: Any = message.messageSupplier().get()
            if (value is RagiumIMC.NewMaterial) {
                val (key: HTMaterialKey, type: HTMaterialType) = value
                check(typeCache.put(key, type) == null) { "Duplicated material registration: ${key.name}" }
            }
        }
        this.typeMap = typeCache
        this.keys = this.typeMap.keys
        LOGGER.info("Registered materials!")

        val propertyCache: MutableMap<HTMaterialKey, HTPropertyHolderBuilder> = mutableMapOf()

        event.getIMCStream(RagiumIMC.SETUP_MATERIAL::equals).forEach { message: InterModComms.IMCMessage ->
            val value: Any = message.messageSupplier().get()
            if (value is RagiumIMC.MaterialProperty) {
                val (key: HTMaterialKey, action: Consumer<HTPropertyHolderBuilder>) = value
                propertyCache.computeIfAbsent(key) { HTPropertyHolderBuilder() }.let(action::accept)
            }
        }
        this.propertyMap = propertyCache.mapValues { (_, builder: HTPropertyHolderBuilder) -> builder.build() }
        LOGGER.info("Completed material property setup!")

        val itemCache: HTTable.Mutable<HTTagPrefix, HTMaterialKey, MutableList<Holder<Item>>> = mutableTableOf()
        val inverseCache: HTMultiMap.Mutable<Item, HTMaterialDefinition> = mutableMultiMapOf()

        event.getIMCStream(RagiumIMC.BIND_ITEM::equals).forEach { message: InterModComms.IMCMessage ->
            val value: Any = message.messageSupplier().get()
            if (value is RagiumIMC.MaterialItems) {
                val (prefix: HTTagPrefix, key: HTMaterialKey, item: ItemLike) = value
                itemCache
                    .computeIfAbsent(prefix, key) { _: HTTagPrefix, _: HTMaterialKey -> mutableListOf() }
                    .add(item.asHolder())
                inverseCache.put(item.asItem(), HTMaterialDefinition(prefix, key))
            }
        }
        this.messageItemCache = itemCache
        this.definitionCache = inverseCache
        LOGGER.info("Bind items with material data!")
    }

    //    HTMaterialRegistryNew    //

    private var tagItemCache: HTTable.Mutable<HTTagPrefix, HTMaterialKey, List<Holder<Item>>> = mutableTableOf()

    @SubscribeEvent
    fun onTagsUpdated(event: TagsUpdatedEvent) {
        val lookup: HolderLookup.RegistryLookup<Item> = event.registryAccess.lookupOrThrow(Registries.ITEM)
        for (prefix: HTTagPrefix in HTTagPrefix.entries) {
            keys.forEach { key: HTMaterialKey ->
                val holderSet: HolderSet.Named<Item> = lookup.get(prefix.createTag(key)).getOrNull() ?: return@forEach
                tagItemCache.put(prefix, key, holderSet.toList())
            }
        }
        LOGGER.info("Reloaded material items!")
    }

    override fun getItems(prefix: HTTagPrefix, key: HTMaterialKey): List<Holder<Item>> = messageItemCache.get(prefix, key) ?: listOf()

    override fun getDefinitions(item: ItemLike): List<HTMaterialDefinition> = definitionCache[item.asItem()].toList()

    override fun getEntryOrNull(key: HTMaterialKey): HTMaterialRegistry.Entry? =
        entryMap.compute(key) { _: HTMaterialKey, oldEntry: HTMaterialRegistry.Entry? ->
            if (oldEntry != null) return@compute oldEntry
            val type: HTMaterialType = typeMap[key] ?: return@compute null
            val property: HTPropertyHolder = propertyMap[key] ?: return@compute null
            Entry(key, type, property)
        }

    //    Entry    //

    class Entry(private val key: HTMaterialKey, override val type: HTMaterialType, property: HTPropertyHolder) :
        HTMaterialRegistry.Entry,
        HTPropertyHolder by property {
        override fun getItems(prefix: HTTagPrefix): List<Holder<Item>> = messageItemCache.column(key)[prefix] ?: listOf()
    }
}
