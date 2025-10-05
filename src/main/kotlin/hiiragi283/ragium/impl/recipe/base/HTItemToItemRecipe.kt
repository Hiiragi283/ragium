package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.extension.unsupported
import hiiragi283.ragium.api.recipe.HTSingleInputRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

abstract class HTItemToItemRecipe(val ingredient: HTItemIngredient, val result: HTItemResult) : HTSingleInputRecipe {
    companion object {
        @JvmStatic
        fun wrapVanilla(registries: HolderLookup.Provider, recipe: Recipe<SingleRecipeInput>): HTItemToItemRecipe =
            object : HTItemToItemRecipe(
                HTItemIngredient.wrapVanilla(recipe.ingredients[0]),
                HTResultHelper.INSTANCE.item(recipe.getResultItem(registries)),
            ) {
                override fun getSerializer(): RecipeSerializer<*> = unsupported()

                override fun getType(): RecipeType<*> = unsupported()
            }
    }

    override fun getRequiredCount(stack: ItemStack): Int = ingredient.getRequiredAmount(stack)

    override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

    final override fun isIncomplete(): Boolean = ingredient.hasNoMatchingStacks() || result.hasNoMatchingStack()

    final override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack =
        getItemResult(input, registries, result)
}
