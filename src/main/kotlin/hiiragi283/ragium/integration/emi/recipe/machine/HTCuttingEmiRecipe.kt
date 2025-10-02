package hiiragi283.ragium.integration.emi.recipe.machine

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.recipe.manager.HTRecipeHolder
import hiiragi283.ragium.impl.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.integration.emi.addArrow
import hiiragi283.ragium.integration.emi.recipe.HTEmiHolderRecipe
import net.minecraft.resources.ResourceLocation

class HTCuttingEmiRecipe : HTEmiHolderRecipe<HTItemToItemRecipe> {
    constructor(
        category: HTEmiRecipeCategory,
        holder: HTRecipeHolder<HTItemToItemRecipe>,
    ) : super(category, holder)

    constructor(
        category: HTEmiRecipeCategory,
        id: ResourceLocation,
        recipe: HTItemToItemRecipe,
    ) : super(category, id, recipe)

    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        // Input
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addSlot(output(0).copy().setAmount(1), getPosition(1), getPosition(2)).catalyst(true)
        // Output
        widgets.addOutput(0, getPosition(4), getPosition(0.5))
        widgets.addOutput(1, getPosition(5), getPosition(0.5))
        widgets.addOutput(2, getPosition(4), getPosition(1.5))
        widgets.addOutput(3, getPosition(5), getPosition(1.5))
    }
}
