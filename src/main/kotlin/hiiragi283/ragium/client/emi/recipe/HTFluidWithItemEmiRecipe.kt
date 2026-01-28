package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.RagiumEmiTextures
import hiiragi283.ragium.common.recipe.HTBathingRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import hiiragi283.ragium.common.recipe.base.HTFluidWithItemRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTFluidWithItemEmiRecipe<RECIPE : HTFluidWithItemRecipe>(
    category: HTEmiRecipeCategory,
    holder: RecipeHolder<RECIPE>,
    catalystItem: Boolean,
) : HTProcessingEmiRecipe<RECIPE>(category, holder) {
    companion object {
        @JvmStatic
        fun bathing(holder: RecipeHolder<HTBathingRecipe>): HTFluidWithItemEmiRecipe<HTBathingRecipe> =
            HTFluidWithItemEmiRecipe(RagiumEmiRecipeCategories.BATHING, holder, false)

        @JvmStatic
        fun solidifying(holder: RecipeHolder<HTSolidifyingRecipe>): HTFluidWithItemEmiRecipe<HTSolidifyingRecipe> =
            HTFluidWithItemEmiRecipe(RagiumEmiRecipeCategories.SOLIDIFYING, holder, true)
    }

    init {
        addInput(recipe.fluidIngredient)

        if (catalystItem) {
            addCatalyst(recipe.itemIngredient)
        } else {
            addInput(recipe.itemIngredient)
        }

        addOutputs(recipe.result)
    }

    private val itemGetter: (Int) -> EmiIngredient = when (catalystItem) {
        true -> ::catalyst
        false -> ::input
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(time = recipe.time)
        RagiumEmiTextures.addWidget(
            widgets,
            "solidify",
            getPosition(2),
            getPosition(1.5),
            recipe.time,
            endToStart = true,
        )
        // input
        widgets.addTank(input(0), getPosition(0.5), HTBackgroundType.INPUT)
        // catalyst
        widgets.addSlot(itemGetter(0), getPosition(2), getPosition(0.5), HTBackgroundType.NONE)
        // output
        widgets.add2x2Slots()
    }
}
