package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTItemToObjRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

abstract class HTItemToItemRecipe(override val ingredient: HTItemIngredient, override val result: HTItemResult) :
    HTItemToObjRecipe<HTItemResult> {
    final override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack =
        getItemResult(input, registries, result)
}
