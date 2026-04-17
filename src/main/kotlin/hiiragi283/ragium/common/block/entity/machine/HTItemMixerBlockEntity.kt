package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.handler.HTFluidInputHandler
import hiiragi283.core.impl.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.core.impl.recipe.handler.HTItemOutputHandler
import hiiragi283.ragium.api.recipe.base.HTMixingRecipe
import hiiragi283.ragium.api.recipe.input.HTMixingRecipeInput
import hiiragi283.ragium.common.block.entity.machine.base.HTMixerBlockEntity
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTItemMixerBlockEntity(pos: BlockPos, state: BlockState) : HTMixerBlockEntity(RagiumBlockEntityTypes.MIXER, pos, state) {
    private lateinit var inputTank: HTBasicFluidTank
    private lateinit var outputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTank = builder.addSlot(HTSlotInfo.EXTRA_INPUT, HTVariableFluidTank.input(listener, getTankCapacity()))
        outputTank = builder.addSlot(HTSlotInfo.EXTRA_OUTPUT, HTVariableFluidTank.output(listener, getTankCapacity()))
    }

    private lateinit var inputSlots: Array<HTBasicItemSlot>
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlots = Array(2) {
            builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        }
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
    }

    //    Processing    //

    override fun createItemOutputs(): HTItemOutputHandler = HTItemOutputHandler.single(outputSlot)

    override fun createFluidOutputs(): HTFluidOutputHandler = HTFluidOutputHandler.single(outputTank)

    override fun createInput(): HTMixingRecipeInput =
        HTMixingRecipeInput(inputSlots.map(HTBasicItemSlot::getStack), listOf(outputTank.getStack()))

    private val itemInputHandlers: List<HTItemInputHandler> by lazy { inputSlots.map(::HTItemInputHandler) }
    private val fluidInputHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(inputTank) }

    override fun consumeInputs(amounts: HTMixingRecipe.RequiredAmounts) {
        val (firstItem: Int, secondItem: Int, firstFluid: Int, _) = amounts
        itemInputHandlers[0].consume(firstItem)
        itemInputHandlers[1].consume(secondItem)
        fluidInputHandler.consume(firstFluid)
    }
}
