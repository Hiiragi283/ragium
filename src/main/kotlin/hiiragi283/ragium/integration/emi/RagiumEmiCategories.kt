package hiiragi283.ragium.integration.emi

import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.setup.RagiumBlocks

object RagiumEmiCategories {
    // Machines
    @JvmField
    val CRUSHING = HTEmiRecipeCategory(RagiumAPI.id(RagiumConstantValues.CRUSHING), RagiumBlocks.CRUSHER)

    @JvmField
    val EXTRACTING = HTEmiRecipeCategory(RagiumAPI.id(RagiumConstantValues.EXTRACTING), RagiumBlocks.EXTRACTOR)

    @JvmField
    val INFUSING = HTEmiRecipeCategory(RagiumAPI.id(RagiumConstantValues.INFUSING), RagiumBlocks.INFUSER)

    @JvmField
    val REFINING = HTEmiRecipeCategory(RagiumAPI.id(RagiumConstantValues.REFINING), RagiumBlocks.REFINERY)

    @JvmField
    val SOLIDIFYING = HTEmiRecipeCategory(RagiumAPI.id(RagiumConstantValues.SOLIDIFYING), RagiumBlocks.REFINERY)

    @JvmField
    val MACHINES: List<HTEmiRecipeCategory> = listOf(
        CRUSHING,
        EXTRACTING,
        INFUSING,
        REFINING,
        SOLIDIFYING,
    )

    @JvmStatic
    fun register(registry: EmiRegistry) {
        // Category
        MACHINES.forEach(registry::addCategory)

        // Workstation
        fun addWorkstation(category: HTEmiRecipeCategory) {
            registry.addWorkstation(category, category.iconStack)
        }

        MACHINES.forEach(::addWorkstation)
        registry.addWorkstation(CRUSHING, EmiStack.of(RagiumBlocks.ADVANCED_CRUSHER))
        registry.addWorkstation(EXTRACTING, EmiStack.of(RagiumBlocks.ADVANCED_EXTRACTOR))
    }
}
