package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTMachineRecipeType
import hiiragi283.ragium.common.recipe.*
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumRecipeTypes {
    @JvmField
    val ASSEMBLER: HTMachineRecipeType<HTAssemblerRecipe> =
        register(RagiumMachineKeys.ASSEMBLER, ::HTAssemblerRecipe)

    @JvmField
    val DISTILLATION: HTMachineRecipeType<HTDistillationRecipe> =
        register(RagiumMachineKeys.DISTILLATION_TOWER, ::HTDistillationRecipe)

    @JvmField
    val GRINDER: HTMachineRecipeType<HTGrinderRecipe> =
        register(RagiumMachineKeys.GRINDER, ::HTGrinderRecipe)

    @JvmField
    val GROWTH_CHAMBER: HTMachineRecipeType<HTGrowthChamberRecipe> =
        register(RagiumMachineKeys.GROWTH_CHAMBER, ::HTGrowthChamberRecipe)

    @JvmField
    val MACHINE: HTMachineRecipeType<HTDefaultMachineRecipe> =
        register(RagiumMachineKeys.BLAST_FURNACE, ::HTDefaultMachineRecipe)

    @JvmField
    val ROCK_GENERATOR: HTMachineRecipeType<HTRockGeneratorRecipe> =
        register(RagiumMachineKeys.ROCK_GENERATOR, ::HTRockGeneratorRecipe)

    @JvmStatic
    private fun <T : HTMachineRecipe> register(key: HTMachineKey, factory: HTMachineRecipe.Factory<T>): HTMachineRecipeType<T> =
        Registry.register(
            Registries.RECIPE_TYPE,
            key.id,
            HTMachineRecipeType(key, factory),
        )
}
