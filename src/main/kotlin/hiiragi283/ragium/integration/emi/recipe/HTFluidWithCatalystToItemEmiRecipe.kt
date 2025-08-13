package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTFluidWithCatalystToItemEmiRecipe(
    private val category: EmiRecipeCategory,
    id: ResourceLocation,
    textureId: ResourceLocation,
    val fluidIngredient: EmiIngredient,
    val itemIngredient: EmiIngredient,
    val result: EmiStack,
) : HTMachineEmiRecipe(id, textureId) {
    companion object {
        @JvmStatic
        fun solidifying(
            id: ResourceLocation,
            fluidIngredient: EmiIngredient,
            itemIngredient: EmiIngredient,
            results: EmiStack,
        ): HTFluidWithCatalystToItemEmiRecipe = HTFluidWithCatalystToItemEmiRecipe(
            RagiumEmiCategories.SOLIDIFYING,
            id,
            RagiumAPI.id("textures/gui/container/solidifier.png"),
            fluidIngredient,
            itemIngredient,
            results,
        )
    }

    override fun getCategory(): EmiRecipeCategory = category

    override fun getInputs(): List<EmiIngredient> = listOf(fluidIngredient)

    override fun getCatalysts(): List<EmiIngredient> = listOf(itemIngredient)

    override fun getOutputs(): List<EmiStack> = listOf(result)

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        // Input
        widgets.addTank(fluidIngredient, getPosition(0.5), getPosition(0)).drawBack(false)
        widgets.addSlot(itemIngredient, getPosition(2), getPosition(1)).drawBack(false).catalyst(true)
        // Output
        widgets.addOutput(result, getPosition(5.5), getPosition(1), true)
    }

    override val arrowPosX: Int = getPosition(3.5)
}
