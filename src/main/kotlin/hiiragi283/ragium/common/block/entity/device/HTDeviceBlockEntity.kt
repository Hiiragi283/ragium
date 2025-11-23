package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.common.block.entity.HTConfigurableBlockEntity
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

/**
 * 電力を消費しない設備に使用される[HTConfigurableBlockEntity]の拡張クラス
 */
abstract class HTDeviceBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(blockHolder, pos, state) {
    //    Tickable    //

    /**
     * 周期的にtick処理を行う[HTDeviceBlockEntity]の拡張クラス
     */
    abstract class Tickable(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) : HTDeviceBlockEntity(blockHolder, pos, state) {
        override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = if (ticks >= getTickRate()) {
            ticks = 0
            actionServer(level, pos, state)
        } else {
            false
        }

        protected abstract fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean

        protected open fun getTickRate(): Int = 20

        val progress: Float
            get() {
                val totalTick: Int = ticks
                val maxTicks = 20
                val fixedTotalTicks: Int = totalTick % maxTicks
                return Mth.clamp(fixedTotalTicks / maxTicks.toFloat(), 0f, 1f)
            }
    }
}
