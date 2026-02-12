package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.recipe.base.HTItemAndFluidEmiRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTSolidifyingEmiRecipe(holder: RecipeHolder<HTSolidifyingRecipe>) :
    HTItemAndFluidEmiRecipe<HTSolidifyingRecipe>("solidify", RagiumEmiRecipeCategories.SOLIDIFYING, holder) {
    init {
        addInput(recipe.ingredient)

        addCatalyst(recipe.catalyst)

        addOutputs(recipe.result)
    }

    override fun setupItemInput(widgets: WidgetHolder) {
        widgets.addSlot(catalyst(0), getPosition(1.5), getPosition(0.5), HTBackgroundType.NONE)
    }
}
