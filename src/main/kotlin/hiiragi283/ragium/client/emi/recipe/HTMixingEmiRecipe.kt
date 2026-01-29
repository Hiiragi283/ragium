package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.RagiumEmiTextures
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.jvm.optionals.getOrNull

class HTMixingEmiRecipe(holder: RecipeHolder<HTMixingRecipe>) :
    HTProcessingEmiRecipe<HTMixingRecipe>(RagiumEmiRecipeCategories.MIXING, holder) {
    init {
        addInput(recipe.itemIngredient.getOrNull())
        recipe.fluidIngredients.forEach(::addInput)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(time = recipe.time)
        RagiumEmiTextures.addWidget(
            widgets,
            "mix",
            getPosition(1.5),
            getPosition(1),
            recipe.time,
            endToStart = true,
        )
        // inputs
        widgets.addSlot(input(0), getPosition(1.5), getPosition(0), HTBackgroundType.INPUT)
        widgets.addSlot(input(1), getPosition(1), getPosition(2), HTBackgroundType.EXTRA_INPUT)
        widgets.addSlot(input(2), getPosition(2), getPosition(2), HTBackgroundType.EXTRA_INPUT)
        // outputs
        widgets.addSlot(output(0), getPosition(5.5), getPosition(1), HTBackgroundType.OUTPUT)
        widgets.addTank(output(1), getPosition(7), HTBackgroundType.OUTPUT)
    }
}
