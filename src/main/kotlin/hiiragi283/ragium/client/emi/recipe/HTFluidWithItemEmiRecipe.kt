package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.emi.RagiumEmiTextures
import hiiragi283.ragium.common.recipe.base.HTFluidWithItemRecipe
import net.minecraft.world.item.crafting.RecipeHolder

abstract class HTFluidWithItemEmiRecipe<RECIPE : HTFluidWithItemRecipe>(
    private val icon: String,
    category: HTEmiRecipeCategory,
    holder: RecipeHolder<RECIPE>,
) : HTProcessingEmiRecipe<RECIPE>(category, holder) {
    init {
        addInput(recipe.fluidIngredient)

        addOutputs(recipe.result)
    }

    final override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(time = recipe.time)
        RagiumEmiTextures.addWidget(
            widgets,
            icon,
            getPosition(2),
            getPosition(1.5),
            recipe.time,
            endToStart = true,
        )
        // input
        widgets.addTank(input(0), getPosition(0.5), HTBackgroundType.INPUT)
        // catalyst
        setupItemInput(widgets)
        // output
        widgets.add2x2Slots()
    }

    protected abstract fun setupItemInput(widgets: WidgetHolder)
}
