package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTCentrifugingEmiRecipe(id: ResourceLocation, definition: HTRecipeDefinition) :
    HTMachineEmiRecipe(RagiumEmiCategories.CENTRIFUGING, id, definition) {
    override fun getDisplayWidth(): Int = getPosition(7)

    override fun getDisplayHeight(): Int = getPosition(3)

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, getPosition(1), getPosition(1))
        // Input
        widgets.addInput(inputs[0], 0.0, 1.0)
        // Item Output
        widgets.addItemOutput(0, 3.0, 1.0)
        widgets.addItemOutput(1, 4.0, 0.0)
        widgets.addItemOutput(2, 4.0, 2.0)
        widgets.addItemOutput(3, 5.0, 1.0)
        // Fluid Output
        widgets.addFluidOutput(0, 6.0, 0.0)
        widgets.addFluidOutput(1, 6.0, 2.0)
    }
}
