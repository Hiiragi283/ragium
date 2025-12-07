package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.block.attribute.getAttributeFront
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeFinder
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTRockGeneratingRecipe
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.recipe.HTFinderRecipeCache
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemSlot
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FluidState

class HTStoneCollectorBlockEntity(pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity.Tickable(RagiumBlocks.STONE_COLLECTOR, pos, state) {
    lateinit var outputSlot: HTBasicItemSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        outputSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTOutputItemSlot.create(listener, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1)),
        )
    }

    //    Ticking    //

    private val recipeCache: HTRecipeCache<HTMultiRecipeInput, HTRockGeneratingRecipe> = HTFinderRecipeCache(RecipeFinder())

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // インプットに一致するレシピを探索する
        val front: Direction = state.getAttributeFront() ?: return false
        val input: HTMultiRecipeInput = createInput(level, pos, front) ?: return false
        val recipe: HTRockGeneratingRecipe = recipeCache.getFirstRecipe(input, level) ?: return false
        // 実際にアウトプットに搬出する
        outputSlot.insert(recipe.assembleItem(input, level.registryAccess()), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 0.5f, 0.5f)
        return true
    }

    private fun createInput(level: ServerLevel, pos: BlockPos, front: Direction): HTMultiRecipeInput? = HTMultiRecipeInput.create {
        val leftPos: BlockPos = pos.relative(front.counterClockWise)
        val rightPos: BlockPos = pos.relative(front.clockWise)
        // 左側は液体のみ
        this.fluids += getFluidInput(level, leftPos)
        // 右側はブロックも判定する
        this.fluids += getFluidInput(level, rightPos)
        this.items += ImmutableItemStack.ofNullable(level.getBlockState(rightPos).block)
        // 下側の触媒を取得
        this.items += ImmutableItemStack.ofNullable(level.getBlockState(pos.below()).block)
    }

    private fun getFluidInput(level: ServerLevel, pos: BlockPos): ImmutableFluidStack? {
        val fluidState: FluidState = level.getFluidState(pos)
        return when {
            fluidState.isSource -> ImmutableFluidStack.ofNullable(fluidState.type, 1000)
            else -> null
        }
    }

    //    RecipeFinder    //

    private class RecipeFinder : HTRecipeFinder<HTMultiRecipeInput, HTRockGeneratingRecipe> {
        override fun getRecipeFor(
            manager: RecipeManager,
            input: HTMultiRecipeInput,
            level: Level,
            lastRecipe: RecipeHolder<HTRockGeneratingRecipe>?,
        ): RecipeHolder<HTRockGeneratingRecipe>? {
            // 入力が空の場合は即座に抜ける
            if (input.isEmpty) return null
            // キャッシュから判定を行う
            if (lastRecipe != null && matches(lastRecipe.value, input, level)) {
                return lastRecipe
            }
            // 次にRecipeManagerから一覧を取得する
            val allRecipes: List<RecipeHolder<HTRockGeneratingRecipe>> =
                manager.getAllRecipesFor(RagiumRecipeTypes.ROCK_GENERATING.get())
            // 触媒ありのレシピから優先して判定を行う
            for (holder: RecipeHolder<HTRockGeneratingRecipe> in allRecipes) {
                val recipe: HTRockGeneratingRecipe = holder.value()
                if (recipe.bottom.isPresent && matches(recipe, input, level)) {
                    return holder
                }
            }
            // 触媒なしのレシピを判定
            for (holder: RecipeHolder<HTRockGeneratingRecipe> in allRecipes) {
                val recipe: HTRockGeneratingRecipe = holder.value()
                if (recipe.bottom.isEmpty && matches(recipe, input, level)) {
                    return holder
                }
            }
            return null
        }
    }
}
