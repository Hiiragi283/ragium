package hiiragi283.ragium.common.data.recipe

import hiiragi283.ragium.api.data.recipe.HTIngredientRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTStackRecipeBuilder
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SmithingTransformRecipe
import net.minecraft.world.level.ItemLike

class HTSmithingRecipeBuilder(stack: ImmutableItemStack) :
    HTStackRecipeBuilder<HTSmithingRecipeBuilder>("smithing", stack),
    HTIngredientRecipeBuilder<HTSmithingRecipeBuilder> {
    companion object {
        @JvmStatic
        fun create(item: ItemLike, count: Int = 1): HTSmithingRecipeBuilder = HTSmithingRecipeBuilder(ImmutableItemStack.of(item, count))
    }

    private val ingredients: MutableList<Ingredient> = mutableListOf()

    override fun addIngredient(ingredient: Ingredient): HTSmithingRecipeBuilder = apply {
        check(ingredients.size <= 2) { "Ingredient has already been initialized!" }
        ingredients.add(ingredient)
    }

    override fun createRecipe(output: ItemStack): SmithingTransformRecipe = SmithingTransformRecipe(
        ingredients[0],
        ingredients[1],
        ingredients.getOrNull(2) ?: Ingredient.of(),
        output,
    )
}
