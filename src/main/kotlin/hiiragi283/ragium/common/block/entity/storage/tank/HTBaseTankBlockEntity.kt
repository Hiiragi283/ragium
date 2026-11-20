package hiiragi283.ragium.common.block.entity.storage.tank

import hiiragi283.lib.transfer.HTResourceSlotUtil
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.ragium.api.machine.HTMachineUpgradeHelper
import hiiragi283.ragium.common.block.entity.HTConfigurableBlockEntity
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

abstract class HTBaseTankBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : HTConfigurableBlockEntity(type, pos, state) {
    abstract val tank: HTFluidTank

    abstract override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable)

    protected fun getTankCapacity(): Int = HTMachineUpgradeHelper.modifyCapacity(components(), RagiumConfig.SERVER.tankCapacity)

    override fun markDirtyComparator() {
        level?.updateNeighbourForOutputSignal(blockPos, blockState.block)
    }

    override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int = HTResourceSlotUtil.calculateRedstoneLevel(tank)

    //    Sync    //

    override fun writeReducedUpdateTag(output: ValueOutput) {
        super.writeReducedUpdateTag(output)
        tank.serialize(output)
    }

    override fun readUpdateTag(input: ValueInput) {
        super.readUpdateTag(input)
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
