package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.category.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiHolderRecipe
import hiiragi283.ragium.client.integration.emi.toEmi
import hiiragi283.ragium.common.recipe.HTBasicCompressingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTCompressingEmiRecipe(holder: RecipeHolder<HTBasicCompressingRecipe>) :
    HTEmiHolderRecipe<HTBasicCompressingRecipe>(RagiumEmiRecipeCategories.COMPRESSING, holder) {
    init {
        addInput(recipe.ingredient)
        addCatalyst(recipe.mold.toEmi())

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        // Input
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addCatalyst(0, getPosition(1), getPosition(2))
        // output
        widgets.addOutput(0, getPosition(4.5), getPosition(1), true)
    }
}
