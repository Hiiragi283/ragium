package hiiragi283.ragium.common.tier

import hiiragi283.ragium.api.tier.HTBaseTier
import hiiragi283.ragium.api.tier.HTTierProvider

enum class HTMachineTier : HTTierProvider {
    BASIC,
    ADVANCED,
    ELITE,
    ULTIMATE,
    CREATIVE,
    ;

    override fun getBaseTier(): HTBaseTier = when (this) {
        BASIC -> HTBaseTier.BASIC
        ADVANCED -> HTBaseTier.ADVANCED
        ELITE -> HTBaseTier.ELITE
        ULTIMATE -> HTBaseTier.ULTIMATE
        CREATIVE -> HTBaseTier.CREATIVE
    }
}
