package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.inventory.HTEngraverMenu
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.minecraft.world.level.block.state.BlockState

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
    override val energyUsage: Int get() = RagiumAPI.getConfig().getBasicMachineEnergyUsage()

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

    override fun serverTickPost(
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
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS)
    }

    //    Menu    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTEngraverMenu = HTEngraverMenu(
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
