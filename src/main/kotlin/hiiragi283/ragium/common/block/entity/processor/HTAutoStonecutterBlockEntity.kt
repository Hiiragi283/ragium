package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.item.getItemStack
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.processor.base.HTSingleItemInputBlockEntity
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.minecraft.world.level.block.state.BlockState

class HTAutoStonecutterBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleItemInputBlockEntity<StonecutterRecipe>(
        RagiumBlocks.AUTO_STONECUTTER,
        pos,
        state,
    ) {
    lateinit var catalystSlot: HTItemStackSlot
        private set
    lateinit var outputSlots: List<HTItemStackSlot>
        private set

    override fun initializeItemHandler(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = singleInput(builder, listener)
        catalystSlot = singleCatalyst(builder, listener)
        outputSlots = multiOutputs(builder, listener)
    }

    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): StonecutterRecipe? {
        // 指定されたアイテムと同じものを出力するレシピだけを選ぶ
        var matchedRecipe: StonecutterRecipe? = null
        for (holder: RecipeHolder<StonecutterRecipe> in level.recipeManager.getAllRecipesFor(RecipeType.STONECUTTING)) {
            val recipe: StonecutterRecipe = holder.value
            if (!recipe.matches(input, level)) continue
            val result: ItemStack = recipe.assemble(input, level.registryAccess())
            if (result.isEmpty) continue
            if (ItemStack.isSameItemSameComponents(catalystSlot.getItemStack(), result)) {
                matchedRecipe = recipe
                break
            }
        }
        return matchedRecipe
    }

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: StonecutterRecipe): Boolean =
        HTStackSlotHelper.insertStacks(
            outputSlots,
            recipe.assemble(input, level.registryAccess()).toImmutable(),
            HTStorageAction.SIMULATE,
        ) ==
            null

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: StonecutterRecipe,
    ) {
        // 実際にアウトプットに搬出する
        HTStackSlotHelper.insertStacks(outputSlots, recipe.assemble(input, level.registryAccess()).toImmutable(), HTStorageAction.SIMULATE)
        // インプットを減らす
        inputSlot.extract(1, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1f, 1f)
    }
}
