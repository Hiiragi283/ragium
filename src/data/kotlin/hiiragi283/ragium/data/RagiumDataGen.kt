package hiiragi283.ragium.data

import hiiragi283.ragium.data.server.recipe.RagiumHeatRecipeProvider
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber
data object RagiumDataGen {
    @SubscribeEvent
    fun gatherServerData(event: GatherDataEvent.Server) {
        event.createProvider(::RagiumHeatRecipeProvider)
    }

    @SubscribeEvent
    fun gatherClientData(event: GatherDataEvent.Client) {
    }
}
