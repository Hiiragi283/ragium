package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTPyrolyzingEmiRecipe(holder: RecipeHolder<HTPyrolyzingRecipe>) :
    HTProcessingEmiRecipe<HTPyrolyzingRecipe>(RagiumEmiRecipeCategories.PYROLYZING, holder) {
    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.itemResult)
        addOutputs(recipe.fluidResult)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(time = recipe.time, x = getPosition(2.5))
        widgets.addBurning(getPosition(1), getPosition(1.5), recipe.time)
        // input
        widgets.addSlot(input(0), getPosition(1), getPosition(0.5), HTBackgroundType.INPUT)
        // outputs
        widgets.add2x2Slots(x = getPosition(4.5), ingredient = { index ->
            when (index) {
                0 -> output(index)
                else -> EmiStack.EMPTY
            }
        })
        widgets.addTank(output(1), getPosition(7), HTBackgroundType.OUTPUT)
    }
}
