package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTContentListener
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
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import org.apache.commons.lang3.math.Fraction

open class HTCrateBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTUpgradableBlockEntity(type, pos, state) {
    constructor(pos: BlockPos, state: BlockState) : this(RagiumBlockEntityTypes.CRATE, pos, state)

    lateinit var slot: HTBasicItemSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        slot = builder.addSlot(HTSlotInfo.BOTH, CrateItemSlot(listener))
    }

    protected fun getCapacity(): Int = HTUpgradeHelper.getItemCapacity(this, RagiumConfig.COMMON.crateCapacity.asInt)

    override fun markDirtyComparator() {
        level?.updateNeighbourForOutputSignal(blockPos, blockState.block)
    }

    final override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        HTStackSlotHelper.calculateRedstoneLevel(slot)

    //    Ticking    //

    private var oldScale: Fraction = Fraction.ZERO

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        val scale: Fraction = slot.getStoredLevel()
        if (scale != this.oldScale) {
            this.oldScale = scale
            return true
        }
        return false
    }

    //    CrateItemSlot    //

    /**
     * @see mekanism.common.inventory.slot.BinInventorySlot
     */
    protected inner class CrateItemSlot(listener: HTContentListener) :
        HTBasicItemSlot(
            getCapacity(),
            HTStoragePredicates.alwaysTrueBi(),
            HTStoragePredicates.alwaysTrueBi(),
            { resource: HTItemResourceType -> resource.toStack().canFitInsideContainerItems() },
            listener,
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
