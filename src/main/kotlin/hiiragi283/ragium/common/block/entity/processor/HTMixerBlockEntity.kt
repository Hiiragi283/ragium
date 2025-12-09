package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.block.attribute.getFluidAttribute
import hiiragi283.ragium.api.function.partially1
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTComplexRecipe
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.processor.base.HTComplexBlockEntity
import hiiragi283.ragium.common.storage.fluid.tank.HTBasicFluidTank
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

class HTMixerBlockEntity(pos: BlockPos, state: BlockState) :
    HTComplexBlockEntity<HTComplexRecipe>(
        RagiumRecipeTypes.MIXING,
        RagiumBlocks.MIXER,
        pos,
        state,
    ) {
    lateinit var inputTank: HTBasicFluidTank
        private set

    override fun initInputTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        // input
        inputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(listener, blockHolder.getFluidAttribute().getInputTank()),
        )
    }

    lateinit var inputSlot: HTBasicItemSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // inputs
        inputSlot = singleInput(builder, listener)
        // output
        outputSlot = upperOutput(builder, listener)
    }

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTRecipeInput? = HTRecipeInput.create {
        items += inputSlot.getStack()
        fluids += inputTank.getStack()
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTRecipeInput,
        recipe: HTComplexRecipe,
    ) {
        super.completeRecipe(level, pos, state, input, recipe)
        // 実際にインプットを減らす
        HTStackSlotHelper.shrinkStack(inputSlot, recipe::getRequiredCount.partially1(0), HTStorageAction.EXECUTE)
        HTStackSlotHelper.shrinkStack(inputTank, recipe::getRequiredAmount.partially1(0), HTStorageAction.EXECUTE)
    }
}
