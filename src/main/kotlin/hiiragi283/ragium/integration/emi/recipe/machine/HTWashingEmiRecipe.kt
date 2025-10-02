package hiiragi283.ragium.integration.emi.recipe.machine

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.recipe.manager.HTRecipeHolder
import hiiragi283.ragium.impl.recipe.HTWashingRecipe
import hiiragi283.ragium.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.integration.emi.addArrow
import hiiragi283.ragium.integration.emi.recipe.HTEmiHolderRecipe

class HTWashingEmiRecipe(category: HTEmiRecipeCategory, holder: HTRecipeHolder<HTWashingRecipe>) :
    HTEmiHolderRecipe<HTWashingRecipe>(category, holder) {
    init {
        addInput(recipe.ingredient)
        addInput(recipe.fluidIngredient)

        recipe.results.forEach(::addChancedOutputs)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        // Input
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addSlot(input(1), getPosition(1), getPosition(2))
        // Output
        widgets.addOutput(0, getPosition(4), getPosition(0.5))
        widgets.addOutput(1, getPosition(5), getPosition(0.5))
        widgets.addOutput(2, getPosition(4), getPosition(1.5))
        widgets.addOutput(3, getPosition(5), getPosition(1.5))
    }
}
