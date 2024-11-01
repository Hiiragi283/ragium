package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockComponent
import hiiragi283.ragium.common.init.RagiumMachineTypes

class HTBlastFurnaceMachineEntity(tier: HTMachineTier) : HTLargeProcessorMachineEntity(RagiumMachineTypes.BLAST_FURNACE, tier) {
    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        builder
            .addLayer(
                -1..1,
                0,
                1..3,
                HTMultiblockComponent.of(tier.getHull()),
            ).addHollow(
                -1..1,
                1,
                1..3,
                HTMultiblockComponent.of(tier.getCoil()),
            ).addHollow(
                -1..1,
                2,
                1..3,
                HTMultiblockComponent.of(tier.getCoil()),
            ).addLayer(
                -1..1,
                3,
                1..3,
                HTMultiblockComponent.of(tier.getBaseBlock()),
            )
    }
}
