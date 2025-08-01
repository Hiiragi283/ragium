package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTItemWithFluidToItemEmiRecipe(
    private val category: EmiRecipeCategory,
    id: ResourceLocation,
    textureId: ResourceLocation,
    val fluidIngredient: EmiIngredient,
    val itemIngredient: EmiIngredient,
    val result: EmiStack,
) : HTMachineEmiRecipe(id, textureId) {
    companion object {
        @JvmStatic
        fun infusing(
            id: ResourceLocation,
            fluidIngredient: EmiIngredient,
            itemIngredient: EmiIngredient,
            results: EmiStack,
        ): HTItemWithFluidToItemEmiRecipe = HTItemWithFluidToItemEmiRecipe(
            RagiumEmiCategories.INFUSING,
            id,
            RagiumAPI.id("textures/gui/container/solidifier.png"),
            fluidIngredient,
            itemIngredient,
            results,
        )

        @JvmStatic
        fun solidifying(
            id: ResourceLocation,
            fluidIngredient: EmiIngredient,
            itemIngredient: EmiIngredient,
            results: EmiStack,
        ): HTItemWithFluidToItemEmiRecipe = HTItemWithFluidToItemEmiRecipe(
            RagiumEmiCategories.SOLIDIFYING,
            id,
            RagiumAPI.id("textures/gui/container/solidifier.png"),
            fluidIngredient,
            itemIngredient,
            results,
        )
    }

    override fun getCategory(): EmiRecipeCategory = category

    override fun getInputs(): List<EmiIngredient> = listOf(fluidIngredient, itemIngredient)

    override fun getOutputs(): List<EmiStack> = listOf(result)

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        // Input
        widgets.addInput(fluidIngredient, getPosition(1), getPosition(0)).drawBack(false)
        widgets.addInput(itemIngredient, getPosition(1), getPosition(2)).drawBack(false)
        // Output
        widgets.addOutput(0, getPosition(4.5), getPosition(1), true)
    }
}
