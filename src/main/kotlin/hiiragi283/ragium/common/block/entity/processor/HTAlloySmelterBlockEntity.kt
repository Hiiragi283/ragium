package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTMultiInputsToObjRecipe
import hiiragi283.ragium.api.recipe.multi.HTMultiItemsToItemRecipe
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.block.state.BlockState

class HTAlloySmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTEnergizedProcessorBlockEntity.Cached<HTMultiRecipeInput, HTMultiItemsToItemRecipe>(
        RagiumRecipeTypes.ALLOYING,
        RagiumBlocks.ALLOY_SMELTER,
        pos,
        state,
    ) {
    lateinit var inputSlots: List<HTItemStackSlot>
        private set
    lateinit var outputSlot: HTItemStackSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlots = (1..3).map { i: Int ->
            builder.addSlot(
                HTSlotInfo.INPUT,
                HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(i), HTSlotHelper.getSlotPosY(0)),
            )
        }
        // output
        outputSlot = singleOutput(builder, listener)
    }

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTMultiRecipeInput = HTMultiRecipeInput.fromSlots(inputSlots)

    override fun canProgressRecipe(level: ServerLevel, input: HTMultiRecipeInput, recipe: HTMultiItemsToItemRecipe): Boolean =
        outputSlot.insert(recipe.assembleItem(input, level.registryAccess()), HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) == null

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTMultiRecipeInput,
        recipe: HTMultiItemsToItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        outputSlot.insert(recipe.assembleItem(input, level.registryAccess()), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // 実際にインプットを減らす
        val ingredients: List<HTItemIngredient> = recipe.ingredients
        HTMultiInputsToObjRecipe.getMatchingSlots(ingredients, input.items).forEachIndexed { index: Int, slot: Int ->
            HTStackSlotHelper.shrinkStack(inputSlots[slot], ingredients[index], HTStorageAction.EXECUTE)
        }
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 0.5f)
    }
}
