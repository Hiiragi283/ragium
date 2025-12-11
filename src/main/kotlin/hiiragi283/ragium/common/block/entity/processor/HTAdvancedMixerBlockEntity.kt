package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.block.attribute.getFluidAttribute
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTComplexRecipe
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.processor.base.HTComplexBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.storage.fluid.tank.HTBasicFluidTank
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemSlot
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTAdvancedMixerBlockEntity(pos: BlockPos, state: BlockState) :
    HTComplexBlockEntity<HTComplexRecipe>(
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
            HTVariableFluidTank.input(listener, blockHolder.getFluidAttribute().getFirstInputTank(this)),
        )
        secondInputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(listener, blockHolder.getFluidAttribute().getSecondInputTank(this)),
        )
    }

    lateinit var inputSlots: List<HTBasicItemSlot>
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // inputs
        inputSlots = (0..1).flatMap { y: Int ->
            (2..3).map { x: Int ->
                builder.addSlot(
                    HTSlotInfo.INPUT,
                    HTBasicItemSlot.input(listener, HTSlotHelper.getSlotPosX(x), HTSlotHelper.getSlotPosX(y)),
                )
            }
        }
        // output
        outputSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTOutputItemSlot.create(listener, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(1.5)),
        )
    }

    override fun buildRecipeInput(builder: HTRecipeInput.Builder) {
        builder.items.addAll(inputSlots.map(HTBasicItemSlot::getStack))
        builder.fluids += firstInputTank.getStack()
        builder.fluids += secondInputTank.getStack()
    }
}
