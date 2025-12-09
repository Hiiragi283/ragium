package hiiragi283.ragium.client.integration.emi.recipe.generator

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.category.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.integration.emi.data.HTBiomassRecipeData
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiRecipe
import net.minecraft.resources.ResourceLocation

class HTBiomassEmiRecipe(id: ResourceLocation, recipe: HTBiomassRecipeData) :
    HTEmiRecipe<HTBiomassRecipeData>(RagiumEmiRecipeCategories.BIOMASS, id, recipe) {
    init {
        addInput(recipe.input)

        addOutputs(recipe.output)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2), getPosition(0))

        // input
        widgets.addSlot(input(0), getPosition(0), getPosition(0))
        // output
        widgets.addOutput(0, getPosition(4), getPosition(0))
    }
}
