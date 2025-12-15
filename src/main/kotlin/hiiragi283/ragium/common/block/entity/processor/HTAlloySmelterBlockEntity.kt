package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTAlloyingRecipe
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

class HTAlloySmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Cached<HTAlloyingRecipe>(
        RagiumRecipeTypes.ALLOYING,
        RagiumBlocks.ALLOY_SMELTER,
        pos,
        state,
    ) {
    lateinit var inputSlots: List<HTBasicItemSlot>
        private set
    lateinit var outputSlot: HTBasicItemSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlots = (1..3).map { i: Int ->
            builder.addSlot(
                HTSlotInfo.INPUT,
                HTBasicItemSlot.input(listener, HTSlotHelper.getSlotPosX(i), HTSlotHelper.getSlotPosY(0)),
            )
        }
        // output
        outputSlot = singleOutput(builder, listener)
    }

    override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean = outputSlot.getNeeded() > 0

    override fun buildRecipeInput(builder: HTRecipeInput.Builder) {
        builder.items.addAll(inputSlots.map(HTBasicItemSlot::getStack))
    }

    override fun canProgressRecipe(level: ServerLevel, input: HTRecipeInput, recipe: HTAlloyingRecipe): Boolean =
        HTStackSlotHelper.canInsertStack(outputSlot, input, level, recipe::assembleItem)

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTRecipeInput,
        recipe: HTAlloyingRecipe,
    ) {
        // 実際にアウトプットに搬出する
        outputSlot.insert(recipe.assembleItem(input, level.registryAccess()), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // 実際にインプットを減らす
        val ingredients: List<HTItemIngredient> = recipe.ingredients
        HTRecipeInput.getMatchingSlots(ingredients, input.items).forEachIndexed { index: Int, slot: Int ->
            inputSlots[slot].extract(ingredients[index].getRequiredAmount(), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        }
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 0.5f)
    }
}
