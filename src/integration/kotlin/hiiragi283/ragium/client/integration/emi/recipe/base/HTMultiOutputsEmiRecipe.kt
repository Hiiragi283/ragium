package hiiragi283.ragium.client.integration.emi.recipe.base

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiHolderRecipe
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder

abstract class HTMultiOutputsEmiRecipe<RECIPE : Recipe<*>>(category: HTEmiRecipeCategory, holder: RecipeHolder<RECIPE>) :
    HTEmiHolderRecipe<RECIPE>(category, holder) {
    final override fun addWidgets(widgets: WidgetHolder) {
        // Input
        initInputSlots(widgets)
        // Output
        widgets.addOutput(0, getPosition(5), getPosition(0) + 4, true)
        widgets.addSlot(output(1), getPosition(5), getPosition(2))
    }

    protected abstract fun initInputSlots(widgets: WidgetHolder)
}
