package hiiragi283.ragium.integration.emi.recipe.generator

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.gui.component.HTEnergyBatteryWidget
import hiiragi283.ragium.common.storage.energy.HTEnergyNetwork
import hiiragi283.ragium.integration.emi.addTank
import hiiragi283.ragium.integration.emi.addWidget
import hiiragi283.ragium.integration.emi.recipe.HTEmiRecipe
import net.minecraft.resources.ResourceLocation

class HTFuelGeneratorEmiRecipe(
    private val category: EmiRecipeCategory,
    id: ResourceLocation,
    private val itemInput: EmiIngredient,
    private val fluidInput: EmiIngredient,
    private val energyRate: Int,
) : HTEmiRecipe.Impl(id) {
    override fun getCategory(): EmiRecipeCategory = category

    override fun getInputs(): List<EmiIngredient> = listOf(itemInput, fluidInput)

    override fun getOutputs(): List<EmiStack> = listOf()

    override fun getDisplayWidth(): Int = getPosition(7)

    override fun getDisplayHeight(): Int = getPosition(3)

    override fun supportsRecipeTree(): Boolean = false

    override fun addWidgets(widgets: WidgetHolder) {
        // item fuel slot
        widgets.addSlot(itemInput, getPosition(1), getPosition(1))
        // energy tank
        HTEnergyBatteryWidget(
            { HTEnergyNetwork(energyRate, energyRate) },
            getPosition(3),
            getPosition(0) + 1,
        ).let(widgets::addWidget)
        // fluid tank
        widgets
            .addTank(
                fluidInput,
                getPosition(5),
                getPosition(0),
            )
    }
}
