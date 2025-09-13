package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.HTRecipeCache
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class HTSimpleRecipeCache<I : RecipeInput, R : Recipe<I>>(val recipeType: RecipeType<R>) : HTRecipeCache.Serializable<I, R>() {
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
