package hiiragi283.ragium.api.machine

/**
 * [HTMachineTier]を更新可能なインターフェース
 */
interface HTMachineTierUpgradable : HTMachineTierProvider {
    fun onUpdateTier(oldTier: HTMachineTier, newTier: HTMachineTier)
}
