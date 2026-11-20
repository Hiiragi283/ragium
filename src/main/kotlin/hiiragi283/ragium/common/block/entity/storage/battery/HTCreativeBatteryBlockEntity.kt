package hiiragi283.ragium.common.block.entity.storage.battery

import hiiragi283.lib.capability.HTEnergyCapabilities
import hiiragi283.lib.serialization.value.EmptyValueIOSerializable
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.energy.HTEnergyStorage
import hiiragi283.lib.transfer.energy.HTEnergyStorageUtil
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.transfer.holder.HTBasicEnergyStorageHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class HTCreativeBatteryBlockEntity(worldPosition: BlockPos, blockState: BlockState) :
    HTBaseBatteryBlockEntity(RagiumBlockEntityTypes.CREATIVE_BATTERY.get(), worldPosition, blockState) {
    override lateinit var storage: HTEnergyStorage

    override fun createEnergyStorage(builder: HTBasicEnergyStorageHolder.Builder, listener: Runnable) {
        storage = builder.addStorage(
            HTSlotInfo.BOTH,
            object : HTEnergyStorage, EmptyValueIOSerializable {
                override fun insert(amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int {
                    TransferPreconditions.checkNonNegative(amount)
                    return 0
                }

                override fun extract(amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int {
                    TransferPreconditions.checkNonNegative(amount)
                    return amount
                }

                override val amount: Int = Int.MAX_VALUE
                override val capacity: Int = Int.MAX_VALUE
            },
        )
    }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        for (direction: Direction in Direction.entries) {
            HTEnergyStorageUtil.move(
                storage,
                HTEnergyCapabilities.getStorage(level, pos.relative(direction), direction.opposite),
                Int.MAX_VALUE,
                null,
            )
        }
        return super.onUpdateServer(level, pos, state)
    }
}
