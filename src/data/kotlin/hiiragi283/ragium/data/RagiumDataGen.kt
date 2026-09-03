package hiiragi283.ragium.data

import hiiragi283.lib.data.createLootTables
import hiiragi283.ragium.data.advancement.RagiumAdvancementProvider
import hiiragi283.ragium.data.lang.RagiumEnglishLangProvider
import hiiragi283.ragium.data.lang.RagiumJapaneseLangProvider
import hiiragi283.ragium.data.loot.RagiumBlockLootTableProvider
import hiiragi283.ragium.data.loot.RagiumGlobalLootModifierProvider
import hiiragi283.ragium.data.loot.RagiumGlobalLootTableProvider
import hiiragi283.ragium.data.model.RagiumModelProvider
import hiiragi283.ragium.data.recipe.RagiumBioRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumChemicalRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumHeatRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumMechanicalRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumRecipePriorityProvider
import hiiragi283.ragium.data.recipe.RagiumVanillaRecipeProvider
import hiiragi283.ragium.data.tag.RagiumBlockTagsProvider
import hiiragi283.ragium.data.tag.RagiumFluidTagsProvider
import hiiragi283.ragium.data.tag.RagiumItemTagsProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber
data object RagiumDataGen {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent.Client) {
        // Server
        event.createProvider(::RagiumAdvancementProvider)

        event.createProvider(::RagiumDataMapProvider)

        event.createLootTables(
            ::RagiumBlockLootTableProvider to LootContextParamSets.BLOCK,
            RagiumGlobalLootTableProvider::EntityProvider to LootContextParamSets.ENTITY
        )
        event.createProvider(::RagiumGlobalLootModifierProvider)

        event.createProvider(::RagiumBioRecipeProvider)
        event.createProvider(::RagiumChemicalRecipeProvider)
        event.createProvider(::RagiumHeatRecipeProvider)
        event.createProvider(::RagiumMechanicalRecipeProvider)
        event.createProvider(::RagiumVanillaRecipeProvider)

        event.createProvider(::RagiumRecipePriorityProvider)

        event.createProvider(::RagiumFluidTagsProvider)
        event.createBlockAndItemTags(::RagiumBlockTagsProvider, ::RagiumItemTagsProvider)
        // Client
        event.createProvider(::RagiumSpriteSourceProvider)

        event.createProvider(::RagiumModelProvider)

        event.createProvider(::RagiumEnglishLangProvider)
        event.createProvider(::RagiumJapaneseLangProvider)
    }
}
