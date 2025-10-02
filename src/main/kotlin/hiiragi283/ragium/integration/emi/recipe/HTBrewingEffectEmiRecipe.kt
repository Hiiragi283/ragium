package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.integration.emi.addArrow
import hiiragi283.ragium.integration.emi.data.HTEmiBrewingEffect
import net.minecraft.resources.ResourceLocation

class HTBrewingEffectEmiRecipe(category: HTEmiRecipeCategory, id: ResourceLocation, recipe: HTEmiBrewingEffect) :
    HTEmiRecipe<HTEmiBrewingEffect>(category, id, recipe) {
    init {
        addInput(recipe.input.let(EmiStack::of))

        addOutputs(recipe.effect.toPotion().let(EmiStack::of))
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addSlot(input(0), getPosition(0), getPosition(0))
        widgets.addArrow(getPosition(1.5), getPosition(0))
        widgets.addOutput(0, getPosition(3), getPosition(0), drawBack = true)
    }
}
