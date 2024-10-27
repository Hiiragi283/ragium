package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.init.RagiumMultiblockPatterns
import net.minecraft.registry.RegistryKey

class HTDistillationTowerMachineEntity(tier: HTMachineTier) :
    HTLargeProcessorMachineEntity(RagiumMachineTypes.DISTILLATION_TOWER, tier) {
    override val pattern: RegistryKey<HTMultiblockPattern>
        get() = RagiumMultiblockPatterns.DISTILLATION_TOWER[tier]!!
}
