package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.RagiumEmiTextures
import hiiragi283.ragium.client.emi.recipe.base.HTProcessingEmiRecipe
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTPlantingEmiRecipe(holder: RecipeHolder<HTPlantingRecipe>) :
    HTProcessingEmiRecipe<HTPlantingRecipe>(RagiumEmiRecipeCategories.PLANTING, holder) {
    init {
        addInput(recipe.seedIngredient)

        addCatalyst(recipe.soil)

        addOutputs(recipe.crop)
        addOutputs(recipe.seedResult)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(time = recipe.time)
        RagiumEmiTextures.addWidget(
            widgets,
            "sun",
            getPosition(1.5),
            getPosition(1),
            recipe.time,
            endToStart = true,
        )
        // input
        widgets.addSlot(input(0), getPosition(1.5), getPosition(0), HTBackgroundType.INPUT)
        // catalyst
        widgets.addSlot(catalyst(0), getPosition(1.5), getPosition(2), HTBackgroundType.NONE)
        // outputs
        widgets.addSlot(output(0), getPosition(5), getPosition(0.5), HTBackgroundType.OUTPUT)
        widgets.addSlot(output(1), getPosition(5), getPosition(2), HTBackgroundType.EXTRA_OUTPUT)
    }
}
