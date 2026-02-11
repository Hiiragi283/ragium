package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.recipe.base.HTFluidWithItemEmiRecipe
import hiiragi283.ragium.common.recipe.HTBathingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTBathingEmiRecipe(holder: RecipeHolder<HTBathingRecipe>) :
    HTFluidWithItemEmiRecipe<HTBathingRecipe>("mix", RagiumEmiRecipeCategories.BATHING, holder) {
    init {
        addInput(recipe.fluidIngredient)
        addInput(recipe.itemIngredient)

        addOutputs(recipe.result)
    }

    override fun setupItemInput(widgets: WidgetHolder) {
        widgets.addSlot(input(1), getPosition(1.5), getPosition(0.5), HTBackgroundType.INPUT)
    }
}
