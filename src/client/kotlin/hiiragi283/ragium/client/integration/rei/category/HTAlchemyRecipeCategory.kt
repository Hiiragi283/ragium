package hiiragi283.ragium.client.integration.rei.category

import hiiragi283.ragium.client.integration.rei.RagiumREIClient
import hiiragi283.ragium.client.integration.rei.display.HTDisplay
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.recipe.alchemy.HTAlchemyRecipe
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.gui.Renderer
import me.shedaniel.rei.api.client.gui.widgets.Widget
import me.shedaniel.rei.api.client.gui.widgets.Widgets
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.text.Text

object HTAlchemyRecipeCategory : HTDisplayCategory<HTDisplay<out HTAlchemyRecipe>> {
    override fun getCategoryIdentifier(): CategoryIdentifier<HTDisplay<out HTAlchemyRecipe>> = RagiumREIClient.ALCHEMY

    override fun getTitle(): Text = RagiumContents.ALCHEMICAL_INFUSER.name

    override fun getIcon(): Renderer = EntryStacks.of(RagiumContents.ALCHEMICAL_INFUSER)

    override fun setupDisplay(display: HTDisplay<out HTAlchemyRecipe>, bounds: Rectangle): List<Widget> = buildList {
        this += Widgets.createRecipeBase(bounds)
        this += Widgets.createArrow(getPoint(bounds, 4.0, 1.5)).animationDurationTicks(200.0)
        this += Widgets.createResultSlotBackground(getPoint(bounds, 6.0, 1.5))
        // inputs
        this +=
            Widgets
                .createSlot(getPoint(bounds, 0, 0))
                .entries(display.inputEntries.getOrNull(0) ?: listOf())
                .markInput()
        this +=
            Widgets
                .createSlot(getPoint(bounds, 3, 0))
                .entries(display.inputEntries.getOrNull(1) ?: listOf())
                .markInput()
        this +=
            Widgets
                .createSlot(getPoint(bounds, 0, 3))
                .entries(display.inputEntries.getOrNull(2) ?: listOf())
                .markInput()
        this +=
            Widgets
                .createSlot(getPoint(bounds, 3, 3))
                .entries(display.inputEntries.getOrNull(3) ?: listOf())
                .markInput()
        // output
        this +=
            Widgets
                .createSlot(getPoint(bounds, 6.0, 1.5))
                .entries(display.outputEntries.getOrNull(0) ?: listOf())
                .disableBackground()
                .markOutput()
    }

    override fun getDisplayHeight(): Int = 18 * 4 + 8

    override fun getDisplayWidth(display: HTDisplay<out HTAlchemyRecipe>): Int = 18 * 8 + 8
}
