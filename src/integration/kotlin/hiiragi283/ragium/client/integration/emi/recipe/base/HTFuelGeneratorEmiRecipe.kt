package hiiragi283.ragium.client.integration.emi.recipe.base

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.RagiumEmiTextures
import hiiragi283.ragium.client.integration.emi.data.HTEmiFluidFuelData
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiRecipe
import net.minecraft.resources.ResourceLocation

abstract class HTFuelGeneratorEmiRecipe(category: HTEmiRecipeCategory, id: ResourceLocation, recipe: HTEmiFluidFuelData) :
    HTEmiRecipe<HTEmiFluidFuelData>(category, id, recipe) {
    init {
        addInput(recipe.input)
    }

    final override fun addWidgets(widgets: WidgetHolder) {
        addFuelWidgets(widgets)
        // Energy Tank
        widgets
            .addTexture(
                RagiumEmiTextures.ENERGY_GAUGE,
                getPosition(3),
                getPosition(0) + 1,
            ).tooltipText(listOf(RagiumTranslation.TICK.translate(recipe.time)))
    }

    protected abstract fun addFuelWidgets(widgets: WidgetHolder)
}
