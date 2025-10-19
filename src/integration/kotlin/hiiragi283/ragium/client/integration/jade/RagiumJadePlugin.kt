package hiiragi283.ragium.client.integration.jade

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.integration.jade.provider.HTBlockConfigurationDataProvider
import hiiragi283.ragium.client.integration.jade.provider.HTBlockOwnerProvider
import net.minecraft.world.level.block.Block
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
        registration.registerBlockDataProvider(HTBlockConfigurationDataProvider, Block::class.java)
        registration.registerBlockDataProvider(HTBlockOwnerProvider, Block::class.java)
    }

    override fun registerClient(registration: IWailaClientRegistration) {
        registration.registerBlockComponent(HTBlockConfigurationDataProvider, Block::class.java)
        registration.registerBlockComponent(HTBlockOwnerProvider, Block::class.java)
    }
}
