package hiiragi283.ragium.integration.emi

import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.util.variant.HTGeneratorVariant
import hiiragi283.ragium.util.variant.HTMachineVariant

object RagiumEmiCategories {
    //    Generators    //

    @JvmField
    val GENERATORS: Map<HTGeneratorVariant, HTEmiRecipeCategory> =
        HTGeneratorVariant.entries.associateWith { variant: HTGeneratorVariant ->
            HTEmiRecipeCategory(variant.serializedName, EmiStack.of(variant))
        }

    @JvmStatic
    fun getGenerator(variant: HTGeneratorVariant): HTEmiRecipeCategory = GENERATORS[variant]!!

    //    Machines    //

    @JvmField
    val ALLOYING = HTEmiRecipeCategory(RagiumConst.ALLOYING, EmiStack.of(HTMachineVariant.ALLOY_SMELTER))

    @JvmField
    val COMPRESSING = HTEmiRecipeCategory(RagiumConst.COMPRESSING, EmiStack.of(HTMachineVariant.COMPRESSOR))

    @JvmField
    val CRUSHING = HTEmiRecipeCategory(RagiumConst.CRUSHING, EmiStack.of(HTMachineVariant.PULVERIZER))

    @JvmField
    val DISTILLATION = HTEmiRecipeCategory("distillation", EmiStack.of(HTMachineVariant.REFINERY))

    @JvmField
    val EXTRACTING = HTEmiRecipeCategory(RagiumConst.EXTRACTING, EmiStack.of(HTMachineVariant.EXTRACTOR))

    @JvmField
    val FLUID_TRANSFORM = HTEmiRecipeCategory(RagiumConst.FLUID_TRANSFORM, EmiStack.of(HTMachineVariant.REFINERY))

    @JvmField
    val MELTING = HTEmiRecipeCategory(RagiumConst.MELTING, EmiStack.of(HTMachineVariant.MELTER))

    @JvmField
    val SOLIDIFYING = HTEmiRecipeCategory(RagiumConst.SOLIDIFYING, EmiStack.of(HTMachineVariant.SOLIDIFIER))

    @JvmField
    val CATEGORIES: List<HTEmiRecipeCategory> = listOf(
        // Machines
        ALLOYING,
        COMPRESSING,
        CRUSHING,
        DISTILLATION,
        EXTRACTING,
        FLUID_TRANSFORM,
        MELTING,
        SOLIDIFYING,
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
            registry.addWorkstation(category, EmiStack.of(HTMachineVariant.SMELTER))
        }
        registry.addWorkstation(VanillaEmiRecipeCategories.STONECUTTING, EmiStack.of(HTMachineVariant.ENGRAVER))

        registry.addWorkstation(CRUSHING, EmiStack.of(HTMachineVariant.CRUSHER))
    }
}
