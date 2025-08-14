package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.EmiPort
import dev.emi.emi.EmiRenderHelper
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

/**
 * @see [dev.emi.emi.recipe.EmiFuelRecipe]
 */
class HTFluidFuelEmiRecipe(private val id: ResourceLocation, val fluid: EmiIngredient, val time: Int) : HTEmiRecipe {
    override fun getCategory(): EmiRecipeCategory = RagiumEmiCategories.FLUID_FUEL

    override fun getId(): ResourceLocation = id

    override fun getInputs(): List<EmiIngredient> = listOf(fluid)

    override fun getOutputs(): List<EmiStack> = listOf()

    override fun getDisplayWidth(): Int = 144

    override fun getDisplayHeight(): Int = 18

    override fun supportsRecipeTree(): Boolean = false

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(EmiTexture.EMPTY_FLAME, 1, 1)
        widgets.addAnimatedTexture(EmiTexture.FULL_FLAME, 1, 1, 1000 * time / 20, false, true, true)
        widgets.addSlot(fluid, 18, 0).recipeContext(this)
        widgets.addText(
            EmiPort.translatable(
                "emi.fuel_time.items",
                EmiRenderHelper.TEXT_FORMAT.format((time / 200f).toDouble()),
            ),
            38,
            5,
            -1,
            true,
        )
    }
}
