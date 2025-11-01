package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.data.advancement.HTAdvancementProvider
import hiiragi283.ragium.data.client.RagiumBlockStateProvider
import hiiragi283.ragium.data.client.RagiumEnglishProvider
import hiiragi283.ragium.data.client.RagiumItemModelProvider
import hiiragi283.ragium.data.client.RagiumJapaneseProvider
import hiiragi283.ragium.data.server.RagiumDataMapProvider
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import hiiragi283.ragium.data.server.advancement.RagiumAdvancementGenerator
import hiiragi283.ragium.data.server.bootstrap.RagiumBiomeModifierProvider
import hiiragi283.ragium.data.server.bootstrap.RagiumBrewingEffectProvider
import hiiragi283.ragium.data.server.bootstrap.RagiumConfiguredProvider
import hiiragi283.ragium.data.server.bootstrap.RagiumEnchantmentProvider
import hiiragi283.ragium.data.server.bootstrap.RagiumPlacedProvider
import hiiragi283.ragium.data.server.bootstrap.RagiumSolarPowerProvider
import hiiragi283.ragium.data.server.loot.RagiumBlockLootProvider
import hiiragi283.ragium.data.server.loot.RagiumCustomLootProvider
import hiiragi283.ragium.data.server.loot.RagiumGlobalLootProvider
import hiiragi283.ragium.data.server.loot.RagiumLootTableProvider
import hiiragi283.ragium.data.server.tag.RagiumBlockTagsProvider
import hiiragi283.ragium.data.server.tag.RagiumDamageTypeTagsProvider
import hiiragi283.ragium.data.server.tag.RagiumEnchantmentTagsProvider
import hiiragi283.ragium.data.server.tag.RagiumEntityTypeTagsProvider
import hiiragi283.ragium.data.server.tag.RagiumFluidTagsProvider
import hiiragi283.ragium.data.server.tag.RagiumItemTagsProvider
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent
import net.neoforged.neoforge.registries.NeoForgeRegistries

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumDatagen {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val context: HTDataGenContext = HTDataGenContext.withDataPack(event) {
            add(Registries.ENCHANTMENT, RagiumEnchantmentProvider)
            add(Registries.CONFIGURED_FEATURE, RagiumConfiguredProvider)
            add(Registries.PLACED_FEATURE, RagiumPlacedProvider)

            add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, RagiumBiomeModifierProvider)

            add(RagiumAPI.BREWING_EFFECT_KEY, RagiumBrewingEffectProvider)
            add(RagiumAPI.SOLAR_POWER_KEY, RagiumSolarPowerProvider)
        }

        // server
        context.addProvider(
            event.includeServer(),
            RagiumLootTableProvider.create(
                ::RagiumBlockLootProvider to LootContextParamSets.BLOCK,
                RagiumCustomLootProvider::Block to LootContextParamSets.BLOCK,
                RagiumCustomLootProvider::Entity to LootContextParamSets.ENTITY,
            ),
        )
        context.addProvider(event.includeServer(), ::RagiumGlobalLootProvider)

        context.addProvider(event.includeServer(), HTAdvancementProvider.create(RagiumAdvancementGenerator))

        context.addProvider(event.includeServer(), ::RagiumRecipeProvider)

        context.addProvider(event.includeServer(), ::RagiumDamageTypeTagsProvider)
        context.addProvider(event.includeServer(), ::RagiumEnchantmentTagsProvider)
        context.addProvider(event.includeServer(), ::RagiumEntityTypeTagsProvider)
        context.addProvider(event.includeServer(), ::RagiumFluidTagsProvider)
        context.addProvider(event.includeServer(), ::RagiumBlockTagsProvider).contentsGetter().apply {
            context.addProvider(event.includeServer(), RagiumItemTagsProvider(this, context))
        }

        context.addProvider(event.includeServer(), ::RagiumDataMapProvider)

        RagiumAPI.LOGGER.info("Gathered server resources!")
        // client
        context.addProvider(event.includeClient(), ::RagiumEnglishProvider)
        context.addProvider(event.includeClient(), ::RagiumJapaneseProvider)

        context.addProvider(event.includeClient(), ::RagiumBlockStateProvider)
        context.addProvider(event.includeClient(), ::RagiumItemModelProvider)

        RagiumAPI.LOGGER.info("Gathered client resources!")
    }
}
