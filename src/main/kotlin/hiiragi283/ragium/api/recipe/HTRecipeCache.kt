package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.machine.HTMachineException
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import java.util.function.Supplier

class HTRecipeCache<I : RecipeInput, R : Recipe<I>>(private val recipeType: RecipeType<R>) {
    constructor(supplier: Supplier<RecipeType<R>>) : this(supplier.get())

    private var lastRecipe: ResourceLocation? = null

    fun getFirstRecipe(input: I, level: Level): Result<RecipeHolder<R>> = level.recipeManager
        .getRecipeFor(recipeType, input, level, lastRecipe)
        .map { holder: RecipeHolder<R> ->
            lastRecipe = holder.id
            Result.success(holder)
        }.orElseGet {
            lastRecipe = null
            Result.failure(HTMachineException.NoMatchingRecipe(false))
        }
}
