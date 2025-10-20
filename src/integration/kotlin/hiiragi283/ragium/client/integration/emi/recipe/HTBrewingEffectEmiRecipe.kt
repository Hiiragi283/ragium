package hiiragi283.ragium.client.integration.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.data.HTEmiBrewingEffect
import net.minecraft.resources.ResourceLocation

class HTBrewingEffectEmiRecipe(category: HTEmiRecipeCategory, id: ResourceLocation, recipe: HTEmiBrewingEffect) :
    HTEmiRecipe<HTEmiBrewingEffect>(category, id, recipe) {
    init {
        addInput(recipe.input)

        addOutputs(recipe.potion)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addSlot(input(0), getPosition(0), getPosition(0))
        widgets.addArrow(getPosition(1.5), getPosition(0))
        widgets.addOutput(0, getPosition(3), getPosition(0), drawBack = true)
    }
}
