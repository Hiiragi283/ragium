package hiiragi283.ragium.data

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.data.client.RagiumBlockStateProvider
import hiiragi283.ragium.data.client.RagiumEnglishProvider
import hiiragi283.ragium.data.client.RagiumItemModelProvider
import hiiragi283.ragium.data.client.RagiumJapaneseProvider
import hiiragi283.ragium.data.server.RagiumAdvancementGenerator
import hiiragi283.ragium.data.server.RagiumBlockLootProvider
import hiiragi283.ragium.data.server.RagiumDataMapProvider
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import hiiragi283.ragium.data.server.bootstrap.RagiumBiomeModifierProvider
import hiiragi283.ragium.data.server.bootstrap.RagiumConfiguredProvider
import hiiragi283.ragium.data.server.bootstrap.RagiumEnchantmentProvider
import hiiragi283.ragium.data.server.bootstrap.RagiumPlacedProvider
import hiiragi283.ragium.data.server.tag.RagiumBlockTagProvider
import hiiragi283.ragium.data.server.tag.RagiumEnchantmentTagProvider
import hiiragi283.ragium.data.server.tag.RagiumFluidTagProvider
import hiiragi283.ragium.data.server.tag.RagiumItemTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.DataGenerator
import net.minecraft.data.PackOutput
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.data.AdvancementProvider
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent
import net.neoforged.neoforge.registries.NeoForgeRegistries
import org.slf4j.Logger
import java.util.concurrent.CompletableFuture

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = RagiumAPI.MOD_ID)
object RagiumDatagen {
    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val generator: DataGenerator = event.generator
        val output: PackOutput = generator.packOutput
        val helper: ExistingFileHelper = event.existingFileHelper
        val provider: CompletableFuture<HolderLookup.Provider> = generator
            .addProvider(
                event.includeServer(),
                DatapackBuiltinEntriesProvider(
                    output,
                    event.lookupProvider,
                    RegistrySetBuilder()
                        .add(Registries.ENCHANTMENT, RagiumEnchantmentProvider)
                        .add(Registries.CONFIGURED_FEATURE, RagiumConfiguredProvider)
                        .add(Registries.PLACED_FEATURE, RagiumPlacedProvider)
                        .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, RagiumBiomeModifierProvider),
                    setOf("minecraft", RagiumAPI.MOD_ID),
                ),
            ).registryProvider

        // server
        generator.addProvider(
            event.includeServer(),
            LootTableProvider(
                output,
                setOf(),
                listOf(LootTableProvider.SubProviderEntry(::RagiumBlockLootProvider, LootContextParamSets.BLOCK)),
                provider,
            ),
        )

        generator.addProvider(
            event.includeServer(),
            AdvancementProvider(
                output,
                provider,
                helper,
                listOf(RagiumAdvancementGenerator),
            ),
        )

        generator.addProvider(event.includeServer(), RagiumRecipeProvider(output, provider))

        generator.addProvider(event.includeServer(), RagiumEnchantmentTagProvider(output, provider, helper))
        generator.addProvider(event.includeServer(), RagiumFluidTagProvider(output, provider, helper))
        val blockTags: RagiumBlockTagProvider =
            generator.addProvider(event.includeServer(), RagiumBlockTagProvider(output, provider, helper))
        generator.addProvider(event.includeServer(), RagiumItemTagProvider(output, provider, blockTags, helper))

        generator.addProvider(event.includeServer(), RagiumDataMapProvider(output, provider))

        LOGGER.info("Gathered server resources!")
        // client
        generator.addProvider(event.includeClient(), ::RagiumEnglishProvider)
        generator.addProvider(event.includeClient(), ::RagiumJapaneseProvider)

        generator.addProvider(event.includeClient(), RagiumBlockStateProvider(output, helper))
        generator.addProvider(event.includeClient(), RagiumItemModelProvider(output, helper))

        LOGGER.info("Gathered client resources!")
    }
}
