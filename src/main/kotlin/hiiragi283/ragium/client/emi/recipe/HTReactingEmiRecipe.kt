package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTReactingRecipe
import hiiragi283.ragium.common.recipe.base.HTChemicalResult
import net.minecraft.world.item.crafting.RecipeHolder

class HTReactingEmiRecipe(holder: RecipeHolder<HTReactingRecipe>) :
    HTChemicalEmiRecipe<HTReactingRecipe>(
        HTReactingRecipe.MAX_ITEM_INPUT,
        HTReactingRecipe.MAX_FLUID_INPUT,
        RagiumEmiRecipeCategories.REACTING,
        holder,
    ) {
    override fun addOutputs() {
        addCatalyst(recipe.catalyst)

        val results: HTChemicalResult = recipe.results
        val items: List<HTItemResult> = results.getLeft() ?: listOf()
        for (i: Int in (0..<HTReactingRecipe.MAX_ITEM_OUTPUT)) {
            addOutputs(items.getOrNull(i))
        }
        val fluids: List<HTFluidResult> = results.getRight() ?: listOf()
        for (i: Int in (0..<HTReactingRecipe.MAX_FLUID_OUTPUT)) {
            addOutputs(fluids.getOrNull(i))
        }
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(time = recipe.time, x = getPosition(3.5))
        // inputs
        widgets.addSlot(input(0), getPosition(1), getPosition(0.5), HTBackgroundType.INPUT)
        widgets.addSlot(input(1), getPosition(2), getPosition(0.5), HTBackgroundType.EXTRA_INPUT)

        widgets.addSlot(input(2), getPosition(0), getPosition(1.5), HTBackgroundType.INPUT)
        widgets.addSlot(input(3), getPosition(1), getPosition(1.5), HTBackgroundType.EXTRA_INPUT)
        widgets.addSlot(input(4), getPosition(2), getPosition(1.5), HTBackgroundType.EXTRA_INPUT)

        widgets.addSlot(catalyst(0), getPosition(3.5), getPosition(0), HTBackgroundType.NONE)
        // outputs
        widgets.addSlot(output(0), getPosition(5), getPosition(0.5), HTBackgroundType.OUTPUT)
        widgets.addSlot(output(1), getPosition(6), getPosition(0.5), HTBackgroundType.EXTRA_OUTPUT)

        widgets.addSlot(output(2), getPosition(5), getPosition(1.5), HTBackgroundType.OUTPUT)
        widgets.addSlot(output(3), getPosition(6), getPosition(1.5), HTBackgroundType.EXTRA_OUTPUT)
        widgets.addSlot(output(4), getPosition(7), getPosition(1.5), HTBackgroundType.EXTRA_OUTPUT)
    }
}
