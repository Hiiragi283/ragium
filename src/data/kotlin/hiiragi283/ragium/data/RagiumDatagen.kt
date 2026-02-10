package hiiragi283.ragium.data

import hiiragi283.core.api.data.HTRootDataGenerator
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.data.client.RagiumTextureProvider
import hiiragi283.ragium.data.server.loot.RagiumBlockLootProvider
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

        // server.addProvider(::RagiumRecipeProvider)

        // server.addProvider(::RagiumFluidTagsProvider)
        // val blockTags: CompletableFuture<TagsProvider.TagLookup<Block>> =
        //     server.addProvider(::RagiumBlockTagsProvider).contentsGetter()
        // server.addProvider(::RagiumItemTagsProvider.partially1(blockTags))

        // server.addProvider(::RagiumDataMapProvider)
        // Client
        // client.addProvider(::RagiumEnglishLangProvider)
        // client.addProvider(::RagiumJapaneseLangProvider)

        client.addProvider(::RagiumTextureProvider)

        // client.addProvider(::RagiumBlockStateProvider)
        // client.addProvider(::RagiumItemModelProvider)
    }
}
