package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTRefiningEmiRecipe(id: ResourceLocation, val ingredient: EmiIngredient, val results: List<EmiStack>) :
    HTEmiRecipe.Base(id, RagiumAPI.id("textures/gui/container/refinery.png")) {
    override fun getCategory(): EmiRecipeCategory = RagiumEmiCategories.REFINING

    override fun getInputs(): List<EmiIngredient> = listOf(ingredient)

    override fun getOutputs(): List<EmiStack> = results

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        // Input
        widgets.addInput(ingredient, 1.0, 1.0).drawBack(false)
        // Output
        widgets.addOutput(0, 4.0, 1.0)
        widgets.addOutput(1, 5.0, 0.5)
        widgets.addOutput(2, 5.0, 1.5)
    }
}
