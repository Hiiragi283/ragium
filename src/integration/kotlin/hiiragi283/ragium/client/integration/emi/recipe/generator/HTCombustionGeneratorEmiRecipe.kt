package hiiragi283.ragium.client.integration.emi.recipe.generator

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.addTank
import hiiragi283.ragium.client.integration.emi.data.HTEmiFluidFuelData
import hiiragi283.ragium.client.integration.emi.recipe.base.HTFuelGeneratorEmiRecipe
import net.minecraft.resources.ResourceLocation

class HTCombustionGeneratorEmiRecipe(category: HTEmiRecipeCategory, id: ResourceLocation, recipe: HTEmiFluidFuelData) :
    HTFuelGeneratorEmiRecipe(category, id, recipe) {
    override fun addFuelWidgets(widgets: WidgetHolder) {
        // Fuel Tank
        widgets.addTank(input(0), getPosition(5), getPosition(0))
    }
}
