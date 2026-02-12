package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.recipe.base.HTChemicalEmiRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.base.HTChemicalResult
import net.minecraft.world.item.crafting.RecipeHolder

class HTMixingEmiRecipe(holder: RecipeHolder<HTMixingRecipe>) :
    HTChemicalEmiRecipe<HTMixingRecipe>(
        HTMixingRecipe.MAX_ITEM_INPUT,
        HTMixingRecipe.MAX_FLUID_INPUT,
        RagiumEmiRecipeCategories.MIXING,
        holder,
    ) {
    override fun addOutputs() {
        val results: HTChemicalResult = recipe.results
        val items: List<HTItemResult> = results.getLeft() ?: listOf()
        for (i: Int in (0..<HTMixingRecipe.MAX_ITEM_OUTPUT)) {
            addOutputs(items.getOrNull(i))
        }
        val fluids: List<HTFluidResult> = results.getRight() ?: listOf()
        for (i: Int in (0..<HTMixingRecipe.MAX_FLUID_OUTPUT)) {
            addOutputs(fluids.getOrNull(i))
        }
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(time = recipe.time, x = getPosition(3.5))
        // inputs
        widgets.addSlot(input(0), getPosition(0), getPosition(0.5), HTBackgroundType.INPUT)
        widgets.addSlot(input(1), getPosition(1), getPosition(0.5), HTBackgroundType.INPUT)
        widgets.addSlot(input(2), getPosition(2), getPosition(0.5), HTBackgroundType.INPUT)

        widgets.addSlot(input(3), getPosition(0), getPosition(2), HTBackgroundType.INPUT)
        widgets.addSlot(input(4), getPosition(1), getPosition(2), HTBackgroundType.INPUT)
        widgets.addSlot(input(5), getPosition(2), getPosition(2), HTBackgroundType.INPUT)
        // outputs
        widgets.addSlot(output(0), getPosition(5), getPosition(0.5), HTBackgroundType.OUTPUT)

        widgets.addSlot(output(1), getPosition(5), getPosition(2), HTBackgroundType.OUTPUT)
        widgets.addSlot(output(2), getPosition(6), getPosition(2), HTBackgroundType.EXTRA_OUTPUT)
    }
}
