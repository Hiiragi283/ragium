package hiiragi283.ragium.data

import hiiragi283.ragium.data.lang.RagiumEnglishLangProvider
import hiiragi283.ragium.data.lang.RagiumJapaneseLangProvider
import hiiragi283.ragium.data.model.RagiumModelProvider
import hiiragi283.ragium.data.recipe.RagiumHeatRecipeProvider
import hiiragi283.ragium.data.tag.RagiumBlockTagsProvider
import hiiragi283.ragium.data.tag.RagiumFluidTagsProvider
import hiiragi283.ragium.data.tag.RagiumItemTagsProvider
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber
data object RagiumDataGen {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent.Client) {
        // Server
        event.createProvider(::RagiumHeatRecipeProvider)

        event.createProvider(::RagiumFluidTagsProvider)
        event.createBlockAndItemTags(::RagiumBlockTagsProvider, ::RagiumItemTagsProvider)
        // Client
        event.createProvider(::RagiumModelProvider)

        event.createProvider(::RagiumEnglishLangProvider)
        event.createProvider(::RagiumJapaneseLangProvider)
    }
}
