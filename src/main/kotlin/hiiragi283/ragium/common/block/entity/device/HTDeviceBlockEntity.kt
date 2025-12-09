package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.math.fraction
import hiiragi283.ragium.common.block.entity.HTConfigurableBlockEntity
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.inventory.container.HTContainerMenu
import hiiragi283.ragium.common.inventory.slot.HTIntSyncSlot
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import org.apache.commons.lang3.math.Fraction

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
        override fun addMenuTrackers(menu: HTContainerMenu) {
            super.addMenuTrackers(menu)
            // Progress
            menu.track(HTIntSyncSlot.create(::ticks))
        }

        override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = if (ticks >= getTickRate()) {
            ticks = 0
            actionServer(level, pos, state)
        } else {
            false
        }

        protected abstract fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean

        protected open fun getTickRate(): Int = 20

        fun getProgress(): Fraction {
            val totalTick: Int = ticks
            val maxTicks = 20
            return fraction(totalTick, maxTicks)
        }
    }
}
