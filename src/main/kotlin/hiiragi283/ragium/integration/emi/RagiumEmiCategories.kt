package hiiragi283.ragium.integration.emi

import dev.emi.emi.api.EmiRegistry
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.setup.RagiumBlocks

object RagiumEmiCategories {
    // Machines
    @JvmField
    val ALLOYING = HTEmiRecipeCategory(RagiumConst.ALLOYING, RagiumBlocks.Machines.ALLOY_SMELTER)

    @JvmField
    val COMPRESSING = HTEmiRecipeCategory(RagiumConst.COMPRESSING, RagiumBlocks.Machines.COMPRESSOR)

    @JvmField
    val CRUSHING = HTEmiRecipeCategory(RagiumConst.CRUSHING, RagiumBlocks.Machines.CRUSHER)

    @JvmField
    val DISTILLATION = HTEmiRecipeCategory("distillation", RagiumBlocks.Machines.REFINERY)

    @JvmField
    val EXTRACTING = HTEmiRecipeCategory(RagiumConst.EXTRACTING, RagiumBlocks.Machines.EXTRACTOR)

    @JvmField
    val INFUSING = HTEmiRecipeCategory(RagiumConst.INFUSING, RagiumBlocks.Machines.INFUSER)

    @JvmField
    val MELTING = HTEmiRecipeCategory(RagiumConst.MELTING, RagiumBlocks.Machines.MELTER)

    @JvmField
    val PRESSING = HTEmiRecipeCategory(RagiumConst.PRESSING, RagiumBlocks.Machines.FORMING_PRESS)

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
        PRESSING,
        REFINING,
        SOLIDIFYING,
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
    }
}
