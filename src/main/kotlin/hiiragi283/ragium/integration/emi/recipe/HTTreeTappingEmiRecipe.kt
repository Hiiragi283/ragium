package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items

class HTTreeTappingEmiRecipe(private val id: ResourceLocation, private val ingredient: EmiIngredient, private val output: EmiStack) :
    HTEmiRecipe {
    override fun getCategory(): EmiRecipeCategory = RagiumEmiCategories.TREE_TAPPING

    override fun getId(): ResourceLocation = id

    override fun getInputs(): List<EmiIngredient> = listOf(ingredient)

    override fun getOutputs(): List<EmiStack> = listOf(output)

    override fun getDisplayWidth(): Int = getPosition(4.5)

    override fun getDisplayHeight(): Int = getPosition(3)

    override fun addWidgets(widgets: WidgetHolder) {
        // Block
        widgets.addInput(ingredient, 0.0, 0.0)
        widgets.addInput(ingredient, 0.0, 1.0)
        widgets.addInput(ingredient, 0.0, 2.0)
        // Tree Tap
        widgets.addInput(EmiStack.of(RagiumBlocks.TREE_TAP), 1.0, 1.0)
        // Cauldron
        widgets.addInput(EmiStack.of(Items.CAULDRON), 1.0, 2.0)
        // Output
        widgets.addArrow(2.0, 2.0)
        widgets.addOutput(output, 3.5, 2.0)
    }
}
