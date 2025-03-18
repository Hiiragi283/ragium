package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import net.neoforged.fml.common.EventBusSubscriber
import org.slf4j.Logger

@EventBusSubscriber(modid = RagiumAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object RagiumModEvents {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()
}
