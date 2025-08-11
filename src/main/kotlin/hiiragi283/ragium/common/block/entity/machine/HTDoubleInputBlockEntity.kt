package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.recipe.HTDoubleItemToItemRecipe
import hiiragi283.ragium.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.state.BlockState

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
    final override val inventory: HTItemHandler = object : HTItemStackHandler(3) {
        override fun isItemValid(slot: Int, stack: ItemStack): Boolean = when (slot) {
            0 -> !ItemStack.isSameItemSameComponents(stack, getStackInSlot(1))
            1 -> !ItemStack.isSameItemSameComponents(stack, getStackInSlot(0))
            else -> false
        }

        override fun onContentsChanged(slot: Int) {
            this@HTDoubleInputBlockEntity.setChanged()
        }

        override val inputSlots: IntArray = intArrayOf(0, 1)
        override val outputSlots: IntArray = intArrayOf(2)
    }

    //    Ticking    //

    final override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTDoubleRecipeInput =
        HTDoubleRecipeInput(inventory.getStackInSlot(0), inventory.getStackInSlot(1))
}
