package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler

class HTEngraverBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<SingleRecipeInput, StonecutterRecipe>(
        RecipeType.STONECUTTING,
        HTMachineVariant.ENGRAVER,
        pos,
        state,
    ) {
    override val inventory: HTItemHandler = HTItemStackHandler
        .Builder(6)
        .addInput(0)
        .addOutput(2..5)
        .build(this)

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.ENGRAVER.openMenu(player, title, this, ::writeExtraContainerData)

    override fun createSound(random: RandomSource, pos: BlockPos): SoundInstance =
        createSound(SoundEvents.UI_STONECUTTER_TAKE_RESULT, random, pos)

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = SingleRecipeInput(inventory.getStackInSlot(0))

    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): StonecutterRecipe? {
        val allRecipes: List<RecipeHolder<StonecutterRecipe>> = level.recipeManager.getAllRecipesFor(RecipeType.STONECUTTING)
        if (allRecipes.isEmpty()) return null
        // 指定されたアイテムと同じものを出力するレシピだけを選ぶ
        var matchedRecipe: StonecutterRecipe? = null
        for (holder: RecipeHolder<StonecutterRecipe> in allRecipes) {
            val recipe: StonecutterRecipe = holder.value
            if (!recipe.matches(input, level)) continue
            val result: ItemStack = recipe.assemble(input, level.registryAccess())
            if (ItemStack.isSameItemSameComponents(inventory.getStackInSlot(1), result)) {
                matchedRecipe = recipe
                break
            }
        }
        return matchedRecipe
    }

    // アウトプットに搬出できるか判定する
    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: StonecutterRecipe): Boolean =
        insertToOutput(recipe.assemble(input, level.registryAccess()), true).isEmpty

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: StonecutterRecipe,
    ) {
        // 実際にアウトプットに搬出する
        insertToOutput(recipe.assemble(input, level.registryAccess()), false)
        // インプットを減らす
        inventory.extractItem(0, 1, false)
    }

    //    Slot    //

    override fun addInputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {
        consumer(inventory, 0, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
        consumer(inventory, 1, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2))
    }

    override fun addOutputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {
        consumer(inventory, 2, HTSlotHelper.getSlotPosX(5), HTSlotHelper.getSlotPosY(0.5))
        consumer(inventory, 3, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(0.5))
        consumer(inventory, 4, HTSlotHelper.getSlotPosX(5), HTSlotHelper.getSlotPosY(1.5))
        consumer(inventory, 5, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(1.5))
    }
}
