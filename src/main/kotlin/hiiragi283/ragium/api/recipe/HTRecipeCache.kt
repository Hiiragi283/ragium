package hiiragi283.ragium.api.recipe

import com.mojang.serialization.DataResult
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
        .map {
            this.id = it.id
            DataResult.success(it.value)
        }.orElseGet {
            this.id = null
            DataResult.error { "Failed to find matching recipe!" }
        }

    fun getAllMatches(input: I, world: World): DataResult<List<R>> = world.recipeManager
        .getAllMatches(recipeType, input, world)
        .let { entries: List<RecipeEntry<R>> ->
            when (entries.isEmpty()) {
                true -> DataResult.error { "Failed to find matching recipes!" }
                false -> DataResult.success(entries.map(RecipeEntry<R>::value))
            }
        }
}
