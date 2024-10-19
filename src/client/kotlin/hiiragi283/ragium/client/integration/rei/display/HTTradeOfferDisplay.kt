package hiiragi283.ragium.client.integration.rei.display

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.client.integration.rei.RagiumREIClient
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.display.Display
import me.shedaniel.rei.api.common.entry.EntryIngredient
import me.shedaniel.rei.api.common.util.EntryIngredients
import net.minecraft.village.TradeOffer
import net.minecraft.village.TradedItem
import kotlin.jvm.optionals.getOrNull

class HTTradeOfferDisplay(val tier: HTMachineTier, private val offer: TradeOffer) : Display {
    override fun getInputEntries(): List<EntryIngredient> = buildList {
        add(ingredient(offer.firstBuyItem) ?: EntryIngredient.empty())
        add(ingredient(offer.secondBuyItem.getOrNull()) ?: EntryIngredient.empty())
    }

    private fun ingredient(item: TradedItem?): EntryIngredient? = item?.itemStack?.let(EntryIngredients::of)

    override fun getOutputEntries(): List<EntryIngredient> = buildList {
        add(EntryIngredients.of(offer.sellItem))
    }

    override fun getCategoryIdentifier(): CategoryIdentifier<*> = RagiumREIClient.TRADE_OFFER
}
