package hiiragi283.ragium.common.block.entity.misc

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.serialization.value.EmptyValueIOSerializable
import hiiragi283.lib.transfer.HTSnapshotStackResourceSlot
import hiiragi283.lib.transfer.HTStackResourceHelper
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.holder.HTResourceSlotHolder
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.lib.transfer.item.HTItemStackSlot
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.api.machine.HTMachineUpgradeHelper
import hiiragi283.ragium.api.machine.HTMachineUpgradeList
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.HTBlockEntityWithMenu
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.gui.slot.HTItemContainerSlot
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class HTUpgradeStationBlockEntity(worldPosition: BlockPos, blockState: BlockState) :
    HTBlockEntity(RagiumBlockEntityTypes.UPGRADE_STATION.get(), worldPosition, blockState),
    HTBlockEntityWithMenu {

    lateinit var toolSlot: HTBasicItemSlot

    override fun createItemHandler(listener: Runnable): HTResourceSlotHolder<HTItemSlot> {
        toolSlot = HTBasicItemSlot.create(
            listener,
            limit = 1,
            filter = { resource: ItemResource -> resource.`is`(RagiumTags.Items.SUPPORT_UPGRADES) },
        )
        return object : HTResourceSlotHolder<HTItemSlot> {
            override fun getSlots(side: Direction?): List<HTItemSlot> = listOf(toolSlot)

            override fun canInsert(side: Direction?): Boolean = false

            override fun canExtract(side: Direction?): Boolean = false
        }
    }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = false

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        // Tool
        widgetHolder += HTItemWidget.Container(
            toolSlot,
            0,
            HTSlotHelper.getSlotPosX(4),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.NONE,
        )
        widgetHolder.track(toolSlot)
        // Upgrades
        for (index: Int in 0..3) {
            widgetHolder += HTItemWidget.Container(
                HTItemContainerSlot.create(
                    UpgradeItemSlot(index),
                    HTSlotHelper.getSlotPosX(6 + index % 2),
                    HTSlotHelper.getSlotPosY(0.5 + index / 2),
                ),
                HTBackgroundType.NONE,
            )
        }
    }

    //    UpgradeItemSlot    //

    inner class UpgradeItemSlot(val slotIndex: Int) :
        HTSnapshotStackResourceSlot<ItemStack, ItemResource>(),
        HTItemStackSlot,
        EmptyValueIOSerializable {
        private fun getUpgrades(): HTMachineUpgradeList = this@HTUpgradeStationBlockEntity
            .toolSlot
            .resource
            .let(HTMachineUpgradeHelper::getUpgrades)

        private fun setUpgrade(mutable: HTMachineUpgradeList.Mutable) {
            val toolStack: ItemStack = this@HTUpgradeStationBlockEntity.toolSlot.getStackCopy()
            HTMachineUpgradeHelper.setUpgrades(toolStack, mutable)
            this@HTUpgradeStationBlockEntity.toolSlot.setStack(toolStack)
        }

        override fun getStackCopy(): ItemStack = getUpgrades()[slotIndex]

        override fun setStack(stack: ItemStack, transaction: TransactionContext?) {
            val oldStack: ItemStack = getStackCopy()
            if (!getHelper().isSame(stack, oldStack)) {
                val mutable = HTMachineUpgradeList.Mutable(getUpgrades())
                transaction?.let {
                    updateSnapshots(it)
                    mutable[slotIndex] = stack
                    setUpgrade(mutable)
                } ?: run {
                    mutable[slotIndex] = stack
                    setUpgrade(mutable)
                    onRootCommit(oldStack)
                }
            }
        }

        override fun getHelper(): HTStackResourceHelper.Item = HTStackResourceHelper.Item

        override fun isValidForInsert(resource: ItemResource, access: HTTransferAccess): Boolean =
            isValid(resource) && access != HTTransferAccess.EXTERNAL

        override fun canResourceExtract(resource: ItemResource, access: HTTransferAccess): Boolean = access != HTTransferAccess.EXTERNAL

        override fun revertToSnapshot(snapshot: ItemStack) {
            val mutable = HTMachineUpgradeList.Mutable(getUpgrades())
            mutable[slotIndex] = snapshot
            setUpgrade(mutable)
        }

        override fun onRootCommit(originalState: ItemStack) {
            if (!getHelper().isSame(originalState, getStackCopy())) {
                this@HTUpgradeStationBlockEntity.setOnlySave()
            }
        }

        override fun isValid(resource: ItemResource): Boolean =
            !this@HTUpgradeStationBlockEntity.toolSlot.isEmpty && resource.has(RagiumDataComponents.UPGRADE)

        override fun getCapacity(resource: ItemResource): Int = 1
    }
}
