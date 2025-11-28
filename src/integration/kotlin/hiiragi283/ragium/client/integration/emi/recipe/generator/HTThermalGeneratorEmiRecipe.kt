package hiiragi283.ragium.client.integration.emi.recipe.generator

import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.data.HTEmiFluidFuelData
import hiiragi283.ragium.client.integration.emi.recipe.base.HTFuelGeneratorEmiRecipe
import net.minecraft.resources.ResourceLocation

class HTThermalGeneratorEmiRecipe(category: HTEmiRecipeCategory, id: ResourceLocation, recipe: HTEmiFluidFuelData) :
    HTFuelGeneratorEmiRecipe(category, id, recipe) {
    override fun addFuelWidgets(widgets: WidgetHolder) {
        // Fuel Slot
        val input: EmiIngredient = input(0)
        widgets.addSlot(input, getPosition(1), getPosition(0))
        widgets.addTexture(EmiTexture.EMPTY_FLAME, getPosition(1) + 2, getPosition(1) + 2)
        // Remainder Slot
        widgets.addSlot(
            input.emiStacks
                .map(EmiStack::getRemainder)
                .filterNot(EmiStack::isEmpty)
                .let(EmiIngredient::of),
            getPosition(1),
            getPosition(2),
        )
    }
}
