package hiiragi283.ragium.api.machine.block

import hiiragi283.ragium.api.extension.energyNetwork
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class HTConsumerBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(type, pos, state) {
    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        val energyRequest: Boolean = world.energyNetwork?.amount?.let { it >= tier.recipeCost } ?: false
        if (energyRequest) {
            if (consumeEnergy(world, pos)) {
                tier.consumerEnergy(world)
            }
        }
    }

    abstract fun consumeEnergy(world: World, pos: BlockPos): Boolean
}
