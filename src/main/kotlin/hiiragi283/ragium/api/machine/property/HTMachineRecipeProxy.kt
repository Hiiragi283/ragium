package hiiragi283.ragium.api.machine.property

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.common.init.RagiumRecipes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import java.util.function.BiFunction
import java.util.function.Consumer

private typealias MaterialFactory = BiFunction<HTMaterialKey, HTMaterialRegistry, RecipeHolder<HTMachineRecipe>?>

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
            allowFromManager: Boolean,
            recipeType: RecipeType<T>,
            converter: (RecipeHolder<T>, HolderLookup.Provider) -> RecipeHolder<HTMachineRecipe>,
        ): HTMachineRecipeProxy = HTMachineRecipeProxy { level: Level, consumer: Consumer<RecipeHolder<HTMachineRecipe>> ->
            if (allowFromManager) {
                DEFAULT.getRecipes(level, consumer)
            }
            level.recipeManager
                .getAllRecipesFor(recipeType)
                .map { converter(it, level.registryAccess()) }
                .forEach(consumer)
        }

        @JvmStatic
        fun material(allowFromManager: Boolean, vararg builders: MaterialFactory): HTMachineRecipeProxy =
            HTMachineRecipeProxy { level: Level, consumer: Consumer<RecipeHolder<HTMachineRecipe>> ->
                if (allowFromManager) {
                    DEFAULT.getRecipes(level, consumer)
                }
                val registry: HTMaterialRegistry = RagiumAPI.materialRegistry
                registry.keys
                    .flatMap { key: HTMaterialKey ->
                        builders.mapNotNull { builder: MaterialFactory -> builder.apply(key, registry) }
                    }.forEach(consumer)
            }
    }
}
