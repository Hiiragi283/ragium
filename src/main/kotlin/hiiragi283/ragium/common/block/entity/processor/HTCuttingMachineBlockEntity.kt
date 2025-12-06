package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.multi.HTItemToExtraItemRecipe
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.storage.item.toRecipeInput
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.inventory.HTSlotHelper
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
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTCuttingMachineBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Cached<SingleRecipeInput, HTItemToExtraItemRecipe>(
        RagiumRecipeTypes.CUTTING,
        RagiumBlocks.CUTTING_MACHINE,
        pos,
        state,
    ) {
    lateinit var inputSlot: HTBasicItemSlot
        private set
    lateinit var outputSlot: HTBasicItemSlot
        private set
    lateinit var extraSlot: HTBasicItemSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = singleInput(builder, listener)
        // outputs
        outputSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTOutputItemSlot.create(listener, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(0.5)),
        )
        extraSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTOutputItemSlot.create(listener, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(2.5)),
        )
    }

    //    Ticking    //

    override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean = outputSlot.getNeeded() > 0 && extraSlot.getNeeded() > 0

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput? = inputSlot.toRecipeInput()

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTItemToExtraItemRecipe): Boolean {
        // アウトプットに搬出できるか判定する
        val access: RegistryAccess = level.registryAccess()
        val bool1: Boolean = outputSlot.insert(
            recipe.assembleItem(input, access),
            HTStorageAction.SIMULATE,
            HTStorageAccess.INTERNAL,
        ) == null
        val bool2: Boolean = extraSlot.insert(
            recipe.assembleExtraItem(input, access),
            HTStorageAction.SIMULATE,
            HTStorageAccess.INTERNAL,
        ) == null
        return bool1 && bool2
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTItemToExtraItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        val access: RegistryAccess = level.registryAccess()
        outputSlot.insert(recipe.assembleItem(input, access), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        extraSlot.insert(recipe.assembleExtraItem(input, access), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // インプットを減らす
        HTStackSlotHelper.shrinkStack(inputSlot, recipe::getRequiredCount, HTStorageAction.EXECUTE)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1f, 1f)
    }
}
