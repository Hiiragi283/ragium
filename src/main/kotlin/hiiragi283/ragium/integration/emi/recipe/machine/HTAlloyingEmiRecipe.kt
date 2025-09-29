package hiiragi283.ragium.integration.emi.recipe.machine

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
        for (i: Int in (0..2)) {
            widgets.addSlot(ingredients.getOrNull(i) ?: EmiStack.EMPTY, getPosition(i), getPosition(0.0)).drawBack(false)
        }
        // Output
        widgets.addOutput(result, getPosition(4.5), getPosition(1), true)
    }
}
