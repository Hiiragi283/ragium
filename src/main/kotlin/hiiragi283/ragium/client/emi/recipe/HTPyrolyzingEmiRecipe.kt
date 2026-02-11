package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.recipe.base.HTProcessingEmiRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTPyrolyzingEmiRecipe(holder: RecipeHolder<HTPyrolyzingRecipe>) :
    HTProcessingEmiRecipe<HTPyrolyzingRecipe>(RagiumEmiRecipeCategories.PYROLYZING, holder) {
    init {
        addInput(recipe.itemIngredient)
        addInput(recipe.fluidIngredient)

        addOutputs(recipe.itemResult)
        addOutputs(recipe.fluidResult)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(time = recipe.time)
        widgets.addBurning(getPosition(1.5), getPosition(1.5), recipe.time)
        // input
        widgets.addSlot(input(0), getPosition(1.5), getPosition(0.5), HTBackgroundType.INPUT)
        widgets.addTank(input(1), getPosition(0), HTBackgroundType.EXTRA_INPUT)
        // outputs
        widgets.addSlot(output(0), getPosition(5), getPosition(0), HTBackgroundType.OUTPUT)
        widgets.addSlot(EmiStack.EMPTY, getPosition(5), getPosition(1), HTBackgroundType.OUTPUT)
        widgets.addSlot(EmiStack.EMPTY, getPosition(5), getPosition(2), HTBackgroundType.OUTPUT)
        widgets.addTank(output(1), getPosition(6.5), HTBackgroundType.EXTRA_OUTPUT)
    }
}
