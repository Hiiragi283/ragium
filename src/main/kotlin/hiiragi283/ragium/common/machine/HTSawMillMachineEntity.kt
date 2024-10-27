package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.init.RagiumMultiblockPatterns
import net.minecraft.registry.RegistryKey

class HTSawMillMachineEntity(tier: HTMachineTier) : HTLargeProcessorMachineEntity(RagiumMachineTypes.SAW_MILL, tier) {
    override val pattern: RegistryKey<HTMultiblockPattern>
        get() = RagiumMultiblockPatterns.SAW_MILL[tier]!!
}
