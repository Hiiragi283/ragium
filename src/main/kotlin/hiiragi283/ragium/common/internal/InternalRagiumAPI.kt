package hiiragi283.ragium.common.internal

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.api.machine.block.HTMachineBlock
import hiiragi283.ragium.api.machine.block.HTMachineBlockItem
import hiiragi283.ragium.api.material.*
import hiiragi283.ragium.api.property.HTMutablePropertyHolder
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.common.advancement.HTBuiltMachineCriterion
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.resource.HTHardModeResourceCondition
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.component.ComponentMap
import net.minecraft.component.DataComponentTypes
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Rarity
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.bufferedReader
import kotlin.io.path.bufferedWriter

internal data object InternalRagiumAPI : RagiumAPI {
    //    RagiumAPI    //

    override lateinit var machineRegistry: HTMachineRegistry
        private set
    override lateinit var materialRegistry: HTMaterialRegistry
        private set

    override fun createBuiltMachineCriterion(
        key: HTMachineKey,
        minTier: HTMachineTier,
    ): AdvancementCriterion<HTBuiltMachineCriterion.Condition> = HTBuiltMachineCriterion.create(key, minTier)

    override fun createFilledCube(fluid: Fluid, count: Int): ItemStack = buildItemStack(
        RagiumItems.FILLED_FLUID_CUBE,
        count,
    ) {
        add(RagiumComponentTypes.FLUID, fluid)
    }

    override fun createHardModeCondition(value: Boolean): ResourceCondition = HTHardModeResourceCondition(value)

    //    Init    //

    @JvmStatic
    fun registerMachines() {
        val keyCache: MutableMap<HTMachineKey, HTMachineType> = mutableMapOf()

        // collect keys from plugins
        fun addMachine(key: HTMachineKey, type: HTMachineType) {
            check(keyCache.put(key, type) == null) { "Machine; ${key.id} is already registered!" }
        }
        RagiumAPI.forEachPlugins {
            it.registerMachineType(::addMachine)
        }
        // sort keys based on its type and id
        val sortedKeys: Map<HTMachineKey, HTMachineType> = keyCache
            .toList()
            .sortedWith(
                compareBy(Pair<HTMachineKey, HTMachineType>::second)
                    .thenBy(Pair<HTMachineKey, HTMachineType>::first),
            ).toMap()
        // register properties
        val propertyCache: MutableMap<HTMachineKey, HTPropertyHolderBuilder> = mutableMapOf()
        RagiumAPI.forEachPlugins { plugin: RagiumPlugin ->
            sortedKeys.keys.forEach { key: HTMachineKey ->
                val builder: HTMutablePropertyHolder = propertyCache.computeIfAbsent(key) { HTPropertyHolderBuilder() }
                val helper: RagiumPlugin.PropertyHelper<HTMachineKey> = RagiumPlugin.PropertyHelper(key, builder)
                plugin.setupMachineProperties(helper)
            }
        }
        // register blocks
        val blockTable: HTTable.Mutable<HTMachineKey, HTMachineTier, HTMachineBlock> = mutableTableOf()
        sortedKeys.keys.forEach { key: HTMachineKey ->
            HTMachineTier.entries.forEach { tier: HTMachineTier ->
                val block = HTMachineBlock(key, tier)
                Registry.register(Registries.BLOCK, tier.createId(key), block)
                blockTable.put(key, tier, block)
                val item = HTMachineBlockItem(block, itemSettings())
                Registry.register(Registries.ITEM, tier.createId(key), item)
            }
        }
        // complete
        machineRegistry = HTMachineRegistry(sortedKeys, blockTable, propertyCache)
        RagiumAPI.LOGGER.info("Registered machine types and properties!")
    }

