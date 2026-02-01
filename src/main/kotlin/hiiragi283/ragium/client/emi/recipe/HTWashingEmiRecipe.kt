package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.RagiumEmiTextures
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTWashingEmiRecipe(holder: RecipeHolder<HTWashingRecipe>) :
    HTChancedEmiRecipe<HTWashingRecipe>(RagiumEmiRecipeCategories.WASHING, holder) {
    override fun addInputs() {
        addInput(recipe.itemIngredient)
        addInput(recipe.fluidIngredient)
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
        widgets.addSlot(input(0), getPosition(1.5), getPosition(0.5), HTBackgroundType.INPUT)
        widgets.addTank(input(1), getPosition(0), HTBackgroundType.INPUT)
        // outputs
        widgets.addTripleOutputs()
    }
}
