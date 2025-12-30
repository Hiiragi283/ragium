package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.addTank
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTRefiningEmiRecipe(holder: RecipeHolder<HTRefiningRecipe>) :
    HTProcessingEmiRecipe<HTRefiningRecipe>(RagiumConst.REFINERY, RagiumEmiRecipeCategories.REFINING, holder) {
    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.result)
        addOutputs(recipe.extraResult.getRight())
        addOutputs(recipe.extraResult.getLeft())
    }

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)

        // Input
        widgets
            .addTank(input(0), getPosition(0.5), getPosition(1))
        // Output
        widgets
            .addTank(output(0), getPosition(5), getPosition(0))
            .recipeContext(this)
        widgets
            .addTank(output(1), getPosition(6.5), getPosition(0))
            .recipeContext(this)
        widgets.addOutput(2, getPosition(3.4), getPosition(0), drawBack = false)
    }

    override fun getArrowX(): Int = getPosition(2)
}
