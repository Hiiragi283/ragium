package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTSimulatingEmiRecipe(
    id: ResourceLocation,
    val ingredient: EmiIngredient,
    val catalyst: EmiIngredient,
    val result: EmiStack,
) : HTMachineEmiRecipe(id, RagiumAPI.id("textures/gui/container/simulator.png")) {
    override fun getCategory(): EmiRecipeCategory = RagiumEmiCategories.SIMULATING

    override fun getInputs(): List<EmiIngredient> = listOf(ingredient)

    override fun getCatalysts(): List<EmiIngredient> = listOf(catalyst)

    override fun getOutputs(): List<EmiStack> = listOf(result)

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        // Input
        widgets.addSlot(ingredient, getPosition(1), getPosition(0)).drawBack(false)
        widgets.addSlot(catalyst, getPosition(1), getPosition(2)).drawBack(false).catalyst(true)
        // Output
        widgets.addOutput(result, getPosition(4.5), getPosition(1), true)
    }
}
