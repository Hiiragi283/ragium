package hiiragi283.ragium.common.integration

import hiiragi283.core.api.mod.HTCommonMod
import hiiragi283.core.common.integration.HCIConstants
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.integration.ae2.RagiumAEIntegration
import hiiragi283.ragium.common.integration.mek.RagiumMekIntegration
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod

/**
 * @see hiiragi283.core.common.integration.HiiragiCoreIntegration
 */
@Mod(RagiumAPI.MOD_ID)
data object RagiumIntegration : HTCommonMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        if (HCIConstants.isLoaded(HCIConstants.AE2)) {
            RagiumAEIntegration.init(eventBus)
        }
        if (HCIConstants.isLoaded(HCIConstants.MEKANISM)) {
            RagiumMekIntegration.init(eventBus)
        }
    }
}
