package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items

class HTCauldronDroppingEmiRecipe(
    private val id: ResourceLocation,
    private val fluid: EmiIngredient,
    private val minLevel: Int,
    private val ingredient: EmiIngredient,
    private val result: EmiStack,
) : HTEmiRecipe {
    override fun getCategory(): EmiRecipeCategory = RagiumEmiCategories.CAULDRON_DROPPING

    override fun getId(): ResourceLocation = id

    override fun getInputs(): List<EmiIngredient> = listOf(fluid, ingredient)

    override fun getOutputs(): List<EmiStack> = listOf(result)

    override fun getDisplayWidth(): Int = getPosition(6)

    override fun getDisplayHeight(): Int = getPosition(2)

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(1.0, 1.0)
        widgets.addArrow(3.5, 1.0)
        // Ingredient
        widgets.addInput(ingredient, 0.0, 1.0)
        // Fluid
        widgets
            .addInput(fluid, 2.5, 0.0)
            .appendTooltip(
                Component.translatable(RagiumTranslationKeys.EMI_CAULDRON_DROPPING_MIN_LEVEL, minLevel),
            )
        widgets.addInput(EmiStack.of(Items.CAULDRON), 2.5, 1.0).catalyst(true).drawBack(false)
        // Result
        widgets.addOutput(result, 5.0, 1.0)
    }
}
