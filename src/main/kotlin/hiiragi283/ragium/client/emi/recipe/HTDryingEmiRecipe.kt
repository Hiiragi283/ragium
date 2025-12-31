package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.addBurning
import hiiragi283.core.api.integration.emi.addTank
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTDryingRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.jvm.optionals.getOrNull

class HTDryingEmiRecipe(holder: RecipeHolder<HTDryingRecipe>) :
    HTProcessingEmiRecipe<HTDryingRecipe>(RagiumConst.DRYER, RagiumEmiRecipeCategories.DRYING, holder) {
    init {
        addInput(recipe.ingredient.left().getOrNull())
        addInput(recipe.ingredient.right().getOrNull())

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        widgets.addBurning(getPosition(2), getPosition(1.5), recipe.time)

        // Input
        widgets.addInput(0, getPosition(2), getPosition(0.5))
        widgets.addTank(input(1), getPosition(0.5), getPosition(0))

        // Output
        widgets.addOutput(0, getPosition(5.5), getPosition(1), true)
        widgets.addTank(output(1), getPosition(7), getPosition(0)).recipeContext(this)
    }
}
