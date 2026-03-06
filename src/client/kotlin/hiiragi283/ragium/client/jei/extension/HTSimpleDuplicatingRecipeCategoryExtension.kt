package hiiragi283.ragium.client.jei.extension

import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.integration.jei.addFluidIngredient
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.ragium.api.integration.jei.HTDuplicatingRecipeCategoryExtension
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.recipe.HTSimpleDuplicatingRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor

data object HTSimpleDuplicatingRecipeCategoryExtension : HTDuplicatingRecipeCategoryExtension<HTSimpleDuplicatingRecipe> {
    override fun <T : IIngredientAcceptor<T>> setInput(recipe: HTSimpleDuplicatingRecipe, accessor: T) {
        accessor.addItemIngredient(recipe.ingredient)
    }

    override fun <T : IIngredientAcceptor<T>> setRequiredMatter(recipe: HTSimpleDuplicatingRecipe, accessor: T) {
        accessor.addFluidIngredient(false, HTIngredientCreator.create(RagiumTags.Fluids.RAGI_MATTER, recipe.requiredMatter))
    }
}
