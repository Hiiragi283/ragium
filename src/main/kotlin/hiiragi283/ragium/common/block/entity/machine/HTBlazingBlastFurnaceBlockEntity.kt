package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumEnergyProviders
import hiiragi283.ragium.common.machine.HTBlockPredicate
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.machine.HTMultiblockBuilder
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTBlazingBlastFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTMultiMachineBlockEntity(HTMachineType.Multi.BLAZING_BLAST_FURNACE, pos, state) {
    override fun canProcessRecipe(world: World, pos: BlockPos, recipe: HTMachineRecipe): Boolean {
        val downPos: BlockPos = pos.down()
        return RagiumEnergyProviders.BLAZING_HEAT.find(
            world,
            downPos,
            world.getBlockState(downPos),
            world.getBlockEntity(downPos),
            Direction.UP,
        ) ?: false
    }

    override fun buildMultiblock(builder: HTMultiblockBuilder): HTMultiblockBuilder = builder
        .addLayer(
            -1..1,
            -1,
            1..3,
            HTBlockPredicate.block(HTMachineTier.KINETIC.base),
        ).addHollow(
            -1..1,
            0,
            1..3,
            HTBlockPredicate.block(RagiumBlocks.RAGI_STEEL_HULL),
        ).addHollow(
            -1..1,
            1,
            1..3,
            HTBlockPredicate.block(Blocks.POLISHED_BASALT),
        ).addHollow(
            -1..1,
            2,
            1..3,
            HTBlockPredicate.block(Blocks.POLISHED_BASALT),
        )
}
