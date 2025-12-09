package hiiragi283.ragium.client.integration.emi.recipe.generator

import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.category.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiRecipe
import net.minecraft.resources.ResourceLocation

class HTCoolantEmiRecipe(id: ResourceLocation, recipe: EmiStack) :
    HTEmiRecipe<EmiStack>(RagiumEmiRecipeCategories.COOLANT, id, recipe) {
    init {
        addInput(recipe)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        // input
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
    }
}
