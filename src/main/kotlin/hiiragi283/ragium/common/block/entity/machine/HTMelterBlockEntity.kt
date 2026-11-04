package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTItemToFluidRecipe
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.handler.HTSingleRecipeHandler
import hiiragi283.lib.recipe.handler.cache.HTRecipeCacheHelper
import hiiragi283.lib.recipe.handler.cache.HTSingleRecipeCache
import hiiragi283.lib.recipe.handler.input.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.input.HTInputSlot
import hiiragi283.lib.recipe.handler.lookup.HTSingleRecipeLookupHandler
import hiiragi283.lib.recipe.handler.output.HTOutputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlotHelper
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
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
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.Transaction

class HTMelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.MELTER.get(), pos, state),
    HTSingleRecipeLookupHandler.ForItem<HTItemToFluidRecipe> {
    override val recipeCache: HTSingleRecipeCache<ItemInstance, HTItemToFluidRecipe> = HTRecipeCacheHelper.SingleItem(RagiumRecipeLookups.MELTING)

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        output.putChild(HTConstants.LAST_RECIPE, recipeCache)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        input.readChild(HTConstants.LAST_RECIPE, recipeCache)
    }

    override fun createRecipeHandler(): HTRecipeHandler = object : HTSingleRecipeHandler<ItemInstance, FluidStack, HTItemToFluidRecipe>() {
        private val inputSlot: HTInputSlot<ItemInstance> by lazy {
            HTInputHandlerHelper.forItem(this@HTMelterBlockEntity.inputSlot)
        }
        private val outputTank: HTOutputSlot<FluidStack> by lazy {
            HTOutputSlotHelper.forFluid(this@HTMelterBlockEntity.outputTank)
        }

        override fun createProgressor(): Progressor<ItemInstance, HTItemToFluidRecipe> = SingleProgressor()

        override fun getInput(): ItemInstance = inputSlot.getStoredInput()

        override fun findRecipe(input: ItemInstance, level: ServerLevel): HTItemToFluidRecipe? = findFirstRecipe(input)

        override fun canComplete(recipe: HTItemToFluidRecipe, input: ItemInstance, output: FluidStack): Boolean =
            useTransaction { transaction: Transaction ->
                val inputConsume: ItemInstance = recipe.getMatchingStack(input)
                when {
                    inputSlot.use(inputConsume, transaction).failed -> false
                    else -> outputTank.take(output, transaction) == HTOutputSlot.TakeResult.FULL
                }
            }

        override fun onComplete(recipe: HTItemToFluidRecipe, input: ItemInstance, output: FluidStack) {
            useTransaction { transaction: Transaction ->
                inputSlot.use(recipe.getMatchingStack(input), transaction)
                outputTank.take(output, transaction)
                transaction.commit()
            }
            playSound(SoundEvents.LAVA_POP)
        }
    }

    private lateinit var outputTank: HTVariableFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        this@HTMelterBlockEntity.outputTank =
            builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(getTankCapacity(), listener))
    }

    private lateinit var inputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener, filter = ::hasRecipe))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder)
        // input
        widgetHolder += HTItemWidget.Container(
            inputSlot,
            0,
            HTSlotHelper.getSlotPosX(2),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(inputSlot)
        // output
        widgetHolder += HTFluidWidget.Tank(
            outputTank,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.OUTPUT,
            false,
        )
        widgetHolder.track(outputTank)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.melter
}
