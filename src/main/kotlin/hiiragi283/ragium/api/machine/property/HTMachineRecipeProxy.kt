package hiiragi283.ragium.api.machine.property

import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.common.init.RagiumRecipes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import java.util.function.Consumer

fun interface HTMachineRecipeProxy {
    fun getRecipes(level: Level, consumer: Consumer<RecipeHolder<HTMachineRecipe>>)

    companion object {
        @JvmField
        val DEFAULT = HTMachineRecipeProxy { level: Level, consumer: Consumer<RecipeHolder<HTMachineRecipe>> ->
            level.recipeManager
                .getAllRecipesFor(RagiumRecipes.MACHINE_TYPE.get())
                .stream()
                .forEach(consumer)
        }

        @JvmStatic
        fun <I : RecipeInput, T : Recipe<I>> convert(
            recipeType: RecipeType<T>,
            converter: (RecipeHolder<T>, HolderLookup.Provider) -> RecipeHolder<HTMachineRecipe>,
        ): HTMachineRecipeProxy = HTMachineRecipeProxy { level: Level, consumer: Consumer<RecipeHolder<HTMachineRecipe>> ->
            level.recipeManager
                .getAllRecipesFor(recipeType)
                .map { converter(it, level.registryAccess()) }
                .forEach(consumer)
        }
    }
}
