package hiiragi283.ragium.integration.emi.recipe.generator

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.gui.component.HTEnergyBatteryWidget
import hiiragi283.ragium.common.storage.energy.HTEnergyNetwork
import hiiragi283.ragium.integration.emi.addTank
import hiiragi283.ragium.integration.emi.addWidget
import hiiragi283.ragium.integration.emi.fluid
import hiiragi283.ragium.integration.emi.recipe.HTEmiRecipe
import net.minecraft.resources.ResourceLocation

class HTFuelGeneratorEmiRecipe(
    private val category: EmiRecipeCategory,
    id: ResourceLocation,
    private val input: EmiStack,
    private val amount: Int,
    private val energyRate: Int,
) : HTEmiRecipe.Impl(id) {
    override fun getCategory(): EmiRecipeCategory = category

    override fun getInputs(): List<EmiIngredient> = listOf(input)

    override fun getOutputs(): List<EmiStack> = listOf()

    override fun getDisplayWidth(): Int = getPosition(7)

    override fun getDisplayHeight(): Int = getPosition(3)

    override fun supportsRecipeTree(): Boolean = false

    override fun addWidgets(widgets: WidgetHolder) {
        // empty slot
        widgets.addSlot(input.fluid?.bucket?.let(EmiStack::of) ?: EmiStack.EMPTY, getPosition(1), getPosition(1))
        // energy tank
        val energy: Int = (1000 / amount) * energyRate
        HTEnergyBatteryWidget(
            { HTEnergyNetwork(energy, energy) },
            getPosition(3),
            getPosition(0) + 1,
        ).let(widgets::addWidget)
        // fluid tank
        widgets
            .addTank(
                input,
                getPosition(5),
                getPosition(0),
            )
    }
}
