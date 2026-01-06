package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.RagiumEmiTextures
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTPressingEmiRecipe(holder: RecipeHolder<HTPressingRecipe>) :
    HTProcessingEmiRecipe<HTPressingRecipe>(RagiumConst.FORMING_PRESS, RagiumEmiRecipeCategories.PRESSING, holder) {
    init {
        addInput(recipe.ingredient)

        addCatalyst(recipe.catalyst)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        RagiumEmiTextures.addWidget(
            widgets,
            "press",
            getPosition(1),
            getPosition(1),
            recipe.time,
            endToStart = false,
        )
        // Input
        widgets.addInput(0, getPosition(1), getPosition(0))
        // Catalyst
        widgets.addCatalyst(0, getPosition(1), getPosition(2))
        // Output
        widgets.addOutput(0, getPosition(4.5), getPosition(0.5))
    }

    override fun getArrowX(): Int = getPosition(2.5)
}
