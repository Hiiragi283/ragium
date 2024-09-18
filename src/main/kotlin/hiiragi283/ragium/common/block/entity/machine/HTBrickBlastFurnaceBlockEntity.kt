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

class HTBrickBlastFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTMultiMachineBlockEntity(HTMachineType.Multi.BRICK_BLAST_FURNACE, pos, state) {
    override fun canProcessRecipe(world: World, pos: BlockPos, recipe: HTMachineRecipe): Boolean = machineType.tier.condition(world, pos)

    override fun buildMultiblock(builder: HTMultiblockBuilder): HTMultiblockBuilder = builder
        .addLayer(
            -1..1,
            -1,
            1..3,
            HTBlockPredicate.block(HTMachineTier.HEAT.base),
        ).addHollow(
            -1..1,
            0,
            1..3,
            HTBlockPredicate.block(RagiumBlocks.RAGI_ALLOY_HULL),
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
