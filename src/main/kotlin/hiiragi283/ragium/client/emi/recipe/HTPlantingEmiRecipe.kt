package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.RagiumEmiTextures
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTPlantingEmiRecipe(holder: RecipeHolder<HTPlantingRecipe>) :
    HTProcessingEmiRecipe<HTPlantingRecipe>(RagiumConst.PLANTER, RagiumEmiRecipeCategories.PLANTING, holder) {
    init {
        addInput(recipe.seedIngredient)

        addCatalyst(recipe.soil)

        addOutputs(recipe.crop)
        addOutputs(recipe.seedResult)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        RagiumEmiTextures.addWidget(
            widgets,
            "sun",
            getPosition(2),
            getPosition(1),
            recipe.time,
            endToStart = true,
        )
        // Input
        widgets.addInput(0, getPosition(2), getPosition(0))
        // Catalyst
        widgets.addCatalyst(0, getPosition(2), getPosition(2))
        // Output
        widgets.addOutput(0, getPosition(5.5), getPosition(0.5), true)
        widgets.addOutput(1, getPosition(5.5), getPosition(2))
    }
}
