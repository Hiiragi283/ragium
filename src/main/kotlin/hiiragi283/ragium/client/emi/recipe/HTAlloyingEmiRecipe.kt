package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.addBurning
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTAlloyingEmiRecipe(holder: RecipeHolder<HTAlloyingRecipe>) :
    HTProcessingEmiRecipe<HTAlloyingRecipe>(RagiumEmiRecipeCategories.ALLOYING, holder) {
    init {
        addInput(recipe.firstIngredient)
        addInput(recipe.secondIngredient)
        addInput(recipe.thirdIngredient)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        widgets.addBurning(getPosition(1), getPosition(2), recipe.time)

        // Input
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addSlot(input(1), getPosition(0.5), getPosition(1))
        widgets.addSlot(input(2), getPosition(1.5), getPosition(1))

        // Output
        widgets.addOutput(0, getPosition(5), getPosition(1), true)
    }
}
