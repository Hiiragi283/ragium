package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.inventory.HTItemToItemMenu
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

abstract class HTItemToItemBlockEntity(
    protected val menuType: HTDeferredMenuType<out HTItemToItemMenu>,
    recipeType: RecipeType<HTItemToItemRecipe>,
    variant: HTMachineVariant,
    pos: BlockPos,
    state: BlockState,
) : HTProcessorBlockEntity<SingleRecipeInput, HTItemToItemRecipe>(recipeType, variant, pos, state) {
    final override val inventory: HTItemHandler = HTItemStackHandler
        .Builder(2)
        .addInput(0)
        .addOutput(1)
        .build(this)

    //    Ticking    //

    final override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput =
        SingleRecipeInput(inventory.getStackInSlot(0))

    // アウトプットに搬出できるか判定する
    final override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTItemToItemRecipe): Boolean =
        insertToOutput(recipe.assemble(input, level.registryAccess()), true).isEmpty

    final override fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTItemToItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        insertToOutput(recipe.assemble(input, level.registryAccess()), false)
        // インプットを減らす
        inventory.shrinkStack(0, recipe.ingredient, false)
        // サウンドを流す
        playSound(level, pos)
    }

    protected abstract fun playSound(level: Level, pos: BlockPos)

    //    Menu    //

    final override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTItemToItemMenu = HTItemToItemMenu(
        menuType,
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
