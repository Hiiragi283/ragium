package hiiragi283.ragium.api.machine

interface HTMachineTierUpgradable : HTMachineTierProvider {
    fun onUpdateTier(oldTier: HTMachineTier, newTier: HTMachineTier)
}
