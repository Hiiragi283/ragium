package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.recipe.base.HTProcessingEmiRecipe
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.base.HTItemOrFluidRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTItemOrFluidEmiRecipe<RECIPE : HTItemOrFluidRecipe>(category: HTEmiRecipeCategory, holder: RecipeHolder<RECIPE>) :
    HTProcessingEmiRecipe<RECIPE>(category, holder) {
    companion object {
        @JvmStatic
        fun freezing(holder: RecipeHolder<HTFreezingRecipe>): HTItemOrFluidEmiRecipe<HTFreezingRecipe> =
            HTItemOrFluidEmiRecipe(RagiumEmiRecipeCategories.FREEZING, holder)

        @JvmStatic
        fun melting(holder: RecipeHolder<HTMeltingRecipe>): HTItemOrFluidEmiRecipe<HTMeltingRecipe> =
            HTItemOrFluidEmiRecipe(RagiumEmiRecipeCategories.MELTING, holder)
    }

    init {
        addInput(recipe.ingredient.getLeft())
        addInput(recipe.ingredient.getRight())

        addOutputs(recipe.result.getLeft())
        addOutputs(recipe.result.getRight())
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(time = recipe.time)
        // inputs
        widgets.addSlot(input(0), getPosition(1.5), getPosition(0.5), HTBackgroundType.INPUT)
        widgets.addTank(input(1), getPosition(0), HTBackgroundType.INPUT)
        // outputs
        widgets.addSlot(output(0), getPosition(5), getPosition(1), HTBackgroundType.OUTPUT)
        widgets.addTank(output(1), getPosition(6.5), HTBackgroundType.OUTPUT)
    }
}
