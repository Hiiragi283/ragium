package hiiragi283.ragium.common.integration

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.addon.HTAddon
import hiiragi283.ragium.api.addon.RagiumAddon
import net.neoforged.fml.ModList

@HTAddon
class RagiumAddonProviders : RagiumAddon.Provider {
    override fun getAddons(modList: ModList): List<RagiumAddon> = buildSet {
        if (modList.isLoaded(RagiumConst.ACCESSORIES)) {
            add(RagiumAccessoriesAddon)
        }
        if (modList.isLoaded(RagiumConst.CREATE)) {
            add(RagiumCreateAddon)
        }
        if (modList.isLoaded(RagiumConst.FARMERS_DELIGHT)) {
            add(RagiumDelightAddon)
        }
        if (modList.isLoaded(RagiumConst.IMMERSIVE)) {
            add(RagiumImmersiveAddon)
        }
        if (modList.isLoaded(RagiumConst.KALEIDO_COOKERY)) {
            add(RagiumKaleidoCookeryAddon)
        }
        if (modList.isLoaded(RagiumConst.MEKANISM)) {
            add(RagiumMekanismAddon)
        }
        if (modList.isLoaded(RagiumConst.REPLICATION)) {
            add(RagiumReplicationAddon)
        }
    }.toList()
}
