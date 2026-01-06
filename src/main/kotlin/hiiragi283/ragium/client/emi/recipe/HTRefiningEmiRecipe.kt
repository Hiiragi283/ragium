package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.config.RagiumFluidConfigType
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
            .addTank(input(0), getPosition(0.5), getCapacity(RagiumFluidConfigType.FIRST_INPUT))
        // Output
        widgets
            .addTank(output(0), getPosition(5), getCapacity(RagiumFluidConfigType.FIRST_OUTPUT))
            .recipeContext(this)
        widgets
            .addTank(output(1), getPosition(6.5), getCapacity(RagiumFluidConfigType.SECOND_OUTPUT))
            .recipeContext(this)
        widgets.addOutput(2, getPosition(3.5), getPosition(1), drawBack = false)
    }

    override fun getArrowX(): Int = getPosition(2)
}
