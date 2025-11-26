package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.recipe.base.HTMultiOutputsEmiRecipe
import hiiragi283.ragium.impl.recipe.HTMixingRecipe
import hiiragi283.ragium.impl.recipe.HTSimpleMixingRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicComplexRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTMixingEmiRecipe(category: HTEmiRecipeCategory, holder: RecipeHolder<HTBasicComplexRecipe>) :
    HTMultiOutputsEmiRecipe<HTBasicComplexRecipe>(category, holder) {
    override fun initInputs() {
        when (recipe) {
            is HTMixingRecipe -> {
                (0..<4).map(recipe.itemIngredients::getOrNull).forEach(::addInput)
                (0..<2).map(recipe.fluidIngredients::getOrNull).forEach(::addInput)
            }

            is HTSimpleMixingRecipe -> {
                addInput(recipe.itemIngredient)
                addEmptyInput()
                addEmptyInput()
                addEmptyInput()
                addInput(recipe.fluidIngredient)
                addEmptyInput()
            }
        }
    }

    override fun initInputSlots(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(3), getPosition(1))

        // Input
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addSlot(input(1), getPosition(2), getPosition(0))
        widgets.addSlot(input(2), getPosition(1), getPosition(1))
        widgets.addSlot(input(3), getPosition(2), getPosition(1))

        widgets.addSlot(input(4), getPosition(1), getPosition(2))
        widgets.addSlot(input(5), getPosition(2), getPosition(2))
    }
}
