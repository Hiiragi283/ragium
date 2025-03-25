package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTCentrifugingEmiRecipe(id: ResourceLocation, definition: HTRecipeDefinition) :
    HTMachineEmiRecipe(RagiumEmiCategories.CENTRIFUGING, id, definition) {
    override fun getDisplayWidth(): Int = getPosition(6.5)

    override fun getDisplayHeight(): Int = getPosition(3)

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(1.0, 1.0)
        // Input
        widgets.addInput(inputs[0], 0.0, 1.0)
        // Item Output
        widgets.addItemOutput(0, 2.5, 1.0)
        widgets.addItemOutput(1, 3.5, 0.0)
        widgets.addItemOutput(2, 3.5, 2.0)
        widgets.addItemOutput(3, 4.5, 1.0)
        // Fluid Output
        widgets.addFluidOutput(0, 5.5, 0.0)
        widgets.addFluidOutput(1, 5.5, 2.0)
    }
}
