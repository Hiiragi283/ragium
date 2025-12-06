package hiiragi283.ragium.client.integration.emi.recipe.base

import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.addPlus
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiHolderRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicCombineRecipe
import net.minecraft.world.item.crafting.RecipeHolder

abstract class HTCombineEmiRecipe<RECIPE : HTBasicCombineRecipe>(category: HTEmiRecipeCategory, holder: RecipeHolder<RECIPE>) :
    HTEmiHolderRecipe<RECIPE>(category, holder) {
    init {
        val (left: HTItemIngredient, right: HTItemIngredient) = recipe.itemIngredients
        addInput(left)
        addInput(right)
        addInput(getFluidIngredient(recipe))

        addOutputs(getResult(recipe))
    }

    protected abstract fun getFluidIngredient(recipe: RECIPE): EmiIngredient

    protected abstract fun getResult(recipe: RECIPE): EmiStack

    final override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))
        widgets.addPlus(getPosition(1), getPosition(0))

        // inputs
        widgets.addSlot(input(0), getPosition(0), getPosition(0))
        widgets.addSlot(input(1), getPosition(2), getPosition(0))

        widgets.addTexture(EmiTexture.EMPTY_FLAME, getPosition(1) + 2, getPosition(1) + 2)
        widgets.addSlot(input(2), getPosition(1), getPosition(2))
        // output
        widgets.addOutput(0, getPosition(4.5), getPosition(1), true)
    }
}
