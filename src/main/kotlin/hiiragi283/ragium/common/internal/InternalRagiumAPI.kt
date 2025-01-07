package hiiragi283.ragium.common.internal

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.block.HTMachineBlock
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.property.HTMutablePropertyHolder
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.api.util.DelegatedLogger
import hiiragi283.ragium.api.util.collection.HTTable
import hiiragi283.ragium.common.advancement.HTDrankFluidCriterion
import hiiragi283.ragium.common.advancement.HTInteractMachineCriterion
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.resource.HTHardModeResourceCondition
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.ConfigHolder
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.block.Block
import net.minecraft.component.ComponentMap
import net.minecraft.component.DataComponentTypes
import net.minecraft.fluid.Fluid
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.util.Rarity
import org.slf4j.Logger

internal data object InternalRagiumAPI : RagiumAPI {
    @JvmStatic
    private val logger: Logger by DelegatedLogger()

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

    override fun createFilledCube(entry: RegistryEntry<Fluid>, count: Int): ItemStack = buildItemStack(
        RagiumItems.FILLED_FLUID_CUBE,
        count,
    ) {
        add(RagiumComponentTypes.FLUID, entry)
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
        RagiumAPI.plugins.forEach {
            it.registerMachine(::addMachine)
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
        RagiumAPI.plugins.forEach { plugin: RagiumPlugin ->
            sortedKeys.keys.forEach { key: HTMachineKey ->
                val builder: HTMutablePropertyHolder = propertyCache.computeIfAbsent(key) { HTPropertyHolderBuilder() }
                val helper: RagiumPlugin.PropertyHelper<HTMachineKey> = RagiumPlugin.PropertyHelper(key, builder)
                plugin.setupMachineProperties(helper)
            }
        }
        // register blocks
        val blockMap: Map<HTMachineKey, MachineContent> = sortedKeys.keys
            .associateWith(::MachineContent)
            .onEach { (key: HTMachineKey, content: MachineContent) ->
                Registry.register(Registries.BLOCK, content.id, content.get())
                val item = BlockItem(content.get(), itemSettings().machine(key))
                Registry.register(Registries.ITEM, content.id, item)
            }

        // complete
        machineRegistry =
            HTMachineRegistry(sortedKeys, blockMap, propertyCache)
        logger.info("Registered machine types and properties!")
    }

    private class MachineContent(machineKey: HTMachineKey) : HTBlockContent {
        override val key: RegistryKey<Block> = RegistryKey.of(RegistryKeys.BLOCK, machineKey.id)

        private val block: HTMachineBlock = HTMachineBlock(machineKey)

        override fun get(): Block = block
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
        RagiumAPI.plugins.forEach {
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
        RagiumAPI.plugins.forEach { plugin: RagiumPlugin ->
            sortedKeys.keys.forEach { key: HTMaterialKey ->
                val builder: HTMutablePropertyHolder = propertyCache.computeIfAbsent(key) { HTPropertyHolderBuilder() }
                val helper: RagiumPlugin.PropertyHelper<HTMaterialKey> = RagiumPlugin.PropertyHelper(key, builder)
                plugin.setupMaterialProperties(helper)
            }
        }
        // bind items
        val itemCache: HTTable.Mutable<HTTagPrefix, HTMaterialKey, MutableSet<Item>> = mutableTableOf()
        RagiumAPI.plugins.forEach {
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
                    logger.warn("Could not bind item with unregistered material: $fixedKey!")
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
            logger.info("Added rarities for material items!")
        }

        // complete
        materialRegistry = HTMaterialRegistry(sortedKeys, itemTable, propertyCache)
        logger.info("Registered material types and properties!")
    }

    //    ConfigImpl    //

    @JvmStatic
    val CONFIG_HOLDER: ConfigHolder<RagiumConfig> by lazy {
        AutoConfig.register(RagiumConfig::class.java, ::Toml4jConfigSerializer)
        AutoConfig.getConfigHolder(RagiumConfig::class.java)
    }

    override val config: RagiumConfig
        get() = CONFIG_HOLDER.get()
}
