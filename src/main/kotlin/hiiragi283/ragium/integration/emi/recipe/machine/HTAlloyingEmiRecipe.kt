package hiiragi283.ragium.integration.emi.recipe.machine

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.manager.HTRecipeHolder
import hiiragi283.ragium.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.integration.emi.addArrow
import hiiragi283.ragium.integration.emi.recipe.HTEmiHolderRecipe

class HTAlloyingEmiRecipe(category: HTEmiRecipeCategory, holder: HTRecipeHolder<HTCombineItemToItemRecipe>) :
    HTEmiHolderRecipe<HTCombineItemToItemRecipe>(category, holder) {
    init {
        recipe.ingredients.forEach(::addInput)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        // Input
        for (i: Int in (0..2)) {
            widgets.addSlot(input(i), getPosition(i), getPosition(0.0))
        }
        // Output
        widgets.addOutput(0, getPosition(4.5), getPosition(1), true)
    }
}
