package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.registry.HTDeferredRecipeType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import java.util.*

sealed class HTRecipeCache<I : RecipeInput, R : Recipe<I>> {
    companion object {
        @JvmStatic
        fun <I : RecipeInput, R : Recipe<I>> simple(recipeType: RecipeType<R>): HTRecipeCache<I, R> = Simple<I, R>(recipeType)

        @JvmStatic
        fun <I : RecipeInput, R : Recipe<I>> reloadable(recipeType: HTDeferredRecipeType<I, R>): HTRecipeCache<I, R> =
            Reloadable<I, R>(recipeType)
    }

    private var lastRecipe: ResourceLocation? = null

    /**
     * 指定した[input]と[level]から最初に一致するレシピを返します。
     * @return 見つからなかった場合は`null`
     */
    fun getFirstRecipe(input: I, level: Level): R? = getFirstHolder(input, level, lastRecipe)
        .map { holder: RecipeHolder<R> ->
            lastRecipe = holder.id
            holder.value
        }.orElseGet {
            lastRecipe = null
            null
        }

    protected abstract fun getFirstHolder(input: I, level: Level, lastRecipe: ResourceLocation?): Optional<RecipeHolder<R>>

    private class Simple<I : RecipeInput, R : Recipe<I>>(val recipeType: RecipeType<R>) : HTRecipeCache<I, R>() {
        override fun getFirstHolder(input: I, level: Level, lastRecipe: ResourceLocation?): Optional<RecipeHolder<R>> =
            level.recipeManager.getRecipeFor(recipeType, input, level, lastRecipe)
    }

    private class Reloadable<I : RecipeInput, R : Recipe<I>>(val recipeType: HTDeferredRecipeType<I, R>) : HTRecipeCache<I, R>() {
        override fun getFirstHolder(input: I, level: Level, lastRecipe: ResourceLocation?): Optional<RecipeHolder<R>> =
            Optional.ofNullable(recipeType.getFirstRecipe(input, level, lastRecipe))
    }
}
