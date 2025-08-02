package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.resources.ResourceLocation

class HTCrushingEmiRecipe(id: ResourceLocation, val ingredient: EmiIngredient, val results: List<EmiStack>) :
    HTMachineEmiRecipe(id, RagiumAPI.id("textures/gui/container/crusher.png")) {
    override fun getCategory(): EmiRecipeCategory = RagiumEmiCategories.CRUSHING

    override fun getInputs(): List<EmiIngredient> = listOf(ingredient)

    override fun getOutputs(): List<EmiStack> = results

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        // Input
        widgets.addInput(ingredient, getPosition(1), getPosition(0)).drawBack(false)
        widgets
            .addInput(EmiIngredient.of(RagiumFluidContents.LUBRICANT.commonTag), getPosition(1), getPosition(2))
            .catalyst(true)
            .drawBack(false)
        // Output
        widgets.addOutput(0, getPosition(4), getPosition(0.5))
        widgets.addOutput(1, getPosition(5), getPosition(0.5))
        widgets.addOutput(2, getPosition(4), getPosition(1.5))
        widgets.addOutput(3, getPosition(5), getPosition(1.5))
    }
}
