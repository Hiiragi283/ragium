package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.HTRecipeCache
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import java.util.Optional

class HTDynamicRecipeCache<I : RecipeInput, R : Recipe<I>>(
    val recipeType: RecipeType<R>,
    private val dynamicFinder: (I, Level) -> RecipeHolder<R>?,
) : HTRecipeCache<I, R> {
    constructor(recipeType: RecipeType<R>, dynamicRecipes: () -> Iterator<RecipeHolder<R>>) : this(
        recipeType,
        { input: I, level: Level ->
            var holder: RecipeHolder<R>? = null
            for (holderIn: RecipeHolder<R> in dynamicRecipes()) {
                if (holderIn.value.matches(input, level)) {
                    holder = holderIn
                    break
                }
            }
            holder
        },
    )

    private var lastRecipe: RecipeHolder<R>? = null

    override fun getFirstHolder(input: I, level: Level): RecipeHolder<R>? = level.recipeManager
        .getRecipeFor(recipeType, input, level, lastRecipe)
        .let { found: Optional<RecipeHolder<R>> ->
            when {
                found.isEmpty -> Optional.ofNullable(dynamicFinder(input, level))
                else -> found
            }
        }.map { holder: RecipeHolder<R> ->
            lastRecipe = holder
            holder
        }.orElseGet {
            lastRecipe = null
            null
        }
}
