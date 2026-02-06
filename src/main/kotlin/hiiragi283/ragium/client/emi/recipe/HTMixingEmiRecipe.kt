package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.RagiumEmiTextures
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTMixingEmiRecipe(holder: RecipeHolder<HTMixingRecipe>) :
    HTChemicalEmiRecipe<HTMixingRecipe>(
        HTMixingRecipe.MAX_ITEM_INPUT,
        HTMixingRecipe.MAX_FLUID_INPUT,
        RagiumEmiRecipeCategories.MIXING,
        holder,
    ) {
    override fun addOutputs() {
        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        RagiumEmiTextures.addWidget(
            widgets,
            "mix",
            getPosition(4),
            getPosition(1),
            recipe.time,
            endToStart = false,
            horizontal = true,
        )
        // inputs
        widgets.addSlot(input(0), getPosition(2.5), getPosition(0), HTBackgroundType.INPUT)
        widgets.addSlot(input(1), getPosition(2.5), getPosition(1), HTBackgroundType.INPUT)
        widgets.addSlot(input(2), getPosition(2.5), getPosition(2), HTBackgroundType.INPUT)

        widgets.addTank(input(3), getPosition(0), HTBackgroundType.EXTRA_INPUT)
        widgets.addTank(input(4), getPosition(1.25), HTBackgroundType.EXTRA_INPUT)
        // outputs
        widgets.addSlot(output(0), getPosition(5.5), getPosition(1), HTBackgroundType.OUTPUT)
        widgets.addTank(output(1), getPosition(7), HTBackgroundType.EXTRA_OUTPUT)
    }
}
