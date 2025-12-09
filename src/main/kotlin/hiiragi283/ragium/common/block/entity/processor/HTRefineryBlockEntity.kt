package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.block.attribute.getFluidAttribute
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTComplexRecipe
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.storage.fluid.tank.HTBasicFluidTank
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemSlot
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.block.state.BlockState

class HTRefineryBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Cached<HTComplexRecipe>(
        RagiumRecipeTypes.REFINING,
        RagiumBlocks.REFINERY,
        pos,
        state,
    ) {
    lateinit var catalystSlot: HTBasicItemSlot
        private set
    lateinit var outputSlot: HTBasicItemSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        catalystSlot = builder.addSlot(
            HTSlotInfo.CATALYST,
            HTBasicItemSlot.input(listener, HTSlotHelper.getSlotPosX(3.5), HTSlotHelper.getSlotPosY(0)),
        )
        // output
        outputSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTOutputItemSlot.create(listener, HTSlotHelper.getSlotPosX(4.5), HTSlotHelper.getSlotPosY(2)),
        )
    }

    lateinit var inputTank: HTBasicFluidTank
        private set
    lateinit var outputTank: HTBasicFluidTank
        private set

    override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        // input
        inputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(listener, blockHolder.getFluidAttribute().getInputTank()),
        )
        // output
        outputTank = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTVariableFluidTank.output(listener, blockHolder.getFluidAttribute().getOutputTank()),
        )
    }

    //    Ticking    //

    override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean = outputSlot.getNeeded() > 0 || outputTank.getNeeded() > 0

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTRecipeInput? = HTRecipeInput.create {
        items += catalystSlot.getStack()
        fluids += inputTank.getStack()
    }

    // アウトプットに搬出できるか判定する
    override fun canProgressRecipe(level: ServerLevel, input: HTRecipeInput, recipe: HTComplexRecipe): Boolean {
        val bool1: Boolean = HTStackSlotHelper.canInsertStack(outputSlot, input, level, recipe::assembleItem)
        val bool2: Boolean = HTStackSlotHelper.canInsertStack(outputTank, input, level, recipe::assembleFluid)
        return bool1 && bool2
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTRecipeInput,
        recipe: HTComplexRecipe,
    ) {
        // 実際にアウトプットに搬出する
        val access: RegistryAccess = level.registryAccess()
        outputSlot.insert(recipe.assembleItem(input, access), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        outputTank.insert(recipe.assembleFluid(input, access), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // インプットを減らす
        inputTank.extract(recipe.getRequiredAmount(0), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1f, 0.5f)
    }
}
