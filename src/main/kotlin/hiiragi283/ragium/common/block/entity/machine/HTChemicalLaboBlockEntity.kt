package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.HTConstants
import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.handler.HTTripleRecipeHandler
import hiiragi283.lib.recipe.handler.cache.HTTripleRecipeCache
import hiiragi283.lib.recipe.handler.input.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.input.HTInputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlotHelper
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.lookup.mapNotNull
import hiiragi283.lib.recipe.result.HTItemAndFluidStack
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.api.recipe.RTReactingRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.Transaction

class HTChemicalLaboBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.CHEMICAL_LABO.get(), pos, state) {
    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        output.putChild(HTConstants.LAST_RECIPE, Cache)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        input.readChild(HTConstants.LAST_RECIPE, Cache)
    }

    override fun createRecipeHandler(): HTRecipeHandler =
        object : HTTripleRecipeHandler<ItemInstance, FluidInstance, ItemInstance, HTItemAndFluidStack, RTReactingRecipe.Simple>() {
            private val inputTank: HTInputSlot<FluidInstance> by lazy { HTInputHandlerHelper.forFluid(this@HTChemicalLaboBlockEntity.inputTank) }
            private val inputSlot: HTInputSlot<ItemInstance> by lazy { HTInputHandlerHelper.forItem(this@HTChemicalLaboBlockEntity.inputSlot) }
            private val outputTank: HTOutputSlot<FluidStack> by lazy { HTOutputSlotHelper.forFluid(this@HTChemicalLaboBlockEntity.outputTank) }
            private val outputSlot: HTOutputSlot<ItemStack> by lazy { HTOutputSlotHelper.forItem(this@HTChemicalLaboBlockEntity.outputSlot) }

            override fun createProgressor(): Progressor<ItemInstance, FluidInstance, ItemInstance, RTReactingRecipe.Simple> = TripleProgressor()

            override fun getFirstInput(): ItemInstance = inputSlot.getStoredInput()

            override fun getSecondInput(): FluidInstance = inputTank.getStoredInput()

            override fun getThirdInput(): ItemInstance = catalystSlot.getStackCopy()

            override fun findRecipe(first: ItemInstance, second: FluidInstance, third: ItemInstance, level: ServerLevel): RTReactingRecipe.Simple? =
                Cache.findFirstRecipe(first, second, third, HTPhysicalSideHelper.createLookupContext(level))

            override fun canComplete(
                recipe: RTReactingRecipe.Simple,
                first: ItemInstance,
                second: FluidInstance,
                third: ItemInstance,
                output: HTItemAndFluidStack,
            ): Boolean = useTransaction { transaction: Transaction ->
                val (itemInput: ItemInstance, fluidInput: FluidInstance, _: ItemInstance) = recipe.getMatchingStack(first, second, third)
                val (itemOutput: ItemStack, fluidOutput: FluidStack) = output
                when {
                    inputSlot.use(itemInput, transaction).failed -> false
                    inputTank.use(fluidInput, transaction).failed -> false
                    !outputSlot.take(itemOutput, transaction).fullOrNoneTaken -> false
                    else -> outputTank.take(fluidOutput, transaction).fullOrNoneTaken
                }
            }

            override fun onComplete(
                recipe: RTReactingRecipe.Simple,
                first: ItemInstance,
                second: FluidInstance,
                third: ItemInstance,
                output: HTItemAndFluidStack,
            ) {
                useTransaction { transaction: Transaction ->
                    val (itemInput: ItemInstance, fluidInput: FluidInstance, _: ItemInstance) = recipe.getMatchingStack(first, second, third)
                    val (itemOutput: ItemStack, fluidOutput: FluidStack) = output
                    inputSlot.use(itemInput, transaction)
                    inputTank.use(fluidInput, transaction)
                    outputSlot.take(itemOutput, transaction)
                    outputTank.take(fluidOutput, transaction)
                    transaction.commit()
                }
                playSound(SoundEvents.PLAYER_LEVELUP)
            }
        }

    private data object Cache : HTTripleRecipeCache<ItemInstance, FluidInstance, ItemInstance, RTReactingRecipe.Simple>(
        RagiumRecipeLookups.REACTING.mapNotNull(RTReactingRecipe::asSimple),
    ) {
        override fun isEmpty(first: ItemInstance, second: FluidInstance, third: ItemInstance): Boolean =
            HTIngredientHelper.isEmpty(first) && HTIngredientHelper.isEmpty(second) && HTIngredientHelper.isEmpty(third)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.chemicalLabo

    private lateinit var inputTank: HTVariableFluidTank
    private lateinit var outputTank: HTVariableFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        inputTank = builder.addSlot(HTSlotInfo.INPUT, HTVariableFluidTank.input(getTankCapacity(), listener))
        outputTank = builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(getTankCapacity(), listener))
    }

    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var catalystSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        catalystSlot = builder.addSlot(HTSlotInfo.NONE, HTBasicItemSlot.catalyst(listener))
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder, y = HTSlotHelper.getSlotPosY(1.5))
        // inputs
        widgetHolder += HTItemWidget.Container(
            inputSlot,
            0,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(inputSlot)
        widgetHolder += HTFluidWidget.Tank(
            inputTank,
            HTSlotHelper.getSlotPosX(1),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
            false,
        )
        widgetHolder.track(inputTank)
        // catalyst
        widgetHolder += HTItemWidget.Container(
            catalystSlot,
            1,
            HTSlotHelper.getSlotPosX(4),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.NONE,
        )
        // output
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            2,
            HTSlotHelper.getSlotPosX(5.5),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder.track(outputSlot)
        widgetHolder += HTFluidWidget.Tank(
            outputTank,
            HTSlotHelper.getSlotPosX(7),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.OUTPUT,
            false,
        )
        widgetHolder.track(outputTank)
    }
}
