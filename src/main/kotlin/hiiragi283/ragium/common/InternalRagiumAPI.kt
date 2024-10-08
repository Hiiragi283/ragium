package hiiragi283.ragium.common

import hiiragi283.ragium.api.HTMachineTypeInitializer
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.HTMachineTypeRegistry
import hiiragi283.ragium.common.advancement.HTBuiltMachineCriterion
import hiiragi283.ragium.common.data.HTHardModeResourceCondition
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.util.Identifier

internal data object InternalRagiumAPI : RagiumAPI {
    override val config: RagiumAPI.Config = RagiumConfig
    override lateinit var machineTypeRegistry: HTMachineTypeRegistry

    override fun createBuiltMachineCriterion(
        machineType: HTMachineType,
        minTier: HTMachineTier,
    ): AdvancementCriterion<HTBuiltMachineCriterion.Condition> = HTBuiltMachineCriterion.create(machineType, minTier)

    override fun getHardModeCondition(isHard: Boolean): ResourceCondition = HTHardModeResourceCondition.fromBool(isHard)

    //    Init    //

    @JvmStatic
    fun initMachineType() {
        val machineTypes: MutableMap<Identifier, HTMachineType> = mutableMapOf()

        fun addMachine(convertible: HTMachineConvertible) {
            val type: HTMachineType = convertible.asMachine()
            check(type.id !in machineTypes) { "Machine Type; ${type.id} is already registered!" }
            machineTypes[type.id] = type
        }

        addMachine(HTMachineType.DEFAULT)

        FabricLoader.getInstance().invokeEntrypoints(
            HTMachineTypeInitializer.KEY,
            HTMachineTypeInitializer::class.java,
        ) { initializer: HTMachineTypeInitializer ->
            initializer.registerType(::addMachine)
        }

        this.machineTypeRegistry = HTMachineTypeRegistry(machineTypes)
        RagiumAPI.log { info("HTMachineTypeRegistry initialized!") }
    }
}
