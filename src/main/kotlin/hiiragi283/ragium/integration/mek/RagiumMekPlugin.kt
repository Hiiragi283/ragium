package hiiragi283.ragium.integration.mek

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTRegisterMaterialEvent
import net.neoforged.bus.api.IEventBus
import org.slf4j.Logger

object RagiumMekPlugin {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @JvmField
    val OSMIUM: HTMaterialKey = HTMaterialKey.of("osmium")

    @JvmField
    val REFINED_GLOWSTONE: HTMaterialKey = HTMaterialKey.of("refined_glowstone")

    @JvmField
    val REFINED_OBSIDIAN: HTMaterialKey = HTMaterialKey.of("refined_obsidian")

    fun init(eventBus: IEventBus) {
        eventBus.addListener(::registerMaterial)

        LOGGER.info("Enabled Mekanism Integration!")
    }

    private fun registerMaterial(event: HTRegisterMaterialEvent) {
        event.register(REFINED_GLOWSTONE, HTMaterialType.ALLOY)
        event.register(REFINED_OBSIDIAN, HTMaterialType.ALLOY)

        event.register(OSMIUM, HTMaterialType.METAL)
    }
}
