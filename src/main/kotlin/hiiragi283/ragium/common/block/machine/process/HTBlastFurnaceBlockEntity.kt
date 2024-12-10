package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.block.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class HTBlastFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntityBase.Large(RagiumBlockEntityTypes.BLAST_FURNACE, pos, state) {
    override var key: HTMachineKey = RagiumMachineKeys.BLAST_FURNACE

    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        builder
            .addLayer(
                -1..1,
                0,
                1..3,
                HTMultiblockPattern.of(tier.getHull()),
            ).addHollow(
                -1..1,
                1,
                1..3,
                HTMultiblockPattern.of(tier.getCoil()),
            ).addHollow(
                -1..1,
                2,
                1..3,
                HTMultiblockPattern.of(tier.getCoil()),
            ).addLayer(
                -1..1,
                3,
                1..3,
                HTMultiblockPattern.of(tier.getCasing()),
            )
    }
}
