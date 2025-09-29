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

    @JvmStatic
    private fun generator(variant: HTGeneratorVariant): HTEmiRecipeCategory =
        HTEmiRecipeCategory(variant.serializedName, EmiStack.of(variant), EmiRecipeSorting.compareInputThenOutput())

    @JvmField
    val THERMAL: HTEmiRecipeCategory = generator(HTGeneratorVariant.THERMAL)

    @JvmField
    val COMBUSTION: HTEmiRecipeCategory = generator(HTGeneratorVariant.COMBUSTION)

    @JvmStatic
    fun getGenerator(variant: HTGeneratorVariant): HTEmiRecipeCategory = when (variant) {
        HTGeneratorVariant.THERMAL -> THERMAL
        HTGeneratorVariant.COMBUSTION -> COMBUSTION
        else -> error("Unsupported variant: ${variant.serializedName}")
    }

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

    @JvmField
    val WASHING: HTEmiRecipeCategory = machine(RagiumConst.WASHING, HTMachineVariant.WASHER)

    // Elite
    @JvmField
    val BREWING_EFFECT: HTEmiRecipeCategory = machine("brewing/effect", HTMachineVariant.BREWERY)

    @JvmField
    val BREWING_MODIFIER: HTEmiRecipeCategory = machine("brewing/modifier", HTMachineVariant.BREWERY)

    @JvmField
    val SIMULATING: HTEmiRecipeCategory = machine(RagiumConst.SIMULATING, HTMachineVariant.SIMULATOR)

    @JvmField
    val CATEGORIES: List<HTEmiRecipeCategory> = listOf(
        // Generator
        THERMAL,
        COMBUSTION,
        // Basic
        ALLOYING,
        COMPRESSING,
        CUTTING,
        CRUSHING,
        EXTRACTING,
        // Advanced
        FLUID_TRANSFORM,
        MELTING,
        WASHING,
        // Elite
        BREWING_EFFECT,
        BREWING_MODIFIER,
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
        CATEGORIES.forEach(registry::addCategory)

        // Workstation
        fun addWorkstation(category: HTEmiRecipeCategory) {
            registry.addWorkstation(category, category.iconStack)
        }

        CATEGORIES.forEach(::addWorkstation)

        for (category: EmiRecipeCategory in SMELTING) {
            registry.addWorkstation(category, EmiStack.of(HTMachineVariant.MULTI_SMELTER))
        }

        registry.addWorkstation(CRUSHING, EmiStack.of(HTMachineVariant.CRUSHER))
    }
}
