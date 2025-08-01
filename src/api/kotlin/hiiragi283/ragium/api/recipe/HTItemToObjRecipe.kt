package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level

abstract class HTItemToObjRecipe<R : HTRecipeResult<*, *>>(
    private val recipeType: RecipeType<*>,
    val ingredient: HTItemIngredient,
    val result: R,
) : HTRecipe<SingleRecipeInput> {
    final override fun test(input: SingleRecipeInput): Boolean = !isIncomplete && ingredient.test(input.item())

    final override fun isIncomplete(): Boolean = ingredient.hasNoMatchingStacks() || result.hasNoMatchingStack

    final override fun matches(input: SingleRecipeInput, level: Level): Boolean = test(input)

    final override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack =
        if (test(input)) getResultItem(registries) else ItemStack.EMPTY

    final override fun getType(): RecipeType<*> = recipeType
}
