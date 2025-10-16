package hiiragi283.ragium.client.integration.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.data.HTEmiBrewingEffect
import hiiragi283.ragium.client.integration.emi.toEmi

class HTBrewingEffectEmiRecipe(category: HTEmiRecipeCategory, recipe: HTEmiBrewingEffect) :
    HTEmiRecipe<HTEmiBrewingEffect>(category, recipe.getId(), recipe) {
    init {
        addInput(recipe.input.toEmi())

        addOutputs(recipe.effect.toPotion().toEmi())
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addSlot(input(0), getPosition(0), getPosition(0))
        widgets.addArrow(getPosition(1.5), getPosition(0))
        widgets.addOutput(0, getPosition(3), getPosition(0), drawBack = true)
    }
}
