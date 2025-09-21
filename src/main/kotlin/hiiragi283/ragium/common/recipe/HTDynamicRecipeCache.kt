package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.extension.recipeGetter
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeHolder
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

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
            level.recipeGetter.getRecipeFor(recipeType, input, level, lastRecipe) ?: dynamicFinder(input, level)
        lastRecipe = holder
        return holder
    }
}
