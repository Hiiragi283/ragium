package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.addBurning
import hiiragi283.core.api.integration.emi.addTank
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTPyrolyzingEmiRecipe(holder: RecipeHolder<HTPyrolyzingRecipe>) :
    HTProcessingEmiRecipe<HTPyrolyzingRecipe>(RagiumConst.PYROLYZER, RagiumEmiRecipeCategories.PYROLYZING, holder) {
    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.itemResult)
        addOutputs(recipe.fluidResult)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        widgets.addBurning(getPosition(2), getPosition(1.5), recipe.time)

        // Input
        widgets.addInput(0, getPosition(2), getPosition(0.5))

        // Output
        widgets.addOutput(0, getPosition(5.5), getPosition(1), true)
        widgets.addTank(output(1), getPosition(7), getPosition(0)).recipeContext(this)
    }
}
