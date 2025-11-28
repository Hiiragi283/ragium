package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeFinder
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level

class HTFinderRecipeCache<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(private val finder: HTRecipeFinder<INPUT, RECIPE>) :
    HTRecipeCache<INPUT, RECIPE> {
    private var lastRecipe: RecipeHolder<RECIPE>? = null

    override fun getFirstRecipe(input: INPUT, level: Level): RECIPE? = finder
        .getRecipeFor(level.recipeManager, input, level, lastRecipe)
        .also { holder: RecipeHolder<RECIPE>? -> lastRecipe = holder }
        ?.value
}
