package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.HTEmiHolderRecipe
import hiiragi283.core.api.integration.emi.addArrow
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.jvm.optionals.getOrNull

class HTAlloyingEmiRecipe(holder: RecipeHolder<HTAlloyingRecipe>) :
    HTEmiHolderRecipe<HTAlloyingRecipe>(RagiumEmiRecipeCategories.ALLOYING, holder) {
    init {
        recipe.ingredients.forEach(::addInput)

        addOutputs(recipe.result)
        addOutputs(recipe.extra.getOrNull())
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))
        widgets.setShapeless()

        // Input
        for (i: Int in (0..2)) {
            widgets.addSlot(input(i), getPosition(i), getPosition(0.0))
        }
        widgets.addTexture(EmiTexture.EMPTY_FLAME, getPosition(1) + 2, getPosition(1) + 2)

        // Output
        widgets.addOutput(0, getPosition(4.5), getPosition(0.5), true)
        widgets.addOutput(1, getPosition(4.5), getPosition(2))
    }
}
