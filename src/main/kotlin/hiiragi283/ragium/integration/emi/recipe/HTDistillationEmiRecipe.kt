package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation
import java.util.Optional

class HTDistillationEmiRecipe(
    id: ResourceLocation,
    val ingredient: EmiIngredient,
    val itemResult: Optional<EmiStack>,
    val fluidResults: List<EmiStack>,
) : HTMachineEmiRecipe(id, RagiumAPI.id("textures/gui/container/distillation_tower.png")) {
    override fun getCategory(): EmiRecipeCategory = RagiumEmiCategories.DISTILLATION

    override fun getInputs(): List<EmiIngredient> = listOf(ingredient)

    override fun getOutputs(): List<EmiStack> = buildList {
        itemResult.ifPresent(::add)
        addAll(fluidResults)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        // Input
        widgets.addInput(ingredient, 1.0, 1.0).drawBack(false)
        // Output
        widgets.addOutput(fluidResults.getOrNull(1) ?: EmiStack.EMPTY, getPosition(4.5), getPosition(0))
        widgets.addOutput(fluidResults[0], getPosition(4.5), getPosition(1))
        widgets.addOutput(itemResult.orElse(EmiStack.EMPTY), getPosition(4.5), getPosition(2))
    }
}
