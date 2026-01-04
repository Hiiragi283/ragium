package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.common.recipe.HTComplexRecipe
import hiiragi283.ragium.config.RagiumFluidConfigType
import net.minecraft.world.item.crafting.RecipeHolder

abstract class HTComplexEmiRecipe<RECIPE : HTComplexRecipe>(
    backgroundTex: String,
    category: HTEmiRecipeCategory,
    holder: RecipeHolder<RECIPE>,
) : HTProcessingEmiRecipe<RECIPE>(backgroundTex, category, holder) {
    init {
        addInput(recipe.getItemIngredient())
        addInput(recipe.getFluidIngredient())

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        addSubProgress(widgets)

        // Input
        widgets.addInput(0, getPosition(2), getPosition(0.5))
        widgets.addTank(input(1), getPosition(0.5), getCapacity(RagiumFluidConfigType.FIRST_INPUT))

        // Output
        widgets.addOutput(0, getPosition(5.5), getPosition(1), true)
        widgets.addTank(output(1), getPosition(7), getCapacity(RagiumFluidConfigType.FIRST_OUTPUT)).recipeContext(this)
    }

    protected abstract fun addSubProgress(widgets: WidgetHolder)
}
