package hiiragi283.ragium.common.block.entity.machine.electric

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.block.entity.machine.HTMultiMachineBlockEntity
import hiiragi283.ragium.common.block.entity.machine.RagiumMachineConditions
import hiiragi283.ragium.common.machine.HTBlockPredicate
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.machine.HTMultiblockBuilder
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTDistillationTowerBlockEntity(pos: BlockPos, state: BlockState) :
    HTMultiMachineBlockEntity(HTMachineType.Multi.DISTILLATION_TOWER, pos, state) {
    override val condition: (World, BlockPos) -> Boolean = RagiumMachineConditions.ELECTRIC

    override fun buildMultiblock(builder: HTMultiblockBuilder): HTMultiblockBuilder = builder
        .addLayer(
            -1..1,
            -1,
            1..3,
            HTBlockPredicate.block(HTMachineTier.ADVANCED.baseBlock),
        ).addHollow(
            -1..1,
            0,
            1..3,
            HTBlockPredicate.block(RagiumContents.Hulls.REFINED_RAGI_STEEL.block),
        ).addCross4(
            -1..1,
            1,
            1..3,
            HTBlockPredicate.block(Blocks.RED_CONCRETE),
        ).addCross4(
            -1..1,
            2,
            1..3,
            HTBlockPredicate.block(Blocks.WHITE_CONCRETE),
        ).addCross4(
            -1..1,
            3,
            1..3,
            HTBlockPredicate.block(Blocks.RED_CONCRETE),
        ).add(
            0,
            4,
            2,
            HTBlockPredicate.block(Blocks.WHITE_CONCRETE),
        )
}
