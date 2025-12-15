package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.category.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiHolderRecipe
import hiiragi283.ragium.common.recipe.HTBasicAlloyingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTAlloyingEmiRecipe(holder: RecipeHolder<HTBasicAlloyingRecipe>) :
    HTEmiHolderRecipe<HTBasicAlloyingRecipe>(RagiumEmiRecipeCategories.ALLOYING, holder) {
    init {
        recipe.ingredients.forEach(::addInput)

        addOutputs(recipe.result)
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
        widgets.addOutput(0, getPosition(4.5), getPosition(1), true)
    }
}
