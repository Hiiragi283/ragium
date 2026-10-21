package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.data.chemical.RagiumChemicals
import hiiragi283.ragium.api.data.element.RagiumElements
import hiiragi283.ragium.api.world.RagiumDamageTypes
import hiiragi283.ragium.data.advancement.RagiumAdvancementProvider
import hiiragi283.ragium.data.lang.RagiumEnglishLangProvider
import hiiragi283.ragium.data.lang.RagiumJapaneseLangProvider
import hiiragi283.ragium.data.loot.RagiumBlockLootTableProvider
import hiiragi283.ragium.data.loot.RagiumGlobalLootModifierProvider
import hiiragi283.ragium.data.loot.RagiumGlobalLootTableProvider
import hiiragi283.ragium.data.model.RagiumModelProvider
import hiiragi283.ragium.data.recipe.RagiumArcaneRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumChemicalRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumColoringRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumCommonRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumElectronicsRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumHeatRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumMechanicalRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumRecipePriorityProvider
import hiiragi283.ragium.data.recipe.RagiumResourceRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumVanillaRecipeProvider
import hiiragi283.ragium.data.tag.RagiumBlockTagsProvider
import hiiragi283.ragium.data.tag.RagiumDamageTypeTagsProvider
import hiiragi283.ragium.data.tag.RagiumFluidTagsProvider
import hiiragi283.ragium.data.tag.RagiumItemTagsProvider
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber
data object RagiumDataGen {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent.Client) {
        event.defaultPackGenerator.createWorldRegistryObjects(
            RegistrySetBuilder()
                .add(Registries.DAMAGE_TYPE, RagiumDamageTypes::bootstrap)
        )
        event.defaultPackGenerator.createReloadableRegistryObjects(
            RegistrySetBuilder()
                .add(
                    Registries.LOOT_TABLE,
                    LootTableProvider(
                        setOf(),
                        listOf(
                            LootTableProvider.SubProviderEntry(
                                ::RagiumBlockLootTableProvider,
                                LootContextParamSets.BLOCK
                            ),
                            LootTableProvider.SubProviderEntry(
                                RagiumGlobalLootTableProvider::ForBlock,
                                LootContextParamSets.BLOCK
                            ),
                            LootTableProvider.SubProviderEntry(
                                RagiumGlobalLootTableProvider::ForEntity,
                                LootContextParamSets.ENTITY
                            )
                        )
                    )
                )
                .add(RagiumRegistries.Keys.ELEMENT, RagiumElements::bootstrap)
                .add(RagiumRegistries.Keys.CHEMICAL, RagiumChemicals::bootstrap)
        )

        // Server
        event.createProvider(::RagiumAdvancementProvider)

        event.createProvider(::RagiumDataMapProvider)

        event.createProvider(::RagiumGlobalLootModifierProvider)

        event.createProvider(::RagiumVanillaRecipeProvider)
        event.createProvider(::RagiumCommonRecipeProvider)
        event.createProvider(::RagiumColoringRecipeProvider)

        event.createProvider(::RagiumMechanicalRecipeProvider)
        event.createProvider(::RagiumHeatRecipeProvider)
        event.createProvider(::RagiumChemicalRecipeProvider)
        event.createProvider(::RagiumResourceRecipeProvider)
        event.createProvider(::RagiumElectronicsRecipeProvider)
        event.createProvider(::RagiumArcaneRecipeProvider)

        event.createProvider(::RagiumRecipePriorityProvider)

        event.createProvider(::RagiumBlockTagsProvider)
        event.createProvider(::RagiumFluidTagsProvider)
        event.createProvider(::RagiumItemTagsProvider)

        event.createProvider(::RagiumDamageTypeTagsProvider)
        // Client
        event.createProvider(::RagiumSpriteSourceProvider)

        event.createProvider(::RagiumModelProvider)

        event.createProvider(::RagiumEnglishLangProvider)
        event.createProvider(::RagiumJapaneseLangProvider)
        // Report
        event.createProvider(::RagiumRegistryDumpReport)
    }
}
