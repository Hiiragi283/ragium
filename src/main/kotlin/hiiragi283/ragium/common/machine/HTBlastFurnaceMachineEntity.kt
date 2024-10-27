package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.init.RagiumMultiblockPatterns
import net.minecraft.registry.RegistryKey

class HTBlastFurnaceMachineEntity(tier: HTMachineTier) : HTLargeProcessorMachineEntity(RagiumMachineTypes.BLAST_FURNACE, tier) {
    override val pattern: RegistryKey<HTMultiblockPattern>
        get() = RagiumMultiblockPatterns.BLAST_FURNACE[tier]!!
}
