package hiiragi283.ragium.data

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent
import org.slf4j.Logger

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = RagiumAPI.MOD_ID)
object RagiumData {
    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    @SubscribeEvent
    fun gatherServerData(event: GatherDataEvent.Server) {
        LOGGER.info("Gathered server resources!")
    }

    @SubscribeEvent
    fun gatherClientData(event: GatherDataEvent.Client) {
        event.createProvider(RagiumCraftingRecipeProvider::Runner)

        LOGGER.info("Gathered client resources!")
    }
}
