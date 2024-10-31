package hiiragi283.ragium.common

import com.google.common.collect.ImmutableBiMap
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.extension.buildItemStack
import hiiragi283.ragium.api.extension.isClientEnv
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.common.RagiumContents.Misc
import hiiragi283.ragium.common.advancement.HTBuiltMachineCriterion
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.fabricmc.api.EnvType
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack

internal data object InternalRagiumAPI : RagiumAPI {
    //    RagiumAPI    //

    override val config: RagiumAPI.Config = RagiumConfig
    override lateinit var machineTypeRegistry: HTMachineTypeRegistry
        private set

    private var commonMachineRegistry: HTMachinePropertyRegistry = HTMachinePropertyRegistry(mapOf())
    private var clientMachineRegistry: HTMachinePropertyRegistry = HTMachinePropertyRegistry(mapOf())

    override fun createBuiltMachineCriterion(
        type: HTMachineConvertible,
        minTier: HTMachineTier,
    ): AdvancementCriterion<HTBuiltMachineCriterion.Condition> = HTBuiltMachineCriterion.create(type, minTier)

    override fun createFilledCube(fluid: Fluid, count: Int): ItemStack = buildItemStack(
        Misc.FILLED_FLUID_CUBE,
        count,
    ) {
        add(RagiumComponentTypes.FLUID, fluid)
    }

    override fun getMachineRegistry(envType: EnvType): HTMachinePropertyRegistry = when (envType) {
        EnvType.CLIENT -> clientMachineRegistry
        EnvType.SERVER -> commonMachineRegistry
    }

    //    Init    //

    private val keyCache: MutableSet<HTMachineTypeKey> = mutableSetOf()

    @JvmStatic
    fun registerMachines() {
        val typeCache: ImmutableBiMap.Builder<HTMachineTypeKey, HTMachineType> = ImmutableBiMap.builder()

        fun addMachine(key: HTMachineTypeKey, flag: Boolean) {
            check(key !in keyCache) { "Machine SizeType; ${key.id} is already registered!" }
            val type: HTMachineType = when (flag) {
                true -> HTMachineType.Generator()
                false -> HTMachineType.Processor()
            }
            keyCache.add(key)
            typeCache.put(key, type)
        }

        RagiumAPI.getPlugins().forEach {
            it.registerMachineType(RagiumPlugin.MachineRegister(::addMachine))
        }

        this.machineTypeRegistry = HTMachineTypeRegistry(typeCache.build())
        RagiumAPI.log { info("Registered machine types!") }
    }

    @JvmStatic
    private fun buildMachinePropertyMap(
        action: (RagiumPlugin, RagiumPlugin.PropertyHelper) -> Unit,
    ): Map<HTMachineTypeKey, HTPropertyHolder> {
        val map: MutableMap<HTMachineTypeKey, HTPropertyHolder> = mutableMapOf()
        RagiumAPI.getPlugins().forEach { plugin: RagiumPlugin ->
            keyCache.forEach { key: HTMachineTypeKey ->
                val builder: HTPropertyHolder.Mutable =
                    map.computeIfAbsent(key) { HTPropertyHolder.builder() }.let(HTPropertyHolder.Companion::builder)
                action(plugin, RagiumPlugin.PropertyHelper(key, builder))
                map[key] = builder
            }
        }
        return map
    }

    @JvmStatic
    fun registerProperties() {
        this.commonMachineRegistry =
            HTMachinePropertyRegistry(buildMachinePropertyMap(RagiumPlugin::setupCommonMachineProperties))
        RagiumAPI.log { info("Registered common machine properties!") }
        if (isClientEnv()) {
            this.clientMachineRegistry =
                HTMachinePropertyRegistry(buildMachinePropertyMap(RagiumPlugin::setupClientMachineProperties))
            RagiumAPI.log { info("Registered client machine properties!") }
        }
    }
}
