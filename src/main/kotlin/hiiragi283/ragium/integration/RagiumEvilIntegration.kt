package hiiragi283.ragium.integration

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.event.HTRegisterMaterialEvent
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialType
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import org.slf4j.Logger

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object RagiumEvilIntegration {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @JvmField
    val DARK_GEM: HTMaterialKey = HTMaterialKey.of("dark")

    @SubscribeEvent
    fun registerMaterial(event: HTRegisterMaterialEvent) {
        event.register(DARK_GEM, HTMaterialType.GEM)

        LOGGER.info("Enabled Evil Craft Integration!")
    }
}
