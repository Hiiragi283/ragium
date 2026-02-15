package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.RagiumEmiTextures
import hiiragi283.ragium.client.emi.recipe.base.HTItemToChancedEmiRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTCrushingEmiRecipe(holder: RecipeHolder<HTCrushingRecipe>) :
    HTItemToChancedEmiRecipe<HTCrushingRecipe>(RagiumEmiRecipeCategories.CRUSHING, holder) {
    override fun addOutputSlots(widgets: WidgetHolder) {
        RagiumEmiTextures.addWidget(
            widgets,
            "hammer",
            getPosition(1.5),
            getPosition(1.5),
            recipe.time,
            endToStart = true,
        )
        // outputs
        widgets.addTripleOutputs()
    }
}
