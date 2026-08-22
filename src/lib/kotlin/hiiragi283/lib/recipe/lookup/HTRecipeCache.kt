package hiiragi283.lib.recipe.lookup

import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.HTRecipePredicate
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.recipe.id
import hiiragi283.lib.recipe.recipe
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.crafting.RecipeInput

class HTRecipeCache<INPUT : RecipeInput, RECIPE : HTRecipePredicate<INPUT>>(private val lookup: HTRecipeLookup<RECIPE>) {
    private var lastRecipe: HTRecipeHolder<RECIPE>? = null

    fun findFirstRecipe(input: INPUT, level: ServerLevel): RECIPE? = findFirstRecipe(input, HTRecipeLookupContext.create(level))

    fun findFirstRecipe(input: INPUT, context: ContextMap): RECIPE? = findFirstHolder(input, context)?.recipe

    fun findFirstHolder(input: INPUT, context: ContextMap): HTRecipeHolder<RECIPE>? {
        if (input.isEmpty) return null
        if (lastRecipe != null && lastRecipe!!.recipe.matches(input)) {
            return lastRecipe
        }
        for ((id: RecipeKey, recipe: RECIPE) in lookup.getAllRecipes(context)) {
            if (recipe.matches(input)) {
                lastRecipe = id to recipe
                break
            }
        }
        return lastRecipe
    }

    override fun toString(): String = "HTRecipeCache(lookup=$lookup, lastRecipe=${lastRecipe?.id})"
}
