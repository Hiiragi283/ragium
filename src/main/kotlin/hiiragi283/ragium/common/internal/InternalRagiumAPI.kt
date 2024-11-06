package hiiragi283.ragium.common.internal

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
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
import hiiragi283.ragium.common.RagiumConfig
import hiiragi283.ragium.common.advancement.HTBuiltMachineCriterion
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

internal data object InternalRagiumAPI : RagiumAPI {
    //    RagiumAPI    //

    override val config: RagiumAPI.Config = RagiumConfig
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

    //    Init    //

    @JvmStatic
    fun registerMachines() {
        val keyCache: MutableMap<HTMachineKey, HTMachineType> = mutableMapOf()

        // collect keys from plugins
        fun addMachine(key: HTMachineKey, type: HTMachineType) {
            check(keyCache.put(key, type) == null) { "Machine; ${key.id} is already registered!" }
        }
        RagiumAPI.getPlugins().forEach {
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
        RagiumAPI.getPlugins().forEach { plugin: RagiumPlugin ->
            sortedKeys.keys.forEach { key: HTMachineKey ->
                val builder: HTMutablePropertyHolder = propertyCache.computeIfAbsent(key) { HTPropertyHolderBuilder() }
                val helper: RagiumPlugin.PropertyHelper<HTMachineKey> = RagiumPlugin.PropertyHelper(key, builder)
                plugin.setupCommonMachineProperties(helper)
                if (isClientEnv()) plugin.setupClientMachineProperties(helper)
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
        RagiumAPI.log { info("Registered machine types and properties!") }
    }

    @JvmStatic
    fun registerMaterials() {
        val keyCache: MutableMap<HTMaterialKey, HTMaterialKey.Type> = mutableMapOf()

        // collect keys from plugins
        fun addMaterial(key: HTMaterialKey, type: HTMaterialKey.Type) {
            check(keyCache.put(key, type) == null) { "Material; ${key.name} is already registered!" }
        }
        RagiumAPI.getPlugins().forEach {
            it.registerMaterial(::addMaterial)
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
        RagiumAPI.getPlugins().forEach { plugin: RagiumPlugin ->
            sortedKeys.keys.forEach { key: HTMaterialKey ->
                val builder: HTMutablePropertyHolder = propertyCache.computeIfAbsent(key) { HTPropertyHolderBuilder() }
                val helper: RagiumPlugin.PropertyHelper<HTMaterialKey> = RagiumPlugin.PropertyHelper(key, builder)
                plugin.setupCommonMaterialProperties(helper)
                if (isClientEnv()) plugin.setupClientMaterialProperties(helper)
            }
        }
        // bind items
        val itemCache: Multimap<Pair<HTTagPrefix, HTMaterialKey>, Item> = HashMultimap.create()
        RagiumAPI.getPlugins().forEach {
            it.bindMaterialToItem { prefix: HTTagPrefix, key: HTMaterialKey, item: Item ->
                itemCache.put(prefix to key, item)
            }
        }

        val itemTable: HTTable.Mutable<HTTagPrefix, HTMaterialKey, Set<Item>> = mutableTableOf()
        itemCache
            .asMap()
            .toSortedMap(
                compareBy(Pair<HTTagPrefix, HTMaterialKey>::second)
                    .thenBy(Pair<HTTagPrefix, HTMaterialKey>::first),
            ).forEach { (pair: Pair<HTTagPrefix, HTMaterialKey>, items: Collection<Item>) ->
                itemTable.put(
                    pair.first,
                    pair.second,
                    items.toSortedSet(idComparator(Registries.ITEM)),
                )
            }

        // complete
        materialRegistry = HTMaterialRegistry(sortedKeys, itemTable, propertyCache)
        RagiumAPI.log { info("Registered material types and properties!") }
    }
}
