package hiiragi283.ragium.data

import hiiragi283.ragium.data.client.lang.RagiumEnglishLangProvider
import hiiragi283.ragium.data.client.lang.RagiumJapaneseLangProvider
import hiiragi283.ragium.data.server.recipe.RagiumHeatRecipeProvider
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber
data object RagiumDataGen {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent.Client) {
        // Server
        event.createProvider(::RagiumHeatRecipeProvider)
        // Client
        event.createProvider(::RagiumEnglishLangProvider)
        event.createProvider(::RagiumJapaneseLangProvider)
    }
}
