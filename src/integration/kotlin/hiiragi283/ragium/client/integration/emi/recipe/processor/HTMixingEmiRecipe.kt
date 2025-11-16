package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.recipe.base.HTMultiOutputsEmiRecipe
import hiiragi283.ragium.impl.recipe.HTMixingRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.jvm.optionals.getOrNull

class HTMixingEmiRecipe(category: HTEmiRecipeCategory, holder: RecipeHolder<HTMixingRecipe>) :
    HTMultiOutputsEmiRecipe<HTMixingRecipe>(category, holder) {
    init {
        val range: IntRange = 0..<2
        range.map(recipe.itemIngredients::getOrNull).forEach(::addInput)
        range.map(recipe.fluidIngredients::getOrNull).forEach(::addInput)

        addOutputs(recipe.itemResult.getOrNull())
        addOutputs(recipe.fluidResult.getOrNull())
    }

    override fun initInputSlots(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(3), getPosition(1))

        // Input
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addSlot(input(1), getPosition(2), getPosition(0))

        widgets.addSlot(input(2), getPosition(1), getPosition(2))
        widgets.addSlot(input(3), getPosition(2), getPosition(2))
    }
}
