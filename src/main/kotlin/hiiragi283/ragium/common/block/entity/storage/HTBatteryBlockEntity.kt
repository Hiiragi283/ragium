package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.sync.HTSyncType
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.serialization.value.read
import hiiragi283.core.api.serialization.value.write
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.amount.HTAmountView
import hiiragi283.core.api.storage.energy.HTEnergyHandler
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCRecipeLookups
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.support.gui.sync.HTIntSyncSlot
import hiiragi283.core.support.gui.sync.HTItemSyncSlot
import hiiragi283.core.support.recipe.cache.HTRecipeCaches
import hiiragi283.core.support.recipe.handler.HTItemInputHandler
import hiiragi283.core.support.recipe.handler.HTItemOutputHandler
import hiiragi283.core.support.storage.energy.HTBasicEnergyHandler
import hiiragi283.core.support.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.support.storage.energy.HTVariableEnergyHandler
import hiiragi283.ragium.support.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.support.storage.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentMap
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class HTBatteryBlockEntity<T : HTEnergyHandler>(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : HTStorageBlockEntity(type, pos, state) {
    abstract val handler: T

    override fun getAmountView(): HTAmountView = handler

    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemHandler(listener: HTContentListener): HTItemSlotHolder? {
        val listener = HTContentListener {
            checkRecipe = true
            listener.onContentsChanged()
        }
        val builder = HTBasicItemSlotHolder.Builder(this)
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
        return builder.build()
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        widgetHolder.rows = 1
        // slot
        widgetHolder += HTEnergySlotWidget(handler, HTSlotHelper.getSlotPosX(4), HTSlotHelper.getSlotPosY(0))

        widgetHolder += HTItemWidget.Container(
            inputSlot,
            HTSlotHelper.getSlotPosX(1.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(HTItemSyncSlot(inputSlot), HTSyncType.S2C)
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            HTSlotHelper.getSlotPosX(6.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder.track(HTItemSyncSlot(outputSlot), HTSyncType.S2C)
    }

    //    Recipe    //

    protected var checkRecipe: Boolean = false
    private val cache: HTRecipeCaches.SingleItem<HCChargingRecipe> = HTRecipeCaches.SingleItem(HCRecipeLookups.CHARGING)
    private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }
    private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        if (checkRecipe) {
            checkRecipe = false
            if (chargeItem()) return true
        }
        return super.onUpdateServer(level, pos, state)
    }

    private fun chargeItem(): Boolean {
        val level: Level = this.level ?: return false
        val input: ItemStack = inputHandler.getItemStack()
        val recipe: HCChargingRecipe = cache.findFirstRecipe(input, level) ?: return false

        val result: ItemStack = recipe.apply(input)
        if (!outputHandler.canInsert(result)) return false
        val requiredEnergy: Int = recipe.energy
        if (handler.extract(requiredEnergy, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) < requiredEnergy) return false
        outputHandler.insert(result)
        inputHandler.consume(1)
        handler.extract(requiredEnergy, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        return true
    }

    //    Simple    //

    class Simple(pos: BlockPos, state: BlockState) : HTBatteryBlockEntity<HTBasicEnergyHandler>(RagiumBlockEntityTypes.BATTERY.get(), pos, state) {
        override lateinit var handler: HTBasicEnergyHandler

        override fun initializeVariables(listener: HTContentListener) {
            super.initializeVariables(listener)
            handler = HTVariableEnergyHandler.create(
                {
                    checkRecipe = true
                    listener.onContentsChanged()
                },
                {
                    capacityComponent.getCapacity(RagiumConfig.SERVER.batteryCapacity)
                },
            )
        }

        override fun setupMenu(widgetHolder: HTWidgetHolder) {
            super.setupMenu(widgetHolder)
            widgetHolder.track(HTIntSyncSlot.create(handler), HTSyncType.S2C)
        }

        //    Sync    //

        override fun applyImplicitComponents(componentInput: DataComponentInput) {
            super.applyImplicitComponents(componentInput)
            componentInput.get(HCDataComponents.ENERGY)?.let(handler::setAmount)
        }

        override fun collectImplicitComponents(builder: DataComponentMap.Builder) {
            super.collectImplicitComponents(builder)
            val amount: Int = handler.getAmount()
            if (amount > 0) {
                builder.set(HCDataComponents.ENERGY, amount)
            }
        }

        override fun writeValue(output: HTValueOutput) {
            super.writeValue(output)
            output.write(HTConst.ENERGY, handler)
        }

        override fun readValue(input: HTValueInput) {
            super.readValue(input)
            input.read(HTConst.ENERGY, handler)
            // migration
            input.child("batteries")?.read(HTConst.SLOT, handler)
        }

        override fun initReducedUpdateTag(output: HTValueOutput) {
            super.initReducedUpdateTag(output)
            handler.serialize(output)
        }

        override fun handleUpdateTag(input: HTValueInput) {
            super.handleUpdateTag(input)
            handler.deserialize(input)
        }
    }
}
