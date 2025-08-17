package hiiragi283.ragium.api.recipe.cache

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class HTSimpleRecipeCache<I : RecipeInput, R : Recipe<I>>(val recipeType: RecipeType<R>) : HTRecipeCache<I, R> {
    override var lastRecipe: ResourceLocation? = null
        private set

    override fun getFirstHolder(input: I, level: Level): RecipeHolder<R>? = level.recipeManager
        .getRecipeFor(recipeType, input, level, lastRecipe)
        .map { holder: RecipeHolder<R> ->
            lastRecipe = holder.id
            holder
        }.orElseGet {
            lastRecipe = null
            null
        }
}
