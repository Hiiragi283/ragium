package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.addTank
import hiiragi283.ragium.client.integration.emi.category.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiHolderRecipe
import hiiragi283.ragium.impl.recipe.HTRefiningRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.jvm.optionals.getOrNull

class HTRefiningEmiRecipe(category: HTEmiRecipeCategory, holder: RecipeHolder<HTRefiningRecipe>) :
    HTEmiHolderRecipe<HTRefiningRecipe>(category, holder) {
    init {
        addInput(recipe.fluidIngredient)
        addCatalyst(recipe.itemIngredient.getOrNull())

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
        widgets.addCatalyst(0, getPosition(2.5), getPosition(0))
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
