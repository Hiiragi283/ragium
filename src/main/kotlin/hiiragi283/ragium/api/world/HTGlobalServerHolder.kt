package hiiragi283.ragium.api.world

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.server.MinecraftServer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent
import net.neoforged.neoforge.event.server.ServerStoppedEvent

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object HTGlobalServerHolder {
    @JvmStatic
    var currentServer: MinecraftServer? = null
        private set

    @SubscribeEvent
    fun onServerStart(event: ServerAboutToStartEvent) {
        currentServer = event.server
    }

    @SubscribeEvent
    fun onStoppedStart(event: ServerStoppedEvent) {
        currentServer = null
    }
}
