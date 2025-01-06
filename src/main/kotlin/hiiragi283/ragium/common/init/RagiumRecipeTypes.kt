package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumRecipeTypes {
    @JvmField
    val MACHINE: RecipeType<HTMachineRecipe> = register("machine")

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
