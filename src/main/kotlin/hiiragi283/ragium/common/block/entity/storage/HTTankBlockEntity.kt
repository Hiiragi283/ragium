package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.data.tank.HTTankInteraction
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.storage.amount.HTAmountView
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTMutableFluidTank
import hiiragi283.core.api.storage.holder.HTFluidTankHolder
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.capability.HTFluidCapabilities
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.recipe.HCRecipeLookups
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.handler.HTFluidInputHandler
import hiiragi283.core.impl.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.core.impl.recipe.handler.HTItemOutputHandler
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidActionResult
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil
import java.util.function.Predicate

/**
 * @see mekanism.common.tile.TileEntityFluidTank
 */
open class HTTankBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTStorageBlockEntity(type, pos, state) {
    constructor(pos: BlockPos, state: BlockState) : this(RagiumBlockEntityTypes.TANK, pos, state)

    lateinit var tank: HTMutableFluidTank
        private set

    final override fun createFluidHandler(listener: HTContentListener): HTFluidTankHolder? {
        val builder = HTBasicFluidTankHolder.Builder(this)
        val listener = HTContentListener {
            checkRecipe = true
            listener.onContentsChanged()
        }
        tank = builder.addSlot(HTSlotInfo.BOTH, createTank(listener))
        return builder.build()
    }

    protected open fun createTank(listener: HTContentListener): HTMutableFluidTank =
        HTVariableFluidTank.create(listener) { capacityComponent.getCapacity(RagiumConfig.COMMON.tankCapacity) }

    final override fun getAmountView(): HTAmountView = tank

    lateinit var inputSlot: HTBasicItemSlot
        private set
    lateinit var outputSlot: HTBasicItemSlot
        private set

    final override fun createItemHandler(listener: HTContentListener): HTItemSlotHolder? {
        val listener = HTContentListener {
            checkRecipe = true
            listener.onContentsChanged()
        }
        val builder = HTBasicItemSlotHolder.Builder(this)
        inputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot.input(
                listener,
                canInsert = Predicate { resource: HTItemResourceType ->
                    if (HTFluidCapabilities.hasCapability(resource)) {
                        return@Predicate true
                    } else {
                        HCRecipeLookups.TANK_INTERACTION.findFirst(level) { recipe: HTTankInteraction ->
                            if (recipe.canEmptyContainer(resource)) {
                                return@findFirst true
                            } else {
                                val fluid: HTFluidResourceType = tank.getResource() ?: return@findFirst false
                                recipe.canFillContainer(resource, fluid)
                            }
                        } != null
                    }
                },
            ),
        )
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
        return builder.build()
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        // slot
        widgetHolder += HTItemSlotWidget.container(
            inputSlot,
            HTSlotHelper.getSlotPosX(1.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
        )
        widgetHolder += HTItemSlotWidget.container(
            outputSlot,
            HTSlotHelper.getSlotPosX(1.5),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.OUTPUT,
        )
        // tank
        val fluidWidget: HTFluidWidget =
            HTFluidWidget.createTank(tank, HTSlotHelper.getSlotPosX(4), HTSlotHelper.getSlotPosY(0))
        if (isCreative()) fluidWidget.setGhost()
        widgetHolder += fluidWidget
    }

    //    Sync    //

    override fun initReducedUpdateTag(output: HTValueOutput) {
        super.initReducedUpdateTag(output)
        tank.serialize(output)
    }

    override fun handleUpdateTag(input: HTValueInput) {
        super.handleUpdateTag(input)
        tank.deserialize(input)
    }

    //    Recipe    //

    private var checkRecipe: Boolean = false

    private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }
    private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

    private val fluidInputHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(tank) }
    private val fluidOutputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(tank) }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        if (checkRecipe) {
            checkRecipe = false
            val isDrained: Boolean = drainContainer()
            val isFilled: Boolean = fillContainer()
            if (isDrained || isFilled) return true
        }
        interactContainer()
        return super.onUpdateServer(level, pos, state)
    }

    private fun drainContainer(): Boolean {
        val filledContainer: HTItemResourceType = inputHandler.getResource() ?: return false
        val recipe: HTTankInteraction = HCRecipeLookups.TANK_INTERACTION
            .findFirst(level) { recipe: HTTankInteraction -> recipe.canEmptyContainer(filledContainer) }
            ?.recipe
            ?: return false

        val (emptyContainer: ItemStack, fluidStack: FluidStack) = recipe.emptyContainer(filledContainer)
        if (outputHandler.canInsert(emptyContainer) && fluidOutputHandler.canInsert(fluidStack)) {
            outputHandler.insert(emptyContainer)
            fluidOutputHandler.insert(fluidStack)
            inputHandler.consume(1)
            return true
        } else {
            return false
        }
    }

    private fun fillContainer(): Boolean {
        val emptyContainer: HTItemResourceType = inputHandler.getResource() ?: return false
        val fluid: HTFluidResourceType = fluidInputHandler.getResource() ?: return false
        val recipe: HTTankInteraction = HCRecipeLookups.TANK_INTERACTION
            .findFirst(level) { recipe: HTTankInteraction -> recipe.canFillContainer(emptyContainer, fluid) }
            ?.recipe
            ?: return false

        val filledContainer: ItemStack = recipe.fillContainer(emptyContainer, fluid)
        if (outputHandler.canInsert(filledContainer)) {
            outputHandler.insert(filledContainer)
            inputHandler.consume(1)
            fluidInputHandler.consume(recipe.amount)
            return true
        } else {
            return false
        }
    }

    private fun interactContainer() {
        // Fill
        var fillResult: FluidActionResult = FluidUtil.tryFillContainer(inputHandler.getItemStack(), this, tank.getCapacity(), null, false)
        if (fillResult.isSuccess) {
            if (outputHandler.canInsert(fillResult.result)) {
                fillResult = FluidUtil.tryFillContainer(inputHandler.getItemStack(), this, tank.getCapacity(), null, true)
                outputHandler.insert(fillResult.result)
                inputHandler.consume(1)
            }
        }
        // Empty
        var emptyResult: FluidActionResult = FluidUtil.tryEmptyContainer(inputHandler.getItemStack(), this, tank.getCapacity(), null, false)
        if (emptyResult.isSuccess) {
            if (outputHandler.canInsert(emptyResult.result)) {
                emptyResult = FluidUtil.tryEmptyContainer(inputHandler.getItemStack(), this, tank.getCapacity(), null, true)
                outputHandler.insert(emptyResult.result)
                inputHandler.consume(1)
            }
        }
    }
}
