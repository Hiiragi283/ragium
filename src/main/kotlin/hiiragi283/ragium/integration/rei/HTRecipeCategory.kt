package hiiragi283.ragium.integration.rei

import hiiragi283.ragium.common.recipe.HTMachineType
import me.shedaniel.math.Point
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.gui.Renderer
import me.shedaniel.rei.api.client.gui.widgets.Widget
import me.shedaniel.rei.api.client.gui.widgets.Widgets
import me.shedaniel.rei.api.client.registry.display.DisplayCategory
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.text.Text

class HTRecipeCategory(val type: HTMachineType) : DisplayCategory<HTRecipeDisplay> {
    override fun getCategoryIdentifier(): CategoryIdentifier<out HTRecipeDisplay> = RagiumREIClient.getCategoryId(type)

    override fun getTitle(): Text = type.text

    override fun getIcon(): Renderer = EntryStacks.of(type)

    override fun setupDisplay(display: HTRecipeDisplay, bounds: Rectangle): List<Widget> = buildList {
        val startPoint = Point(bounds.centerX - 41, bounds.centerY - 13)
        this += Widgets.createRecipeBase(bounds)
        this += Widgets.createArrow(Point(startPoint.x + 27, startPoint.y + 4))
        // this += Widgets.createResultSlotBackground(Point(startPoint.x + 61, startPoint.y + 5))
        // outputs
        this += Widgets.createSlot(Point(startPoint.x + 61, startPoint.y + 5))
            .entries(display.outputEntries.getOrNull(0) ?: listOf())
            .markOutput()
        this += Widgets.createSlot(Point(startPoint.x + 79, startPoint.y + 5))
            .entries(display.outputEntries.getOrNull(1) ?: listOf())
            .markOutput()
        this += Widgets.createSlot(Point(startPoint.x + 97, startPoint.y + 5))
            .entries(display.outputEntries.getOrNull(2) ?: listOf())
            .markOutput()
        // inputs
        this += Widgets.createSlot(Point(startPoint.x - 32, startPoint.y + 5))
            .entries(display.inputEntries.getOrNull(0) ?: listOf())
            .markInput()
        this += Widgets.createSlot(Point(startPoint.x - 14, startPoint.y + 5))
            .entries(display.inputEntries.getOrNull(1) ?: listOf())
            .markInput()
        this += Widgets.createSlot(Point(startPoint.x + 4, startPoint.y + 5))
            .entries(display.inputEntries.getOrNull(2) ?: listOf())
            .markInput()
    }

    override fun getDisplayHeight(): Int = 36
}