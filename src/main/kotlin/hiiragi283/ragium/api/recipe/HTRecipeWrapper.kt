package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.util.DUMMY_LOOKUP
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.world.World

class HTRecipeWrapper<T : RecipeInput>(val recipe: Recipe<T>) : HTRecipeBase<T> {
    override val inputs: List<WeightedIngredient> =
        recipe.ingredients.map(WeightedIngredient.Companion::of)
    override val outputs: List<HTRecipeResult> =
        listOf(recipe.getResult(DUMMY_LOOKUP).let(HTRecipeResult.Companion::stack))

    override fun getSerializer(): RecipeSerializer<*> = recipe.serializer

    override fun getType(): RecipeType<*> = recipe.type

    override fun matches(input: T, world: World): Boolean = recipe.matches(input, world)
}
