package hiiragi283.ragium.common.block.entity.consumer.base

import hiiragi283.ragium.api.block.entity.HTBlockEntityFactory
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeFinder
import hiiragi283.ragium.api.recipe.single.HTSingleInputRecipe
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.item.toRecipeInput
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.recipe.manager.HTFinderRecipeCache
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class HTSingleItemInputBlockEntity<RECIPE : Any>(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<SingleRecipeInput, RECIPE>(blockHolder, pos, state) {
    companion object {
        @JvmStatic
        fun <RECIPE : HTSingleInputRecipe> createSimple(
            sound: SoundEvent,
            soundValues: Pair<Float, Float>,
            recipeType: HTRecipeFinder<SingleRecipeInput, RECIPE>,
        ): HTBlockEntityFactory<HTSingleItemInputBlockEntity<RECIPE>> = HTBlockEntityFactory { pos: BlockPos, state: BlockState ->
            Simple(sound, soundValues, recipeType, state.blockHolder, pos, state)
        }
    }

    lateinit var inputSlot: HTItemStackSlot
        protected set

    final override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = inputSlot.toRecipeInput()

    //    Cached    //

    abstract class Cached<RECIPE : Recipe<SingleRecipeInput>>(
        private val recipeCache: HTRecipeCache<SingleRecipeInput, RECIPE>,
        blockHolder: Holder<Block>,
        pos: BlockPos,
        state: BlockState,
    ) : HTSingleItemInputBlockEntity<RECIPE>(blockHolder, pos, state) {
        constructor(
            finder: HTRecipeFinder<SingleRecipeInput, RECIPE>,
            blockHolder: Holder<Block>,
            pos: BlockPos,
            state: BlockState,
        ) : this(HTFinderRecipeCache(finder), blockHolder, pos, state)

        final override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): RECIPE? =
            recipeCache.getFirstRecipe(input, level)
    }

    //    Simple    //

    private class Simple<RECIPE : HTSingleInputRecipe>(
        private val sound: SoundEvent,
        private val soundValues: Pair<Float, Float>,
        recipeType: HTRecipeFinder<SingleRecipeInput, RECIPE>,
        blockHolder: Holder<Block>,
        pos: BlockPos,
        state: BlockState,
    ) : Cached<RECIPE>(
            recipeType,
            blockHolder,
            pos,
            state,
        ) {
        lateinit var outputSlot: HTItemStackSlot
            private set

        override fun initializeItemHandler(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
            // input
            inputSlot = singleInput(builder, listener)
            // output
            outputSlot = singleOutput(builder, listener)
        }

        override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: RECIPE): Boolean = outputSlot
            .insert(recipe.assembleItem(input, level.registryAccess()), HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) == null

        override fun completeRecipe(
            level: ServerLevel,
            pos: BlockPos,
            state: BlockState,
            input: SingleRecipeInput,
            recipe: RECIPE,
        ) {
            // 実際にアウトプットに搬出する
            outputSlot.insert(recipe.assembleItem(input, level.registryAccess()), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
            // インプットを減らす
            HTStackSlotHelper.shrinkStack(inputSlot, recipe::getRequiredCount, HTStorageAction.EXECUTE)
            // SEを鳴らす
            level.playSound(null, pos, sound, SoundSource.BLOCKS, soundValues.first, soundValues.second)
        }
    }
}
