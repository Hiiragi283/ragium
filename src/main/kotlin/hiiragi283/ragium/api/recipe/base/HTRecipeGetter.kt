package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.extension.getAllRecipes
import hiiragi283.ragium.api.machine.HTMachineException
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import java.util.function.Consumer

fun interface HTRecipeGetter<I : RecipeInput, R : Recipe<I>> {
    fun getFirstRecipe(input: I, level: Level): Result<R>

    //    Listed    //

    class Listed<I : RecipeInput, R : Recipe<I>>(val recipeType: RecipeType<R>, val runtimeRecipes: Consumer<(R) -> Unit>) :
        HTRecipeGetter<I, R> {
        override fun getFirstRecipe(input: I, level: Level): Result<R> {
            // from RecipeManager
            val recipes: MutableList<R> = level.recipeManager
                .getAllRecipes(recipeType)
                .toMutableList()
            // from Runtime Recipes
            runtimeRecipes.accept(recipes::add)
            return recipes
                .firstOrNull { recipe: R -> recipe.matches(input, level) }
                ?.let(Result.Companion::success)
                ?: Result.failure(HTMachineException.NoMatchingRecipe(false))
        }
    }

    //    Cached    //

    class Cached<I : RecipeInput, R : Recipe<I>>(private val recipeType: RecipeType<R>) : HTRecipeGetter<I, R> {
        private var lastRecipe: ResourceLocation? = null

        override fun getFirstRecipe(input: I, level: Level): Result<R> = level.recipeManager
            .getRecipeFor(recipeType, input, level, lastRecipe)
            .map { holder: RecipeHolder<R> ->
                lastRecipe = holder.id
                Result.success(holder.value)
            }.orElseGet {
                lastRecipe = null
                Result.failure(HTMachineException.NoMatchingRecipe(false))
            }
    }
}
