package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.base.HTTankEmptyingRecipe
import hiiragi283.core.api.recipe.base.HTTankFillingRecipe
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.serialization.value.read
import hiiragi283.core.api.serialization.value.write
import hiiragi283.core.api.storage.amount.HTAmountView
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.api.storage.holder.HTFluidTankHolder
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.recipe.HCRecipeLookups
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.HTLookupRecipeCache
import hiiragi283.core.impl.recipe.handler.HTFluidInputHandler
import hiiragi283.core.impl.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.core.impl.recipe.handler.HTItemOutputHandler
import hiiragi283.core.impl.storage.fluid.HTFluidStackResourceSlot
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.core.component.DataComponentMap
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidActionResult
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.fluids.SimpleFluidContent

/**
 * @see mekanism.common.tile.TileEntityFluidTank
 * @see hiiragi283.core.common.block.entity.HTCopperBasinBlockEntity
 */
open class HTTankBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTStorageBlockEntity(type, pos, state) {
    constructor(pos: BlockPos, state: BlockState) : this(RagiumBlockEntityTypes.TANK, pos, state)

    lateinit var tank: HTFluidStackResourceSlot
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

    protected open fun createTank(listener: HTContentListener): HTFluidStackResourceSlot =
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
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
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

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        output.write(HTConst.EMPTYING, emptyingCache)
        output.write(HTConst.FILLING, fillingCache)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        input.read(HTConst.EMPTYING, emptyingCache)
        input.read(HTConst.FILLING, fillingCache)
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        componentInput.get(HCDataComponents.FLUID)?.copy()?.let(tank::setStack)
    }

    override fun collectImplicitComponents(builder: DataComponentMap.Builder) {
        super.collectImplicitComponents(builder)
        val content: SimpleFluidContent = tank.getStack().let(SimpleFluidContent::copyOf)
        if (!content.isEmpty) {
            builder.set(HCDataComponents.FLUID, content)
        }
    }

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
    private val emptyingCache: HTLookupRecipeCache<SingleRecipeInput, HTTankEmptyingRecipe> =
        HTLookupRecipeCache.forRecipe(HCRecipeLookups.EMPTYING)
    private val fillingCache: HTLookupRecipeCache<HTItemAndFluidRecipeInput, HTTankFillingRecipe> =
        HTLookupRecipeCache.forRecipe(HCRecipeLookups.FILLING)

    private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }
    private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

    private val fluidInputHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(tank) }
    private val fluidOutputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(tank) }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        if (checkRecipe) {
            checkRecipe = false
            val isDrained: Boolean = drainContainer(level)
            val isFilled: Boolean = fillContainer(level)
            if (isDrained || isFilled) return true
        }
        interactContainer()
        return super.onUpdateServer(level, pos, state)
    }

    private fun drainContainer(level: ServerLevel): Boolean {
        val input = SingleRecipeInput(inputHandler.getItemStack())
        if (input.isEmpty) return false
        val recipe: HTTankEmptyingRecipe = emptyingCache.getFirstRecipe(input, level) ?: return false

        val access: RegistryAccess = level.registryAccess()
        val emptyContainer: ItemStack = recipe.assemble(input, access)
        val fluidStack: FluidStack = recipe.assembleFluid(input, access)
        if (outputHandler.canInsert(emptyContainer) && fluidOutputHandler.canInsert(fluidStack)) {
            // outputs
            outputHandler.insert(emptyContainer)
            fluidOutputHandler.insert(fluidStack)
            // input
            inputHandler.consume(1)
            return true
        } else {
            return false
        }
    }

    private fun fillContainer(level: ServerLevel): Boolean {
        val input = HTItemAndFluidRecipeInput(inputHandler.getItemStack(), fluidInputHandler.getFluidStack())
        if (input.isEmpty) return false

        val recipe: HTTankFillingRecipe = fillingCache.getFirstRecipe(input, level) ?: return false

        val filledContainer: ItemStack = recipe.assemble(input, level.registryAccess())
        if (outputHandler.canInsert(filledContainer)) {
            outputHandler.insert(filledContainer)
            inputHandler.consume(1)
            recipe.getRequiredFluidAmount(input).let(fluidInputHandler::consume)
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
