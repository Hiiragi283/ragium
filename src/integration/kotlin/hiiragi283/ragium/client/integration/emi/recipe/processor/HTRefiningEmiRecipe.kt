package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.addTank
import hiiragi283.ragium.client.integration.emi.category.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiHolderRecipe
import hiiragi283.ragium.common.recipe.HTBasicRefiningRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.jvm.optionals.getOrNull

class HTRefiningEmiRecipe(holder: RecipeHolder<HTBasicRefiningRecipe>) :
    HTEmiHolderRecipe<HTBasicRefiningRecipe>(RagiumEmiRecipeCategories.REFINING, holder) {
    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.itemResult.getOrNull())
        addOutputs(recipe.fluidResult)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        // Input
        widgets
            .addTank(
                input(0),
                getPosition(1),
                getPosition(0),
            )
        // Output
        widgets.addSlot(output(0), getPosition(4), getPosition(1))
        widgets
            .addTank(
                output(1),
                getPosition(5),
                getPosition(0),
            ).recipeContext(this)
    }
}
