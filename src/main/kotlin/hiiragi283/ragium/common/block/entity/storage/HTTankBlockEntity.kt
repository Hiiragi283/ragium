package hiiragi283.ragium.common.block.entity.storage

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import com.lowdragmc.lowdraglib2.syncdata.annotation.RequireRerender
import hiiragi283.core.api.gui.element.alineCenter
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.util.HTStackSlotHelper
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.common.block.entity.HTUpgradableBlockEntity
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

/**
 * @see mekanism.common.tile.TileEntityFluidTank
 */
open class HTTankBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTUpgradableBlockEntity(type, pos, state) {
    constructor(pos: BlockPos, state: BlockState) : this(RagiumBlockEntityTypes.TANK, pos, state)

    @DescSynced
    @Persisted(subPersisted = true)
    @RequireRerender
    val tank: HTBasicFluidTank = createTank()

    protected open fun createTank(): HTBasicFluidTank = TankFluidTank()

    final override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder) {
        builder.addSlot(HTSlotInfo.BOTH, tank)
    }

    protected fun getCapacity(): Int = HTUpgradeHelper.getFluidCapacity(this, RagiumConfig.COMMON.tankCapacity.asInt)

    final override fun markDirtyComparator() {
        level?.updateNeighbourForOutputSignal(blockPos, blockState.block)
    }

    final override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        HTStackSlotHelper.calculateRedstoneLevel(tank)

    override fun setupMainTab(root: UIElement) {
        root
            .addChild(
                UIElement()
                    .alineCenter()
                    .addChild(createFluidSlot(0))
            )
    }

    //    TankFluidTank    //

    /**
     * @see mekanism.common.capabilities.fluid.FluidTankFluidTank
     */
    protected inner class TankFluidTank :
        HTBasicFluidTank(
            getCapacity(),
            HTStoragePredicates.alwaysTrueBi(),
            HTStoragePredicates.alwaysTrueBi(),
            { !isCreative() },
        ) {
        private val isCreative: Boolean get() = this@HTTankBlockEntity.isCreative()

        override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int =
            super.extract(amount, action.combine(!isCreative), access)

        override fun getCapacity(resource: HTFluidResourceType?): Int = when (isCreative) {
            true -> Int.MAX_VALUE
            false -> this@HTTankBlockEntity.getCapacity()
        }

        override fun getAmount(): Int = when (isCreative) {
            true -> Int.MAX_VALUE
            false -> super.getAmount()
        }
    }
}
