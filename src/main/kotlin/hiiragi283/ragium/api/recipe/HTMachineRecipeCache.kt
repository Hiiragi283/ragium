package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.property.getOrDefault
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.Level

class HTMachineRecipeCache private constructor(val machine: HTMachineKey) {
    companion object {
        @JvmStatic
        private val instances: MutableMap<HTMachineKey, HTMachineRecipeCache> = mutableMapOf()

        @JvmStatic
        fun of(machine: HTMachineKey): HTMachineRecipeCache = instances.computeIfAbsent(machine, ::HTMachineRecipeCache)
    }

    private var recipeCache: MutableList<RecipeHolder<HTMachineRecipe>> = mutableListOf()

    private fun reloadRecipes(level: Level) {
        recipeCache.clear()
        machine
            .getProperty()
            .getOrDefault(HTMachinePropertyKeys.RECIPE_PROXY)
            .getRecipes(level, recipeCache::add)
    }

    fun getFirstMatch(level: Level, pos: BlockPos, stack: ItemStack): Result<RecipeHolder<HTMachineRecipe>> {
        reloadRecipes(level)
        val input: HTMachineInput = HTMachineInput.createSimple(pos, machine, stack)
        return recipeCache
            .firstOrNull { holder: RecipeHolder<HTMachineRecipe> -> holder.value.matches(input, level) }
            ?.let(Result.Companion::success)
            ?: Result.failure(HTMachineException.NoMatchingRecipe(false))
    }

    fun getFirstMatch(
        level: Level,
        pos: BlockPos,
        builderAction: HTMachineInput.Builder.() -> Unit,
    ): Result<RecipeHolder<HTMachineRecipe>> {
        reloadRecipes(level)
        val input: HTMachineInput = HTMachineInput.create(pos, machine, builderAction)
        return recipeCache
            .firstOrNull { holder: RecipeHolder<HTMachineRecipe> -> holder.value.matches(input, level) }
            ?.let(Result.Companion::success)
            ?: Result.failure(HTMachineException.NoMatchingRecipe(false))
    }
}
