package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.extension.unsupported
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.HTSingleInputRecipe
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.value.HTValueInput
import hiiragi283.ragium.api.storage.value.HTValueOutput
import hiiragi283.ragium.common.recipe.HTMultiRecipeCache
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.common.util.HTIngredientHelper
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.min

class HTMultiSmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<SingleRecipeInput, HTSingleInputRecipe>(
        HTMachineVariant.MULTI_SMELTER,
        pos,
        state,
    ) {
    private lateinit var inputSlot: HTItemSlot
    private lateinit var outputSlot: HTItemSlot

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        // input
        inputSlot = HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
        // output
        outputSlot = HTItemStackSlot.output(listener, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1))
        return HTSimpleItemSlotHolder(this, listOf(inputSlot), listOf(outputSlot))
    }

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        baseCache.serialize(output)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        baseCache.deserialize(input)
    }

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.SMELTER.openMenu(player, title, this, ::writeExtraContainerData)

    private val baseCache: HTMultiRecipeCache<SingleRecipeInput, AbstractCookingRecipe> =
        HTMultiRecipeCache(RecipeType.SMELTING, RecipeType.SMOKING, RecipeType.BLASTING)

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = SingleRecipeInput(inputSlot.getStack())

    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): HTSingleInputRecipe? {
        val baseRecipe: AbstractCookingRecipe = baseCache.getFirstRecipe(input, level) ?: return null
        val result: ItemStack = baseRecipe.assemble(input, level.registryAccess())
        if (result.isEmpty) return null
        val resultMaxSize: Int = result.maxStackSize

        var inputCount: Int = min(inputSlot.count, getMaxParallel())
        val maxParallel: Int = min(inputCount, getMaxParallel())
        var outputCount: Int = result.count * maxParallel
        if (outputCount > resultMaxSize) {
            outputCount = resultMaxSize - (resultMaxSize % maxParallel)
            inputCount = outputCount / maxParallel
        }
        if (inputCount <= 0 || outputCount <= 0) return null
        return MultiSmeltingRecipe(baseRecipe, outputCount)
    }

    private fun getMaxParallel(): Int = when (upgradeHandler.getTier()) {
        HTComponentTier.BASIC -> 2
        HTComponentTier.ADVANCED -> 4
        HTComponentTier.ELITE -> 8
        HTComponentTier.ULTIMATE -> 16
        HTComponentTier.ETERNAL -> inputSlot.getStack().maxStackSize
        null -> 1
    }

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTSingleInputRecipe): Boolean =
        outputSlot.insertItem(recipe.assemble(input, level.registryAccess()), true, HTStorageAccess.INTERNAl).isEmpty

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTSingleInputRecipe,
    ) {
        // 実際にアウトプットに搬出する
        outputSlot.insertItem(recipe.assemble(input, level.registryAccess()), false, HTStorageAccess.INTERNAl)
        // インプットを減らす
        HTIngredientHelper.shrinkStack(inputSlot, recipe::getRequiredCount, false)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 1f)
    }

    private class MultiSmeltingRecipe(private val recipe: AbstractCookingRecipe, private val count: Int) : HTSingleInputRecipe {
        private val ingredient: Ingredient get() = recipe.ingredients[0]

        override fun getRequiredCount(stack: ItemStack): Int = when {
            ingredient.test(stack) -> 1
            else -> 0
        }

        override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

        override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack =
            recipe.assemble(input, registries).copyWithCount(count)

        override fun getSerializer(): RecipeSerializer<*> = unsupported()

        override fun getType(): RecipeType<*> = unsupported()

        override fun isIncomplete(): Boolean = ingredient.items.isEmpty()
    }
}
