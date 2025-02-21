package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.machine.HTMachineException
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level
import java.util.function.BiFunction

fun interface HTRecipeGetter<I : RecipeInput, R : Recipe<I>> {
    companion object {
        @JvmStatic
        fun <I : RecipeInput, R : Recipe<I>> listed(
            runtimeRecipes: BiFunction<HolderGetter<Item>, RecipeManager, List<RecipeHolder<R>>>,
        ): HTRecipeGetter<I, R> = Listed(runtimeRecipes)

        @JvmStatic
        fun <I : RecipeInput, R : Recipe<I>> cached(recipeType: RecipeType<R>): HTRecipeGetter<I, R> = Cached(recipeType)
    }

    fun getFirstRecipe(input: I, level: Level): Result<R>

    //    Listed    //

    private class Listed<I : RecipeInput, R : Recipe<I>>(
        val runtimeRecipes: BiFunction<HolderGetter<Item>, RecipeManager, List<RecipeHolder<R>>>,
    ) : HTRecipeGetter<I, R> {
        private var lastRecipe: ResourceLocation? = null

        override fun getFirstRecipe(input: I, level: Level): Result<R> {
            val lookup: HolderLookup.RegistryLookup<Item> = level.registryAccess().lookupOrThrow(Registries.ITEM)
            return runtimeRecipes
                .apply(lookup, level.recipeManager)
                .stream()
                .filter { recipe: RecipeHolder<R> -> recipe.value.matches(input, level) }
                .findFirst()
                .map { holder: RecipeHolder<R> ->
                    lastRecipe = holder.id
                    Result.success(holder.value)
                }.orElseGet {
                    lastRecipe = null
                    Result.failure(HTMachineException.NoMatchingRecipe(false))
                }
        }
    }

    //    Cached    //

    private class Cached<I : RecipeInput, R : Recipe<I>>(private val recipeType: RecipeType<R>) : HTRecipeGetter<I, R> {
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
