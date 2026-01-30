package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.common.recipe.base.HTComplexResultRecipe
import net.minecraft.world.item.crafting.RecipeHolder

abstract class HTComplexEmiRecipe<RECIPE : HTComplexResultRecipe.Simple>(category: HTEmiRecipeCategory, holder: RecipeHolder<RECIPE>) :
    HTProcessingEmiRecipe<RECIPE>(category, holder) {
    init {
        addInput(recipe.getItemIngredient())
        addInput(recipe.getFluidIngredient())

        addOutputs(recipe.result)
    }

    final override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(time = recipe.time)
        addSubProgress(widgets)
        // inputs
        widgets.addSlot(input(0), getPosition(2), getPosition(0.5), HTBackgroundType.INPUT)
        widgets.addTank(input(1), getPosition(0.5), HTBackgroundType.INPUT)
        // outputs
        widgets.addSlot(output(0), getPosition(5.5), getPosition(1), HTBackgroundType.OUTPUT)
        widgets.addTank(output(1), getPosition(7), HTBackgroundType.OUTPUT)
    }

    protected abstract fun addSubProgress(widgets: WidgetHolder)
}
