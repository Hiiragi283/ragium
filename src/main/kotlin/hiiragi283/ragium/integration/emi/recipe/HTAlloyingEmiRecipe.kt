package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTSlotPos
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTAlloyingEmiRecipe(id: ResourceLocation, val ingredients: List<EmiIngredient>, val results: List<EmiStack>) : HTEmiRecipe.Base(id) {
    override fun getCategory(): EmiRecipeCategory = RagiumEmiCategories.ALLOYING

    override fun getInputs(): List<EmiIngredient> = ingredients

    override fun getOutputs(): List<EmiStack> = results

    override fun getDisplayWidth(): Int = getPosition(7)

    override fun getDisplayHeight(): Int = getPosition(3)

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(
            EmiTexture(
                RagiumAPI.id("textures/gui/container/alloy_smelter.png"),
                HTSlotPos.getSlotPosX(1) - 1,
                HTSlotPos.getSlotPosY(0) - 1,
                getPosition(6),
                getPosition(3),
            ),
            0,
            0,
        )
        widgets.addArrow(2.5, 1.0)
        // Input
        widgets.addInput(ingredients[0], 0.5, 0.0).drawBack(false)
        widgets.addInput(ingredients[1], 1.5, 0.0).drawBack(false)

        // Output
        widgets.addOutput(0, 4.0, 0.5)
        widgets.addOutput(1, 5.0, 0.5)
        widgets.addOutput(2, 4.0, 1.5)
        widgets.addOutput(3, 5.0, 1.5)
    }
}
