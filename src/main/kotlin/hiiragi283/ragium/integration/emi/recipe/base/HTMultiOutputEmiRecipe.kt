package hiiragi283.ragium.integration.emi.recipe.base

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.recipe.manager.HTRecipeHolder
import hiiragi283.ragium.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.integration.emi.addArrow
import hiiragi283.ragium.integration.emi.recipe.HTEmiHolderRecipe
import net.minecraft.world.item.crafting.Recipe

abstract class HTMultiOutputEmiRecipe<RECIPE : Recipe<*>>(category: HTEmiRecipeCategory, holder: HTRecipeHolder<RECIPE>) :
    HTEmiHolderRecipe<RECIPE>(category, holder) {
    final override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        // Input
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addSlot(getCatalystStack(), getPosition(1), getPosition(2)).catalyst(shouldCatalyst())
        // Output
        widgets.addOutput(0, getPosition(4), getPosition(0.5))
        widgets.addOutput(1, getPosition(5), getPosition(0.5))
        widgets.addOutput(2, getPosition(4), getPosition(1.5))
        widgets.addOutput(3, getPosition(5), getPosition(1.5))
    }

    protected open fun getCatalystStack(): EmiIngredient = input(1)

    protected open fun shouldCatalyst(): Boolean = false
}
