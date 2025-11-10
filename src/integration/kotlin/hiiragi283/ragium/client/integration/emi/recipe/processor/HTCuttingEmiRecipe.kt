package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.RagiumEmiPlugin
import hiiragi283.ragium.client.integration.emi.recipe.base.HTMultiOutputEmiRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.SingleItemRecipe

class HTCuttingEmiRecipe(category: HTEmiRecipeCategory, holder: RecipeHolder<SingleItemRecipe>) :
    HTMultiOutputEmiRecipe<SingleItemRecipe>(category, holder) {
    init {
        addInput(HTItemIngredient.of(recipe.ingredients[0]))
        addOutputs(HTResultHelper.item(recipe.getResultItem(RagiumEmiPlugin.registryAccess)))
    }

    override fun initInputSlots(widgets: WidgetHolder) {
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addSlot(output(0).copy().setAmount(1), getPosition(1), getPosition(2)).catalyst(true)
    }
}
