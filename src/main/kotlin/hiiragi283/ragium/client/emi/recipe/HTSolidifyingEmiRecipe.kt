package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.emi.HTEmiHolderRecipe
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.RagiumEmiTextures
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTSolidifyingEmiRecipe(holder: RecipeHolder<HTSolidifyingRecipe>) :
    HTEmiHolderRecipe<HTSolidifyingRecipe>(RagiumEmiRecipeCategories.SOLIDIFYING, holder) {
    init {
        addInput(recipe.ingredient)

        addCatalyst(recipe.catalyst)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(3.5), getPosition(1), recipe.time)
        RagiumEmiTextures.addWidget(
            widgets,
            "solidify",
            getPosition(2),
            getPosition(1.5),
            recipe.time,
            endToStart = true,
        )
        // input
        widgets.addTank(input(0), getPosition(0.5), HTBackgroundType.INPUT)
        // catalyst
        widgets.addSlot(catalyst(0), getPosition(2), getPosition(0.5), HTBackgroundType.NONE)
        // output
        widgets.addSlot(output(0), getPosition(5.5), getPosition(0.5), HTBackgroundType.OUTPUT)
    }
}
