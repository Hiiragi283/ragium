package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeFinder
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.recipe.HTFinderRecipeCache
import hiiragi283.ragium.common.recipe.vanilla.HTVanillaCookingRecipe
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.common.upgrade.RagiumUpgradeKeys
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.BlastingRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.SmokingRecipe
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import kotlin.jvm.optionals.getOrNull

abstract class HTAbstractSmelterBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTSingleItemOutputBlockEntity<HTVanillaCookingRecipe>(blockHolder, pos, state) {
    lateinit var inputSlot: HTBasicItemSlot
        protected set

    final override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = singleInput(builder, listener)
        // output
        outputSlot = singleOutput(builder, listener)
    }

    private fun <RECIPE : AbstractCookingRecipe> findRecipe(
        recipeType: RecipeType<RECIPE>,
    ): HTRecipeFinder.Vanilla<SingleRecipeInput, RECIPE> =
        HTRecipeFinder.Vanilla { input: SingleRecipeInput, level: Level, lastRecipe: RecipeHolder<RECIPE>? ->
            level.recipeManager
                .getRecipeFor(recipeType, input, level, lastRecipe)
                .getOrNull()
        }

    private val smeltingCache: HTRecipeCache<SingleRecipeInput, SmeltingRecipe> = HTFinderRecipeCache(findRecipe(RecipeType.SMELTING))
    private val blastingCache: HTRecipeCache<SingleRecipeInput, BlastingRecipe> = HTFinderRecipeCache(findRecipe(RecipeType.BLASTING))
    private val smokingCache: HTRecipeCache<SingleRecipeInput, SmokingRecipe> = HTFinderRecipeCache(findRecipe(RecipeType.SMOKING))

    protected fun getRecipeCache(): HTRecipeCache<SingleRecipeInput, out AbstractCookingRecipe> = when {
        hasUpgrade(RagiumUpgradeKeys.BLASTING) -> blastingCache
        hasUpgrade(RagiumUpgradeKeys.SMOKING) -> smokingCache
        else -> smeltingCache
    }

    final override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean = outputSlot.getNeeded() > 0

    final override fun buildRecipeInput(builder: HTRecipeInput.Builder) {
        builder.items += inputSlot.getStack()
    }

    final override fun getRecipeTime(recipe: HTVanillaCookingRecipe): Int = recipe.cookingTime

    final override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTRecipeInput,
        recipe: HTVanillaCookingRecipe,
    ) {
        super.completeRecipe(level, pos, state, input, recipe)
        // インプットを減らす
        inputSlot.extract(recipe.getRequiredCount(), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 1f)
    }
}
