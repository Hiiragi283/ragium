package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class HTFluidCollectorBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity.Tickable(blockHolder, pos, state) {
    lateinit var tank: HTVariableFluidStackTank
        private set

    final override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        // output
        tank = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTVariableFluidStackTank.output(listener, RagiumConfig.COMMON.deviceCollectorTankCapacity),
        )
    }

    final override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        HTStackSlotHelper.calculateRedstoneLevel(tank)
}
