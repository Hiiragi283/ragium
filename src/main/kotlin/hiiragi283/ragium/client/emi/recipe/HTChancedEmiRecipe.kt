package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.integration.emi.toEmi
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.recipe.base.HTChancedRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import org.apache.commons.lang3.math.Fraction

abstract class HTChancedEmiRecipe<RECIPE : HTChancedRecipe>(category: HTEmiRecipeCategory, holder: RecipeHolder<RECIPE>) :
    HTProcessingEmiRecipe<RECIPE>(category, holder) {
    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.result)
        for ((result: HTItemResult, chance: Fraction) in recipe.extraResults) {
            addOutputs(result.toEmi().setChance(chance.toFloat()))
        }
    }

    final override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(time = recipe.time)
        // input
        widgets.addSlot(input(0), getPosition(2), getPosition(0.5), HTBackgroundType.INPUT)
        // outputs
        addOutputSlots(widgets)
    }

    protected abstract fun addOutputSlots(widgets: WidgetHolder)
}
