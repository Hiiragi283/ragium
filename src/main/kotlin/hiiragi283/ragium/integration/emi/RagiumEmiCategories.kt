package hiiragi283.ragium.integration.emi

import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.util.variant.HTGeneratorVariant

object RagiumEmiCategories {
    //    Generators    //

    @JvmField
    val GENERATORS: Map<HTGeneratorVariant, HTEmiRecipeCategory> =
        HTGeneratorVariant.entries.associateWith { variant: HTGeneratorVariant ->
            HTEmiRecipeCategory(variant.serializedName, RagiumBlocks.getGenerator(variant))
        }

    @JvmStatic
    fun getGenerator(variant: HTGeneratorVariant): HTEmiRecipeCategory = GENERATORS[variant]!!

    //    Machines    //

    @JvmField
    val ALLOYING = HTEmiRecipeCategory(RagiumConst.ALLOYING, RagiumBlocks.Machines.ALLOY_SMELTER)

    @JvmField
    val COMPRESSING = HTEmiRecipeCategory(RagiumConst.COMPRESSING, RagiumBlocks.Machines.COMPRESSOR)

    @JvmField
    val CRUSHING = HTEmiRecipeCategory(RagiumConst.CRUSHING, RagiumBlocks.Machines.PULVERIZER)

    @JvmField
    val DISTILLATION = HTEmiRecipeCategory("distillation", RagiumBlocks.Machines.REFINERY)

    @JvmField
    val EXTRACTING = HTEmiRecipeCategory(RagiumConst.EXTRACTING, RagiumBlocks.Machines.EXTRACTOR)

    @JvmField
    val INFUSING = HTEmiRecipeCategory(RagiumConst.INFUSING, RagiumBlocks.Machines.INFUSER)

    @JvmField
    val MELTING = HTEmiRecipeCategory(RagiumConst.MELTING, RagiumBlocks.Machines.MELTER)

    @JvmField
    val MIXING = HTEmiRecipeCategory(RagiumConst.MIXING, RagiumBlocks.Machines.MIXER)

    @JvmField
    val REFINING = HTEmiRecipeCategory(RagiumConst.REFINING, RagiumBlocks.Machines.REFINERY)

    @JvmField
    val SOLIDIFYING = HTEmiRecipeCategory(RagiumConst.SOLIDIFYING, RagiumBlocks.Machines.SOLIDIFIER)

    @JvmField
    val CATEGORIES: List<HTEmiRecipeCategory> = listOf(
        // Machines
        ALLOYING,
        COMPRESSING,
        CRUSHING,
        DISTILLATION,
        EXTRACTING,
        INFUSING,
        MELTING,
        MIXING,
        REFINING,
        SOLIDIFYING,
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
        registry.addWorkstation(CRUSHING, EmiStack.of(RagiumBlocks.Machines.CRUSHER))
    }
}
