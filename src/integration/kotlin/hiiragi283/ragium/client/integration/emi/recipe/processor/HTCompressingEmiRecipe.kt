package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiHolderRecipe
import hiiragi283.ragium.impl.recipe.HTCompressingRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.jvm.optionals.getOrNull

class HTCompressingEmiRecipe(category: HTEmiRecipeCategory, holder: RecipeHolder<HTCompressingRecipe>) :
    HTEmiHolderRecipe<HTCompressingRecipe>(category, holder) {
    init {
        addInput(recipe.required)
        addCatalyst(recipe.optional.getOrNull())
        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        // Input
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addSlot(catalyst(0), getPosition(1), getPosition(2)).catalyst(true)
        // Output
        widgets.addOutput(0, getPosition(4.5), getPosition(1), true)
    }
}