    @JvmStatic
    fun registerMaterials() {
        val keyCache: MutableMap<HTMaterialKey, HTMaterialKey.Type> = mutableMapOf()
        val rarityCache: MutableMap<HTMaterialKey, Rarity> = mutableMapOf()
        val altNameCache: MutableMap<String, HTMaterialKey> = mutableMapOf()

        // collect keys from plugins
        fun addMaterial(key: HTMaterialKey, type: HTMaterialKey.Type, rarity: Rarity) {
            check(keyCache.put(key, type) == null) { "Material; ${key.name} is already registered!" }
            rarityCache[key] = rarity
        }

        fun addAltName(parent: HTMaterialKey, child: String) {
            check(parent.name != child) { "Could not register same alternative name!" }
            check(altNameCache.put(child, parent) == null) { "Alternative Name: $child already has redirect material!" }
        }
        RagiumAPI.forEachPlugins {
            it.registerMaterial(RagiumPlugin.MaterialHelper(::addMaterial, ::addAltName))
        }
        // sort keys based on its type and id
        val sortedKeys: Map<HTMaterialKey, HTMaterialKey.Type> = keyCache
            .toList()
            .sortedWith(
                compareBy(Pair<HTMaterialKey, HTMaterialKey.Type>::second)
                    .thenBy(Pair<HTMaterialKey, HTMaterialKey.Type>::first),
            ).toMap()
        // register properties
        val propertyCache: MutableMap<HTMaterialKey, HTPropertyHolderBuilder> = mutableMapOf()
        RagiumAPI.forEachPlugins { plugin: RagiumPlugin ->
            sortedKeys.keys.forEach { key: HTMaterialKey ->
                val builder: HTMutablePropertyHolder = propertyCache.computeIfAbsent(key) { HTPropertyHolderBuilder() }
                val helper: RagiumPlugin.PropertyHelper<HTMaterialKey> = RagiumPlugin.PropertyHelper(key, builder)
                plugin.setupMaterialProperties(helper)
            }
        }
        // bind items
        val itemCache: Multimap<Pair<HTTagPrefix, HTMaterialKey>, Item> = HashMultimap.create()
        RagiumAPI.forEachPlugins {
            it.bindMaterialToItem { prefix: HTTagPrefix, key: HTMaterialKey, item: ItemConvertible ->
                itemCache.put(prefix to key, item.asItem())
            }
        }
        val itemTable: HTTable.Mutable<HTTagPrefix, HTMaterialKey, Set<Item>> = mutableTableOf()
        itemCache
            .asMap()
            .toSortedMap(
                compareBy(Pair<HTTagPrefix, HTMaterialKey>::second)
                    .thenBy(Pair<HTTagPrefix, HTMaterialKey>::first),
            ).forEach { (pair: Pair<HTTagPrefix, HTMaterialKey>, items: Collection<Item>) ->
                val (prefix: HTTagPrefix, key: HTMaterialKey) = pair
                val fixedKey: HTMaterialKey = altNameCache.getOrDefault(key.name, key)
                if (fixedKey !in keyCache.keys) {
                    RagiumAPI.LOGGER.warn("Could not bind item with unregistered material: $fixedKey!")
                    return@forEach
                }
                itemTable.put(
                    prefix,
                    fixedKey,
                    items.toSortedSet(idComparator(Registries.ITEM)),
                )
            }

        DefaultItemComponentEvents.MODIFY.register { context: DefaultItemComponentEvents.ModifyContext ->
            itemTable.forEach { (_: HTTagPrefix, key: HTMaterialKey, items: Set<Item>) ->
                context.modify(items) { builder: ComponentMap.Builder, _: Item ->
                    rarityCache[key]?.let { builder.add(DataComponentTypes.RARITY, it) }
                }
            }
            RagiumAPI.LOGGER.info("Added rarities for material items!")
        }

        // complete
        materialRegistry = HTMaterialRegistry(sortedKeys, itemTable, propertyCache)
        RagiumAPI.LOGGER.info("Registered material types and properties!")
    }

    //    ConfigImpl    //

    @JvmStatic
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    @JvmStatic
    private val configPath: Path = FabricLoader.getInstance().configDir.resolve("${RagiumAPI.MOD_ID}.json")

    override val config: RagiumAPI.Config by lazy { readConfig() }

    @JvmStatic
    private fun readConfig(): RagiumAPI.Config {
        var config = ConfigImpl()
        if (Files.exists(configPath)) {
            configPath
                .bufferedReader()
                .use { gson.fromJson(it, ConfigImpl::class.java) }
                ?.let { config = it }
        } else {
            configPath.bufferedWriter().use { gson.toJson(config, it) }
        }
        return config
    }

    @JvmStatic
    private fun getVersion(): String = getModMetadata(RagiumAPI.MOD_ID)?.version?.friendlyString ?: "MISSING"

    class ConfigImpl(
        val version: String,
        @SerializedName("auto_illuminator_radius")
        override val autoIlluminatorRadius: Int,
        @SerializedName("hard_mode")
        override val isHardMode: Boolean,
    ) : RagiumAPI.Config {
        constructor() : this(getVersion(), 64, false)

        fun validate(): ConfigImpl = apply {
            check(version == getVersion()) { "Not matching config version! Remove old config file!" }
            RagiumAPI.LOGGER.info("Loaded config!")
        }
    }
}
