package hiiragi283.ragium.integration.emi

import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Items

object RagiumEmiCategories {
    @JvmField
    val CAULDRON_DROPPING = HTEmiRecipeCategory(
        RagiumAPI.id(RagiumConstantValues.CAULDRON_DROPPING),
        EmiStack.of(Items.CAULDRON),
        Component.translatable(RagiumTranslationKeys.EMI_CAULDRON_DROPPING),
    )

    @JvmField
    val TREE_TAPPING = HTEmiRecipeCategory(RagiumAPI.id("tree_tapping"), RagiumBlocks.TREE_TAP)

    // Machines
    @JvmField
    val ALLOYING = HTEmiRecipeCategory(RagiumAPI.id(RagiumConstantValues.ALLOYING), RagiumBlocks.CRUSHER)

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
    val CATEGORIES: List<HTEmiRecipeCategory> = listOf(
        CAULDRON_DROPPING,
        TREE_TAPPING,
        // Machines
        ALLOYING,
        CRUSHING,
        EXTRACTING,
        INFUSING,
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
        registry.addWorkstation(CRUSHING, EmiStack.of(RagiumBlocks.ADVANCED_CRUSHER))
        registry.addWorkstation(EXTRACTING, EmiStack.of(RagiumBlocks.ADVANCED_EXTRACTOR))
    }
}
