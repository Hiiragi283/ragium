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
    val DEVICE = HTEmiRecipeCategory(
        RagiumAPI.id("device"),
        EmiStack.of(RagiumItems.RAGI_ALLOY_TOOLS.hammerItem),
        Component.literal("Device"),
    )

    // Machines
    @JvmField
    val CENTRIFUGING = HTEmiRecipeCategory(RagiumRecipes.CENTRIFUGING, RagiumBlocks.CENTRIFUGE)

    @JvmField
    val CRUSHING = HTEmiRecipeCategory(RagiumRecipes.CRUSHING, RagiumBlocks.CRUSHER)

    @JvmField
    val EXTRACTING = HTEmiRecipeCategory(RagiumRecipes.EXTRACTING, RagiumBlocks.EXTRACTOR)

    @JvmField
    val INFUSING = HTEmiRecipeCategory(RagiumRecipes.INFUSING, RagiumBlocks.INFUSER)

    @JvmField
    val REFINING = HTEmiRecipeCategory(RagiumRecipes.REFINING, RagiumBlocks.EXTRACTOR)

    @JvmStatic
    fun register(registry: EmiRegistry) {
        // Category
        registry.addCategory(DEVICE)

        registry.addCategory(CENTRIFUGING)
        registry.addCategory(CRUSHING)
        registry.addCategory(EXTRACTING)
        registry.addCategory(INFUSING)
        registry.addCategory(REFINING)

        // Workstation
        fun addWorkstation(category: HTEmiRecipeCategory) {
            registry.addWorkstation(category, category.icon)
        }

        addWorkstation(CENTRIFUGING)
        addWorkstation(CRUSHING)
        addWorkstation(EXTRACTING)
        addWorkstation(REFINING)
    }
}
