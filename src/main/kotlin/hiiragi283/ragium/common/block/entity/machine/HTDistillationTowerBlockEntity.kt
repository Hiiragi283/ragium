package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.machine.HTBlockPredicate
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.machine.HTMultiblockBuilder
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTDistillationTowerBlockEntity(pos: BlockPos, state: BlockState) :
    HTMultiMachineBlockEntity(HTMachineType.Multi.DISTILLATION_TOWER, pos, state) {
    override fun canProcessRecipe(world: World, pos: BlockPos, recipe: HTMachineRecipe): Boolean = false

    override fun buildMultiblock(builder: HTMultiblockBuilder): HTMultiblockBuilder = builder
        .addLayer(
            -1..1,
            -1,
            1..3,
            HTBlockPredicate.block(HTMachineTier.ELECTRIC.base),
        ).addHollow(
            -1..1,
            0,
            1..3,
            HTBlockPredicate.block(RagiumBlocks.REFINED_RAGI_STEEL_HULL),
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
