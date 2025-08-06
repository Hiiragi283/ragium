package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation

class HTItemWithItemToItemEmiRecipe(
    id: ResourceLocation,
    textureId: ResourceLocation,
    private val category: EmiRecipeCategory,
    private val slotPos: DoubleArray,
    val ingredients: List<EmiIngredient>,
    val result: EmiStack,
) : HTMachineEmiRecipe(
        id,
        textureId,
    ) {
    companion object {
        @JvmStatic
        fun alloying(id: ResourceLocation, ingredients: List<EmiIngredient>, result: EmiStack): HTItemWithItemToItemEmiRecipe =
            HTItemWithItemToItemEmiRecipe(
                id,
                RagiumAPI.id("textures/gui/container/alloy_smelter.png"),
                RagiumEmiCategories.ALLOYING,
                doubleArrayOf(0.5, 0.0, 1.5, 0.0),
                ingredients,
                result,
            )

        @JvmStatic
        fun pressing(
            id: ResourceLocation,
            ingredient: EmiIngredient,
            catalyst: EmiIngredient,
            result: EmiStack,
        ): HTItemWithItemToItemEmiRecipe = HTItemWithItemToItemEmiRecipe(
            id,
            RagiumAPI.id("textures/gui/container/forming_press.png"),
            RagiumEmiCategories.PRESSING,
            doubleArrayOf(1.0, 0.0, 1.0, 2.0),
            listOf(ingredient, catalyst),
            result,
        )
    }

    override fun getCategory(): EmiRecipeCategory = category

    override fun getInputs(): List<EmiIngredient> = ingredients

    override fun getOutputs(): List<EmiStack> = listOf(result)

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        // Input
        widgets.addSlot(ingredients[0], getPosition(slotPos[0]), getPosition(slotPos[1])).drawBack(false)
        widgets.addSlot(ingredients[1], getPosition(slotPos[2]), getPosition(slotPos[3])).drawBack(false)
        // Output
        widgets.addOutput(result, getPosition(4.5), getPosition(1), true)
    }
}
