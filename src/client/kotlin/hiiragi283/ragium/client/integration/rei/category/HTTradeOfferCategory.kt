package hiiragi283.ragium.client.integration.rei.category

import hiiragi283.ragium.client.integration.rei.RagiumREIClient
import hiiragi283.ragium.client.integration.rei.display.HTTradeOfferDisplay
import hiiragi283.ragium.common.RagiumContents
import me.shedaniel.math.Point
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.gui.Renderer
import me.shedaniel.rei.api.client.gui.widgets.Widget
import me.shedaniel.rei.api.client.gui.widgets.Widgets
import me.shedaniel.rei.api.client.registry.display.DisplayCategory
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.text.Text

object HTTradeOfferCategory : DisplayCategory<HTTradeOfferDisplay> {
    override fun getCategoryIdentifier(): CategoryIdentifier<out HTTradeOfferDisplay> = RagiumREIClient.TRADE_OFFER

    override fun getTitle(): Text = Text.literal("Trade Offer")

    override fun getIcon(): Renderer = EntryStacks.of(RagiumContents.Ingots.RAGI_STEEL)

    override fun setupDisplay(display: HTTradeOfferDisplay, bounds: Rectangle): List<Widget> {
        val startPoint = Point(bounds.centerX - 41, bounds.centerY - 13)
        return buildList {
            add(Widgets.createRecipeBase(bounds))
            add(Widgets.createArrow(Point(startPoint.x + 27, startPoint.y + 4)))
            add(Widgets.createResultSlotBackground(Point(startPoint.x + 61, startPoint.y + 5)))
            add(
                Widgets
                    .createSlot(Point(startPoint.x + 61, startPoint.y + 5))
                    .entries(display.outputEntries[0])
                    .disableBackground()
                    .markOutput(),
            )
            add(
                Widgets
                    .createSlot(Point(startPoint.x + 4, startPoint.y + 5))
                    .entries(display.inputEntries[1])
                    .markInput(),
            )
            add(
                Widgets
                    .createSlot(Point(startPoint.x - 14, startPoint.y + 5))
                    .entries(display.inputEntries[0])
                    .markInput(),
            )
        }
    }

    override fun getDisplayHeight(): Int = 36
}
