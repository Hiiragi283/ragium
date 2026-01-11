package hiiragi283.ragium.common.block.entity.storage

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.util.HTStackSlotHelper
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.common.block.entity.HTUpgradableBlockEntity
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

open class HTCrateBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTUpgradableBlockEntity(type, pos, state) {
    constructor(pos: BlockPos, state: BlockState) : this(RagiumBlockEntityTypes.CRATE, pos, state)

    @DescSynced
    @Persisted(subPersisted = true)
    val slot: HTBasicItemSlot = CrateItemSlot()

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder) {
        builder.addSlot(HTSlotInfo.BOTH, slot)
    }

    protected fun getCapacity(): Int = HTUpgradeHelper.getItemCapacity(this, RagiumConfig.COMMON.crateCapacity.asInt)

    override fun markDirtyComparator() {
        level?.updateNeighbourForOutputSignal(blockPos, blockState.block)
    }

    final override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        HTStackSlotHelper.calculateRedstoneLevel(slot)

    final override fun setupElements(root: UIElement) {}

    //    CrateItemSlot    //

    /**
     * @see mekanism.common.inventory.slot.BinInventorySlot
     */
    protected inner class CrateItemSlot :
        HTBasicItemSlot(
            getCapacity(),
            HTStoragePredicates.alwaysTrueBi(),
            HTStoragePredicates.alwaysTrueBi(),
            { resource: HTItemResourceType -> resource.toStack().canFitInsideContainerItems() },
        ) {
        private val isCreative: Boolean get() = this@HTCrateBlockEntity.isCreative()

        override fun insert(
            resource: HTItemResourceType?,
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

        override fun getCapacity(resource: HTItemResourceType?): Int = when (isCreative) {
            true -> Int.MAX_VALUE
            false -> this@HTCrateBlockEntity.getCapacity()
        }

        override fun getAmount(): Int = when (isCreative) {
            true -> Int.MAX_VALUE
            false -> super.getAmount()
        }

        override fun outputRate(access: HTStorageAccess): Int = when {
            access == HTStorageAccess.MANUAL && isCreative -> getItemStack().maxStackSize
            else -> super.outputRate(access)
        }
    }
}
