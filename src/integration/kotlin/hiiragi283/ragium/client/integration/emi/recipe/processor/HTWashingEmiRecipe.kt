package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.recipe.base.HTChancedOutputsEmiRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicItemWithFluidToChancedItemRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTWashingEmiRecipe(category: HTEmiRecipeCategory, holder: RecipeHolder<HTBasicItemWithFluidToChancedItemRecipe>) :
    HTChancedOutputsEmiRecipe<HTBasicItemWithFluidToChancedItemRecipe>(category, holder) {
    init {
        addInput(recipe.ingredient)
        addInput(recipe.fluidIngredient)

        recipe.results.forEach(::addChancedOutputs)
    }

    override fun initInputSlots(widgets: WidgetHolder) {
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addSlot(input(1), getPosition(1), getPosition(2))
    }
}
