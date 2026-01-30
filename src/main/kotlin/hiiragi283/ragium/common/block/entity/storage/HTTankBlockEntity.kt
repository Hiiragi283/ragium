package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.storage.amount.HTAmountView
import hiiragi283.core.api.storage.fluid.HTMutableFluidTank
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

/**
 * @see mekanism.common.tile.TileEntityFluidTank
 */
open class HTTankBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTStorageBlockEntity(type, pos, state) {
    constructor(pos: BlockPos, state: BlockState) : this(RagiumBlockEntityTypes.TANK, pos, state)

    lateinit var tank: HTMutableFluidTank
        private set

    final override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        tank = builder.addSlot(HTSlotInfo.BOTH, createTank(listener))
    }

    protected open fun createTank(listener: HTContentListener): HTMutableFluidTank =
        HTVariableFluidTank.create(listener) { HTUpgradeHelper.getFluidCapacity(this, RagiumConfig.COMMON.tankCapacity.asInt) }

    final override fun getAmountView(): HTAmountView = tank
}
