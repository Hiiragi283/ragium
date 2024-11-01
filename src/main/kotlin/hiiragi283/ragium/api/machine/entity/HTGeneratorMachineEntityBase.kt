package hiiragi283.ragium.api.machine.entity

import hiiragi283.ragium.api.extension.energyNetwork
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.property.HTMachinePropertyKeys
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class HTGeneratorMachineEntityBase(type: HTMachineConvertible, tier: HTMachineTier) :
    HTMachineEntity<HTMachineType.Generator>(type.asGenerator(), tier) {
    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        val energy: Long = generateEnergy(world, pos)
        if (energy > 0) {
            useTransaction { transaction: Transaction ->
                val inserted: Long = world.energyNetwork?.insert(energy, transaction) ?: 0
                if (inserted > 0) {
                    onSucceeded(world, pos)
                    transaction.commit()
                } else {
                    onFailed(world, pos)
                    transaction.abort()
                }
            }
        }
    }

    abstract fun generateEnergy(world: World, pos: BlockPos): Long

    open fun onSucceeded(world: World, pos: BlockPos) {}

    open fun onFailed(world: World, pos: BlockPos) {}

    //    Simple    //

    class Simple(type: HTMachineConvertible, tier: HTMachineTier) : HTGeneratorMachineEntityBase(type, tier) {
        override fun generateEnergy(world: World, pos: BlockPos): Long =
            if (machineType.getOrDefault(HTMachinePropertyKeys.GENERATOR_PREDICATE)(world, pos)) tier.recipeCost else 0
    }
}
