package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTMixingEmiRecipe(
    id: ResourceLocation,
    val fluidIngredient: EmiIngredient,
    val itemIngredient: EmiIngredient,
    val result: EmiStack,
) : HTMachineEmiRecipe(id, RagiumAPI.id("textures/gui/container/mixer.png")) {
    override fun getCategory(): EmiRecipeCategory = RagiumEmiCategories.MIXING

    override fun getInputs(): List<EmiIngredient> = listOf(fluidIngredient, itemIngredient)

    override fun getOutputs(): List<EmiStack> = listOf(result)

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        // Input
        widgets.addTank(fluidIngredient, getPosition(0.5), getPosition(0)).drawBack(false)
        widgets.addSlot(itemIngredient, getPosition(2), getPosition(1)).drawBack(false)
        // Output
        widgets.addTank(result, getPosition(5.5), getPosition(0)).drawBack(false).recipeContext(this)
    }

    override val arrowPosX: Int = getPosition(3.5)
}
