package hiiragi283.ragium.common.internal

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.api.machine.block.HTMachineBlock
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.property.HTMutablePropertyHolder
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.api.util.collection.HTTable
import hiiragi283.ragium.common.advancement.HTDrankFluidCriterion
import hiiragi283.ragium.common.advancement.HTInteractMachineCriterion
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
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntryList
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

    override fun createInteractMachineCriterion(
        key: HTMachineKey,
        minTier: HTMachineTier,
    ): AdvancementCriterion<HTInteractMachineCriterion.Condition> = HTInteractMachineCriterion.create(key, minTier)

    override fun createFluidDrinkCriterion(entryList: RegistryEntryList<Fluid>): AdvancementCriterion<HTDrankFluidCriterion.Condition> =
        HTDrankFluidCriterion.create(entryList)

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
        /*val blockTable: HTTable.Mutable<HTMachineKey, HTMachineTier, HTMachineBlock> = mutableTableOf()
        sortedKeys.keys.forEach { key: HTMachineKey ->
            HTMachineTier.entries.forEach { tier: HTMachineTier ->
                val block = HTMachineBlock(key, tier)
                Registry.register(Registries.BLOCK, tier.createId(key), block)
                blockTable.put(key, tier, block)
                val item = BlockItem(
                    block,
                    itemSettings().machine(key, tier),
                )
                Registry.register(Registries.ITEM, tier.createId(key), item)
            }
        }*/
        val blockMap: Map<HTMachineKey, HTMachineBlock> = sortedKeys.keys
            .associateWith(::HTMachineBlock)
            .onEach { (key: HTMachineKey, block: HTMachineBlock) ->
                Registry.register(Registries.BLOCK, key.id, block)
                val item = BlockItem(block, itemSettings().machine(key))
                Registry.register(Registries.ITEM, key.id, item)
            }

        // complete
        machineRegistry =
            HTMachineRegistry(sortedKeys, blockMap, propertyCache)
        RagiumAPI.LOGGER.info("Registered machine types and properties!")
    }

    @JvmStatic
    fun registerMaterials() {
        val keyCache: MutableMap<HTMaterialKey, HTMaterialType> = mutableMapOf()
        val rarityCache: MutableMap<HTMaterialKey, Rarity> = mutableMapOf()
        val altNameCache: MutableMap<String, HTMaterialKey> = mutableMapOf()

        // collect keys from plugins
        fun addMaterial(key: HTMaterialKey, type: HTMaterialType, rarity: Rarity) {
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
        val sortedKeys: Map<HTMaterialKey, HTMaterialType> = keyCache
            .toList()
            .sortedWith(
                compareBy(Pair<HTMaterialKey, HTMaterialType>::second)
                    .thenBy(Pair<HTMaterialKey, HTMaterialType>::first),
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
        val itemCache: HTTable.Mutable<HTTagPrefix, HTMaterialKey, MutableSet<Item>> = mutableTableOf()
        RagiumAPI.forEachPlugins {
            it.bindMaterialToItem { prefix: HTTagPrefix, key: HTMaterialKey, item: ItemConvertible ->
                itemCache
                    .computeIfAbsent(prefix, key) { _: HTTagPrefix, _: HTMaterialKey -> mutableSetOf() }
                    .add(item.asItem())
            }
        }
        val itemTable: HTTable<HTTagPrefix, HTMaterialKey, MutableSet<Item>> = itemCache
            .asPairMap()
            .toSortedMap(
                compareBy(Pair<HTTagPrefix, HTMaterialKey>::second)
                    .thenBy(Pair<HTTagPrefix, HTMaterialKey>::first),
            ).filter { (pair: Pair<HTTagPrefix, HTMaterialKey>, _) ->
                val (_: HTTagPrefix, key: HTMaterialKey) = pair
                val fixedKey: HTMaterialKey = altNameCache.getOrDefault(key.name, key)
                if (fixedKey !in keyCache.keys) {
                    RagiumAPI.LOGGER.warn("Could not bind item with unregistered material: $fixedKey!")
                    false
                } else {
                    true
                }
            }.onEach { (_: Pair<HTTagPrefix, HTMaterialKey>, items: MutableSet<Item>) ->
                items.sortedWith(idComparator(Registries.ITEM))
            }.toTable()

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

        init {
            validate()
        }

        fun validate(): ConfigImpl = apply {
            check(version == getVersion()) { "Not matching config version! Remove old config file!" }
            RagiumAPI.LOGGER.info("Loaded config!")
        }
    }
}
