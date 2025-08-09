package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTItemToItemEmiRecipe(
    id: ResourceLocation,
    textureId: ResourceLocation,
    private val category: EmiRecipeCategory,
    val ingredient: EmiIngredient,
    val result: EmiStack,
) : HTMachineEmiRecipe(
        id,
        textureId,
    ) {
    companion object {
        @JvmStatic
        fun compressing(id: ResourceLocation, ingredient: EmiIngredient, result: EmiStack): HTItemToItemEmiRecipe = HTItemToItemEmiRecipe(
            id,
            RagiumAPI.id("textures/gui/container/compressor.png"),
            RagiumEmiCategories.COMPRESSING,
            ingredient,
            result,
        )

        @JvmStatic
        fun extracting(id: ResourceLocation, ingredient: EmiIngredient, result: EmiStack): HTItemToItemEmiRecipe = HTItemToItemEmiRecipe(
            id,
            RagiumAPI.id("textures/gui/container/extractor.png"),
            RagiumEmiCategories.EXTRACTING,
            ingredient,
            result,
        )
    }

    override fun getCategory(): EmiRecipeCategory = category

    override fun getInputs(): List<EmiIngredient> = listOf(ingredient)

    override fun getOutputs(): List<EmiStack> = listOf(result)

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        // Input
        widgets.addSlot(ingredient, getPosition(1), getPosition(0)).drawBack(false)
        // Output
        widgets.addOutput(result, getPosition(4.5), getPosition(1), true)
    }
}
