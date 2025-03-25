package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTInfusingEmiRecipe(id: ResourceLocation, definition: HTRecipeDefinition) :
    HTMachineEmiRecipe(RagiumEmiCategories.INFUSING, id, definition) {
    override fun getDisplayWidth(): Int = getPosition(5.5)

    override fun getDisplayHeight(): Int = getPosition(1)

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(2.0, 0.0)
        // Input
        widgets.addItemInput(0, 0.0, 0.0)
        widgets.addFluidInput(0, 1.0, 0.0)
        // Output
        widgets.addItemOutput(0, 3.5, 0.0)
        widgets.addFluidOutput(0, 4.5, 0.0)
    }
}
