package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.integration.emi.toFluidEmi
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTExtraProcessingRecipe
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.jvm.optionals.getOrNull

class HTExtraProcessingEmiRecipe<RECIPE : HTExtraProcessingRecipe>(category: HTEmiRecipeCategory, holder: RecipeHolder<RECIPE>) :
    HTProcessingEmiRecipe<RECIPE>(category, holder) {
    companion object {
        @JvmStatic
        fun crushing(holder: RecipeHolder<HTCrushingRecipe>): HTExtraProcessingEmiRecipe<HTCrushingRecipe> =
            HTExtraProcessingEmiRecipe(RagiumEmiRecipeCategories.CRUSHING, holder)
    }

    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.result)
        addOutputs(recipe.extra.getOrNull())
    }

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)

        // Input
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addSlot(RagiumFluids.LUBRICANT.toFluidEmi(), getPosition(1), getPosition(2))

        // Output
        widgets.addOutput(0, getPosition(4.5), getPosition(0) + 4, true)
        widgets.addOutput(1, getPosition(4.5), getPosition(2))
    }
}
