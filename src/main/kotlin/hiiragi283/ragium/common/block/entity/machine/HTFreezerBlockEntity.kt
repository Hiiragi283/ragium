package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.handler.HTDoubleRecipeHandler
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.handler.cache.HTRecipeCacheHelper
import hiiragi283.lib.recipe.handler.input.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.input.HTInputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlotHelper
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.api.recipe.RTFreezingRecipe
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
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.transfer.transaction.Transaction

class HTFreezerBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.FREEZER.get(), pos, state) {
    override fun createRecipeHandler(): HTRecipeHandler =
        object : HTDoubleRecipeHandler.Cached<ItemInstance, FluidInstance, ItemStack, RTFreezingRecipe>(
            HTRecipeCacheHelper.ItemAndFluid(RagiumRecipeLookups.FREEZING, false)
        ) {
            private val inputTank: HTInputSlot<FluidInstance> by lazy {
                HTInputHandlerHelper.forFluid(this@HTFreezerBlockEntity.inputTank)
            }
            private val catalystSlot: HTInputSlot<ItemInstance> by lazy {
                HTInputHandlerHelper.forItem(this@HTFreezerBlockEntity.catalystSlot)
            }
            private val outputSlot: HTOutputSlot<ItemStack> by lazy {
                HTOutputSlotHelper.forItem(this@HTFreezerBlockEntity.outputSlot)
            }

            override fun createProgressor(): Progressor<ItemInstance, FluidInstance, RTFreezingRecipe> =
                DoubleProgressor()

            override fun getFirstInput(): ItemInstance = catalystSlot.getStoredInput()

            override fun getSecondInput(): FluidInstance = inputTank.getStoredInput()

            override fun canComplete(
                recipe: RTFreezingRecipe,
                first: ItemInstance,
                second: FluidInstance,
                output: ItemStack
            ): Boolean = useTransaction { transaction: Transaction ->
                when {
                    inputTank.use(second, transaction).failed -> false
                    else -> outputSlot.take(output, transaction) == HTOutputSlot.TakeResult.FULL
                }
            }

            override fun onComplete(
                recipe: RTFreezingRecipe,
                first: ItemInstance,
                second: FluidInstance,
                output: ItemStack
            ) {
                useTransaction { transaction: Transaction ->
                    inputTank.use(second, transaction)
                    outputSlot.take(output, transaction)
                    transaction.commit()
                }
                playSound(SoundEvents.BUCKET_FILL_POWDER_SNOW)
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
