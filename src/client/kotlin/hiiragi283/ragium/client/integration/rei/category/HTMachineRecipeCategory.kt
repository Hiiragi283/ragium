package hiiragi283.ragium.client.integration.rei.category

import hiiragi283.ragium.client.integration.rei.categoryId
import hiiragi283.ragium.client.integration.rei.display.HTMachineRecipeDisplay
import hiiragi283.ragium.common.machine.HTMachineType
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.gui.Renderer
import me.shedaniel.rei.api.client.gui.widgets.Widget
import me.shedaniel.rei.api.client.gui.widgets.Widgets
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.util.EntryIngredients
import me.shedaniel.rei.api.common.util.EntryStacks
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.text.Text

@Environment(EnvType.CLIENT)
class HTMachineRecipeCategory(val type: HTMachineType) : HTDisplayCategory<HTMachineRecipeDisplay> {
    override fun getCategoryIdentifier(): CategoryIdentifier<out HTMachineRecipeDisplay> = type.categoryId

    override fun getTitle(): Text = type.text

    override fun getIcon(): Renderer = EntryStacks.of(type)

    override fun setupDisplay(display: HTMachineRecipeDisplay, bounds: Rectangle): List<Widget> = buildList {
        this += Widgets.createRecipeBase(bounds)
        this += Widgets.createArrow(getPoint(bounds, 3.25, 0.0)).animationDurationTicks(200.0)
        // inputs
        this +=
            Widgets
                .createSlot(getPoint(bounds, 0, 0))
                .entries(display.inputEntries.getOrNull(0) ?: listOf())
                .markInput()
        this +=
            Widgets
                .createSlot(getPoint(bounds, 1, 0))
                .entries(display.inputEntries.getOrNull(1) ?: listOf())
                .markInput()
        this +=
            Widgets
                .createSlot(getPoint(bounds, 2, 0))
                .entries(display.inputEntries.getOrNull(2) ?: listOf())
                .markInput()
        // catalyst
        this +=
            Widgets
                .createSlot(getPoint(bounds, 3.5, 1.0))
                .entries(EntryIngredients.ofIngredient(display.catalyst))
                .markInput()
        // outputs
        this +=
            Widgets
                .createSlot(getPoint(bounds, 5, 0))
                .entries(display.outputEntries.getOrNull(0) ?: listOf())
                .markOutput()
        this +=
            Widgets
                .createSlot(getPoint(bounds, 6, 0))
                .entries(display.outputEntries.getOrNull(1) ?: listOf())
                .markOutput()
        this +=
            Widgets
                .createSlot(getPoint(bounds, 7, 0))
                .entries(display.outputEntries.getOrNull(2) ?: listOf())
                .markOutput()
    }

    override fun getDisplayHeight(): Int = 18 * 2 + 8

    override fun getDisplayWidth(display: HTMachineRecipeDisplay): Int = 18 * 8 + 8
}
