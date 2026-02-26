package hiiragi283.ragium.client.jei.extension

import hiiragi283.ragium.api.integration.jei.HTItemAndItemRecipeCategoryExtension
import hiiragi283.ragium.common.recipe.special.HTBookCloningRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

data object HTBookCopyingRecipeCategoryExtension : HTItemAndItemRecipeCategoryExtension<HTBookCloningRecipe> {
    override fun <T : IIngredientAcceptor<T>> setFirstInput(recipe: HTBookCloningRecipe, accessor: T) {
        accessor.addItemStack(ItemStack(Items.WRITABLE_BOOK))
    }

    override fun <T : IIngredientAcceptor<T>> setSecondInput(recipe: HTBookCloningRecipe, accessor: T) {
        accessor.addItemStack(ItemStack(Items.WRITTEN_BOOK))
    }

    override fun <T : IIngredientAcceptor<T>> setOutput(recipe: HTBookCloningRecipe, accessor: T) {
        accessor.addItemStack(ItemStack(Items.WRITTEN_BOOK))
    }
}
