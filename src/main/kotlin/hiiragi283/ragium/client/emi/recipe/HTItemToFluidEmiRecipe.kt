package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.recipe.base.HTProcessingEmiRecipe
import hiiragi283.ragium.common.recipe.HTSqueezingRecipe
import hiiragi283.ragium.common.recipe.base.HTSingleProcessingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTItemToFluidEmiRecipe<RECIPE : HTSingleProcessingRecipe.ItemToFluid>(category: HTEmiRecipeCategory, holder: RecipeHolder<RECIPE>) :
    HTProcessingEmiRecipe<RECIPE>(category, holder) {
    companion object {
        @JvmStatic
        fun squeezing(holder: RecipeHolder<HTSqueezingRecipe>): HTItemToFluidEmiRecipe<HTSqueezingRecipe> =
            HTItemToFluidEmiRecipe(RagiumEmiRecipeCategories.SQUEEZING, holder)
    }

    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(time = recipe.time)
        // input
        widgets.addSlot(input(0), getPosition(1.5), getPosition(0.5), HTBackgroundType.INPUT)
        // output
        widgets.addTank(output(0), getPosition(5), HTBackgroundType.OUTPUT)
    }
}
