package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeFinder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level

class HTFinderRecipeCache<INPUT : RecipeInput, RECIPE : Any>(private val finder: HTRecipeFinder<INPUT, RECIPE>) :
    HTRecipeCache<INPUT, RECIPE> {
    private var lastRecipe: Pair<ResourceLocation, RECIPE>? = null

    override fun getFirstRecipe(input: INPUT, level: Level): RECIPE? = finder
        .getRecipeFor(level.recipeManager, input, level, lastRecipe)
        .also(::lastRecipe::set)
        ?.second
}
