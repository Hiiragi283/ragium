package hiiragi283.ragium.client.jei.extension

import hiiragi283.ragium.api.integration.jei.HTItemAndItemRecipeCategoryExtension
import hiiragi283.ragium.common.recipe.special.HTBannerCopyingRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import net.minecraft.world.item.ItemStack

data object HTBannerCopyingRecipeCategoryExtension : HTItemAndItemRecipeCategoryExtension<HTBannerCopyingRecipe> {
    override fun <T : IIngredientAcceptor<T>> setFirstInput(recipe: HTBannerCopyingRecipe, accessor: T) {
        accessor.addItemStack(ItemStack(recipe.banner))
    }

    override fun <T : IIngredientAcceptor<T>> setSecondInput(recipe: HTBannerCopyingRecipe, accessor: T) {
        accessor.addItemStack(ItemStack(recipe.banner))
    }

    override fun <T : IIngredientAcceptor<T>> setOutput(recipe: HTBannerCopyingRecipe, accessor: T) {
        accessor.addItemStack(ItemStack(recipe.banner))
    }
}
