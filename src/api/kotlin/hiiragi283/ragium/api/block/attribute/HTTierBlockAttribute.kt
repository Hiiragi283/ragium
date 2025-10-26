package hiiragi283.ragium.api.block.attribute

import hiiragi283.ragium.api.tier.HTTierProvider

/**
 * @see mekanism.common.block.attribute.AttributeTier
 */
@JvmRecord
data class HTTierBlockAttribute<TIER : HTTierProvider>(val provider: TIER) : HTBlockAttribute
