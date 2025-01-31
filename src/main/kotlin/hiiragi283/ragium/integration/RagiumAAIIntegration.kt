package hiiragi283.ragium.integration

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.event.HTRegisterMaterialEvent
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import org.slf4j.Logger

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object RagiumAAIIntegration {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @SubscribeEvent
    fun registerMaterial(event: HTRegisterMaterialEvent) {
        event.register(IntegrationMaterials.BLACK_QUARTZ, HTMaterialType.GEM)

        LOGGER.info("Enabled AA Integration!")
    }
}
