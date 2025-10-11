package hiiragi283.ragium.api.tier

/**
 * [HTBaseTier]を保持するインターフェース
 * @see [mekanism.api.tier.ITier]
 */
fun interface HTTierProvider {
    fun getBaseTier(): HTBaseTier
}
