package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.common.inventory.HTItemToItemMenu
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler

abstract class HTItemToItemBlockEntity(
    protected val menuType: HTDeferredMenuType<out HTItemToItemMenu>,
    recipeType: RecipeType<HTItemToItemRecipe>,
    type: HTDeferredBlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTProcessorBlockEntity<SingleRecipeInput, HTItemToItemRecipe>(recipeType, type, pos, state) {
    final override val inventory = HTItemStackHandler(2, this::setChanged)

    //    Ticking    //

    final override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput =
        SingleRecipeInput(inventory.getStackInSlot(0))

    // アウトプットに搬出できるか判定する
    final override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTItemToItemRecipe): Boolean =
        insertToOutput(1..1, recipe.assemble(input, level.registryAccess()), true).isEmpty

    final override fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTItemToItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        insertToOutput(1..1, recipe.assemble(input, level.registryAccess()), false)
        // インプットを減らす
        inventory.consumeStackInSlot(0, recipe.ingredient, false)
        // サウンドを流す
        playSound(level, pos)
    }

    protected abstract fun playSound(level: Level, pos: BlockPos)

    final override fun getItemHandler(direction: Direction?): HTFilteredItemHandler = HTFilteredItemHandler(
        inventory,
        object : HTItemFilter {
            override fun canInsert(handler: IItemHandler, slot: Int, stack: ItemStack): Boolean = slot == 0

            override fun canExtract(handler: IItemHandler, slot: Int, amount: Int): Boolean = slot != 0
        },
    )

    //    Menu    //

    final override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTItemToItemMenu = HTItemToItemMenu(
        menuType,
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
