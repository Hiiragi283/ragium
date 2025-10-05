package hiiragi283.ragium.impl.recipe.manager

import hiiragi283.ragium.api.extension.recipeAccess
import hiiragi283.ragium.api.recipe.manager.HTRecipeCache
import hiiragi283.ragium.api.recipe.manager.HTRecipeHolder
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class HTSimpleRecipeCache<I : RecipeInput, R : Recipe<I>>(val recipeType: RecipeType<R>) : HTRecipeCache.Serializable<I, R>() {
    override fun getFirstHolder(input: I, level: Level): HTRecipeHolder<R>? = level.recipeAccess
        .getRecipeFor(recipeType, input, level, lastRecipe)
        ?.let(::updateCache)
}
