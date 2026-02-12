package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.recipe.base.HTProcessingEmiRecipe
import hiiragi283.ragium.common.recipe.HTDistillingRecipe
import hiiragi283.ragium.common.recipe.base.HTChemicalResult
import net.minecraft.world.item.crafting.RecipeHolder

class HTDistillingEmiRecipe(holder: RecipeHolder<HTDistillingRecipe>) :
    HTProcessingEmiRecipe<HTDistillingRecipe>(RagiumEmiRecipeCategories.DISTILLING, holder) {
    init {
        addInput(recipe.ingredient)

        val results: HTChemicalResult = recipe.results
        val items: List<HTItemResult> = results.getLeft() ?: listOf()
        for (i: Int in (0..<HTDistillingRecipe.MAX_ITEM_OUTPUT)) {
            addOutputs(items.getOrNull(i))
        }
        val fluids: List<HTFluidResult> = results.getRight() ?: listOf()
        for (i: Int in (0..<HTDistillingRecipe.MAX_FLUID_OUTPUT)) {
            addOutputs(fluids.getOrNull(i))
        }
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(time = recipe.time)
        // input
        widgets.addTank(input(0), getPosition(1.5), HTBackgroundType.INPUT)
        // outputs
        widgets.addSlot(output(0), getPosition(5.5), getPosition(4), HTBackgroundType.EXTRA_OUTPUT)
        val maxFluids: Int = HTDistillingRecipe.MAX_FLUID_OUTPUT
        for (i: Int in (0..<maxFluids)) {
            widgets.addSlot(output(i + 1), getPosition(7), getPosition(maxFluids - i), HTBackgroundType.OUTPUT)
        }
    }
}
