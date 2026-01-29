package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTSolidifyingEmiRecipe(holder: RecipeHolder<HTSolidifyingRecipe>) :
    HTFluidWithItemEmiRecipe<HTSolidifyingRecipe>("solidify", RagiumEmiRecipeCategories.SOLIDIFYING, holder) {
    init {
        addCatalyst(recipe.itemIngredient)
    }

    override fun setupItemInput(widgets: WidgetHolder) {
        widgets.addSlot(catalyst(0), getPosition(2), getPosition(0.5), HTBackgroundType.NONE)
    }
}
