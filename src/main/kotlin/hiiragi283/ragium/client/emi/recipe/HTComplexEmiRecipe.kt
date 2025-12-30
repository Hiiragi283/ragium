package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.integration.emi.addBurning
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTComplexRecipe
import hiiragi283.ragium.common.recipe.HTDryingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.jvm.optionals.getOrNull

class HTComplexEmiRecipe<RECIPE : HTComplexRecipe>(category: HTEmiRecipeCategory, holder: RecipeHolder<RECIPE>) :
    HTProcessingEmiRecipe<RECIPE>(category, holder) {
    companion object {
        @JvmStatic
        fun drying(holder: RecipeHolder<HTDryingRecipe>): HTComplexEmiRecipe<HTDryingRecipe> =
            HTComplexEmiRecipe(RagiumEmiRecipeCategories.DRYING, holder)

        @JvmStatic
        fun pyrolyzing(holder: RecipeHolder<HTPyrolyzingRecipe>): HTComplexEmiRecipe<HTPyrolyzingRecipe> =
            HTComplexEmiRecipe(RagiumEmiRecipeCategories.PYROLYZING, holder)
    }

    init {
        addInput(recipe.ingredient.left().getOrNull())
        addInput(recipe.ingredient.right().getOrNull())

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        widgets.addBurning(getPosition(1), getPosition(1), recipe.time)

        // Input
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addSlot(input(1), getPosition(1), getPosition(2))

        // Output
        widgets.addOutput(0, getPosition(4.5), getPosition(0) + 4, true)
        widgets.addOutput(1, getPosition(4.5), getPosition(2))
    }
}
