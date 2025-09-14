package hiiragi283.ragium.common.tier

import hiiragi283.ragium.api.tier.HTBaseTier
import hiiragi283.ragium.api.tier.HTTierProvider

enum class HTMachineTier(private val base: HTBaseTier, val generatorRate: Int, val processorRate: Int) : HTTierProvider {
    BASIC(HTBaseTier.BASIC, 32),
    ADVANCED(HTBaseTier.ADVANCED, 32 * 4),
    ELITE(HTBaseTier.ELITE, 32 * 8),
    ULTIMATE(HTBaseTier.ULTIMATE, 32 * 16),
    CREATIVE(HTBaseTier.CREATIVE, Int.MAX_VALUE),
    ;

    constructor(base: HTBaseTier, generatorRate: Int) : this(base, generatorRate, generatorRate / 2)

    override fun getBaseTier(): HTBaseTier = base
}
