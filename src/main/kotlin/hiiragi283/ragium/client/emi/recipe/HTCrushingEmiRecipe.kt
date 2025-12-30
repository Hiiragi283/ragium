package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTCrushingEmiRecipe(holder: RecipeHolder<HTCrushingRecipe>) :
    HTChancedEmiRecipe<HTCrushingRecipe>(RagiumConst.CRUSHER, RagiumEmiRecipeCategories.CRUSHING, holder) {
    override fun addOutputSlots(widgets: WidgetHolder) {
        widgets.addOutput(0, getPosition(5.5), getPosition(0.5))
        widgets.addOutput(1, getPosition(6.5), getPosition(0.5))
        widgets.addOutput(2, getPosition(5.5), getPosition(1.5))
        widgets.addOutput(3, getPosition(6.5), getPosition(1.5))
    }
}
