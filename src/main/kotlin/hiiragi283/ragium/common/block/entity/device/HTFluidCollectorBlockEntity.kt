package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack

abstract class HTFluidCollectorBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity.Tickable(blockHolder, pos, state) {
    lateinit var tank: HTVariableFluidStackTank
        private set

    final override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        val builder: HTBasicFluidTankHolder.Builder = HTBasicFluidTankHolder.builder(this)
        tank = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTVariableFluidStackTank.output(listener, RagiumConfig.COMMON.deviceCollectorTankCapacity),
        )
        return builder.build()
    }

    final override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        HTStackSlotHelper.calculateRedstoneLevel(tank)

    //    Ticking    //

    final override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 液体を生成できるかチェック
        val stack: ImmutableFluidStack = getGeneratedFluid(level, pos).toImmutable() ?: return false
        // 液体を搬入できるかチェック
        if (tank.insert(stack, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) != null) return false
        tank.insert(stack, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        playSound(level, pos)
        return true
    }

    protected abstract fun getGeneratedFluid(level: ServerLevel, pos: BlockPos): FluidStack

    protected abstract fun playSound(level: ServerLevel, pos: BlockPos)
}
