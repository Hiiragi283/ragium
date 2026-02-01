package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTRefiningEmiRecipe(holder: RecipeHolder<HTRefiningRecipe>) :
    HTProcessingEmiRecipe<HTRefiningRecipe>(RagiumEmiRecipeCategories.REFINING, holder) {
    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(time = recipe.time, x = getPosition(3.5))
        widgets.addBurning(getPosition(3.5), getPosition(2), recipe.time)
        // input
        widgets.addTank(input(0), getPosition(2), HTBackgroundType.INPUT)
        // output
        widgets.addTank(output(0), getPosition(5.5), HTBackgroundType.OUTPUT)
    }
}
