package hiiragi283.ragium.common.block.entity.storage

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.capability.HTFluidCapabilities
import hiiragi283.core.api.capability.tankRange
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.util.HTStackSlotHelper
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.common.block.entity.HTUpgradableBlockEntity
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

/**
 * @see mekanism.common.tile.TileEntityFluidTank
 */
open class HTTankBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTUpgradableBlockEntity(type, pos, state) {
    constructor(pos: BlockPos, state: BlockState) : this(RagiumBlockEntityTypes.TANK, pos, state)

    @DescSynced
    @Persisted(subPersisted = true)
    val tank: HTBasicFluidTank = TankFluidTank()

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder) {
        builder.addSlot(HTSlotInfo.BOTH, tank)
    }

    protected fun getCapacity(): Int = HTUpgradeHelper.getFluidCapacity(this, RagiumConfig.COMMON.tankCapacity.asInt)

    @DescSynced
    @Persisted(subPersisted = true)
    val emptySlot: HTBasicItemSlot = HTBasicItemSlot.input(
        canInsert = HTFluidCapabilities::hasCapability,
    )

    @DescSynced
    @Persisted(subPersisted = true)
    val fillSlot: HTBasicItemSlot = HTBasicItemSlot.input(
        canInsert = { resource: HTItemResourceType ->
            val handler: IFluidHandlerItem = HTFluidCapabilities.getCapability(resource) ?: return@input false
            for (i: Int in handler.tankRange) {
                if (!handler.getFluidInTank(i).isEmpty) return@input false
            }
            true
        },
    )

    @DescSynced
    @Persisted(subPersisted = true)
    val outputSlot: HTBasicItemSlot = HTBasicItemSlot.output()

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder) {
        builder.addSlot(HTSlotInfo.INPUT, emptySlot)
        builder.addSlot(HTSlotInfo.EXTRA_INPUT, fillSlot)
        builder.addSlot(HTSlotInfo.OUTPUT, outputSlot)
    }

    override fun markDirtyComparator() {
        level?.updateNeighbourForOutputSignal(blockPos, blockState.block)
    }

    final override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        HTStackSlotHelper.calculateRedstoneLevel(tank)

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState) {
        // スロットから液体を搬入する
        if (HTStackSlotHelper.moveFluid(emptySlot, outputSlot::setStack, tank)) return
        // スロットに液体を搬出する
    }

    override fun setupElements(root: UIElement) {}

    //    TankFluidTank    //

    /**
     * @see mekanism.common.capabilities.fluid.FluidTankFluidTank
     */
    protected inner class TankFluidTank :
        HTBasicFluidTank(
            getCapacity(),
            HTStoragePredicates.alwaysTrueBi(),
            HTStoragePredicates.alwaysTrueBi(),
            HTStoragePredicates.alwaysTrue(),
        ) {
        private val isCreative: Boolean get() = this@HTTankBlockEntity.isCreative()

        override fun insert(
            resource: HTFluidResourceType?,
            amount: Int,
            action: HTStorageAction,
            access: HTStorageAccess,
        ): Int {
            val remainder: Int
            if (isCreative && this.getResource() == null && action.execute() && access != HTStorageAccess.EXTERNAL) {
                remainder = super.insert(resource, amount, HTStorageAction.SIMULATE, access)
                if (remainder == 0) {
                    setResource(resource)
                    setAmount(getCapacity())
                }
            } else {
                remainder = super.insert(resource, amount, action.combine(!isCreative), access)
            }
            return remainder
        }

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
