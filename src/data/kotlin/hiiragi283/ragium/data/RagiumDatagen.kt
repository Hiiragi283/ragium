package hiiragi283.ragium.data

import hiiragi283.core.api.data.HTRootDataGenerator
import hiiragi283.core.api.function.partially1
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.data.client.RagiumTextureProvider
import hiiragi283.ragium.data.client.lang.RagiumEnglishLangProvider
import hiiragi283.ragium.data.client.lang.RagiumJapaneseLangProvider
import hiiragi283.ragium.data.client.model.RagiumBlockStateProvider
import hiiragi283.ragium.data.client.model.RagiumItemModelProvider
import hiiragi283.ragium.data.server.RagiumDataMapProvider
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import hiiragi283.ragium.data.server.bootstrap.RagiumWoodDefinition
import hiiragi283.ragium.data.server.loot.RagiumBlockLootProvider
import hiiragi283.ragium.data.server.tag.RagiumBlockTagsProvider
import hiiragi283.ragium.data.server.tag.RagiumFluidTagsProvider
import hiiragi283.ragium.data.server.tag.RagiumItemTagsProvider
import net.minecraft.data.tags.TagsProvider
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent
import java.util.concurrent.CompletableFuture

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumDatagen {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val (server: HTRootDataGenerator, client: HTRootDataGenerator) = HTRootDataGenerator.withDataPack(event) {
            add(RagiumAPI.WOOD_DEFINITION_KEY, RagiumWoodDefinition)
        }
        // Server
        server.addLootTables(
            ::RagiumBlockLootProvider to LootContextParamSets.BLOCK,
        )

        server.addProvider(::RagiumRecipeProvider)

        server.addProvider(::RagiumFluidTagsProvider)
        val blockTags: CompletableFuture<TagsProvider.TagLookup<Block>> =
            server.addProvider(::RagiumBlockTagsProvider).contentsGetter()
        server.addProvider(::RagiumItemTagsProvider.partially1(blockTags))

        server.addProvider(::RagiumDataMapProvider)
        // Client
        client.addProvider(::RagiumEnglishLangProvider)
        client.addProvider(::RagiumJapaneseLangProvider)

        client.addProvider(::RagiumTextureProvider)

        client.addProvider(::RagiumBlockStateProvider)
        client.addProvider(::RagiumItemModelProvider)
    }
}
