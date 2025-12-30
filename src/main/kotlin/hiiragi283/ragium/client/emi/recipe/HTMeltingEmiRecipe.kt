package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.addBurning
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTMeltingEmiRecipe(holder: RecipeHolder<HTMeltingRecipe>) :
    HTProcessingEmiRecipe<HTMeltingRecipe>(RagiumEmiRecipeCategories.MELTING, holder) {
    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)

        // Input
        widgets.addSlot(input(0), getPosition(1), getPosition(0.5))
        widgets.addBurning(getPosition(1), getPosition(1.5), recipe.time)
        // Output
        widgets
            .addTank(output(0), getPosition(4.5), getPosition(0), getPosition(1), getPosition(3), output(0).amount.toInt())
            .drawBack(false)
            .recipeContext(this)
    }
}
