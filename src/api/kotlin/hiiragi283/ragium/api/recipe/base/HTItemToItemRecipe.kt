package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTItemToObjRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack

abstract class HTItemToItemRecipe(override val ingredient: HTItemIngredient, override val result: HTItemResult) :
    HTItemToObjRecipe<HTItemResult> {
    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.get()
}
