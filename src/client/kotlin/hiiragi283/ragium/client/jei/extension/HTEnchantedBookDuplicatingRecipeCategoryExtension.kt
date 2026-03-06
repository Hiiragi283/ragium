package hiiragi283.ragium.client.jei.extension

import hiiragi283.core.api.integration.jei.addFluidIngredient
import hiiragi283.core.api.util.getOrEmpty
import hiiragi283.ragium.api.integration.jei.HTDuplicatingRecipeCategoryExtension
import hiiragi283.ragium.common.recipe.special.HTEnchantedBookDuplicatingRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.runtime.IIngredientManager
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class HTEnchantedBookDuplicatingRecipeCategoryExtension(val manager: IIngredientManager) :
    HTDuplicatingRecipeCategoryExtension<HTEnchantedBookDuplicatingRecipe> {
    override fun <T : IIngredientAcceptor<T>> setInput(recipe: HTEnchantedBookDuplicatingRecipe, accessor: T) {
        accessor.addItemStacks(manager.allItemStacks.filter { stack: ItemStack -> stack.`is`(Items.ENCHANTED_BOOK) })
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: HTEnchantedBookDuplicatingRecipe,
        inputSlot: IRecipeSlotDrawable,
        matterSlot: IRecipeSlotDrawable,
        focuses: IFocusGroup,
    ) {
        val inputStack: ItemStack = inputSlot.displayedItemStack.getOrEmpty()
        if (inputStack.isEmpty) return
        matterSlot
            .createDisplayOverrides()
            .addFluidIngredient(false, recipe.createFluidIngredient(inputStack))
    }
}
