package hiiragi283.ragium.impl.recipe.manager

import hiiragi283.ragium.api.extension.recipeAccess
import hiiragi283.ragium.api.recipe.manager.HTRecipeCache
import hiiragi283.ragium.api.recipe.manager.HTRecipeHolder
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
            foundHolder = level.recipeAccess.getRecipeFor(type, input, level, lastRecipe)
            if (foundHolder != null) break
        }
        return updateCache(foundHolder)?.let { (id, recipe) -> HTRecipeHolder(id, recipe) }
    }
}
