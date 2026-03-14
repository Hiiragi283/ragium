package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.storage.amount.HTAmountView
import hiiragi283.core.api.storage.fluid.HTFluidView
import hiiragi283.core.api.storage.fluid.HTMutableFluidTank
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.api.storage.holder.HTFluidTankHolder
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.capability.HTFluidCapabilities
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.recipe.handler.HTFluidInputHandler
import hiiragi283.core.common.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.common.recipe.handler.HTItemInputHandler
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.api.recipe.HTTankInteractingRecipe
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
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
        tank = builder.addSlot(HTSlotInfo.BOTH, createTank(listener))
        return builder.build()
    }

    protected open fun createTank(listener: HTContentListener): HTMutableFluidTank =
        HTVariableFluidTank.create(listener) { HTUpgradeHelper.getFluidCapacity(this, RagiumConfig.COMMON.tankCapacity.asInt) }

    final override fun getAmountView(): HTAmountView = tank

    lateinit var drainInputSlot: HTBasicItemSlot
        private set
    lateinit var drainOutputSlot: HTBasicItemSlot
        private set

    lateinit var fillInputSlot: HTBasicItemSlot
        private set
    lateinit var fillOutputSlot: HTBasicItemSlot
        private set

    final override fun createItemHandler(listener: HTContentListener): HTItemSlotHolder? {
        val listener = HTContentListener {
            checkRecipe = true
            listener.onContentsChanged()
        }
        val builder = HTBasicItemSlotHolder.Builder(this)
        drainInputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot.input(
                listener,
                canInsert = Predicate { resource: HTItemResourceType ->
                    if (HTFluidCapabilities.getFluidViews(resource).any { it.getResource() != null }) {
                        return@Predicate true
                    } else {
                        RagiumRecipeTypes.TANK_INTERACTION.findFirst(level) { recipe: HTTankInteractingRecipe ->
                            recipe.canEmptyContainer(resource.toStack())
                        } != null
                    }
                },
            ),
        )
        drainOutputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))

        fillInputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot.input(
                listener,
                canInsert = Predicate { resource: HTItemResourceType ->
                    HTFluidCapabilities.getFluidViews(resource).all(HTFluidView::isEmpty)
                },
            ),
        )
        fillOutputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
        return builder.build()
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        // slot
        widgetHolder += HTItemSlotWidget.container(
            drainInputSlot,
            HTSlotHelper.getSlotPosX(1.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
        )
        widgetHolder += HTItemSlotWidget.container(
            drainOutputSlot,
            HTSlotHelper.getSlotPosX(1.5),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.OUTPUT,
        )

        widgetHolder += HTItemSlotWidget.container(
            fillInputSlot,
            HTSlotHelper.getSlotPosX(6.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
        )
        widgetHolder += HTItemSlotWidget.container(
            fillOutputSlot,
            HTSlotHelper.getSlotPosX(6.5),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.OUTPUT,
        )
        // tank
        val fluidWidget: HTFluidWidget =
            HTFluidWidget.createTank(tank, HTSlotHelper.getSlotPosX(4), HTSlotHelper.getSlotPosY(0))
        if (isCreative()) fluidWidget.setGhost()
        widgetHolder += fluidWidget
    }

    //    Recipe    //

    private var checkRecipe: Boolean = false

    private val drainInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(drainInputSlot) }
    private val fillInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(fillInputSlot) }
    private val fluidInputHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(tank) }

    private val drainOutputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(drainOutputSlot) }
    private val fillOutputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(fillOutputSlot) }
    private val fluidOutputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(tank) }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        if (checkRecipe) {
            checkRecipe = false
            val isDrained: Boolean = drainContainer()
            val isFilled: Boolean = fillContainer()
            if (isDrained || isFilled) return true
        }
        return super.onUpdateServer(level, pos, state)
    }

    private fun drainContainer(): Boolean {
        val filledContainer: ItemStack = drainInputHandler.getItemStack()
        val recipe: HTTankInteractingRecipe = RagiumRecipeTypes.TANK_INTERACTION
            .findFirst(level) { recipe: HTTankInteractingRecipe -> recipe.canEmptyContainer(filledContainer) }
            ?.value()
            ?: return false

        val (emptyContainer: ItemStack, fluidStack: FluidStack) = recipe.emptyContainer(filledContainer)
        if (drainOutputHandler.canInsert(emptyContainer) && fluidOutputHandler.canInsert(fluidStack)) {
            drainOutputHandler.insert(emptyContainer)
            fluidOutputHandler.insert(fluidStack)
            drainInputHandler.consume(1)
            return true
        } else {
            return false
        }
    }

    private fun fillContainer(): Boolean {
        val emptyContainer: ItemStack = fillInputHandler.getItemStack()
        val fluidStack: FluidStack = fluidInputHandler.getFluidStack()
        val recipe: HTTankInteractingRecipe = RagiumRecipeTypes.TANK_INTERACTION
            .findFirst(level) { recipe: HTTankInteractingRecipe -> recipe.canFillContainer(emptyContainer, fluidStack) }
            ?.value()
            ?: return false

        val filledContainer: ItemStack = recipe.fillContainer(emptyContainer, fluidStack)
        if (fillOutputHandler.canInsert(filledContainer)) {
            fillOutputHandler.insert(filledContainer)
            fillInputHandler.consume(1)
            fluidInputHandler.consume(recipe.amount)
            return true
        } else {
            return false
        }
    }
}
