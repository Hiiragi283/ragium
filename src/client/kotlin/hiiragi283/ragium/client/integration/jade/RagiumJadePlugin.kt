package hiiragi283.ragium.client.integration.jade

import hiiragi283.ragium.common.block.HTBasicEntityBlock
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import snownee.jade.api.IWailaClientRegistration
import snownee.jade.api.IWailaCommonRegistration
import snownee.jade.api.IWailaPlugin
import snownee.jade.api.WailaPlugin

@WailaPlugin
class RagiumJadePlugin : IWailaPlugin {
    override fun register(registration: IWailaCommonRegistration) {
        registration.registerBlockDataProvider(HTMachineOwnerProvider.Server, HTBlockEntity::class.java)
        registration.registerBlockDataProvider(HTMachineUpgradeProvider.Server, HTBlockEntity::class.java)
    }

    override fun registerClient(registration: IWailaClientRegistration) {
        registration.registerBlockComponent(HTMachineOwnerProvider.Client, HTBasicEntityBlock::class.java)
        registration.registerBlockComponent(HTMachineUpgradeProvider.Client, HTBasicEntityBlock::class.java)
    }
}
