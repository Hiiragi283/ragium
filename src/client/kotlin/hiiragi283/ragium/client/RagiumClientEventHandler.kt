package hiiragi283.ragium.client

import hiiragi283.ragium.api.RagiumAPI
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent

@EventBusSubscriber(value = [Dist.CLIENT], modid = RagiumAPI.MOD_ID)
object RagiumClientEventHandler {
    @SubscribeEvent
    fun onClientTick(event: ClientTickEvent.Post) {}

    /*fun onItemTooltip(event: ItemTooltipEvent) {
        val stack: ItemStack = event.itemStack
        // Memory Disc Data
        stack.get(RagiumDataComponents.MEMORY_DISC_DATA)?.let { resource: HTItemResourceType ->
            event.toolTip += RagiumTranslation.TOOLTIPS_MEMORY_DISC_DATA.translateColored(HTDefaultColor.YELLOW, resource)
        }
    }*/
}
