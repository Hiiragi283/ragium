package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.recipe.HTFinderRecipeCache
import hiiragi283.ragium.common.recipe.HTVanillaCookingRecipe
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.common.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.BlastingRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.SmokingRecipe
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import kotlin.jvm.optionals.getOrNull

abstract class HTAbstractSmelterBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTSingleItemInputBlockEntity<HTVanillaCookingRecipe>(blockHolder, pos, state) {
    lateinit var catalystSlot: HTBasicItemSlot
        private set
    lateinit var outputSlot: HTBasicItemSlot
        private set

    final override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = singleInput(builder, listener)
        // catalyst
        catalystSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTBasicItemSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2)),
        )
        // output
        outputSlot = singleOutput(builder, listener)
    }

    private val smeltingCache: HTRecipeCache<SingleRecipeInput, SmeltingRecipe> =
        HTFinderRecipeCache { manager: RecipeManager, input: SingleRecipeInput, level: Level, lastRecipe: RecipeHolder<SmeltingRecipe>? ->
            manager.getRecipeFor(RecipeType.SMELTING, input, level, lastRecipe).getOrNull()
        }
    private val blastingCache: HTRecipeCache<SingleRecipeInput, BlastingRecipe> =
        HTFinderRecipeCache { manager: RecipeManager, input: SingleRecipeInput, level: Level, lastRecipe: RecipeHolder<BlastingRecipe>? ->
            manager.getRecipeFor(RecipeType.BLASTING, input, level, lastRecipe).getOrNull()
        }
    private val smokingCache: HTRecipeCache<SingleRecipeInput, SmokingRecipe> =
        HTFinderRecipeCache { manager: RecipeManager, input: SingleRecipeInput, level: Level, lastRecipe: RecipeHolder<SmokingRecipe>? ->
            manager.getRecipeFor(RecipeType.SMOKING, input, level, lastRecipe).getOrNull()
        }

    protected fun getRecipeCache(): HTRecipeCache<SingleRecipeInput, out AbstractCookingRecipe> = when (catalystSlot.getStack()?.value()) {
        Items.BLAST_FURNACE -> blastingCache
        Items.SMOKER -> smokingCache
        else -> smeltingCache
    }

    final override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean = outputSlot.getNeeded() > 0

    final override fun getRecipeTime(recipe: HTVanillaCookingRecipe): Int = recipe.cookingTime

    final override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTVanillaCookingRecipe): Boolean =
        HTStackSlotHelper.canInsertStack(outputSlot, input, level, recipe::assembleItem)

    final override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTVanillaCookingRecipe,
    ) {
        // 実際にアウトプットに搬出する
        outputSlot.insert(recipe.assembleItem(input, level.registryAccess()), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // インプットを減らす
        HTStackSlotHelper.shrinkStack(inputSlot, recipe::getRequiredCount, HTStorageAction.EXECUTE)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 1f)
    }
}
