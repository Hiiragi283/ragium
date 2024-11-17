package hiiragi283.ragium.api.machine.block

import hiiragi283.ragium.api.machine.property.HTMachinePropertyKeys
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.sound.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class HTConsumerBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(type, pos, state) {
    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        if (tier.canProcess(world)) {
            if (consumeEnergy(world, pos)) {
                key.entry.ifPresent(HTMachinePropertyKeys.SOUND) {
                    world.playSound(null, pos, it, SoundCategory.BLOCKS)
                }
                tier.consumerEnergy(world)
                activateState(world, pos, true)
                return
            }
        }
        activateState(world, pos, false)
    }

    abstract fun consumeEnergy(world: World, pos: BlockPos): Boolean
}
