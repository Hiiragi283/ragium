package hiiragi283.ragium.common.block.entity.storage

import com.google.common.primitives.Ints
import hiiragi283.lib.player.HTExperienceHelper
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.storage.base.HTBaseTankBlockEntity
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class HTExperienceTankBlockEntity(pos: BlockPos, state: BlockState) :
    HTBaseTankBlockEntity(RagiumBlockEntityTypes.EXPERIENCE_TANK.get(), pos, state) {
    override lateinit var tank: HTFluidTank
        private set

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        tank = builder.addSlot(HTSlotInfo.OUTPUT, ExpTank())
    }

    //    ExpTank    //

    inner class ExpTank :
        SnapshotJournal<Long>(),
        HTFluidTank {
        private val player: ServerPlayer?
            get() = (this@HTExperienceTankBlockEntity.level as? ServerLevel)?.let(this@HTExperienceTankBlockEntity::getOwnerPlayer)
        private val playerExp: Long get() = player?.let(HTExperienceHelper::getPlayerExp) ?: 0

        override fun createSnapshot(): Long = playerExp

        override fun revertToSnapshot(snapshot: Long) {
            player?.let { HTExperienceHelper.setPlayerExp(it, snapshot) }
        }

        override fun isValid(resource: FluidResource): Boolean = RagiumFluids.EXPERIENCE.isOf(resource)

        override fun insert(resource: FluidResource, amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int {
            TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
            val amountIn: Int = this.amount
            if (amountIn == 0 || this.resource == resource) {
                if (isValid(resource)) {
                    val needed: Int = getNeeded(resource)
                    val inserted: Int = minOf(amount, needed)
                    val inserted1: Int = inserted - (inserted % HTExperienceHelper.getExpRatio())
                    if (inserted1 > 0) {
                        updateSnapshots(transaction)
                        player?.let { HTExperienceHelper.setPlayerExp(it, playerExp + HTExperienceHelper.expAmountFromFluid(inserted1)) }
                        return inserted1
                    }
                }
            }
            return 0
        }

        override fun extract(resource: FluidResource, amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int {
            TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
            if (this.resource == resource) {
                val amountIn: Int = this.amount
                val extracted: Int = minOf(amount, amountIn)
                val extracted1: Int = extracted - (extracted % HTExperienceHelper.getExpRatio())
                if (extracted1 > 0) {
                    updateSnapshots(transaction)
                    player?.let { HTExperienceHelper.setPlayerExp(it, playerExp - HTExperienceHelper.expAmountFromFluid(extracted1)) }
                    return extracted1
                }
            }
            return 0
        }

        override val resource: FluidResource
            get() = if (playerExp > 0) RagiumFluids.EXPERIENCE.asResourceOrEmpty() else FluidResource.EMPTY
        override val amount: Int
            get() = playerExp.let(HTExperienceHelper::fluidAmountFromExp).let(Ints::saturatedCast)

        override fun getCapacity(resource: FluidResource): Int = Int.MAX_VALUE
    }
}
