package hiiragi283.ragium.client.emi.recipe.base

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.common.recipe.base.HTItemToChancedRecipe
import net.minecraft.world.item.crafting.RecipeHolder

abstract class HTItemToChancedEmiRecipe<RECIPE : HTItemToChancedRecipe>(category: HTEmiRecipeCategory, holder: RecipeHolder<RECIPE>) :
    HTChancedEmiRecipe<RECIPE>(category, holder) {
    final override fun addInputs() {
        addInput(recipe.ingredient)
    }

    final override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(time = recipe.time)
        // input
        widgets.addSlot(input(0), getPosition(1.5), getPosition(0.5), HTBackgroundType.INPUT)
        // outputs
        addOutputSlots(widgets)
    }

    protected abstract fun addOutputSlots(widgets: WidgetHolder)
}
