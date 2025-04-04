package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTAlloyingEmiRecipe(id: ResourceLocation, definition: HTRecipeDefinition) :
    HTMachineEmiRecipe(RagiumEmiCategories.ALLOYING, id, definition) {
    override fun getDisplayWidth(): Int = getPosition(4.5)

    override fun getDisplayHeight(): Int = getPosition(1)

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addInput(itemInputs[0], 0.0, 0.0)
        widgets.addInput(itemInputs[1], 1.0, 0.0)
        widgets.addArrow(2.0, 0.0)
        widgets.addOutput(itemOutputs[0], 3.5, 0.0)
    }
}
