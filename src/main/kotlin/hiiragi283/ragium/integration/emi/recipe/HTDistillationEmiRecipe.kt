package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation
import java.util.*
import kotlin.jvm.optionals.getOrNull

class HTDistillationEmiRecipe(
    private val id: ResourceLocation,
    val ingredient: EmiIngredient,
    val itemResult: Optional<EmiStack>,
    val fluidResults: List<EmiStack>,
) : HTEmiRecipe {
    override fun getCategory(): EmiRecipeCategory = RagiumEmiCategories.DISTILLATION

    override fun getId(): ResourceLocation = id

    override fun getInputs(): List<EmiIngredient> = listOf(ingredient)

    override fun getOutputs(): List<EmiStack> = buildList {
        itemResult.ifPresent(::add)
        addAll(fluidResults)
    }

    override fun getDisplayWidth(): Int = getPosition(7)

    override fun getDisplayHeight(): Int = getPosition(1)

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(0))
        // Input
        widgets.addInput(ingredient, getPosition(1), getPosition(0))
        // Outputs
        widgets.addOutput(itemResult.getOrNull(), getPosition(4), getPosition(0))
        widgets.addOutput(fluidResults.getOrNull(0), getPosition(5), getPosition(0))
        widgets.addOutput(fluidResults.getOrNull(1), getPosition(6), getPosition(0))
    }
}
