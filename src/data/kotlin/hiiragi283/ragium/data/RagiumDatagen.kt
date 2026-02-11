package hiiragi283.ragium.data

import hiiragi283.core.api.data.HTRootDataGenerator
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.data.client.RagiumBlockStateProvider
import hiiragi283.ragium.data.client.RagiumEnglishLangProvider
import hiiragi283.ragium.data.client.RagiumItemModelProvider
import hiiragi283.ragium.data.client.RagiumJapaneseLangProvider
import hiiragi283.ragium.data.server.RagiumDataMapProvider
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import hiiragi283.ragium.data.server.loot.RagiumBlockLootProvider
import hiiragi283.ragium.data.server.tag.RagiumBlockTagsProvider
import hiiragi283.ragium.data.server.tag.RagiumFluidTagsProvider
import hiiragi283.ragium.data.server.tag.RagiumItemTagsProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumDatagen {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val (server: HTRootDataGenerator, client: HTRootDataGenerator) = HTRootDataGenerator.withDataPack(event)
        // Server
        server.addLootTables(
            ::RagiumBlockLootProvider to LootContextParamSets.BLOCK,
        )

        server.addProvider(::RagiumRecipeProvider)

        server.addProvider(::RagiumFluidTagsProvider)
        server.addBlockAndItemTags(::RagiumBlockTagsProvider, ::RagiumItemTagsProvider)

        server.addProvider(::RagiumDataMapProvider)
        // Client
        client.addProvider(::RagiumEnglishLangProvider)
        client.addProvider(::RagiumJapaneseLangProvider)

        // client.addProvider(::RagiumTextureProvider)

        client.addProvider(::RagiumBlockStateProvider)
        client.addProvider(::RagiumItemModelProvider)
    }
}
