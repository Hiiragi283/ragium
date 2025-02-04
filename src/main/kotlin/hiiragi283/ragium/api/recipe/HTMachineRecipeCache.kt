package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.property.get
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.Level

class HTMachineRecipeCache<T : HTMachineRecipeBase>(val machine: HTMachineKey) {
    private var recipeCache: MutableList<RecipeHolder<T>> = mutableListOf()

    @Suppress("UNCHECKED_CAST")
    private fun reloadRecipes(level: Level) {
        recipeCache.clear()
        machine
            .getProperty()[HTMachinePropertyKeys.RECIPE_PROXY]
            ?.getRecipes(level) { holder: RecipeHolder<out HTMachineRecipeBase> ->
                val id: ResourceLocation = holder.id
                val recipe: HTMachineRecipeBase = holder.value
                (recipe as? T)?.let { RecipeHolder(id, it) }?.let(recipeCache::add)
            }
    }

    fun getFirstMatch(level: Level, input: HTMachineRecipeInput): Result<RecipeHolder<T>> {
        reloadRecipes(level)
        return recipeCache
            .firstOrNull { holder: RecipeHolder<T> -> holder.value.matches(input, level) }
            ?.let(Result.Companion::success)
            ?: Result.failure(HTMachineException.NoMatchingRecipe(false))
    }
}
