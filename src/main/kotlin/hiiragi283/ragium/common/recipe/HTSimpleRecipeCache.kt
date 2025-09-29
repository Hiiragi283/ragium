package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.extension.recipeGetter
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeHolder
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class HTSimpleRecipeCache<I : RecipeInput, R : Recipe<I>>(val recipeType: RecipeType<R>) : HTRecipeCache.Serializable<I, R>() {
    override fun getFirstHolder(input: I, level: Level): HTRecipeHolder<R>? = level.recipeGetter
        .getRecipeFor(recipeType, input, level, lastRecipe)
        ?.let(::updateCache)
}
