package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.single.HTSingleItemRecipe
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.SingleRecipeInput

open class HTVanillaSingleItemRecipe(protected val recipe: Recipe<SingleRecipeInput>) :
    HTSingleItemRecipe,
    HTRecipe.Fake<SingleRecipeInput> {
    val ingredient: Ingredient = recipe.ingredients[0]

    final override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

    final override fun assembleItem(input: SingleRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        recipe.assemble(input, provider).toImmutable()

    final override fun isIncomplete(): Boolean = ingredient.hasNoItems()

    final override fun getRequiredCount(stack: ImmutableItemStack): Int = when {
        ingredient.test(stack.unwrap()) -> 1
        else -> 0
    }
}
