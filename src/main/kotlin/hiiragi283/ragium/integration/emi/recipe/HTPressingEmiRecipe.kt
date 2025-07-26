package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTPressingEmiRecipe(
    id: ResourceLocation,
    val ingredient: EmiIngredient,
    val catalyst: EmiIngredient,
    val results: EmiStack,
) : HTMachineEmiRecipe(id, RagiumAPI.id("textures/gui/container/forming_press.png")) {
    override fun getCategory(): EmiRecipeCategory = RagiumEmiCategories.PRESSING

    override fun getInputs(): List<EmiIngredient> = listOf(ingredient, catalyst)

    override fun getOutputs(): List<EmiStack> = listOf(results)

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        // Input
        widgets.addInput(ingredient, 1.0, 0.0).drawBack(false)
        widgets.addInput(catalyst, 1.0, 2.0).drawBack(false)
        // Output
        widgets.addOutput(0, 4.5, 1.0, true)
    }
}
