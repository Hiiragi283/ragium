package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.block.attribute.getFluidAttribute
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTComplexRecipe
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.processor.base.HTComplexBlockEntity
import hiiragi283.ragium.common.storage.fluid.tank.HTBasicFluidTank
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemStackSlot
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

class HTAdvancedMixerBlockEntity(pos: BlockPos, state: BlockState) :
    HTComplexBlockEntity<HTMultiRecipeInput, HTComplexRecipe>(
        RagiumRecipeTypes.MIXING,
        RagiumBlocks.ADVANCED_MIXER,
        pos,
        state,
    ) {
    lateinit var firstInputTank: HTBasicFluidTank
        private set
    lateinit var secondInputTank: HTBasicFluidTank
        private set

    override fun initInputTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        // input
        firstInputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(listener, blockHolder.getFluidAttribute().getFirstInputTank()),
        )
        secondInputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(listener, blockHolder.getFluidAttribute().getSecondInputTank()),
        )
    }

    lateinit var inputSlots: List<HTItemStackSlot>
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // inputs
        inputSlots = (0..1).flatMap { y: Int ->
            (2..3).map { x: Int ->
                builder.addSlot(
                    HTSlotInfo.INPUT,
                    HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(x), HTSlotHelper.getSlotPosX(y)),
                )
            }
        }
        // output
        outputSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTOutputItemStackSlot.create(listener, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(1.5)),
        )
    }

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTMultiRecipeInput? = HTMultiRecipeInput.create {
        items.addAll(inputSlots.map(HTItemStackSlot::getStack))
        fluids += firstInputTank.getStack()
        fluids += secondInputTank.getStack()
    }
}
