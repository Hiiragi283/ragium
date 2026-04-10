package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.handler.HTHandledRecipe
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.handler.HTFluidInputHandler
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.ragium.common.block.entity.machine.base.HTMultiOutputBlockEntity
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTWasherBlockEntity(pos: BlockPos, state: BlockState) :
    HTMultiOutputBlockEntity<HTItemAndFluidRecipeInput, HTWashingRecipe>(RagiumBlockEntityTypes.WASHER, pos, state) {
    private lateinit var inputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(listener, getTankCapacity()),
        )
    }

    private lateinit var inputSlot: HTBasicItemSlot

    override fun createInputSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
    }

    override fun getOutputSlotSize(): Int = 4

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
    }

    //    Processing    //

    private val itemInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }
    private val fluidInputHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(inputTank) }

    override fun getLookup(): HTRecipeLookup<HTItemAndFluidRecipeInput, out HTWashingRecipe> = RagiumRecipeLookups.WASHING

    override fun createInput(level: ServerLevel, pos: BlockPos): HTItemAndFluidRecipeInput? =
        createInput(itemInputHandler, fluidInputHandler)

    override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: HTHandledRecipe<HTItemAndFluidRecipeInput, out HTWashingRecipe>) {
        itemInputHandler.consume(recipe.recipe.ingredient)
        fluidInputHandler.consume(HTWashingRecipe.WATER_INGREDIENT)
        playSound(SoundEvents.BUBBLE_COLUMN_UPWARDS_INSIDE)
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.machine.washer
}
