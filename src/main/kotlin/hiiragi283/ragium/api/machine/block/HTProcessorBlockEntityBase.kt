package hiiragi283.ragium.api.machine.block

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class HTProcessorBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(type, pos, state) {
    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        if (tier.canProcess(world)) {
            if (processRecipe(world, pos)) {
                tier.consumerEnergy(world)
            }
        }
    }

    abstract fun processRecipe(world: World, pos: BlockPos): Boolean
}
