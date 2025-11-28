package hiiragi283.ragium.client.integration.emi.recipe.generator

import dev.emi.emi.EmiPort
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.data.map.HTFluidCoolantData
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.registry.getHolderDataMap
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.addTank
import hiiragi283.ragium.client.integration.emi.data.HTEmiFluidFuelData
import hiiragi283.ragium.client.integration.emi.recipe.base.HTFuelGeneratorEmiRecipe
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.Fluid

class HTCombustionGeneratorEmiRecipe(category: HTEmiRecipeCategory, id: ResourceLocation, recipe: HTEmiFluidFuelData) :
    HTFuelGeneratorEmiRecipe(category, id, recipe) {
    companion object {
        @JvmStatic
        val COOLANT: EmiIngredient by lazy {
            EmiPort.getFluidRegistry()
                .getHolderDataMap(RagiumDataMaps.COOLANT)
                .mapNotNull { (holder: Holder.Reference<Fluid>, data: HTFluidCoolantData) ->
                    EmiStack.of(holder.value(), data.amount.toLong())
                }.let(EmiIngredient::of)
        }
    }
    
    override fun addFuelWidgets(widgets: WidgetHolder) {
        // Coolant Tank
        widgets.addTank(COOLANT, getPosition(1), getPosition(0))
        // Fuel Tank
        widgets.addTank(input(0), getPosition(5), getPosition(0))
    }
}
