package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.advancement.HTBuiltMachineCriterion
import hiiragi283.ragium.common.data.HTHardModeResourceCondition
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition
import net.minecraft.advancement.AdvancementCriterion

internal data object InternalRagiumAPI : RagiumAPI {
    override val config: RagiumAPI.Config = RagiumConfig

    override fun createBuiltMachineCriterion(
        machineType: HTMachineType,
        minTier: HTMachineTier,
    ): AdvancementCriterion<HTBuiltMachineCriterion.Condition> = HTBuiltMachineCriterion.create(machineType, minTier)

    override fun getHardModeCondition(isHard: Boolean): ResourceCondition = HTHardModeResourceCondition.fromBool(isHard)
}
