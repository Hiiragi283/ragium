package hiiragi283.ragium.client.jei.extension

import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.integration.jei.addFluidIngredient
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.item.createEnchantedBook
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.integration.jei.HTEnchantingRecipeCategoryExtension
import hiiragi283.ragium.common.recipe.HTHolderEnchantingRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import net.minecraft.world.item.Items

data object HTHolderEnchantingRecipeCategoryExtension : HTEnchantingRecipeCategoryExtension<HTHolderEnchantingRecipe> {
    override fun <T : IIngredientAcceptor<T>> setExpInput(recipe: HTHolderEnchantingRecipe, accessor: T) {
        accessor.addFluidIngredient(HTIngredientCreator.create(HCFluids.EXPERIENCE, recipe.requiredExpAmount))
    }

    override fun <T : IIngredientAcceptor<T>> setBookInput(recipe: HTHolderEnchantingRecipe, accessor: T) {
        accessor.addItemLike(Items.BOOK)
    }

    override fun <T : IIngredientAcceptor<T>> setItemInput(recipe: HTHolderEnchantingRecipe, accessor: T) {
        accessor.addItemIngredient(recipe.ingredient)
    }

    override fun <T : IIngredientAcceptor<T>> setOutput(recipe: HTHolderEnchantingRecipe, accessor: T) {
        accessor.addItemStack(createEnchantedBook(recipe.holder))
    }
}
