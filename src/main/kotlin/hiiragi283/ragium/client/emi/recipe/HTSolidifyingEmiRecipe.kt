package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.RagiumEmiTextures
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import hiiragi283.ragium.config.RagiumFluidConfigType
import net.minecraft.world.item.crafting.RecipeHolder

class HTSolidifyingEmiRecipe(holder: RecipeHolder<HTSolidifyingRecipe>) :
    HTProcessingEmiRecipe<HTSolidifyingRecipe>(RagiumConst.SOLIDIFIER, RagiumEmiRecipeCategories.SOLIDIFYING, holder) {
    init {
        addInput(recipe.ingredient)

        addCatalyst(recipe.catalyst)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        RagiumEmiTextures.addWidget(
            widgets,
            "solidify",
            getPosition(2),
            getPosition(1.5),
            recipe.time,
            endToStart = true,
        )

        // Input
        widgets.addTank(input(0), getPosition(0.5), getCapacity(RagiumFluidConfigType.FIRST_INPUT))
        widgets.addCatalyst(0, getPosition(2), getPosition(0.5))
        // Output
        widgets.addOutput(0, getPosition(5.5), getPosition(0.5), true)
    }
}
