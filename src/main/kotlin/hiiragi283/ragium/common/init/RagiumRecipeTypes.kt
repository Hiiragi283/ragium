package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTMachineRecipeType
import hiiragi283.ragium.common.recipe.HTDefaultMachineRecipe
import hiiragi283.ragium.common.recipe.HTDistillationRecipe
import hiiragi283.ragium.common.recipe.HTGrinderRecipe
import hiiragi283.ragium.common.recipe.HTRockGeneratorRecipe
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumRecipeTypes {
    @JvmField
    val DISTILLATION: HTMachineRecipeType<HTDistillationRecipe> =
        register(RagiumMachineKeys.DISTILLATION_TOWER, ::HTDistillationRecipe)

    @JvmField
    val GRINDER: HTMachineRecipeType<HTGrinderRecipe> =
        register(RagiumMachineKeys.GRINDER, ::HTGrinderRecipe)

    @JvmField
    val MACHINE: HTMachineRecipeType<HTDefaultMachineRecipe> =
        register(HTMachineKey.of(RagiumAPI.id("machine")), ::HTDefaultMachineRecipe)

    @JvmField
    val ROCK_GENERATOR: HTMachineRecipeType<HTRockGeneratorRecipe> =
        register(RagiumMachineKeys.ROCK_GENERATOR, ::HTRockGeneratorRecipe)

    @JvmStatic
    private fun <T : HTMachineRecipe> register(machineKey: HTMachineKey, factory: HTMachineRecipe.Factory<T>): HTMachineRecipeType<T> =
        Registry.register(
            Registries.RECIPE_TYPE,
            machineKey.id,
            HTMachineRecipeType(machineKey, factory),
        )
}
