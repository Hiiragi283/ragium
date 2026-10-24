package hiiragi283.ragium.common.block.entity.misc

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.transfer.HTSnapshotStackResourceSlot
import hiiragi283.lib.transfer.HTTransferPredicates
import hiiragi283.lib.transfer.holder.HTResourceSlotHolder
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.lib.transfer.item.HTItemStackResourceSlot
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
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.item.ItemResource

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
        HTSnapshotStackResourceSlot.Basic<ItemStack, ItemResource>(
            1,
            HTTransferPredicates.notExternal(),
            HTTransferPredicates.notExternal(),
            { !this@HTUpgradeStationBlockEntity.toolSlot.isEmpty && it.has(RagiumDataComponents.UPGRADE) },
            this@HTUpgradeStationBlockEntity::setOnlySave,
        ),
        HTItemStackResourceSlot {
        private fun getUpgrades(): HTMachineUpgradeList = this@HTUpgradeStationBlockEntity
            .toolSlot
            .resource
            .let(HTMachineUpgradeHelper::getUpgrades)

        override fun getStackCopy(): ItemStack = getUpgrades()[slotIndex]

        override fun setStack(stack: ItemStack) {
            setStackInternal(stack)
        }

        override fun setStackInternal(stack: ItemStack) {
            val mutable = HTMachineUpgradeList.Mutable(getUpgrades())
            mutable[slotIndex] = stack
            val toolStack: ItemStack = this@HTUpgradeStationBlockEntity.toolSlot.getStackCopy()
            HTMachineUpgradeHelper.setUpgrades(toolStack, mutable)
            this@HTUpgradeStationBlockEntity.toolSlot.setStack(toolStack)
        }

        override fun serialize(output: ValueOutput): Unit = Unit

        override fun deserialize(input: ValueInput): Unit = Unit
    }
}
