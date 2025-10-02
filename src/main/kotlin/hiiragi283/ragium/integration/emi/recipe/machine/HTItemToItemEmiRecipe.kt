package hiiragi283.ragium.integration.emi.recipe.machine

import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.recipe.manager.HTRecipeHolder
import hiiragi283.ragium.impl.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.integration.emi.addArrow
import hiiragi283.ragium.integration.emi.recipe.HTEmiHolderRecipe
import net.minecraft.resources.ResourceLocation

/**
 * @see [mekanism.client.recipe_viewer.emi.recipe.ItemStackToItemStackEmiRecipe]
 */
class HTItemToItemEmiRecipe : HTEmiHolderRecipe<HTItemToItemRecipe> {
    constructor(category: HTEmiRecipeCategory, id: ResourceLocation, recipe: HTItemToItemRecipe) : super(category, id, recipe)

    constructor(category: HTEmiRecipeCategory, holder: HTRecipeHolder<HTItemToItemRecipe>) : super(category, holder)

    init {
        addInput(recipe.ingredient)
        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addSlot(EmiStack.EMPTY, getPosition(1), getPosition(2))

        widgets.addOutput(0, getPosition(4.5), getPosition(1), true)
    }
}
