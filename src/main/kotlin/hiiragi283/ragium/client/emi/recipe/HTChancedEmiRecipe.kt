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
        addInputs()

        addOutputs(recipe.result)
        for ((result: HTItemResult, chance: Fraction) in recipe.extraResults) {
            addOutputs(result.toEmi().setChance(chance.toFloat()))
        }
    }

    protected abstract fun addInputs()

    protected fun WidgetHolder.addTripleOutputs(x: Int = getPosition(5)) {
        this.addSlot(output(0), x, getPosition(1), HTBackgroundType.OUTPUT)

        this.addSlot(output(1), x + getPosition(1.5), getPosition(0), HTBackgroundType.EXTRA_OUTPUT)
        this.addSlot(output(2), x + getPosition(1.5), getPosition(1), HTBackgroundType.EXTRA_OUTPUT)
        this.addSlot(output(3), x + getPosition(1.5), getPosition(2), HTBackgroundType.EXTRA_OUTPUT)
    }
}
