package hiiragi283.ragium.client.integration.emi.recipe.base

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.category.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiHolderRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicComplexOutputRecipe
import net.minecraft.world.item.crafting.RecipeHolder

abstract class HTMultiOutputsEmiRecipe<RECIPE : HTBasicComplexOutputRecipe>(category: HTEmiRecipeCategory, holder: RecipeHolder<RECIPE>) :
    HTEmiHolderRecipe<RECIPE>(category, holder) {
    init {
        initInputs()
        addOutputs(recipe.results)
    }

    protected abstract fun initInputs()

    final override fun addWidgets(widgets: WidgetHolder) {
        // Input
        initInputSlots(widgets)
        // Output
        widgets.addOutput(0, getPosition(4.5), getPosition(0) + 4, true)
        widgets.addSlot(output(1), getPosition(4.5), getPosition(2))
    }

    protected abstract fun initInputSlots(widgets: WidgetHolder)
}
