package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTAlloyingEmiRecipe(id: ResourceLocation, val ingredients: List<EmiIngredient>, val result: EmiStack) :
    HTMachineEmiRecipe(id, RagiumAPI.id("textures/gui/container/alloy_smelter.png")) {
    override fun getCategory(): EmiRecipeCategory = RagiumEmiCategories.ALLOYING

    override fun getInputs(): List<EmiIngredient> = ingredients

    override fun getOutputs(): List<EmiStack> = listOf(result)

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        // Input
        widgets.addInput(ingredients[0], getPosition(0.5), getPosition(0)).drawBack(false)
        widgets.addInput(ingredients[1], getPosition(1.5), getPosition(0)).drawBack(false)
        // Output
        widgets.addOutput(0, getPosition(4.5), getPosition(1), true)
    }
}
