package hiiragi283.ragium.common.block.entity.machine.heat

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

class HTBrickBlastFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTMultiMachineBlockEntity(HTMachineType.Multi.BRICK_BLAST_FURNACE, pos, state) {
    override val condition: (World, BlockPos) -> Boolean = RagiumMachineConditions.HEAT

    override fun buildMultiblock(builder: HTMultiblockBuilder): HTMultiblockBuilder = builder
        .addLayer(
            -1..1,
            -1,
            1..3,
            HTBlockPredicate.block(HTMachineTier.PRIMITIVE.baseBlock),
        ).addHollow(
            -1..1,
            0,
            1..3,
            HTBlockPredicate.block(RagiumContents.Hulls.RAGI_ALLOY.block),
        ).addHollow(
            -1..1,
            1,
            1..3,
            HTBlockPredicate.block(Blocks.CUT_COPPER),
        ).addHollow(
            -1..1,
            2,
            1..3,
            HTBlockPredicate.block(Blocks.CUT_COPPER),
        )
}
