package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.addTank
import hiiragi283.ragium.client.integration.emi.category.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiHolderRecipe
import hiiragi283.ragium.common.recipe.base.HTBasicFluidWithCatalystRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.jvm.optionals.getOrNull

class HTSolidifyingEmiRecipe(holder: RecipeHolder<HTBasicFluidWithCatalystRecipe>) :
    HTEmiHolderRecipe<HTBasicFluidWithCatalystRecipe>(RagiumEmiRecipeCategories.SOLIDIFYING, holder) {
    init {
        addInput(recipe.ingredient)
        addCatalyst(recipe.catalyst.getOrNull())

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        // inputs
        widgets
            .addTank(
                input(0),
                getPosition(1),
                getPosition(0),
            )
        widgets.addCatalyst(0, getPosition(2.5), getPosition(0))
        // output
        widgets.addOutput(0, getPosition(4.5), getPosition(1), true)
    }
}
