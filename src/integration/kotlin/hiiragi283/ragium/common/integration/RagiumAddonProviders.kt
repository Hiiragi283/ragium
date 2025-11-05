package hiiragi283.ragium.common.integration

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.addon.HTAddon
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.common.integration.food.RagiumDelightAddon
import hiiragi283.ragium.common.integration.food.RagiumFoodAddon
import hiiragi283.ragium.common.integration.food.RagiumKaleidoCookeryAddon
import net.neoforged.fml.ModList

@HTAddon
class RagiumAddonProviders : RagiumAddon.Provider {
    override fun getAddons(modList: ModList): List<RagiumAddon> = buildSet {
        if (modList.isLoaded(RagiumConst.ACCESSORIES)) {
            add(RagiumAccessoriesAddon)
        }
        if (modList.isLoaded(RagiumConst.FARMERS_DELIGHT)) {
            add(RagiumFoodAddon)
            add(RagiumDelightAddon)
        }
        if (modList.isLoaded(RagiumConst.IMMERSIVE)) {
            add(RagiumImmersiveAddon)
        }
        if (modList.isLoaded(RagiumConst.KALEIDO_COOKERY)) {
            add(RagiumFoodAddon)
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
