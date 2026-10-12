package hiiragi283.ragium.common.block.entity.misc

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.transfer.energy.HTBasicEnergyHandler
import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.transfer.holder.HTResourceSlotHolder
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.api.machine.HTMachineUpgradeHelper
import hiiragi283.ragium.api.machine.HTMachineUpgradeList
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.HTBlockEntityWithMenu
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.gui.HTWidgetSlot
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.world.inventory.StackCopySlot

class HTUpgradeStationBlockEntity(worldPosition: BlockPos, blockState: BlockState) :
    HTBlockEntity(RagiumBlockEntityTypes.UPGRADE_STATION.get(), worldPosition, blockState),
    HTBlockEntityWithMenu {

    lateinit var energyHandler: HTBasicEnergyHandler
        private set

    override fun initializeVariables(listener: Runnable) {
        super.initializeVariables(listener)
        energyHandler = HTBasicEnergyHandler.input(8000, listener) // TODO
    }

    lateinit var inputTank: HTBasicFluidTank
        private set

    override fun createFluidHandler(listener: Runnable): HTResourceSlotHolder<HTFluidTank> {
        inputTank = HTVariableFluidTank.create({ 4000 }, listener) // TODO
        return object : HTResourceSlotHolder<HTFluidTank> {
            override fun getSlots(side: Direction?): List<HTFluidTank> = listOf(inputTank)

            override fun canInsert(side: Direction?): Boolean = true

            override fun canExtract(side: Direction?): Boolean = true
        }
    }

    lateinit var toolSlot: HTBasicItemSlot

    override fun createItemHandler(listener: Runnable): HTResourceSlotHolder<HTItemSlot> {
        toolSlot = HTBasicItemSlot.create(
            listener,
            limit = 1,
            filter = { resource: ItemResource -> resource.`is`(RagiumTags.Items.SUPPORT_UPGRADES) }
        )
        return object : HTResourceSlotHolder<HTItemSlot> {
            override fun getSlots(side: Direction?): List<HTItemSlot> = listOf(toolSlot)

            override fun canInsert(side: Direction?): Boolean = false

            override fun canExtract(side: Direction?): Boolean = false
        }
    }

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        output.putChild(HTConstants.ENERGY, energyHandler)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        input.readChild(HTConstants.ENERGY, energyHandler)
    }

    /*override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean =
        useTransaction { transaction: Transaction ->
            // 液体の搬入
            val fluidMoved: Int = ResourceHandlerUtil.moveStacking(
                getFluidHandler(null),
                HTFluidCapabilities.getCapability(itemAccess),
                HTTransferPredicates.alwaysTrue(),
                Int.MAX_VALUE, // TODO,
                transaction
            )
            // 充電
            val energyMoved: Int = EnergyHandlerUtil.move(
                this.energyHandler.asForge(),
                HTEnergyCapabilities.getCapability(itemAccess),
                Int.MAX_VALUE, // TODO,
                transaction
            )
            val moved: Boolean = fluidMoved > 0 || energyMoved > 0
            if (moved) {
                transaction.commit()
            }
            moved
        }

    private val itemAccess = ToolItemAccess()

    private inner class ToolItemAccess : ItemAccess {
        override fun getResource(): ItemResource = toolSlot.resource

        override fun getAmount(): Int = toolSlot.amount

        override fun insert(resource: ItemResource, amount: Int, transaction: TransactionContext): Int =
            toolSlot.insert(resource, amount, transaction, HTTransferAccess.INTERNAL)

        override fun extract(resource: ItemResource, amount: Int, transaction: TransactionContext): Int =
            toolSlot.extract(resource, amount, transaction, HTTransferAccess.INTERNAL)
    }*/

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = false

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        // Energy
        widgetHolder += HTEnergySlotWidget(energyHandler, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1.5))
        widgetHolder.track(energyHandler)
        // Fluid
        widgetHolder += HTFluidWidget.Tank(
            inputTank,
            HTSlotHelper.getSlotPosX(1),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.BOTH,
            false
        )
        widgetHolder.track(inputTank)
        // Tool
        widgetHolder += HTItemWidget.Container(
            toolSlot,
            0,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.NONE
        )
        widgetHolder.track(toolSlot)
        // Upgrades
        for (index: Int in 0..3) {
            widgetHolder += HTItemWidget.Container(
                UpgradeSlot(index, HTSlotHelper.getSlotPosX(6 + index % 2), HTSlotHelper.getSlotPosY(0.5 + index / 2)),
                HTBackgroundType.NONE
            )
        }
    }

    //    UpgradeSlot    //

    inner class UpgradeSlot(slot: Int, x: Int, y: Int) :
        StackCopySlot(slot, x, y),
        HTWidgetSlot {
        private fun getUpgrades(): HTMachineUpgradeList = this@HTUpgradeStationBlockEntity
            .toolSlot
            .resource
            .let(HTMachineUpgradeHelper::getUpgrades)

        override fun mayPlace(itemStack: ItemStack): Boolean =
            !this@HTUpgradeStationBlockEntity.toolSlot.isEmpty && itemStack.has(RagiumDataComponents.UPGRADE)

        override fun getStackCopy(): ItemStack = getUpgrades()[slotIndex]

        override fun setStackCopy(stack: ItemStack) {
            val mutable = HTMachineUpgradeList.Mutable(getUpgrades())
            mutable[slotIndex] = stack
            val toolStack: ItemStack = this@HTUpgradeStationBlockEntity.toolSlot.getStackCopy()
            HTMachineUpgradeHelper.setUpgrades(toolStack, mutable)
            this@HTUpgradeStationBlockEntity.toolSlot.setStack(toolStack)
        }

        override fun onQuickCraft(picked: ItemStack, original: ItemStack): Unit = Unit

        override fun getMaxStackSize(): Int = 1

        override val slotType: HTBackgroundType = HTBackgroundType.NONE

        override fun toString(): String = "UpgradeSlot(slot=$slotIndex)"
    }
}
