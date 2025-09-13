package hiiragi283.ragium.client.event

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.RagiumKeyMappings
import hiiragi283.ragium.client.network.HTOpenPotionBundlePacket
import hiiragi283.ragium.client.network.HTOpenUniversalBundlePacket
import hiiragi283.ragium.common.util.HTPacketHelper
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent

@EventBusSubscriber(value = [Dist.CLIENT], modid = RagiumAPI.MOD_ID)
object RagiumKeyPressingHandler {
    @SubscribeEvent
    fun onClientTick(event: ClientTickEvent.Post) {
        if (RagiumKeyMappings.OPEN_POTION_BUNDLE.consumeClick()) {
            HTPacketHelper.sendToServer(HTOpenPotionBundlePacket)
        }
        if (RagiumKeyMappings.OPEN_UNIVERSAL_BUNDLE.consumeClick()) {
            HTPacketHelper.sendToServer(HTOpenUniversalBundlePacket)
        }
    }
}
