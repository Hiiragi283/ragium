package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.base.HTSingleItemRecipe
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.amount.HTAmountView
import hiiragi283.core.api.storage.energy.HTEnergyBattery
import hiiragi283.core.api.storage.holder.HTEnergyBatteryHolder
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.recipe.HCRecipeLookups
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.HTLookupRecipeCache
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.core.impl.recipe.handler.HTItemOutputHandler
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.common.storge.energy.HTVariableEnergyBattery
import hiiragi283.ragium.common.storge.holder.HTBasicEnergyBatteryHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentMap
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

open class HTBatteryBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTStorageBlockEntity(type, pos, state) {
    constructor(pos: BlockPos, state: BlockState) : this(RagiumBlockEntityTypes.BATTERY, pos, state)

    lateinit var battery: HTEnergyBattery.Basic
        private set

    final override fun createEnergyHandler(listener: HTContentListener): HTEnergyBatteryHolder? {
        val builder = HTBasicEnergyBatteryHolder.Builder(this)
        battery = builder.addSlot(
            HTSlotInfo.BOTH,
            createBattery {
                checkRecipe = true
                listener.onContentsChanged()
            },
        )
        return builder.build()
    }

    protected open fun createBattery(listener: HTContentListener): HTEnergyBattery.Basic =
        HTVariableEnergyBattery.create(listener) { capacityComponent.getCapacity(RagiumConfig.COMMON.batteryCapacity) }

    final override fun getAmountView(): HTAmountView = battery

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
        widgetHolder += HTEnergySlotWidget(battery, HTSlotHelper.getSlotPosX(4), HTSlotHelper.getSlotPosY(0))

        widgetHolder += HTItemSlotWidget.container(
            inputSlot,
            HTSlotHelper.getSlotPosX(1.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
        )
        widgetHolder += HTItemSlotWidget.container(
            outputSlot,
            HTSlotHelper.getSlotPosX(6.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.OUTPUT,
        )
    }

    //    Sync    //

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        componentInput.get(HCDataComponents.ENERGY)?.let(battery::setAmount)
    }

    override fun collectImplicitComponents(builder: DataComponentMap.Builder) {
        super.collectImplicitComponents(builder)
        val amount: Int = battery.getAmount()
        if (amount > 0) {
            builder.set(HCDataComponents.ENERGY, amount)
        }
    }

    override fun initReducedUpdateTag(output: HTValueOutput) {
        super.initReducedUpdateTag(output)
        battery.serialize(output)
    }

    override fun handleUpdateTag(input: HTValueInput) {
        super.handleUpdateTag(input)
        battery.deserialize(input)
    }

    //    Recipe    //

    private var checkRecipe: Boolean = false

    private val cache: HTLookupRecipeCache<SingleRecipeInput, HTSingleItemRecipe> = HTLookupRecipeCache.forRecipe(HCRecipeLookups.CHARGING)
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
        val stack: ItemStack = inputHandler.getItemStack()
        if (stack.isEmpty) return false
        val input = SingleRecipeInput(stack)
        val recipe: HTSingleItemRecipe = cache.getFirstRecipe(input, level) ?: return false

        val result: ItemStack = recipe.assemble(input, level.registryAccess())
        if (!outputHandler.canInsert(result)) return false
        val energy = 1_000_000 // TODO
        if (battery.extract(energy, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) < energy) return false
        outputHandler.insert(result)
        inputHandler.consume(recipe.getRequiredAmount(input))
        battery.extract(energy, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        return true
    }
}
