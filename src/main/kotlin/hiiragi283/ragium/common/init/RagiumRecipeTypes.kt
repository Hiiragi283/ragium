package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTMachineRecipeType
import hiiragi283.ragium.common.recipe.*
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumRecipeTypes {
    @JvmField
    val DISTILLATION: HTMachineRecipeType<HTDistillationRecipe> =
        register("distillation", ::HTDistillationRecipe)

    @JvmField
    val GRINDER: HTMachineRecipeType<HTGrinderRecipe> =
        register("grinder", ::HTGrinderRecipe)

    @JvmField
    val GROWTH_CHAMBER: HTMachineRecipeType<HTGrowthChamberRecipe> =
        register("growth_chamber", ::HTGrowthChamberRecipe)

    @JvmField
    val MACHINE: HTMachineRecipeType<HTDefaultMachineRecipe> =
        register("machine", ::HTDefaultMachineRecipe)

    @JvmField
    val ROCK_GENERATOR: HTMachineRecipeType<HTRockGeneratorRecipe> =
        register("rock_generator", ::HTRockGeneratorRecipe)

    @JvmStatic
    private fun <T : HTMachineRecipe> register(path: String, factory: HTMachineRecipe.Factory<T>): HTMachineRecipeType<T> =
        Registry.register(
            Registries.RECIPE_TYPE,
            RagiumAPI.id(path),
            HTMachineRecipeType(factory),
        )
}
