package hiiragi283.ragium.api.trade

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.RagiumContents
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.village.TradeOffer
import net.minecraft.village.TradedItem
import java.util.*

object HTTradeOfferRegistry {
    @JvmStatic
    val registry: Map<HTMachineTier, List<Factory>>
        get() = registry1

    @JvmStatic
    private val registry1: MutableMap<HTMachineTier, MutableList<Factory>> =
        EnumMap(HTMachineTier::class.java)

    @JvmStatic
    fun getFactories(tier: HTMachineTier): List<Factory> = registry1.getOrDefault(tier, listOf())

    @JvmStatic
    fun registerFactory(tier: HTMachineTier, factory: Factory) {
        registry1.computeIfAbsent(tier) { mutableListOf() }.add(factory)
    }

    @JvmStatic
    private var loaded: Boolean = false

    @JvmStatic
    fun init() {
        if (loaded) return
        registerFactory(HTMachineTier.PRIMITIVE) { _: PlayerEntity? ->
            TradeOffer(
                TradedItem(RagiumContents.Plates.GOLD),
                Items.DIAMOND.defaultStack,
                5,
                1,
                0.05f,
            )
        }
        loaded = true
    }

    //    Factory    //

    fun interface Factory {
        fun create(player: PlayerEntity?): TradeOffer?
    }
}
