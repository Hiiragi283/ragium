package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.addTank
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiHolderRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicComplexRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTRefiningEmiRecipe(category: HTEmiRecipeCategory, holder: RecipeHolder<HTBasicComplexRecipe>) :
    HTEmiHolderRecipe<HTBasicComplexRecipe>(category, holder) {
    init {
        addInput(recipe.fluidIngredients[0])
        addCatalyst(recipe.itemIngredients.getOrNull(0))

        addOutputs(recipe.results)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(3), getPosition(1))

        // Input
        widgets
            .addTank(
                input(0),
                getPosition(1),
                getPosition(0),
            )
        widgets.addSlot(catalyst(0), getPosition(2.5), getPosition(0)).catalyst(true)
        // Output
        widgets.addSlot(output(0), getPosition(3.5), getPosition(2))
        widgets
            .addTank(
                output(1),
                getPosition(5),
                getPosition(0),
            ).recipeContext(this)
    }
}
