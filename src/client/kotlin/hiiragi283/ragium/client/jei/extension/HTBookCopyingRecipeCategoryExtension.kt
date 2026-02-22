package hiiragi283.ragium.client.jei.extension

import hiiragi283.ragium.api.integration.jei.HTItemAndItemRecipeCategoryExtension
import hiiragi283.ragium.common.recipe.special.HTBookCopyingRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

data object HTBookCopyingRecipeCategoryExtension : HTItemAndItemRecipeCategoryExtension<HTBookCopyingRecipe> {
    override fun <T : IIngredientAcceptor<T>> setFirstInput(recipe: HTBookCopyingRecipe, accessor: T) {
        accessor.addItemStack(ItemStack(Items.WRITABLE_BOOK))
    }

    override fun <T : IIngredientAcceptor<T>> setSecondInput(recipe: HTBookCopyingRecipe, accessor: T) {
        accessor.addItemStack(ItemStack(Items.WRITTEN_BOOK))
    }

    override fun <T : IIngredientAcceptor<T>> setOutput(recipe: HTBookCopyingRecipe, accessor: T) {
        accessor.addItemStack(ItemStack(Items.WRITTEN_BOOK))
    }
}
