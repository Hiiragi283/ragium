package hiiragi283.ragium.integration.emi

import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumRecipes
import net.minecraft.network.chat.Component

object RagiumEmiCategories {
    @JvmField
    val BLOCK_INFO = HTEmiRecipeCategory(
        RagiumAPI.id("block_info"),
        EmiStack.of(RagiumItems.RAGI_ALLOY_TOOLS.hammerItem),
        Component.literal("Block Information"),
    )

    // Catalysts
    @JvmField
    val CATALYST_AZURE = HTEmiRecipeCategory(RagiumAPI.id("catalyst/azure"), RagiumBlocks.AZURE_CATALYST)

    @JvmField
    val CATALYST_DEEP = HTEmiRecipeCategory(RagiumAPI.id("catalyst/deep"), RagiumBlocks.DEEP_CATALYST)

    @JvmField
    val CATALYST_RAGIUM = HTEmiRecipeCategory(RagiumAPI.id("catalyst/ragium"), RagiumBlocks.RAGIUM_CATALYST)

    @JvmField
    val CATALYSTS: List<HTEmiRecipeCategory> = listOf(
        CATALYST_AZURE,
        CATALYST_DEEP,
        CATALYST_RAGIUM,
    )

    // Machines
    @JvmField
    val CRUSHING = HTEmiRecipeCategory(RagiumRecipes.CRUSHING, RagiumBlocks.CRUSHER)

    @JvmField
    val EXTRACTING = HTEmiRecipeCategory(RagiumRecipes.EXTRACTING, RagiumBlocks.EXTRACTOR)

    @JvmField
    val INFUSING = HTEmiRecipeCategory(RagiumRecipes.INFUSING, RagiumBlocks.INFUSER)

    @JvmField
    val REFINING = HTEmiRecipeCategory(RagiumRecipes.REFINING, RagiumBlocks.REFINERY)

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

        CATALYSTS.forEach(registry::addCategory)
        MACHINES.forEach(registry::addCategory)

        // Workstation
        fun addWorkstation(category: HTEmiRecipeCategory) {
            registry.addWorkstation(category, category.icon)
        }

        CATALYSTS.forEach(::addWorkstation)
        MACHINES.forEach(::addWorkstation)
    }
}
