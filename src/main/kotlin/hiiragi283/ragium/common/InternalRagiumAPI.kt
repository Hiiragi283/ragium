package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.extension.buildItemStack
import hiiragi283.ragium.api.extension.isClientEnv
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.api.extension.mutableTableOf
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.api.machine.block.HTMachineBlock
import hiiragi283.ragium.api.machine.block.HTMachineBlockItem
import hiiragi283.ragium.api.property.HTMutablePropertyHolder
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.common.RagiumContents.Misc
import hiiragi283.ragium.common.advancement.HTBuiltMachineCriterion
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

internal data object InternalRagiumAPI : RagiumAPI {
    //    RagiumAPI    //

    override val config: RagiumAPI.Config = RagiumConfig
    override lateinit var machineRegistry: HTMachineRegistry
        private set

    override fun createBuiltMachineCriterion(
        type: HTMachine,
        minTier: HTMachineTier,
    ): AdvancementCriterion<HTBuiltMachineCriterion.Condition> = HTBuiltMachineCriterion.create(type, minTier)

    override fun createFilledCube(fluid: Fluid, count: Int): ItemStack = buildItemStack(
        Misc.FILLED_FLUID_CUBE,
        count,
    ) {
        add(RagiumComponentTypes.FLUID, fluid)
    }

    //    Init    //

    @JvmStatic
    fun registerMachines() {
        val keyCache: MutableMap<HTMachineKey, HTMachineTypeNew> = mutableMapOf()

        // collect keys from plugins
        fun addMachine(key: HTMachineKey, type: HTMachineTypeNew) {
            check(keyCache.put(key, type) == null) { "Machine SizeType; ${key.id} is already registered!" }
        }
        RagiumAPI.getPlugins().forEach {
            it.registerMachineType(RagiumPlugin.MachineRegister(::addMachine))
        }
        // sort keys based on its type and id
        val sortedKeys: Map<HTMachineKey, HTMachineTypeNew> = keyCache
            .toList()
            .sortedWith(
                compareBy(Pair<HTMachineKey, HTMachineTypeNew>::second)
                    .thenBy(Pair<HTMachineKey, HTMachineTypeNew>::first),
            ).toMap()
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
        // register properties
        val propertyCache: MutableMap<HTMachineKey, HTPropertyHolderBuilder> = mutableMapOf()
        RagiumAPI.getPlugins().forEach { plugin: RagiumPlugin ->
            sortedKeys.keys.forEach { key: HTMachineKey ->
                val builder: HTMutablePropertyHolder = propertyCache.computeIfAbsent(key) { HTPropertyHolderBuilder() }
                val helper = RagiumPlugin.PropertyHelper(key, builder)
                plugin.setupCommonMachineProperties(helper)
                if (isClientEnv()) plugin.setupClientMachineProperties(helper)
            }
        }
        // complete
        machineRegistry = HTMachineRegistry(sortedKeys, blockTable, propertyCache)
        RagiumAPI.log { info("Registered machine types and properties!") }
    }
}
