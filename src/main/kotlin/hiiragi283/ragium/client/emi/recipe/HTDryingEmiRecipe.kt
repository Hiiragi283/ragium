package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTDryingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTDryingEmiRecipe(holder: RecipeHolder<HTDryingRecipe>) :
    HTComplexEmiRecipe<HTDryingRecipe>(RagiumEmiRecipeCategories.DRYING, holder) {
    override fun addSubProgress(widgets: WidgetHolder) {
        widgets.addBurning(getPosition(2), getPosition(1.5), recipe.time)
    }
}
