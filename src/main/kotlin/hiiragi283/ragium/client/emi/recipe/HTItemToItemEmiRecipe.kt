package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.recipe.base.HTProcessingEmiRecipe
import hiiragi283.ragium.common.recipe.HTBendingRecipe
import hiiragi283.ragium.common.recipe.HTCompressingRecipe
import hiiragi283.ragium.common.recipe.HTLathingRecipe
import hiiragi283.ragium.common.recipe.base.HTSingleProcessingRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTItemToItemEmiRecipe<RECIPE : HTSingleProcessingRecipe.ItemToItem>(category: HTEmiRecipeCategory, holder: RecipeHolder<RECIPE>) :
    HTProcessingEmiRecipe<RECIPE>(category, holder) {
    companion object {
        @JvmStatic
        fun bending(holder: RecipeHolder<HTBendingRecipe>): HTItemToItemEmiRecipe<HTBendingRecipe> =
            HTItemToItemEmiRecipe(RagiumEmiRecipeCategories.BENDING, holder)

        @JvmStatic
        fun compressing(holder: RecipeHolder<HTCompressingRecipe>): HTItemToItemEmiRecipe<HTCompressingRecipe> =
            HTItemToItemEmiRecipe(RagiumEmiRecipeCategories.COMPRESSING, holder)

        @JvmStatic
        fun lathing(holder: RecipeHolder<HTLathingRecipe>): HTItemToItemEmiRecipe<HTLathingRecipe> =
            HTItemToItemEmiRecipe(RagiumEmiRecipeCategories.LATHING, holder)
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
        widgets.addSlot(output(0), getPosition(5), getPosition(1), HTBackgroundType.OUTPUT)
    }
}
