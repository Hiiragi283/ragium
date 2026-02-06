package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTAlloyingEmiRecipe(holder: RecipeHolder<HTAlloyingRecipe>) :
    HTChancedEmiRecipe<HTAlloyingRecipe>(RagiumEmiRecipeCategories.ALLOYING, holder) {
    override fun addInputs() {
        recipe.ingredients.forEach(::addInput)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(time = recipe.time, x = getPosition(3.5))
        widgets.addBurning(getPosition(1), getPosition(1.5), recipe.time)
        // inputs
        widgets.addSlot(input(0), getPosition(0), getPosition(0.5), HTBackgroundType.INPUT)
        widgets.addSlot(input(1), getPosition(1), getPosition(0.5), HTBackgroundType.INPUT)
        widgets.addSlot(input(2), getPosition(2), getPosition(0.5), HTBackgroundType.INPUT)
        // outputs
        widgets.addSlot(output(0), getPosition(5.5), getPosition(0.5), HTBackgroundType.OUTPUT)
        widgets.addSlot(output(1), getPosition(5.5), getPosition(2), HTBackgroundType.EXTRA_OUTPUT)
    }
}
