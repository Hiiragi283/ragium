package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.RagiumEmiTextures
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTCuttingEmiRecipe(holder: RecipeHolder<HTCuttingRecipe>) :
    HTChancedEmiRecipe<HTCuttingRecipe>(RagiumEmiRecipeCategories.CUTTING, holder) {
    override fun addOutputSlots(widgets: WidgetHolder) {
        RagiumEmiTextures.addWidget(
            widgets,
            "saw",
            getPosition(2),
            getPosition(1.5),
            recipe.time,
            endToStart = true,
        )
        // outputs
        widgets.addSlot(output(0), getPosition(5.5), getPosition(0.5), HTBackgroundType.OUTPUT)
        widgets.addSlot(output(1), getPosition(5.5), getPosition(2), HTBackgroundType.EXTRA_OUTPUT)
    }
}
