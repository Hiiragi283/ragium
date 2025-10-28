package hiiragi283.ragium.client.integration.emi.recipe.generator

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.gui.component.HTEnergyBatteryWidget
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.addTank
import hiiragi283.ragium.client.integration.emi.addWidget
import hiiragi283.ragium.client.integration.emi.data.HTEmiFluidFuelData
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiRecipe
import hiiragi283.ragium.common.storage.energy.HTEnergyNetwork
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
        val energyRate: Int = recipe.energyRate
        HTEnergyBatteryWidget(
            HTEnergyNetwork(energyRate, energyRate),
            getPosition(5),
            getPosition(0) + 1,
        ).let(widgets::addWidget)
    }
}
