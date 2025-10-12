package hiiragi283.ragium.client.integration.jade

import hiiragi283.ragium.api.RagiumAPI
import snownee.jade.api.IWailaClientRegistration
import snownee.jade.api.IWailaCommonRegistration
import snownee.jade.api.IWailaPlugin
import snownee.jade.api.WailaPlugin

@WailaPlugin
class RagiumJadePlugin : IWailaPlugin {
    init {
        RagiumAPI.LOGGER.info("Jade integration enabled!")
    }

    override fun register(registration: IWailaCommonRegistration) {
        // registration.registerBlockDataProvider(HTOutputSideDataProvider, Block::class.java)
    }

    override fun registerClient(registration: IWailaClientRegistration) {
        // registration.registerBlockComponent(HTOutputSideDataProvider, Block::class.java)
    }
}
