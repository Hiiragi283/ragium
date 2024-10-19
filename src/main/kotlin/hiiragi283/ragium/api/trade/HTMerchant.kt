package hiiragi283.ragium.api.trade

import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.village.Merchant
import net.minecraft.village.TradeOffer
import net.minecraft.village.TradeOfferList

class HTMerchant(private val player: PlayerEntity) : Merchant {
    private var offers = TradeOfferList()
    private var exp: Int = 0

    override fun setCustomer(customer: PlayerEntity?) {
    }

    override fun getCustomer(): PlayerEntity = player

    override fun getOffers(): TradeOfferList {
        initOffers()
        return offers
    }

    private fun initOffers() {
        val factories: List<HTTradeOfferRegistry.Factory> = HTTradeOfferRegistry.getFactories(HTMachineTier.PRIMITIVE)
        val factories1: List<HTTradeOfferRegistry.Factory> = HTTradeOfferRegistry.getFactories(HTMachineTier.BASIC)

        loadFactories(factories, 5)
        loadFactories(factories1, 1)
    }

    private fun loadFactories(factories: List<HTTradeOfferRegistry.Factory>, count: Int) {
        val copied: MutableList<HTTradeOfferRegistry.Factory> = factories.toMutableList()
        var i = 0
        while (i < count && copied.isNotEmpty()) {
            val factory: HTTradeOfferRegistry.Factory = copied.random()
            copied.random().create(player)?.let {
                this.offers.add(it)
                copied.remove(factory)
                i++
            }
        }
    }

    override fun setOffersFromServer(offers: TradeOfferList) {
        this.offers = offers
    }

    override fun trade(offer: TradeOffer) {
        offer.use()
    }

    override fun onSellingItem(stack: ItemStack) {
        if (!isClient) {
            player.playSound(
                when (stack.isEmpty) {
                    true -> SoundEvents.ENTITY_VILLAGER_NO
                    false -> SoundEvents.ENTITY_VILLAGER_YES
                },
            )
        }
    }

    override fun getExperience(): Int = exp

    override fun setExperienceFromServer(experience: Int) {
        exp = experience
    }

    override fun isLeveledMerchant(): Boolean = true

    override fun getYesSound(): SoundEvent = SoundEvents.ENTITY_VILLAGER_YES

    override fun isClient(): Boolean = player.world.isClient
}
