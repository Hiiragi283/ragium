package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.extension.recipeGetter
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeHolder
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class HTMultiRecipeCache<I : RecipeInput, R : Recipe<I>>(private val recipeTypes: List<RecipeType<out R>>) :
    HTRecipeCache.Serializable<I, R>() {
    constructor(vararg recipeTypes: RecipeType<out R>) : this(recipeTypes.toList())

    override fun getFirstHolder(input: I, level: Level): HTRecipeHolder<R>? {
        var foundHolder: HTRecipeHolder<out R>? = null
        for (type: RecipeType<out R> in recipeTypes) {
            foundHolder = level.recipeGetter.getRecipeFor(type, input, level, lastRecipe)
            if (foundHolder != null) break
        }
        return updateCache(foundHolder)?.let { (id, recipe) -> HTRecipeHolder(id, recipe) }
    }
}
