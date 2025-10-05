package hiiragi283.ragium.impl.recipe.manager

import hiiragi283.ragium.api.extension.recipeAccess
import hiiragi283.ragium.api.recipe.manager.HTRecipeCache
import hiiragi283.ragium.api.recipe.manager.HTRecipeHolder
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import kotlin.collections.iterator

class HTDynamicRecipeCache<I : RecipeInput, R : Recipe<I>>(
    val recipeType: RecipeType<R>,
    private val dynamicFinder: (I, Level) -> HTRecipeHolder<R>?,
) : HTRecipeCache<I, R> {
    constructor(recipeType: RecipeType<R>, dynamicRecipes: () -> Iterator<HTRecipeHolder<R>>) : this(
        recipeType,
        { input: I, level: Level ->
            var holder: HTRecipeHolder<R>? = null
            for (holderIn: HTRecipeHolder<R> in dynamicRecipes()) {
                if (holderIn.recipe.matches(input, level)) {
                    holder = holderIn
                    break
                }
            }
            holder
        },
    )

    private var lastRecipe: HTRecipeHolder<R>? = null

    override fun getFirstHolder(input: I, level: Level): HTRecipeHolder<R>? {
        val holder: HTRecipeHolder<R>? =
            level.recipeAccess.getRecipeFor(recipeType, input, level, lastRecipe) ?: dynamicFinder(input, level)
        lastRecipe = holder
        return holder
    }
}
