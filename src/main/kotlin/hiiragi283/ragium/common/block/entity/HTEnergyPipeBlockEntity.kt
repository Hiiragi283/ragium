package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.common.transfer.energy.HTEnergyNetwork
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class HTEnergyPipeBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(RagiumBlockEntityTypes.ENERGY_PIPE.get(), pos, state) {
    private val budget = TransferBudget()
    private val inputs: Map<Direction, EnergyHandler> = Direction.entries.associateWith(::Inlet)

    fun getEnergyInput(side: Direction?): EnergyHandler? = if (level is ServerLevel && !isRemoved) inputs[side] else null

    override fun onLoad() {
        super.onLoad()
        val serverLevel: ServerLevel = level as? ServerLevel ?: return
        serverLevel.scheduleTick(blockPos, blockState.block, 1)
    }

    override fun setRemoved() {
        (level as? ServerLevel)?.let { HTEnergyNetwork.onPipeChanged(it, blockPos) }
        super.setRemoved()
    }

    private inner class Inlet(private val side: Direction) : EnergyHandler {
        override fun getAmountAsLong(): Long = 0

        override fun getCapacityAsLong(): Long = RagiumConfig.SERVER.energyPipeRate.get().toLong()

        override fun insert(amount: Int, transaction: TransactionContext): Int {
            TransferPreconditions.checkNonNegative(amount)
            val serverLevel: ServerLevel = level as? ServerLevel ?: return 0
            if (amount == 0 || isRemoved) return 0

            val tick: Long = serverLevel.gameTime
            val limit: Int = minOf(amount, budget.remaining(tick))
            if (limit == 0) return 0
            val moved: Int = HTEnergyNetwork[serverLevel].forward(blockPos, side, limit, transaction)
            if (moved > 0) budget.consume(tick, moved, transaction)
            return moved
        }

        override fun extract(amount: Int, transaction: TransactionContext): Int {
            TransferPreconditions.checkNonNegative(amount)
            return 0
        }
    }

    private data class Usage(val tick: Long, val amount: Int)

    private class TransferBudget : SnapshotJournal<Usage>() {
        private var usage = Usage(Long.MIN_VALUE, 0)

        private fun usedAt(tick: Long): Int = if (usage.tick == tick) usage.amount else 0

        fun remaining(tick: Long): Int = (RagiumConfig.SERVER.energyPipeRate.get() - usedAt(tick)).coerceAtLeast(0)

        fun consume(tick: Long, amount: Int, transaction: TransactionContext) {
            updateSnapshots(transaction)
            usage = Usage(tick, usedAt(tick) + amount)
        }

        override fun createSnapshot(): Usage = usage

        override fun revertToSnapshot(snapshot: Usage) {
            usage = snapshot
        }
    }
}
