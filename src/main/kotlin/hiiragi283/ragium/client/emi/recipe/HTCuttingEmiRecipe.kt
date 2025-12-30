package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTCuttingEmiRecipe(holder: RecipeHolder<HTCuttingRecipe>) :
    HTChancedEmiRecipe<HTCuttingRecipe>(RagiumConst.CUTTING_MACHINE, RagiumEmiRecipeCategories.CUTTING, holder) {
    override fun addOutputSlots(widgets: WidgetHolder) {
        widgets.addOutput(0, getPosition(5.5), getPosition(0.5), true)
        widgets.addOutput(1, getPosition(5.5), getPosition(2))
    }
}
