package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.integration.emi.addTank
import hiiragi283.core.api.integration.emi.toEmi
import hiiragi283.core.api.integration.emi.toFluidEmi
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.recipe.HTChancedRecipe
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.world.item.crafting.RecipeHolder
import org.apache.commons.lang3.math.Fraction

abstract class HTChancedEmiRecipe<RECIPE : HTChancedRecipe>(
    backgroundTex: String,
    category: HTEmiRecipeCategory,
    holder: RecipeHolder<RECIPE>,
) : HTProcessingEmiRecipe<RECIPE>(backgroundTex, category, holder) {
    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.result)
        for ((result: HTItemResult, chance: Fraction) in recipe.extraResults) {
            addOutputs(result.toEmi().setChance(chance.toFloat()))
        }
    }

    final override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)

        // Input
        widgets.addTank(RagiumFluids.LUBRICANT.toFluidEmi(1000), getPosition(0.5), getPosition(0)).catalyst(true)
        widgets.addInput(0, getPosition(2), getPosition(0.5))

        // Output
        addOutputSlots(widgets)
    }

    protected abstract fun addOutputSlots(widgets: WidgetHolder)
}
