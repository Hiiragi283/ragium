package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTSolidifyingEmiRecipe(
    id: ResourceLocation,
    val ingredient: EmiIngredient,
    val catalyst: EmiIngredient,
    val results: EmiStack,
) : HTMachineEmiRecipe(id, RagiumAPI.id("textures/gui/container/solidifier.png")) {
    override fun getCategory(): EmiRecipeCategory = RagiumEmiCategories.SOLIDIFYING

    override fun getInputs(): List<EmiIngredient> = listOf(ingredient, catalyst)

    override fun getOutputs(): List<EmiStack> = listOf(results)

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        // Input
        widgets.addInput(ingredient, getPosition(1), getPosition(0)).drawBack(false)
        widgets.addInput(catalyst, getPosition(1), getPosition(2)).drawBack(false)
        // Output
        widgets.addOutput(0, getPosition(4.5), getPosition(1), true)
    }
}
