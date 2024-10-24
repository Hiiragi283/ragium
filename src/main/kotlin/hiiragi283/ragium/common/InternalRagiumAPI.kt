package hiiragi283.ragium.common

import com.google.common.collect.ImmutableBiMap
import hiiragi283.ragium.api.HTMachineTypeInitializer
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.buildItemStack
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.common.RagiumContents.Misc
import hiiragi283.ragium.common.advancement.HTBuiltMachineCriterion
import hiiragi283.ragium.common.data.HTHardModeResourceCondition
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack

internal data object InternalRagiumAPI : RagiumAPI {
    //    RagiumAPI    //

    override val config: RagiumAPI.Config = RagiumConfig
    override lateinit var machineTypeRegistry: HTMachineTypeRegistry

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

    override fun getHardModeCondition(isHard: Boolean): ResourceCondition = HTHardModeResourceCondition.fromBool(isHard)

    internal val integrationCache: MutableList<Runnable> = mutableListOf()

    override fun registerIntegration(action: () -> Unit) {
        integrationCache.add(action)
    }

    //    Init    //

    @JvmStatic
    fun initMachineType() {
        val initializers: List<HTMachineTypeInitializer> = FabricLoader
            .getInstance()
            .getEntrypoints(
                HTMachineTypeInitializer.KEY,
                HTMachineTypeInitializer::class.java,
            ).sortedWith(compareBy(HTMachineTypeInitializer::priority).thenBy { it::class.java.canonicalName })
            .filter(HTMachineTypeInitializer::shouldLoad)

        val keyCache: MutableSet<HTMachineTypeKey> = mutableSetOf()
        val builder: ImmutableBiMap.Builder<HTMachineTypeKey, HTMachineType> = ImmutableBiMap.builder()

        fun addMachine(key: HTMachineTypeKey, properties: HTPropertyHolder, category: HTMachineType.Category) {
            check(key !in keyCache) { "Machine SizeType; ${key.id} is already registered!" }
            val type: HTMachineType = HTMachineType.create(HTPropertyHolder.builder(properties), category)
            keyCache.add(key)
            builder.put(key, type)
        }

        keyCache.add(HTMachineTypeKey.DEFAULT)
        builder.put(HTMachineTypeKey.DEFAULT, HTMachineType.DEFAULT)

        initializers.forEach {
            it.registerType(HTMachineTypeInitializer.Register(::addMachine))
        }

        val map: ImmutableBiMap<HTMachineTypeKey, HTMachineType> = builder.build()

        initializers.forEach {
            map.forEach { (key: HTMachineTypeKey, type: HTMachineType) ->
                val builder1: HTPropertyHolder.Mutable = HTPropertyHolder.builder(type)
                it.modifyProperties(HTMachineTypeInitializer.Helper(key, builder1))
                type.delegated = builder1
            }
        }

        this.machineTypeRegistry = HTMachineTypeRegistry(map)
        RagiumAPI.log { info("HTMachineTypeRegistry initialized!") }
    }
}
