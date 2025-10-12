package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipe
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.recipe.base.HTMultiOutputEmiRecipe
import hiiragi283.ragium.client.integration.emi.toTagEmi
import hiiragi283.ragium.impl.recipe.HTCrushingRecipe
import hiiragi283.ragium.impl.recipe.HTPulverizingRecipe
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.world.item.crafting.RecipeHolder

class HTCrushingEmiRecipe(category: HTEmiRecipeCategory, holder: RecipeHolder<HTItemToChancedItemRecipe>) :
    HTMultiOutputEmiRecipe<HTItemToChancedItemRecipe>(category, holder) {
    init {
        if (recipe is HTCrushingRecipe) {
            addInput(recipe.ingredient)

            recipe.results.forEach(::addChancedOutputs)
        } else if (recipe is HTPulverizingRecipe) {
            addInput(recipe.ingredient)

            addOutputs(recipe.result)
        }
        addCatalyst(RagiumFluidContents.LUBRICANT.toTagEmi())
    }

    override fun initInputSlots(widgets: WidgetHolder) {
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addSlot(catalyst(0), getPosition(1), getPosition(2)).catalyst(true)
    }
}
