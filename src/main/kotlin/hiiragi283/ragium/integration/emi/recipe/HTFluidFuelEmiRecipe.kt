package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

/**
 * @see [dev.emi.emi.recipe.EmiFuelRecipe]
 */
class HTFluidFuelEmiRecipe(
    private val category: EmiRecipeCategory,
    private val id: ResourceLocation,
    private val fluid: EmiStack,
    private val amount: Int,
    private val energyRate: Int,
) : HTEmiRecipe {
    override fun getCategory(): EmiRecipeCategory = category

    override fun getId(): ResourceLocation = id

    override fun getInputs(): List<EmiIngredient> = listOf(fluid)

    override fun getOutputs(): List<EmiStack> = listOf()

    override fun getDisplayWidth(): Int = getPosition(5)

    override fun getDisplayHeight(): Int = getPosition(1)

    override fun supportsRecipeTree(): Boolean = false

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(EmiTexture.EMPTY_FLAME, 1, 1)
        widgets.addAnimatedTexture(EmiTexture.FULL_FLAME, 1, 1, 2000, false, true, true)
        widgets.addSlot(fluid, getPosition(1), 0).recipeContext(this)
        widgets.addText(
            Component.literal("${energyRate / amount.toFloat()} FE/t"),
            getPosition(2) + 2,
            5,
            -1,
            true,
        )
    }
}
