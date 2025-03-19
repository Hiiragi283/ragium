package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import org.slf4j.Logger

// @EventBusSubscriber(modid = RagiumAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object RagiumModEvents {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()
}
