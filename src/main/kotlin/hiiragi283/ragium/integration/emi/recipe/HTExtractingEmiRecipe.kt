package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTExtractingEmiRecipe(id: ResourceLocation, val ingredient: EmiIngredient, val result: EmiStack) :
    HTMachineEmiRecipe(id, RagiumAPI.id("textures/gui/container/extractor.png")) {
    override fun getCategory(): EmiRecipeCategory = RagiumEmiCategories.EXTRACTING

    override fun getInputs(): List<EmiIngredient> = listOf(ingredient)

    override fun getOutputs(): List<EmiStack> = listOf(result)

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        // Input
        widgets.addInput(ingredient, 1.0, 0.0).drawBack(false)
        // Output
        widgets.addOutput(0, 4.0, 0.5)
        widgets.addOutput(1, 5.0, 0.5)
        widgets.addOutput(2, 4.0, 1.5)
        widgets.addOutput(3, 5.0, 1.5)
    }
}
