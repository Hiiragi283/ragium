package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.block.HTKineticNode
import hiiragi283.ragium.common.block.entity.HTKineticProcessor
import hiiragi283.ragium.common.block.entity.generator.HTKineticGeneratorBlockEntity
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

abstract class HTSingleMachineBlockEntity(machineType: HTMachineType.Single, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity<HTMachineType.Single>(machineType, pos, state),
    HTKineticProcessor {
    companion object {
        @JvmStatic
        fun findProcessor(world: World, pos: BlockPos): BlockPos? {
            val state = world.getBlockState(pos)
            val toDirection: Direction = state.get(Properties.HORIZONTAL_FACING).opposite
            val toPos: BlockPos = pos.offset(toDirection)
            val toBlock: Block = world.getBlockState(toPos).block
            return when {
                toBlock is HTKineticNode -> toBlock.findProcessor(world, toPos, toDirection.opposite)
                world.getBlockEntity(toPos) is HTKineticGeneratorBlockEntity -> toPos
                else -> null
            }
        }
    }

    var receivingPower: Boolean = false

    override fun canProcessRecipe(world: World, pos: BlockPos, recipe: HTMachineRecipe): Boolean = machineType.tier.condition(world, pos)

    //    HTKineticProcessor    //

    override fun onActive(world: World, pos: BlockPos) {
        receivingPower = true
    }

    override fun onInactive(world: World, pos: BlockPos) {
        receivingPower = false
    }
}
