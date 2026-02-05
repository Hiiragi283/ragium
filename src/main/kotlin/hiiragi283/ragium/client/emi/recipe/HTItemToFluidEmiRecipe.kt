package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder

class HTItemToFluidEmiRecipe(category: HTEmiRecipeCategory, holder: RecipeHolder<HTMeltingRecipe>) :
    HTProcessingEmiRecipe<HTMeltingRecipe>(category, holder) {
    companion object {
        @JvmStatic
        fun fermenting(id: ResourceLocation, recipe: HTMeltingRecipe): HTItemToFluidEmiRecipe =
            HTItemToFluidEmiRecipe(RagiumEmiRecipeCategories.FERMENTING, RecipeHolder(id, recipe))

        @JvmStatic
        fun melting(holder: RecipeHolder<HTMeltingRecipe>): HTItemToFluidEmiRecipe =
            HTItemToFluidEmiRecipe(RagiumEmiRecipeCategories.MELTING, holder)
    }

    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(time = recipe.time)
        widgets.addBurning(getPosition(1.5), getPosition(1.5), recipe.time)
        // input
        widgets.addSlot(input(0), getPosition(1.5), getPosition(0.5), HTBackgroundType.INPUT)
        // output
        widgets.addTank(output(0), getPosition(5), HTBackgroundType.OUTPUT)
    }
}
