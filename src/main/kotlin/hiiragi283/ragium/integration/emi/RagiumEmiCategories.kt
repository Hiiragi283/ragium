package hiiragi283.ragium.integration.emi

import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.network.chat.Component

object RagiumEmiCategories {
    @JvmField
    val BLOCK_INFO = HTEmiRecipeCategory(
        RagiumAPI.id("block_info"),
        EmiStack.of(RagiumItems.RAGI_ALLOY_TOOLS.hammerItem),
        Component.literal("Block Information"),
    )

    // Machines
    @JvmField
    val CRUSHING = HTEmiRecipeCategory(RagiumAPI.id("crushing"), RagiumBlocks.CRUSHER)

    @JvmField
    val EXTRACTING = HTEmiRecipeCategory(RagiumAPI.id("extracting"), RagiumBlocks.EXTRACTOR)

    @JvmField
    val INFUSING = HTEmiRecipeCategory(RagiumAPI.id("infusing"), RagiumBlocks.INFUSER)

    @JvmField
    val REFINING = HTEmiRecipeCategory(RagiumAPI.id("refining"), RagiumBlocks.REFINERY)

    @JvmField
    val MACHINES: List<HTEmiRecipeCategory> = listOf(
        CRUSHING,
        EXTRACTING,
        INFUSING,
        REFINING,
    )

    @JvmStatic
    fun register(registry: EmiRegistry) {
        // Category
        registry.addCategory(BLOCK_INFO)

        MACHINES.forEach(registry::addCategory)

        // Workstation
        fun addWorkstation(category: HTEmiRecipeCategory) {
            registry.addWorkstation(category, category.iconStack)
        }

        MACHINES.forEach(::addWorkstation)
        registry.addWorkstation(EXTRACTING, EmiStack.of(RagiumBlocks.ADVANCED_EXTRACTOR))
    }
}
