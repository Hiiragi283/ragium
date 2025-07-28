package hiiragi283.ragium.data

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.data.client.RagiumBlockStateProvider
import hiiragi283.ragium.data.client.RagiumEnglishProvider
import hiiragi283.ragium.data.client.RagiumItemModelProvider
import hiiragi283.ragium.data.client.RagiumJapaneseProvider
import hiiragi283.ragium.data.server.RagiumAdvancementGenerator
import hiiragi283.ragium.data.server.RagiumDataMapProvider
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import hiiragi283.ragium.data.server.bootstrap.RagiumBiomeModifierProvider
import hiiragi283.ragium.data.server.bootstrap.RagiumConfiguredProvider
import hiiragi283.ragium.data.server.bootstrap.RagiumEnchantmentProvider
import hiiragi283.ragium.data.server.bootstrap.RagiumPlacedProvider
import hiiragi283.ragium.data.server.loot.RagiumBlockLootProvider
import hiiragi283.ragium.data.server.loot.RagiumCustomLootProvider
import hiiragi283.ragium.data.server.loot.RagiumGlobalLootProvider
import hiiragi283.ragium.data.server.tag.RagiumBlockTagsProvider
import hiiragi283.ragium.data.server.tag.RagiumDamageTypeTagsProvider
import hiiragi283.ragium.data.server.tag.RagiumEnchantmentTagsProvider
import hiiragi283.ragium.data.server.tag.RagiumEntityTypeTagsProvider
import hiiragi283.ragium.data.server.tag.RagiumFluidTagsProvider
import hiiragi283.ragium.data.server.tag.RagiumItemTagsProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.DataGenerator
import net.minecraft.data.PackOutput
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.data.tags.TagsProvider
import net.minecraft.world.level.block.Block
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

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
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
                    setOf(RagiumConst.MINECRAFT, RagiumAPI.MOD_ID),
                ),
            ).registryProvider

        // server
        generator.addProvider(
            event.includeServer(),
            LootTableProvider(
                output,
                setOf(),
                listOf(
                    LootTableProvider.SubProviderEntry(::RagiumBlockLootProvider, LootContextParamSets.BLOCK),
                    LootTableProvider.SubProviderEntry(RagiumCustomLootProvider::Block, LootContextParamSets.BLOCK),
                    LootTableProvider.SubProviderEntry(RagiumCustomLootProvider::Entity, LootContextParamSets.ENTITY),
                ),
                provider,
            ),
        )
        generator.addProvider(event.includeServer(), RagiumGlobalLootProvider(output, provider))

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

        generator.addProvider(event.includeServer(), RagiumDamageTypeTagsProvider(output, provider, helper))
        generator.addProvider(event.includeServer(), RagiumEnchantmentTagsProvider(output, provider, helper))
        generator.addProvider(event.includeServer(), RagiumEntityTypeTagsProvider(output, provider, helper))
        generator.addProvider(event.includeServer(), RagiumFluidTagsProvider(output, provider, helper))
        val blockTags: CompletableFuture<TagsProvider.TagLookup<Block>> =
            generator.addProvider(event.includeServer(), RagiumBlockTagsProvider(output, provider, helper)).contentsGetter()
        generator.addProvider(event.includeServer(), RagiumItemTagsProvider(output, provider, blockTags, helper))

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
