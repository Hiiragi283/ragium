package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.HTConfigurableBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.storage.fluid.tank.HTBasicFluidTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class HTDrumBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(blockHolder, pos, state) {
    lateinit var tank: HTBasicFluidTank
        private set

    final override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        tank = builder.addSlot(HTSlotInfo.BOTH, createTank(listener))
    }

    protected abstract fun createTank(listener: HTContentListener): HTBasicFluidTank

    lateinit var slot: HTBasicItemSlot
        private set

    final override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        slot = builder.addSlot(
            HTSlotInfo.CATALYST,
            HTBasicItemSlot.create(
                listener,
                HTSlotHelper.getSlotPosX(2),
                HTSlotHelper.getSlotPosY(1),
                canExtract = HTPredicates.manualOnly(),
                canInsert = HTPredicates.manualOnly(),
            ),
        )
    }

    override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int = HTStackSlotHelper.calculateRedstoneLevel(tank)

    //    Ticking    //

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean =
        HTStackSlotHelper.moveFluid(slot, slot::setStackUnchecked, tank)
}
