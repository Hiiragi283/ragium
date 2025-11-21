package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiHolderRecipe
import hiiragi283.ragium.impl.recipe.HTAlloyingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTAlloyingEmiRecipe(category: HTEmiRecipeCategory, holder: RecipeHolder<HTAlloyingRecipe>) :
    HTEmiHolderRecipe<HTAlloyingRecipe>(category, holder) {
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
