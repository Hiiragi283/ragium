package hiiragi283.ragium.integration.jade

import com.mojang.logging.LogUtils
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
        registration.registerBlockDataProvider(HTAdvancedCrusherProvider, Block::class.java)
        registration.registerBlockDataProvider(HTAdvancedExtractorProvider, Block::class.java)
        registration.registerBlockDataProvider(HTCrusherProvider, Block::class.java)
        registration.registerBlockDataProvider(HTEnchantmentHolderProvider, Block::class.java)
        registration.registerBlockDataProvider(HTExtractorProvider, Block::class.java)
    }

    override fun registerClient(registration: IWailaClientRegistration) {
        registration.registerBlockComponent(HTAdvancedCrusherProvider, Block::class.java)
        registration.registerBlockComponent(HTAdvancedExtractorProvider, Block::class.java)
        registration.registerBlockComponent(HTCrusherProvider, Block::class.java)
        registration.registerBlockComponent(HTEnchantmentHolderProvider, Block::class.java)
        registration.registerBlockComponent(HTExtractorProvider, Block::class.java)
    }
}
