package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.material.HTTierType
import hiiragi283.ragium.common.recipe.HTFakeRecipeCache
import hiiragi283.ragium.common.recipe.HTMultiRecipeCache
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.min

class HTMultiSmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleItemInputBlockEntity<HTItemToItemRecipe>(
        HTFakeRecipeCache(),
        HTMachineVariant.MULTI_SMELTER,
        pos,
        state,
    ) {
    private lateinit var outputSlot: HTItemSlot

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        // input
        inputSlot = HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
        // output
        outputSlot = HTItemStackSlot.output(listener, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1))
        return HTSimpleItemSlotHolder(this, listOf(inputSlot), listOf(outputSlot))
    }

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.SMELTER.openMenu(player, title, this, ::writeExtraContainerData)

    private val baseCache: HTMultiRecipeCache<SingleRecipeInput, AbstractCookingRecipe> =
        HTMultiRecipeCache(RecipeType.SMELTING, RecipeType.SMOKING, RecipeType.BLASTING)

    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): HTItemToItemRecipe? {
        val baseRecipe: AbstractCookingRecipe = baseCache.getFirstRecipe(input, level) ?: return null
        val result: ItemStack = baseRecipe.assemble(input, level.registryAccess())
        if (result.isEmpty) return null
        val resultMaxSize: Int = result.maxStackSize

        val maxParallel: Int = getMaxParallel()
        var inputCount: Int = min(inputSlot.count, maxParallel)
        var outputCount: Int = result.count * maxParallel
        if (outputCount > resultMaxSize) {
            outputCount = resultMaxSize - (resultMaxSize % maxParallel)
            inputCount = outputCount / maxParallel
        }
        if (inputCount <= 0 || outputCount <= 0) return null
        result.count = outputCount

        return MultiSmeltingRecipe(
            HTIngredientHelper.item(baseRecipe.ingredients[0], inputCount),
            HTResultHelper.INSTANCE.item(result),
        )
    }

    private fun getMaxParallel(): Int = when (upgradeHandler.componentTier) {
        HTTierType.BASIC -> 2
        HTTierType.ADVANCED -> 4
        HTTierType.ELITE -> 8
        HTTierType.ULTIMATE -> 16
        HTTierType.CREATIVE -> inputSlot.getStack().maxStackSize
        null -> 1
    }

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTItemToItemRecipe): Boolean =
        outputSlot.insertItem(recipe.assemble(input, level.registryAccess()), true, HTStorageAccess.INTERNAl).isEmpty

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTItemToItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        outputSlot.insertItem(recipe.assemble(input, level.registryAccess()), false, HTStorageAccess.INTERNAl)
        // インプットを減らす
        inputSlot.shrinkStack(recipe.ingredient, false)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 1f)
    }

    private class MultiSmeltingRecipe(ingredient: HTItemIngredient, result: HTItemResult) : HTItemToItemRecipe(ingredient, result) {
        override fun getSerializer(): RecipeSerializer<*> = error("")

        override fun getType(): RecipeType<*> = error("")
    }
}
