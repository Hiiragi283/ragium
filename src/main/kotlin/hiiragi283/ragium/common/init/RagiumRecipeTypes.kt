package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.alchemy.HTAlchemyRecipe
import hiiragi283.ragium.api.recipe.machine.HTFluidDrillRecipe
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumRecipeTypes {
    @JvmField
    val ALCHEMY: RecipeType<HTAlchemyRecipe> = register("alchemy")

    @JvmField
    val FLUID_DRILL: RecipeType<HTFluidDrillRecipe> = register("fluid_drill")

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
