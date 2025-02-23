package hiiragi283.ragium.data

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.data.client.*
import hiiragi283.ragium.data.server.*
import net.minecraft.DetectedVersion
import net.minecraft.core.HolderLookup
import net.minecraft.data.DataGenerator
import net.minecraft.data.PackOutput
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.data.metadata.PackMetadataGenerator
import net.minecraft.network.chat.Component
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.metadata.pack.PackMetadataSection
import net.minecraft.util.InclusiveRange
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.data.AdvancementProvider
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent
import org.slf4j.Logger
import java.util.*
import java.util.concurrent.CompletableFuture

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = RagiumAPI.MOD_ID)
object RagiumData {
    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val generator: DataGenerator = event.generator
        val output: PackOutput = generator.packOutput
        val helper: ExistingFileHelper = event.existingFileHelper
        val provider: CompletableFuture<HolderLookup.Provider> = event.lookupProvider

        generator.addProvider(
            true,
            PackMetadataGenerator(output)
                .add(
                    PackMetadataSection.TYPE,
                    PackMetadataSection(
                        Component.literal(RagiumAPI.MOD_NAME),
                        DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA),
                        Optional.of(InclusiveRange(0, Integer.MAX_VALUE)),
                    ),
                ),
        )

        // server
        generator.addProvider(event.includeServer(), AdvancementProviderImpl(output, provider, helper))

        generator.addProvider(
            event.includeServer(),
            LootTableProvider(
                output,
                setOf(),
                listOf(LootTableProvider.SubProviderEntry(::RagiumBlockLootProvider, LootContextParamSets.BLOCK)),
                provider,
            ),
        )

        generator.addProvider(event.includeServer(), RagiumDataMapProvider(output, provider))
        generator.addProvider(event.includeServer(), RagiumRecipeProvider(output, provider))

        generator.addProvider(event.includeServer(), RagiumBlockTagProvider(output, provider, helper))
        generator.addProvider(event.includeServer(), RagiumFluidTagProvider(output, provider, helper))
        generator.addProvider(event.includeServer(), RagiumItemTagProvider(output, provider, helper))

        generator.addProvider(
            event.includeServer(),
            DatapackBuiltinEntriesProvider(
                output,
                provider,
                RagiumWorldGenBoostrap.createBuilder(),
                setOf(RagiumAPI.MOD_ID),
            ),
        )
        LOGGER.info("Gathered server resources!")
        // client
        generator.addProvider(event.includeClient(), ::RagiumEnglishProvider)
        generator.addProvider(event.includeClient(), ::RagiumJapaneseProvider)

        generator.addProvider(event.includeClient(), RagiumBlockStateProvider(output, helper))
        generator.addProvider(event.includeClient(), RagiumBlockModelProvider(output, helper))
        generator.addProvider(event.includeClient(), RagiumItemModelProvider(output, helper))

        LOGGER.info("Gathered client resources!")
    }

    private class AdvancementProviderImpl(
        output: PackOutput,
        registries: CompletableFuture<HolderLookup.Provider>,
        existingFileHelper: ExistingFileHelper,
    ) : AdvancementProvider(output, registries, existingFileHelper, listOf(RagiumAdvancementGenerator))
}
