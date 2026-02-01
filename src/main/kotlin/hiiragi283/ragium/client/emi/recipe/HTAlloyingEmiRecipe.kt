package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTAlloyingEmiRecipe(holder: RecipeHolder<HTAlloyingRecipe>) :
    HTProcessingEmiRecipe<HTAlloyingRecipe>(RagiumEmiRecipeCategories.ALLOYING, holder) {
    init {
        recipe.ingredients.forEach(::addInput)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(time = recipe.time, x = getPosition(4))
        widgets.addBurning(getPosition(1.5), getPosition(1), recipe.time)
        // inputs
        widgets.addSlot(input(0), getPosition(0.5), getPosition(0), HTBackgroundType.INPUT)
        widgets.addSlot(input(1), getPosition(1.5), getPosition(0), HTBackgroundType.INPUT)
        widgets.addSlot(input(2), getPosition(2.5), getPosition(0), HTBackgroundType.INPUT)

        widgets.addSlot(catalyst(0), getPosition(1.5), getPosition(2), HTBackgroundType.EXTRA_INPUT)
        // output
        widgets.addSlot(output(0), getPosition(6), getPosition(1), HTBackgroundType.OUTPUT)
    }
}
