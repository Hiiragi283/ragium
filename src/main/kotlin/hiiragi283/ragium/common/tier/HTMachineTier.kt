package hiiragi283.ragium.common.tier

import hiiragi283.ragium.api.tier.HTBaseTier
import hiiragi283.ragium.api.tier.HTTierProvider

enum class HTMachineTier(private val base: HTBaseTier, val generatorRate: Int, val processorRate: Int) : HTTierProvider {
    BASIC(HTBaseTier.BASIC, 32, 16),
    ADVANCED(HTBaseTier.ADVANCED, 128, 32),
    ELITE(HTBaseTier.ELITE, 512, 64),
    ULTIMATE(HTBaseTier.ULTIMATE, 2048, 128),
    CREATIVE(HTBaseTier.CREATIVE, Int.MAX_VALUE, Int.MAX_VALUE),
    ;

    override fun getBaseTier(): HTBaseTier = base
}
