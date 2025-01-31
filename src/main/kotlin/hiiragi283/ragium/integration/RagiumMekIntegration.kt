package hiiragi283.ragium.integration

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.event.HTRegisterMaterialEvent
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import org.slf4j.Logger

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object RagiumMekIntegration {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @SubscribeEvent
    fun registerMaterial(event: HTRegisterMaterialEvent) {
        event.register(IntegrationMaterials.REFINED_GLOWSTONE, HTMaterialType.ALLOY)
        event.register(IntegrationMaterials.REFINED_OBSIDIAN, HTMaterialType.ALLOY)

        LOGGER.info("Enabled Mekanism Integration!")
    }
}
