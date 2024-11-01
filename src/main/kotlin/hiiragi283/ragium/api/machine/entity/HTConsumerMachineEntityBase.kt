package hiiragi283.ragium.api.machine.entity

import hiiragi283.ragium.api.extension.energyNetwork
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class HTConsumerMachineEntityBase(type: HTMachineConvertible, tier: HTMachineTier) :
    HTMachineEntity<HTMachineType.Consumer>(type.asConsumer(), tier) {
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
