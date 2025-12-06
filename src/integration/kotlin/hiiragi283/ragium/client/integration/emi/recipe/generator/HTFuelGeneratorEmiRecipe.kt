package hiiragi283.ragium.client.integration.emi.recipe.generator

import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.client.integration.emi.category.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.data.HTEmiFluidFuelData
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiRecipe
import hiiragi283.ragium.client.integration.emi.toEmi
import net.minecraft.resources.ResourceLocation

/**
 * @see dev.emi.emi.recipe.EmiFuelRecipe
 */
open class HTFuelGeneratorEmiRecipe(category: HTEmiRecipeCategory, id: ResourceLocation, recipe: HTEmiFluidFuelData) :
    HTEmiRecipe<HTEmiFluidFuelData>(category, id, recipe) {
    init {
        val input: EmiStack = recipe.input
        input.remainder = input.itemStack.craftingRemainingItem.toEmi()
        addInput(input)
    }

    final override fun addWidgets(widgets: WidgetHolder) {
        val time: Int = recipe.time
        if (time in (1..<Int.MAX_VALUE)) {
            widgets.addTexture(EmiTexture.EMPTY_FLAME, 1, 1)
            widgets.addAnimatedTexture(EmiTexture.FULL_FLAME, 1, 1, 1000 * time / 20, false, true, true)
        } else {
            widgets.addTexture(EmiTexture.FULL_FLAME, 1, 1)
        }

        widgets.addText(
            RagiumTranslation.TICK.translate(recipe.time),
            38,
            5,
            -1,
            true,
        )

        // input
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
    }
}
