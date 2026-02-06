package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.emi.HTEmiHolderRecipe
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTAssemblingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTAssemblingEmiRecipe(holder: RecipeHolder<HTAssemblingRecipe>) :
    HTEmiHolderRecipe<HTAssemblingRecipe>(RagiumEmiRecipeCategories.ASSEMBLING, holder) {
    init {
        addInput(recipe.fluidIngredient)
        recipe.itemIngredients.forEach(::addInput)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(time = recipe.time, x = getPosition(4.5))
        // inputs
        widgets.addTank(input(0), getPosition(0), HTBackgroundType.EXTRA_INPUT)
        for (i: Int in (0..<HTAssemblingRecipe.MAX_ITEM_INPUTS)) {
            widgets.addSlot(input(i + 1), getPosition(i % 3 + 1), getPosition(i / 3), HTBackgroundType.INPUT)
        }
        // output
        widgets.addSlot(output(0), getPosition(6.5), getPosition(1), HTBackgroundType.OUTPUT)
    }
}
