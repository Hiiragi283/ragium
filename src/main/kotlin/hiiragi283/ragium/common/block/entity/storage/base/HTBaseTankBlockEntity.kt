package hiiragi283.ragium.common.block.entity.storage.base

import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.transfer.holder.HTResourceSlotHolder
import hiiragi283.ragium.api.util.HTStorageHelper
import hiiragi283.ragium.common.block.entity.HTConfigurableBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

abstract class HTBaseTankBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(type, pos, state) {
    abstract val tank: HTFluidTank

    override fun createFluidHandler(listener: Runnable): HTResourceSlotHolder<HTFluidTank> {
        initTank(listener)
        return object : HTResourceSlotHolder<HTFluidTank> {
            override fun getSlots(side: Direction?): List<HTFluidTank> = listOf(tank)

            override fun canInsert(side: Direction?): Boolean = true

            override fun canExtract(side: Direction?): Boolean = true
        }
    }

    protected abstract fun initTank(listener: Runnable)

    override fun markDirtyComparator() {
        level?.updateNeighbourForOutputSignal(blockPos, blockState.block)
    }

    override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        HTStorageHelper.calculateRedstoneLevel(tank)

    //    Sync    //

    override fun writeReducedUpdateTag(output: ValueOutput) {
        super.writeReducedUpdateTag(output)
        tank.serialize(output)
    }

    override fun readUpdateTag(input: ValueInput) {
        tank.deserialize(input)
    }

    //    Ticking    //

    private var oldScale: Float = 0f

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 保持する量の変化があれば更新させる
        val scale: Float = tank.currentFilledLevel
        if (scale != this.oldScale) {
            this.oldScale = scale
            return true
        }
        return false
    }
}
