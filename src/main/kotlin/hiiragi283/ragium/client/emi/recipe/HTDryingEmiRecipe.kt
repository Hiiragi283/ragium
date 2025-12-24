package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.HTEmiHolderRecipe
import hiiragi283.core.api.integration.emi.addArrow
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTDryingRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.jvm.optionals.getOrNull

class HTDryingEmiRecipe(holder: RecipeHolder<HTDryingRecipe>) :
    HTEmiHolderRecipe<HTDryingRecipe>(RagiumEmiRecipeCategories.DRYING, holder) {
    init {
        addInput(recipe.ingredient.left().getOrNull())
        addInput(recipe.ingredient.right().getOrNull())

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1), recipe.time)

        // Input
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addSlot(input(1), getPosition(1), getPosition(2))
        widgets.addTexture(EmiTexture.EMPTY_FLAME, getPosition(1) + 2, getPosition(1) + 2)

        // Output
        widgets.addOutput(0, getPosition(4.5), getPosition(0) + 4, true)
        widgets.addOutput(1, getPosition(4.5), getPosition(2))
    }
}
