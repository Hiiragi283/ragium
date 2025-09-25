package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTBrewingEffectEmiRecipe(private val id: ResourceLocation, private val input: EmiIngredient, private val output: EmiStack) :
    HTEmiRecipe {
    override fun getCategory(): EmiRecipeCategory = RagiumEmiCategories.BREWING_EFFECT

    override fun getId(): ResourceLocation = id

    override fun getInputs(): List<EmiIngredient> = listOf(input)

    override fun getOutputs(): List<EmiStack> = listOf(output)

    override fun getDisplayWidth(): Int = getPosition(4)

    override fun getDisplayHeight(): Int = getPosition(1)

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addSlot(input, getPosition(0), getPosition(0))
        widgets.addArrow(getPosition(1.5), getPosition(0))
        widgets.addOutput(output, getPosition(3), getPosition(0), drawBack = true)
    }
}
