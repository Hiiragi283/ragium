package hiiragi283.ragium.integration.emi.recipe.machine

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTWashingEmiRecipe(
    id: ResourceLocation,
    val ingredient: EmiIngredient,
    val fluidIngredient: EmiIngredient,
    val results: List<EmiStack>,
) : HTMachineEmiRecipe(id, RagiumAPI.id("textures/gui/container/washer.png")) {
    override fun getCategory(): EmiRecipeCategory = RagiumEmiCategories.WASHING

    override fun getInputs(): List<EmiIngredient> = listOf(ingredient, fluidIngredient)

    override fun getOutputs(): List<EmiStack> = results

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        // Input
        widgets.addSlot(ingredient, getPosition(1), getPosition(0)).drawBack(false)
        widgets.addSlot(fluidIngredient, getPosition(1), getPosition(2)).drawBack(false)
        // Output
        widgets.addOutput(results.getOrNull(0), getPosition(4), getPosition(0.5))
        widgets.addOutput(results.getOrNull(1), getPosition(5), getPosition(0.5))
        widgets.addOutput(results.getOrNull(2), getPosition(4), getPosition(1.5))
        widgets.addOutput(results.getOrNull(3), getPosition(5), getPosition(1.5))
    }
}
