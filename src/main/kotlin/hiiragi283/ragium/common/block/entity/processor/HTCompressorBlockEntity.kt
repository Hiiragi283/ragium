package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTCompressingRecipe
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.block.state.BlockState

class HTCompressorBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Cached<HTCompressingRecipe>(
        RagiumRecipeTypes.COMPRESSING,
        RagiumBlocks.COMPRESSOR,
        pos,
        state,
    ) {
    lateinit var inputSlot: HTBasicItemSlot
        private set
    lateinit var catalystSlot: HTBasicItemSlot
        private set
    lateinit var outputSlot: HTBasicItemSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = singleInput(builder, listener)
        // catalyst
        catalystSlot = builder.addSlot(
            HTSlotInfo.CATALYST,
            HTBasicItemSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2), 1),
        )
        // output
        outputSlot = upperOutput(builder, listener)
    }

    override fun buildRecipeInput(builder: HTRecipeInput.Builder) {
        builder.items += inputSlot.getStack()
        builder.items += catalystSlot.getStack()
    }

    override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean = outputSlot.getNeeded() > 0

    override fun canProgressRecipe(level: ServerLevel, input: HTRecipeInput, recipe: HTCompressingRecipe): Boolean =
        HTStackSlotHelper.canInsertStack(outputSlot, input, level, recipe::assembleItem)

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTRecipeInput,
        recipe: HTCompressingRecipe,
    ) {
        // 実際にアウトプットに搬出する
        outputSlot.insert(recipe.assembleItem(input, level.registryAccess()), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // 実際にインプットを減らす
        inputSlot.extract(recipe.getRequiredCount(), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 0.25f, 0.5f)
    }
}
