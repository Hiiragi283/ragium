package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTRefiningEmiRecipe(id: ResourceLocation, definition: HTRecipeDefinition) :
    HTMachineEmiRecipe(RagiumEmiCategories.REFINING, id, definition) {
    override fun getDisplayWidth(): Int = getPosition(4.5)

    override fun getDisplayHeight(): Int = getPosition(1)

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(1.0, 0.0)
        // Input
        widgets.addFluidInput(0, 0.0, 0.0)
        // Output
        widgets.addItemOutput(0, 2.5, 0.0)
        widgets.addFluidOutput(0, 3.5, 0.0)
    }
}
