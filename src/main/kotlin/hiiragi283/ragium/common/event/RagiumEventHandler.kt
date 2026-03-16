package hiiragi283.ragium.common.event

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.tank.HTTankInteractionDataLoader
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.AddReloadListenerEvent

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumEventHandler {
    @SubscribeEvent
    fun addReloadListener(event: AddReloadListenerEvent) {
        event.addListener(HTTankInteractionDataLoader())
    }
}
