package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTMeltingEmiRecipe(id: ResourceLocation, val ingredient: EmiIngredient, val result: EmiStack) :
    HTMachineEmiRecipe(id, RagiumAPI.id("textures/gui/container/melter.png")) {
    override fun getCategory(): EmiRecipeCategory = RagiumEmiCategories.MELTING

    override fun getInputs(): List<EmiIngredient> = listOf(ingredient)

    override fun getOutputs(): List<EmiStack> = listOf(result)

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        // Input
        widgets.addSlot(ingredient, getPosition(1), getPosition(0.5)).drawBack(false)
        // Output
        widgets
            .addTank(
                result,
                getPosition(4.5),
                getPosition(0),
                18,
                18 * 3,
                RagiumAPI.getConfig().getDefaultTankCapacity(),
            ).drawBack(false)
            .recipeContext(this)
    }
}
