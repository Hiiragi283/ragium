package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.handler.HTFluidInputTank
import hiiragi283.lib.recipe.handler.HTItemInputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlotHelper
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.lookup.HTRecipeCache
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.config.HTEnergyConfig
import hiiragi283.ragium.api.recipe.RTFreezingRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.transfer.transaction.Transaction

class HTFreezerBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.FREEZER.get(), pos, state) {
    private val cache: HTRecipeCache<HTItemAndFluidRecipeInput, RTFreezingRecipe> =
        HTRecipeCache(RagiumRecipeLookups.FREEZING)

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        output.putChild(HTConstants.LAST_RECIPE, cache)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        input.readChild(HTConstants.LAST_RECIPE, cache)
    }

    override fun initializeVariables(listener: Runnable) {
        super.initializeVariables(listener)
        recipeHandler = object : EnergizedHandler<HTItemAndFluidRecipeInput, ItemStack, RTFreezingRecipe>() {
            private val inputTank: HTFluidInputTank by lazy { HTFluidInputTank(this@HTFreezerBlockEntity.inputTank) }
            private val catalystSlot: HTItemInputSlot by lazy {
                HTItemInputSlot(this@HTFreezerBlockEntity.catalystSlot)
            }
            private val outputSlot: HTOutputSlot<ItemStack> by lazy {
                HTOutputSlotHelper.forItem(this@HTFreezerBlockEntity.outputSlot)
            }

            override fun createInput(): HTItemAndFluidRecipeInput =
                HTItemAndFluidRecipeInput(catalystSlot.getStoredInput(), inputTank.getStoredInput())

            override fun findRecipe(level: ServerLevel, input: HTItemAndFluidRecipeInput): RTFreezingRecipe? =
                cache.findFirstRecipe(input, level)

            override fun canComplete(
                recipe: RTFreezingRecipe,
                input: HTItemAndFluidRecipeInput,
                output: ItemStack
            ): Boolean = useTransaction { transaction: Transaction ->
                val (_, inputConsume: FluidInstance) = recipe.getMatchingStack(input.item, input.fluid)
                when {
                    inputTank.use(inputConsume, transaction).failed -> false
                    else -> outputSlot.take(output, transaction) == HTOutputSlot.TakeResult.FULL
                }
            }

            override fun onComplete(recipe: RTFreezingRecipe, input: HTItemAndFluidRecipeInput, output: ItemStack) {
                useTransaction { transaction: Transaction ->
                    val (_, fluidInput: FluidInstance) = recipe.getMatchingStack(input.item, input.fluid)
                    inputTank.use(fluidInput, transaction)
                    outputSlot.take(output, transaction)
                    transaction.commit()
                }
                playSound(SoundEvents.BUCKET_FILL_POWDER_SNOW)
            }
        }
    }

    private lateinit var inputTank: HTVariableFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        inputTank = builder.addSlot(HTSlotInfo.INPUT, HTVariableFluidTank.input(getTankCapacity(), listener))
    }

    private lateinit var catalystSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        catalystSlot = builder.addSlot(HTSlotInfo.NONE, HTBasicItemSlot.input(listener))
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder)
        // input
        widgetHolder += HTFluidWidget.Tank(
            inputTank,
            HTSlotHelper.getSlotPosX(1),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
            false
        )
        widgetHolder.track(inputTank)
        // catalyst
        widgetHolder += HTItemWidget.Container(
            catalystSlot,
            0,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.NONE
        )
        widgetHolder.track(catalystSlot)
        // output
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            1,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT
        )
        widgetHolder.track(outputSlot)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.freezer
}
