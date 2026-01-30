package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.RagiumEmiTextures
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTPressingEmiRecipe(holder: RecipeHolder<HTPressingRecipe>) :
    HTProcessingEmiRecipe<HTPressingRecipe>(RagiumEmiRecipeCategories.PRESSING, holder) {
    init {
        addInput(recipe.ingredient)

        addCatalyst(recipe.catalyst)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(time = recipe.time)
        RagiumEmiTextures.addWidget(
            widgets,
            "press",
            getPosition(2),
            getPosition(1),
            recipe.time,
            endToStart = false,
        )
        // input
        widgets.addSlot(input(0), getPosition(2), getPosition(0), HTBackgroundType.INPUT)
        // catalyst
        widgets.addSlot(catalyst(0), getPosition(2), getPosition(2), HTBackgroundType.NONE)
        // output
        widgets.add2x2Slots()
    }
}
