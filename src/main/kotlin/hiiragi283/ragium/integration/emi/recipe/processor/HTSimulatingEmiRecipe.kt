package hiiragi283.ragium.integration.emi.recipe.processor

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.manager.HTRecipeHolder
import hiiragi283.ragium.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.integration.emi.addArrow
import hiiragi283.ragium.integration.emi.recipe.HTEmiHolderRecipe
import kotlin.jvm.optionals.getOrNull

class HTSimulatingEmiRecipe(category: HTEmiRecipeCategory, holder: HTRecipeHolder<HTItemWithCatalystToItemRecipe>) :
    HTEmiHolderRecipe<HTItemWithCatalystToItemRecipe>(category, holder) {
    init {
        addInput(recipe.ingredient.getOrNull())
        addCatalyst(recipe.catalyst)
        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        // Input
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addSlot(catalyst(0), getPosition(1), getPosition(2)).catalyst(true)
        // Output
        widgets.addOutput(0, getPosition(4.5), getPosition(1), true)
    }
}
