package hiiragi283.ragium.api.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.toDataResult
import hiiragi283.ragium.api.extension.validate
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeEntry
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.util.Identifier
import net.minecraft.world.World

class HTRecipeCache<I : RecipeInput, R : Recipe<I>>(private val recipeType: RecipeType<R>) {
    private var id: Identifier? = null

    fun getFirstMatch(input: I, world: World): DataResult<R> = world.recipeManager
        .getFirstMatch(recipeType, input, world, id)
        .toDataResult { "Failed to find matching recipe!" }
        .ifSuccess { this.id = it.id }
        .ifError { this.id = null }
        .map(RecipeEntry<R>::value)

    fun getAllMatches(input: I, world: World): DataResult<List<R>> = world.recipeManager
        .getAllMatches(recipeType, input, world)
        .map(RecipeEntry<R>::value)
        .toDataResult { "Failed to find matching recipes!" }
        .validate(List<R>::isNotEmpty) { "Failed to find matching recipes!" }
}
