package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipeNew
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumRecipeTypes {
    @JvmField
    val MACHINE: RecipeType<HTMachineRecipe> = register("machine")

    @JvmField
    val SIMPLE_MACHINE: RecipeType<HTMachineRecipeNew.Simple> = register("simple_machine")

    @JvmField
    val LARGE_MACHINE: RecipeType<HTMachineRecipeNew.Simple> = register("large_machine")

    @JvmStatic
    private fun <T : Recipe<*>> register(name: String): RecipeType<T> = RagiumAPI.id(name).let {
        Registry.register(
            Registries.RECIPE_TYPE,
            it,
            object : RecipeType<T> {
                override fun toString(): String = it.toString()
            },
        )
    }
}
