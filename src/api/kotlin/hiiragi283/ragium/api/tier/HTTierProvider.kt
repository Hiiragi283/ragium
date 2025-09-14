package hiiragi283.ragium.api.tier

/**
 * @see [mekanism.api.tier.ITier]
 */
fun interface HTTierProvider {
    fun getBaseTier(): HTBaseTier
}
