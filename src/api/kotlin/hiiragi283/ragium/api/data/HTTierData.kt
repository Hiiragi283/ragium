package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.tier.HTBaseTier
import hiiragi283.ragium.api.tier.HTTierProvider
import net.minecraft.network.FriendlyByteBuf

data class HTTierData(val tier: HTBaseTier) {
    companion object {
        @JvmField
        val CODEC: BiCodec<FriendlyByteBuf, HTTierData> = BiCodec.composite(
            HTBaseTier.CODEC.fieldOf("tier"),
            HTTierData::tier,
            ::HTTierData,
        )
    }

    constructor(provider: HTTierProvider) : this(provider.getBaseTier())
}
