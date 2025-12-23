package hiiragi283.ragium.data

import hiiragi283.core.api.data.HTRootDataGenerator
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.data.client.RagiumTextureProvider
import hiiragi283.ragium.data.client.lang.RagiumEnglishLangProvider
import hiiragi283.ragium.data.client.lang.RagiumJapaneseLangProvider
import hiiragi283.ragium.data.client.model.RagiumItemModelProvider
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import hiiragi283.ragium.data.server.tag.RagiumItemTagsProvider
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumDatagen {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val (server: HTRootDataGenerator, client: HTRootDataGenerator) = HTRootDataGenerator.withDataPack(event)
        // Server
        server.addProvider(::RagiumRecipeProvider)

        server.addProvider(::RagiumItemTagsProvider)
        // Client
        client.addProvider(::RagiumEnglishLangProvider)
        client.addProvider(::RagiumJapaneseLangProvider)

        client.addProvider(::RagiumTextureProvider)

        client.addProvider(::RagiumItemModelProvider)
    }
}
