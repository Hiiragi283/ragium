package hiiragi283.ragium.integration.jade

import com.mojang.logging.LogUtils
import hiiragi283.ragium.common.block.HTEnergyNetworkBlock
import org.slf4j.Logger
import snownee.jade.api.IWailaClientRegistration
import snownee.jade.api.IWailaCommonRegistration
import snownee.jade.api.IWailaPlugin
import snownee.jade.api.WailaPlugin

@WailaPlugin
class RagiumJadePlugin : IWailaPlugin {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    init {
        LOGGER.info("Jade integration enabled!")
    }

    override fun register(registration: IWailaCommonRegistration) {
        registration.registerBlockDataProvider(HTEnergyNetworkProvider, HTEnergyNetworkBlock::class.java)
    }

    override fun registerClient(registration: IWailaClientRegistration) {
        registration.registerBlockComponent(HTEnergyNetworkProvider, HTEnergyNetworkBlock::class.java)
    }
}
