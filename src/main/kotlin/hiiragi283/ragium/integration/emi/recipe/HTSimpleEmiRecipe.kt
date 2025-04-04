package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.resources.ResourceLocation

interface HTSimpleEmiRecipe : HTEmiRecipe {
    override fun getDisplayWidth(): Int = getPosition(3.5)

    override fun getDisplayHeight(): Int = getPosition(1)

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addInput(firstInput, 0.0, 0.0)
        widgets.addArrow(1.0, 0.0)
        widgets.addOutput(firstOutput, 2.5, 0.0)
    }

    val firstInput: EmiIngredient
    val firstOutput: EmiStack

    abstract class Base(
        private val category: EmiRecipeCategory,
        private val id: ResourceLocation,
        override val firstInput: EmiIngredient,
        override val firstOutput: EmiStack,
    ) : HTSimpleEmiRecipe {
        final override fun getCategory(): EmiRecipeCategory = category

        final override fun getId(): ResourceLocation = id

        final override fun getInputs(): List<EmiIngredient> = listOf(firstInput)

        final override fun getOutputs(): List<EmiStack> = listOf(firstOutput)
    }
}
