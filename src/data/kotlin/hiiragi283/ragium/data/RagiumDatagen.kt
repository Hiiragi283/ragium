package hiiragi283.ragium.data

import com.simibubi.create.api.registry.CreateRegistries
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRootDataGenerator
import hiiragi283.ragium.api.data.advancement.HTAdvancementProvider
import hiiragi283.ragium.api.function.partially1
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.data.client.RagiumBlockStateProvider
import hiiragi283.ragium.data.client.RagiumEnglishProvider
import hiiragi283.ragium.data.client.RagiumItemModelProvider
import hiiragi283.ragium.data.client.RagiumJapaneseProvider
import hiiragi283.ragium.data.server.RagiumDataMapProvider
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import hiiragi283.ragium.data.server.advancement.RagiumAdvancementGenerator
import hiiragi283.ragium.data.server.bootstrap.RagiumBiomeModifierProvider
import hiiragi283.ragium.data.server.bootstrap.RagiumBrewingRecipeDataProvider
import hiiragi283.ragium.data.server.bootstrap.RagiumConfiguredProvider
import hiiragi283.ragium.data.server.bootstrap.RagiumEnchantmentProvider
import hiiragi283.ragium.data.server.bootstrap.RagiumPlacedProvider
import hiiragi283.ragium.data.server.bootstrap.RagiumPotatoProjectileProvider
import hiiragi283.ragium.data.server.loot.RagiumBlockLootProvider
import hiiragi283.ragium.data.server.loot.RagiumCustomLootProvider
import hiiragi283.ragium.data.server.loot.RagiumGlobalLootProvider
import hiiragi283.ragium.data.server.loot.RagiumLootTableProvider
import hiiragi283.ragium.data.server.tag.RagiumBlockEntityTypeTagsProvider
import hiiragi283.ragium.data.server.tag.RagiumBlockTagsProvider
import hiiragi283.ragium.data.server.tag.RagiumDamageTypeTagsProvider
import hiiragi283.ragium.data.server.tag.RagiumEnchantmentTagsProvider
import hiiragi283.ragium.data.server.tag.RagiumEntityTypeTagsProvider
import hiiragi283.ragium.data.server.tag.RagiumFluidTagsProvider
import hiiragi283.ragium.data.server.tag.RagiumItemTagsProvider
import hiiragi283.ragium.impl.material.RagiumMaterialManager
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.metadata.PackMetadataGenerator
import net.minecraft.data.tags.TagsProvider
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.concurrent.CompletableFuture

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumDatagen {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val (server: HTRootDataGenerator, client: HTRootDataGenerator) = HTRootDataGenerator.withDataPack(event) {
            add(Registries.ENCHANTMENT, RagiumEnchantmentProvider)
            add(Registries.CONFIGURED_FEATURE, RagiumConfiguredProvider)
            add(Registries.PLACED_FEATURE, RagiumPlacedProvider)

            add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, RagiumBiomeModifierProvider)

            add(RagiumAPI.BREWING_RECIPE_KEY, RagiumBrewingRecipeDataProvider)

            add(CreateRegistries.POTATO_PROJECTILE_TYPE, RagiumPotatoProjectileProvider)
        }

        RagiumMaterialManager.gatherAttributes()

        // server
        server.addProvider(
            RagiumLootTableProvider.create(
                ::RagiumBlockLootProvider to LootContextParamSets.BLOCK,
                RagiumCustomLootProvider::Block to LootContextParamSets.BLOCK,
                RagiumCustomLootProvider::Entity to LootContextParamSets.ENTITY,
            ),
        )
        server.addProvider(::RagiumGlobalLootProvider)

        server.addProvider(HTAdvancementProvider.create(RagiumAdvancementGenerator))

        server.addProvider(::RagiumRecipeProvider)

        server.addProvider(::RagiumBlockEntityTypeTagsProvider)
        server.addProvider(::RagiumDamageTypeTagsProvider)
        server.addProvider(::RagiumEnchantmentTagsProvider)
        server.addProvider(::RagiumEntityTypeTagsProvider)
        server.addProvider(::RagiumFluidTagsProvider)
        val blockContents: CompletableFuture<TagsProvider.TagLookup<Block>> = server.addProvider(::RagiumBlockTagsProvider).contentsGetter()
        server.addProvider(::RagiumItemTagsProvider.partially1(blockContents))

        server.addProvider(::RagiumDataMapProvider)

        server
            .createDataPackGenerator(RagiumAPI.id("work_in_progress"))
            .addProvider { output: PackOutput ->
                PackMetadataGenerator.forFeaturePack(
                    output,
                    RagiumTranslation.DATAPACK_WIP.translate(),
                    FeatureFlagSet.of(RagiumAPI.WORK_IN_PROGRESS),
                )
            }

        RagiumAPI.LOGGER.info("Gathered server resources!")
        // client
        client.addProvider(::RagiumEnglishProvider)
        client.addProvider(::RagiumJapaneseProvider)

        client.addProvider(::RagiumBlockStateProvider)
        client.addProvider(::RagiumItemModelProvider)

        RagiumAPI.LOGGER.info("Gathered client resources!")
    }
}
