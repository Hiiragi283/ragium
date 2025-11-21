package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.processor.base.HTComplexBlockEntity
import hiiragi283.ragium.common.storage.fluid.tank.HTFluidStackTank
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemStackSlot
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

class HTMixerBlockEntity(pos: BlockPos, state: BlockState) :
    HTComplexBlockEntity(
        RagiumRecipeTypes.MIXING,
        RagiumBlocks.MIXER,
        pos,
        state,
    ) {
    lateinit var firstInputTank: HTFluidStackTank
        private set
    lateinit var secondInputTank: HTFluidStackTank
        private set

    override fun initInputTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        // input
        firstInputTank =
            builder.addSlot(HTSlotInfo.INPUT, HTVariableFluidStackTank.input(listener, RagiumConfig.COMMON.mixerFirstInputTankCapacity))
        secondInputTank =
            builder.addSlot(HTSlotInfo.INPUT, HTVariableFluidStackTank.input(listener, RagiumConfig.COMMON.mixerSecondInputTankCapacity))
    }

    override fun getOutputTankCapacity(): Int = RagiumConfig.COMMON.mixerOutputTankCapacity.asInt

    lateinit var firstInputSlot: HTItemStackSlot
        private set
    lateinit var secondInputSlot: HTItemStackSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        firstInputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosX(0)),
        )
        secondInputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(3), HTSlotHelper.getSlotPosX(0)),
        )
        // output
        outputSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTOutputItemStackSlot.create(listener, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(1.5)),
        )
    }

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTMultiRecipeInput? = HTMultiRecipeInput.create {
        items += firstInputSlot.getStack()
        items += secondInputSlot.getStack()
        fluids += firstInputTank.getStack()
        fluids += secondInputTank.getStack()
    }
}
