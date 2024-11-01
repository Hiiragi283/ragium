package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockComponent
import hiiragi283.ragium.common.init.RagiumMachineTypes
import net.minecraft.block.Blocks

class HTDistillationTowerMachineEntity(tier: HTMachineTier) :
    HTLargeProcessorMachineEntity(RagiumMachineTypes.DISTILLATION_TOWER, tier) {
    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        builder
            .addLayer(
                -1..1,
                -1,
                1..3,
                HTMultiblockComponent.of(tier.getBaseBlock()),
            ).addHollow(
                -1..1,
                0,
                1..3,
                HTMultiblockComponent.of(tier.getHull()),
            ).addCross4(
                -1..1,
                1,
                1..3,
                HTMultiblockComponent.of(Blocks.RED_CONCRETE),
            ).addCross4(
                -1..1,
                2,
                1..3,
                HTMultiblockComponent.of(Blocks.WHITE_CONCRETE),
            ).addCross4(
                -1..1,
                3,
                1..3,
                HTMultiblockComponent.of(Blocks.RED_CONCRETE),
            )
        builder.add(
            0,
            4,
            2,
            HTMultiblockComponent.of(Blocks.WHITE_CONCRETE),
        )
    }
}
