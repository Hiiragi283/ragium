package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.util.useTransaction
import hiiragi283.ragium.common.init.RagiumApiLookupInit
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import team.reborn.energy.api.EnergyStorage

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
                val stateTo: BlockState = world.getBlockState(posTo)
                val blockEntityTo: BlockEntity? = world.getBlockEntity(posTo)
                val storageTo: EnergyStorage =
                    RagiumApiLookupInit.ENERGY.find(world, posTo, stateTo, blockEntityTo, direction.opposite) ?: return@forEach
                if (storageTo.supportsInsertion()) {
                    useTransaction { transaction: Transaction ->
                        storageTo.insert(storageTo.capacity, transaction)
                        transaction.commit()
                    }
                }
            }
        }
    }
}
