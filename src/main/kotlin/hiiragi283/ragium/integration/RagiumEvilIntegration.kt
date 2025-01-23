package hiiragi283.ragium.integration

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTRegisterMaterialEvent
import net.neoforged.bus.api.IEventBus
import org.slf4j.Logger

object RagiumEvilIntegration {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @JvmField
    val DARK_GEM: HTMaterialKey = HTMaterialKey.Companion.of("dark")

    fun init(eventBus: IEventBus) {
        eventBus.addListener(::registerMaterial)

        LOGGER.info("Enabled Evil Craft Integration!")
    }

    private fun registerMaterial(event: HTRegisterMaterialEvent) {
        event.register(DARK_GEM, HTMaterialType.GEM)
    }
}
