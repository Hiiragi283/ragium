package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.RagiumEmiTextures
import hiiragi283.ragium.client.emi.recipe.base.HTProcessingEmiRecipe
import hiiragi283.ragium.common.recipe.HTArcSmeltingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTArcSmeltingEmiRecipe(holder: RecipeHolder<HTArcSmeltingRecipe>) :
    HTProcessingEmiRecipe<HTArcSmeltingRecipe>(RagiumEmiRecipeCategories.ARC_FURNACE, holder) {
    init {
        addInput(recipe.fluidIngredient)
        addInput(recipe.itemIngredient)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(time = recipe.time)
        RagiumEmiTextures.addWidget(
            widgets,
            "mix",
            getPosition(1.5),
            getPosition(1.5),
            recipe.time,
            endToStart = true,
        )
        // inputs
        widgets.addTank(input(0), getPosition(0), HTBackgroundType.INPUT)
        widgets.addSlot(input(1), getPosition(1.5), getPosition(0.5), HTBackgroundType.INPUT)
        // outputs
        widgets.addTank(output(0), getPosition(5), HTBackgroundType.OUTPUT)
    }
}
