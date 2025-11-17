package hiiragi283.ragium.client.integration.emi.recipe.generator

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.RagiumEmiTextures
import hiiragi283.ragium.client.integration.emi.addTank
import hiiragi283.ragium.client.integration.emi.data.HTEmiFluidFuelData
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiRecipe
import net.minecraft.resources.ResourceLocation

class HTFuelGeneratorEmiRecipe(category: HTEmiRecipeCategory, id: ResourceLocation, recipe: HTEmiFluidFuelData) :
    HTEmiRecipe<HTEmiFluidFuelData>(category, id, recipe) {
    init {
        addInput(recipe.itemInput)
        addInput(recipe.fluidInput)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        // item fuel slot
        widgets.addSlot(input(0), getPosition(1), getPosition(1))
        // fluid tank
        widgets.addTank(input(1), getPosition(3), getPosition(0))
        // energy tank
        val amountPerTick: Int = recipe.fuelData.amount
        val energyRate: Int = recipe.energyRate
        widgets
            .addTexture(
                RagiumEmiTextures.ENERGY_GAUGE,
                getPosition(5),
                getPosition(0) + 1,
            ).tooltipText(
                listOf(
                    RagiumTranslation.BURN_TIME.translate(input(1).amount / amountPerTick.toFloat()),
                    RagiumTranslation.PER_MB.translate(RagiumTranslation.STORED_FE.translate(energyRate)),
                ),
            )
    }
}
