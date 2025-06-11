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

class HTCrushingEmiRecipe(private val id: ResourceLocation, val ingredient: EmiIngredient, val results: List<EmiStack>) : HTEmiRecipe {
    override fun getCategory(): EmiRecipeCategory = RagiumEmiCategories.CRUSHING

    override fun getId(): ResourceLocation = id

    override fun getInputs(): List<EmiIngredient> = listOf(ingredient)

    override fun getOutputs(): List<EmiStack> = results

    override fun getDisplayWidth(): Int = getPosition(7)

    override fun getDisplayHeight(): Int = getPosition(3)

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(
            EmiTexture(
                RagiumAPI.id("textures/gui/container/crusher.png"),
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
        widgets.addInput(ingredient, 1.0, 0.0).drawBack(false)
        widgets.addInput(EmiStack.EMPTY, 1.0, 2.0).drawBack(false)

        // Output
        fun addOutput(index: Int, x: Double, y: Double) {
            widgets.addOutput(results.getOrNull(index) ?: EmiStack.EMPTY, x, y).drawBack(false)
        }

        addOutput(0, 4.0, 0.5)
        addOutput(1, 5.0, 0.5)
        addOutput(2, 4.0, 1.5)
        addOutput(3, 5.0, 1.5)
    }
}
