package hiiragi283.ragium.integration.emi

import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.EmiRecipeSorting
import dev.emi.emi.api.render.EmiRenderable
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.client.gui.GuiGraphics

object RagiumEmiCategories {
    @JvmField
    val FLUID_FUEL: EmiRecipeCategory

    init {
        val flame = EmiRenderable { matrices: GuiGraphics, x: Int, y: Int, delta: Float ->
            EmiTexture.FULL_FLAME.render(matrices, x + 1, y + 1, delta)
        }
        FLUID_FUEL = EmiRecipeCategory(RagiumAPI.id("fluid_fuel"), flame, flame, EmiRecipeSorting.compareInputThenOutput())
    }

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
        CATEGORIES.forEach(registry::addCategory)
        registry.addCategory(FLUID_FUEL)

        // Workstation
        fun addWorkstation(category: HTEmiRecipeCategory) {
            registry.addWorkstation(category, category.iconStack)
        }

        CATEGORIES.forEach(::addWorkstation)
        registry.addWorkstation(CRUSHING, EmiStack.of(RagiumBlocks.Machines.PULVERIZER))
    }
}
