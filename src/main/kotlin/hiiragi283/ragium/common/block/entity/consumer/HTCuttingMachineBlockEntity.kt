package hiiragi283.ragium.common.block.entity.consumer

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.manager.HTRecipeType
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.item.getItemStack
import hiiragi283.ragium.api.storage.item.toRecipeInput
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.recipe.VanillaRecipeTypes
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HTCuttingMachineBlockEntity(pos: BlockPos, state: BlockState) :
    HTChancedItemOutputBlockEntity<SingleRecipeInput, HTItemToChancedItemRecipe>(
        RagiumBlocks.CUTTING_MACHINE,
        pos,
        state,
    ) {
    lateinit var catalystSlot: HTItemStackSlot
        private set

    override fun initCatalyst(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        catalystSlot = singleCatalyst(builder, listener)
    }

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = inputSlot.toRecipeInput()

    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): HTItemToChancedItemRecipe? {
        // Cutting from Ragium
        val cuttingRecipe: HTItemToChancedItemRecipe? =
            getFirstRecipe(input, level, RagiumRecipeTypes.CUTTING)
        if (cuttingRecipe != null) return cuttingRecipe
        // Cutting from Farmers Delight
        // Stone Cutting from Vanilla
        val stoneRecipe: HTItemToChancedItemRecipe? =
            getFirstRecipe(input, level, VanillaRecipeTypes.STONECUTTING)
        if (stoneRecipe != null) return stoneRecipe
        return null
    }

    private fun getFirstRecipe(
        input: SingleRecipeInput,
        level: Level,
        recipeType: HTRecipeType<SingleRecipeInput, out HTItemToChancedItemRecipe>,
    ): HTItemToChancedItemRecipe? = getFirstRecipe(input, level, recipeType.getAllRecipes(level.recipeManager))

    private fun getFirstRecipe(
        input: SingleRecipeInput,
        level: Level,
        recipes: Sequence<HTItemToChancedItemRecipe>,
    ): HTItemToChancedItemRecipe? {
        // 指定されたアイテムと同じものを出力するレシピだけを選ぶ
        var matchedHolder: HTItemToChancedItemRecipe? = null
        for (recipe: HTItemToChancedItemRecipe in recipes) {
            if (!recipe.matches(input, level)) continue
            val result: ImmutableItemStack = recipe.assembleItem(input, level.registryAccess()) ?: continue
            if (ItemStack.isSameItemSameComponents(catalystSlot.getItemStack(), result.unwrap())) {
                matchedHolder = recipe
                break
            }
        }
        return matchedHolder
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTItemToChancedItemRecipe,
    ) {
        super.completeRecipe(level, pos, state, input, recipe)
        // インプットを減らす
        inputSlot.extract(1, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1f, 1f)
    }
}
