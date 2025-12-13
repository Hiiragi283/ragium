package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.HTUpgradableBlockEntity
import hiiragi283.ragium.common.inventory.HTContainerItemSlot
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

open class HTCrateBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTUpgradableBlockEntity(blockHolder, pos, state) {
    constructor(pos: BlockPos, state: BlockState) : this(RagiumBlocks.CRATE, pos, state)

    lateinit var slot: HTBasicItemSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        slot = builder.addSlot(HTSlotInfo.BOTH, CrateItemSlot(listener))
    }

    protected fun getCapacity(): Int = HTUpgradeHelper.getItemCapacity(this, RagiumConfig.COMMON.crateCapacity.asInt)

    final override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        HTStackSlotHelper.calculateRedstoneLevel(slot)

    //    Ticking    //
    
    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = false

    //    CrateItemSlot    //

    /**
     * @see mekanism.common.inventory.slot.BinInventorySlot
     */
    protected inner class CrateItemSlot(listener: HTContentListener) :
        HTBasicItemSlot(
            getCapacity(),
            HTPredicates.alwaysTrueBi(),
            HTPredicates.alwaysTrueBi(),
            { stack: ImmutableItemStack -> stack.unwrap().canFitInsideContainerItems() },
            listener,
            HTSlotHelper.getSlotPosX(4),
            HTSlotHelper.getSlotPosY(1),
            HTContainerItemSlot.Type.BOTH,
        ) {
        private val isCreative: Boolean get() = this@HTCrateBlockEntity.isCreative()

        override fun insert(stack: ImmutableItemStack?, action: HTStorageAction, access: HTStorageAccess): ImmutableItemStack? {
            val remainder: ImmutableItemStack?
            if (isCreative && this.getStack() == null && action.execute && access != HTStorageAccess.EXTERNAL) {
                remainder = super.insert(stack, HTStorageAction.SIMULATE, access)
                if (remainder == null) {
                    setStackUnchecked(stack?.copyWithAmount(getCapacity()))
                }
            } else {
                remainder = super.insert(stack, action.combine(!isCreative), access)
            }
            return remainder
        }

        override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): ImmutableItemStack? =
            super.extract(amount, action.combine(!isCreative), access)

        override fun getCapacity(stack: ImmutableItemStack?): Int = this@HTCrateBlockEntity.getCapacity()
    }
}
