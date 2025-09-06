package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

class HTFluidTransformingEmiRecipe(
    id: ResourceLocation,
    val fluidIngredient: EmiIngredient,
    val itemIngredient: EmiIngredient,
    val itemResult: EmiStack,
    val fluidResult: EmiStack,
) : HTMachineEmiRecipe(id, RagiumAPI.id("textures/gui/container/refinery.png")) {
    init {
        for (stack: EmiStack in itemIngredient.emiStacks) {
            val stackIn: ItemStack = stack.itemStack
            if (stackIn.hasCraftingRemainingItem()) {
                stack.remainder = EmiStack.of(stackIn.craftingRemainingItem)
                break
            }
        }
    }

    override fun getCategory(): EmiRecipeCategory = RagiumEmiCategories.FLUID_TRANSFORM

    override fun getInputs(): List<EmiIngredient> = listOf(fluidIngredient)

    override fun getCatalysts(): List<EmiIngredient> = listOf(itemIngredient)

    override fun getOutputs(): List<EmiStack> = listOf(itemResult, fluidResult)

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        // Input
        widgets
            .addTank(
                fluidIngredient,
                getPosition(1),
                getPosition(0),
                RagiumConfig.CONFIG.refineryInputTankCapacity.asInt,
            ).drawBack(false)
        widgets.addSlot(itemIngredient, getPosition(2.5), getPosition(0)).drawBack(false)
        // Output
        widgets.addSlot(itemResult, getPosition(3.5), getPosition(2)).drawBack(false)
        widgets
            .addTank(
                fluidResult,
                getPosition(5),
                getPosition(0),
                RagiumConfig.CONFIG.refineryOutputTankCapacity.asInt,
            ).drawBack(false)
            .recipeContext(this)
    }

    override val arrowPosX: Int = getPosition(3)
}
