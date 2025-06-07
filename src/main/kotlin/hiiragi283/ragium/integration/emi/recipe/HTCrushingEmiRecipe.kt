package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import dev.emi.emi.screen.tooltip.EmiTooltip
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTCrushingEmiRecipe(private val id: ResourceLocation, val ingredient: EmiIngredient, val results: Map<EmiStack, Float>) :
    HTEmiRecipe {
    override fun getCategory(): EmiRecipeCategory = RagiumEmiCategories.CRUSHING

    override fun getId(): ResourceLocation = id

    override fun getInputs(): List<EmiIngredient> = listOf(ingredient)

    override fun getOutputs(): List<EmiStack> = results.keys.toList()

    override fun getDisplayWidth(): Int = getPosition(7)

    override fun getDisplayHeight(): Int = getPosition(3)

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(2.5, 1.0)
        // Input
        widgets.addInput(ingredient, 1.0, 0.0)
        widgets.addInput(EmiStack.EMPTY, 1.0, 2.0)

        // Output
        fun addOutput(index: Int, x: Double, y: Double) {
            val resultList: List<Pair<EmiStack, Float>> = results.toList()
            val (result: EmiStack, chance: Float) = resultList.getOrNull(index) ?: run {
                widgets.addOutput(EmiStack.EMPTY, x, y)
                return
            }
            widgets.addOutput(result, x, y).appendTooltip { _: EmiIngredient ->
                EmiTooltip.chance("produce", chance)
            }
        }

        addOutput(0, 4.0, 0.5)
        addOutput(1, 5.0, 0.5)
        addOutput(2, 4.0, 1.5)
        addOutput(3, 5.0, 1.5)
    }
}
