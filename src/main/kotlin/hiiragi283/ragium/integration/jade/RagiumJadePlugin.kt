package hiiragi283.ragium.integration.jade

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.block.HTHorizontalMachineBlock
import hiiragi283.ragium.api.block.HTMachineBlock
import hiiragi283.ragium.common.block.addon.HTEnergyNetworkBlock
import net.minecraft.world.level.block.Block
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
        registration.registerBlockDataProvider(HTEnergyNetworkProvider, HTMachineBlock::class.java)
        registration.registerBlockDataProvider(HTEnergyNetworkProvider, HTHorizontalMachineBlock::class.java)

        registration.registerBlockDataProvider(HTErrorMessageProvider, Block::class.java)
        registration.registerBlockDataProvider(HTMachineInfoProvider, Block::class.java)
        registration.registerBlockDataProvider(HTEnchantableBlockProvider, Block::class.java)
    }

    override fun registerClient(registration: IWailaClientRegistration) {
        registration.registerBlockComponent(HTEnergyNetworkProvider, HTEnergyNetworkBlock::class.java)
        registration.registerBlockComponent(HTEnergyNetworkProvider, HTMachineBlock::class.java)
        registration.registerBlockComponent(HTEnergyNetworkProvider, HTHorizontalMachineBlock::class.java)

        registration.registerBlockComponent(HTErrorMessageProvider, Block::class.java)
        registration.registerBlockComponent(HTMachineInfoProvider, Block::class.java)
        registration.registerBlockComponent(HTEnchantableBlockProvider, Block::class.java)
    }
}
