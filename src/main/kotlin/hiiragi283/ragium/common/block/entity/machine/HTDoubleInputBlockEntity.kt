package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.recipe.HTDoubleItemToItemRecipe
import hiiragi283.ragium.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler

abstract class HTDoubleInputBlockEntity<R : HTDoubleItemToItemRecipe>(
    recipeType: RecipeType<R>,
    type: HTDeferredBlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTProcessorBlockEntity<HTDoubleRecipeInput, R>(
        recipeType,
        type,
        pos,
        state,
    ) {
    final override val inventory: HTItemHandler = HTItemStackHandler(3, this::setChanged)
    final override val itemFilter: HTItemFilter = object : HTItemFilter {
        override fun canInsert(handler: IItemHandler, slot: Int, stack: ItemStack): Boolean = when (slot) {
            0 -> !ItemStack.isSameItemSameComponents(stack, handler.getStackInSlot(1))
            1 -> !ItemStack.isSameItemSameComponents(stack, handler.getStackInSlot(0))
            else -> false
        }

        override fun canExtract(handler: IItemHandler, slot: Int, amount: Int): Boolean = slot == 2
    }

    //    Ticking    //

    final override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTDoubleRecipeInput =
        HTDoubleRecipeInput(inventory.getStackInSlot(0), inventory.getStackInSlot(1))
}
