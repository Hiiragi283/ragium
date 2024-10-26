package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.energy.HTEnergyStorage
import hiiragi283.ragium.api.energy.HTEnergyType
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTCreativeSourceBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(RagiumBlockEntityTypes.CREATIVE_SOURCE, pos, state) {
    override fun tickEach(
        world: World,
        pos: BlockPos,
        state: BlockState,
        ticks: Int,
    ) {
        if (!world.isClient) {
            Direction.entries.forEach { direction: Direction ->
                val posTo: BlockPos = pos.offset(direction)
                val storageTo: Storage<HTEnergyType> =
                    HTEnergyStorage.SIDED.find(world, posTo, direction.opposite) ?: return@forEach
                if (storageTo.supportsInsertion()) {
                    useTransaction { transaction: Transaction ->
                        HTEnergyType.REGISTRY.forEach { type: HTEnergyType ->
                            storageTo.insert(type, Long.MAX_VALUE, transaction)
                        }
                        transaction.commit()
                    }
                }
            }
        }
    }
}
