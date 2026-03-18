package hiiragi283.ragium.common.event

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.tank.HTTankInteraction
import hiiragi283.ragium.common.data.HTJsonResourceReloadListener
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.AddReloadListenerEvent

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumEventHandler {
    @JvmStatic
    private lateinit var tankInteraction: HTJsonResourceReloadListener<HTTankInteraction.Serializable>

    @JvmStatic
    val tankInteractionMap: Map<ResourceLocation, HTTankInteraction.Serializable>
        get() = tankInteraction.resultMap

    @SubscribeEvent
    fun addReloadListener(event: AddReloadListenerEvent) {
        tankInteraction = HTJsonResourceReloadListener.create(RagiumConst.TANK_INTERACTION, HTTankInteraction.Serializable.CODEC)
        event.addListener(tankInteraction)
    }
}
