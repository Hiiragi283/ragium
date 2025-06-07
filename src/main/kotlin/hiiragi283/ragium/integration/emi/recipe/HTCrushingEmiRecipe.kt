package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTCrushingEmiRecipe(id: ResourceLocation, definition: HTRecipeDefinition) :
    HTMachineEmiRecipe(RagiumEmiCategories.CRUSHING, id, definition) {
    override fun getDisplayWidth(): Int = getPosition(7)

    override fun getDisplayHeight(): Int = getPosition(3)

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(2.5, 1.0)
        // Input
        widgets.addItemInput(0, 1.0, 0.0)
        widgets.addInput(EmiStack.EMPTY, 1.0, 2.0)
        // Output
        widgets.addItemOutput(0, 4.0, 0.5)
        widgets.addItemOutput(1, 5.0, 0.5)
        widgets.addItemOutput(2, 4.0, 1.5)
        widgets.addItemOutput(3, 5.0, 1.5)
    }
}
