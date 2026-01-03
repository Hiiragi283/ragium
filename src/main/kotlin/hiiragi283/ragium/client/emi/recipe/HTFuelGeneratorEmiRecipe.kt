package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.HTEmiRecipe
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.ragium.client.emi.data.HTEmiFluidFuelData
import net.minecraft.resources.ResourceLocation

/**
 * @see dev.emi.emi.recipe.EmiFuelRecipe
 */
open class HTFuelGeneratorEmiRecipe(category: HTEmiRecipeCategory, id: ResourceLocation, recipe: HTEmiFluidFuelData) :
    HTEmiRecipe<HTEmiFluidFuelData>(category, id, recipe) {
    init {
        addInput(recipe.input)
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
            HTCommonTranslation.TICK.translate(recipe.time),
            38,
            5,
            -1,
            true,
        )

        // input
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
    }
}
