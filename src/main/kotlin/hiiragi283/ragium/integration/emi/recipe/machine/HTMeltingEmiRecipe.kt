package hiiragi283.ragium.integration.emi.recipe.machine

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.recipe.manager.HTRecipeHolder
import hiiragi283.ragium.impl.recipe.base.HTItemToFluidRecipe
import hiiragi283.ragium.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.integration.emi.addArrow
import hiiragi283.ragium.integration.emi.addTank
import hiiragi283.ragium.integration.emi.recipe.HTEmiHolderRecipe

class HTMeltingEmiRecipe(category: HTEmiRecipeCategory, holder: HTRecipeHolder<HTItemToFluidRecipe>) :
    HTEmiHolderRecipe<HTItemToFluidRecipe>(category, holder) {
    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        // Input
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        // Output
        widgets.addTank(output(0), getPosition(4.5), getPosition(0)).recipeContext(this)
    }
}
