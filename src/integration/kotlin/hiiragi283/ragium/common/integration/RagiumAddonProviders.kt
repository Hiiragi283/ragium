package hiiragi283.ragium.common.integration

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.addon.HTAddon
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.common.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.common.integration.immersive.RagiumImmersiveAddon
import hiiragi283.ragium.common.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.common.integration.replication.RagiumReplicationAddon
import net.neoforged.fml.ModList

@HTAddon
class RagiumAddonProviders : RagiumAddon.Provider {
    override fun getAddons(modList: ModList): List<RagiumAddon> = buildList {
        if (modList.isLoaded(RagiumConst.FARMERS_DELIGHT)) {
            add(RagiumDelightAddon)
        }
        if (modList.isLoaded(RagiumConst.IMMERSIVE)) {
            add(RagiumImmersiveAddon)
        }
        if (modList.isLoaded(RagiumConst.MEKANISM)) {
            add(RagiumMekanismAddon)
        }
        if (modList.isLoaded(RagiumConst.REPLICATION)) {
            add(RagiumReplicationAddon)
        }
    }
}
