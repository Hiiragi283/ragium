package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.recipe.base.HTMultiOutputEmiRecipe
import hiiragi283.ragium.impl.recipe.base.HTItemToChancedItemRecipeBase
import net.minecraft.world.item.crafting.RecipeHolder

class HTCuttingEmiRecipe(category: HTEmiRecipeCategory, holder: RecipeHolder<HTItemToChancedItemRecipeBase>) :
    HTMultiOutputEmiRecipe<HTItemToChancedItemRecipeBase>(category, holder) {
    init {
        addInput(recipe.ingredient)
        recipe.results.forEach(::addChancedOutputs)
    }

    override fun initInputSlots(widgets: WidgetHolder) {
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addSlot(output(0).copy().setAmount(1), getPosition(1), getPosition(2)).catalyst(true)
    }
}
