package hiiragi283.ragium.integration.emi

import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.EmiRecipeSorting
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import hiiragi283.ragium.common.variant.HTMachineVariant

object RagiumEmiCategories {
    //    Generators    //

    @JvmField
    val GENERATORS: Map<HTGeneratorVariant, HTEmiRecipeCategory> =
        HTGeneratorVariant.entries.associateWith { variant: HTGeneratorVariant ->
            HTEmiRecipeCategory(variant.serializedName, EmiStack.of(variant), EmiRecipeSorting.identifier())
        }

    @JvmStatic
    fun getGenerator(variant: HTGeneratorVariant): HTEmiRecipeCategory = GENERATORS[variant]!!

    //    Machines    //

    @JvmStatic
    private fun machine(path: String, variant: HTMachineVariant): HTEmiRecipeCategory =
        HTEmiRecipeCategory(path, EmiStack.of(variant), EmiRecipeSorting.compareOutputThenInput())

    // Basic
    @JvmField
    val ALLOYING: HTEmiRecipeCategory = machine(RagiumConst.ALLOYING, HTMachineVariant.ALLOY_SMELTER)

    @JvmField
    val COMPRESSING: HTEmiRecipeCategory = machine(RagiumConst.COMPRESSING, HTMachineVariant.COMPRESSOR)

    @JvmField
    val CUTTING: HTEmiRecipeCategory = machine("cutting", HTMachineVariant.CUTTING_MACHINE)

    @JvmField
    val CRUSHING: HTEmiRecipeCategory = machine(RagiumConst.CRUSHING, HTMachineVariant.PULVERIZER)

    @JvmField
    val EXTRACTING: HTEmiRecipeCategory = machine(RagiumConst.EXTRACTING, HTMachineVariant.EXTRACTOR)

    // Advanced
    @JvmField
    val FLUID_TRANSFORM: HTEmiRecipeCategory = machine(RagiumConst.FLUID_TRANSFORM, HTMachineVariant.REFINERY)

    @JvmField
    val MELTING: HTEmiRecipeCategory = machine(RagiumConst.MELTING, HTMachineVariant.MELTER)

    // Elite
    @JvmField
    val SIMULATING: HTEmiRecipeCategory = machine(RagiumConst.SIMULATING, HTMachineVariant.SIMULATOR)

    @JvmField
    val CATEGORIES: List<HTEmiRecipeCategory> = listOf(
        // Basic
        ALLOYING,
        COMPRESSING,
        CUTTING,
        CRUSHING,
        EXTRACTING,
        // Advanced
        FLUID_TRANSFORM,
        MELTING,
        // Elite
        SIMULATING,
    )

    @JvmField
    val SMELTING: List<EmiRecipeCategory> = listOf(
        VanillaEmiRecipeCategories.BLASTING,
        VanillaEmiRecipeCategories.SMELTING,
        VanillaEmiRecipeCategories.SMOKING,
    )

    @JvmStatic
    fun register(registry: EmiRegistry) {
        // Category
        GENERATORS.values.forEach(registry::addCategory)
        CATEGORIES.forEach(registry::addCategory)

        // Workstation
        fun addWorkstation(category: HTEmiRecipeCategory) {
            registry.addWorkstation(category, category.iconStack)
        }

        GENERATORS.values.forEach(::addWorkstation)
        CATEGORIES.forEach(::addWorkstation)

        for (category: EmiRecipeCategory in SMELTING) {
            registry.addWorkstation(category, EmiStack.of(HTMachineVariant.MULTI_SMELTER))
        }

        registry.addWorkstation(CRUSHING, EmiStack.of(HTMachineVariant.CRUSHER))
    }
}
