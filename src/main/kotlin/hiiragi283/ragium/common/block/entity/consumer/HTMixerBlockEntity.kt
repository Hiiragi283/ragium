package hiiragi283.ragium.common.block.entity.consumer

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTComplexRecipe
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.consumer.base.HTMultiOutputsBlockEntity
import hiiragi283.ragium.common.storage.fluid.tank.HTFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

class HTMixerBlockEntity(pos: BlockPos, state: BlockState) :
    HTMultiOutputsBlockEntity<HTMultiRecipeInput, HTComplexRecipe>(
        RagiumRecipeTypes.MIXING,
        TODO(),
        pos,
        state,
    ) {
    lateinit var firstInputTank: HTFluidStackTank
        private set
    lateinit var secondInputTank: HTFluidStackTank
        private set

    override fun initInputTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
    }

    override fun getOutputTankCapacity(): Int {
        TODO("Not yet implemented")
    }

    lateinit var firstInputSlot: HTItemStackSlot
        private set
    lateinit var secondInputSlot: HTItemStackSlot
        private set

    override fun initializeItemHandler(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        firstInputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosX(0)),
        )
        secondInputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(3), HTSlotHelper.getSlotPosX(0)),
        )
    }

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTMultiRecipeInput {
        TODO("Not yet implemented")
    }
}
