package hiiragi283.ragium.data

import hiiragi283.core.api.data.createLootTables
import hiiragi283.core.api.data.createProviderWithHelper
import hiiragi283.core.setup.HCEnchantments
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.data.advancement.RagiumAdvancementProvider
import hiiragi283.ragium.data.bootstrap.RagiumWorldData
import hiiragi283.ragium.data.lang.RagiumEnglishLangProvider
import hiiragi283.ragium.data.lang.RagiumJapaneseLangProvider
import hiiragi283.ragium.data.loot.RagiumBlockLootProvider
import hiiragi283.ragium.data.model.RagiumBlockStateProvider
import hiiragi283.ragium.data.model.RagiumItemModelProvider
import hiiragi283.ragium.data.recipe.RagiumArcaneRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumBasicRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumBioRecipeBuilder
import hiiragi283.ragium.data.recipe.RagiumChemicalRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumColdRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumFluidRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumHeatRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumMaterialRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumVanillaRecipeProvider
import hiiragi283.ragium.data.recipe.integration.RagiumAERecipeProvider
import hiiragi283.ragium.data.recipe.integration.RagiumIERecipeProvider
import hiiragi283.ragium.data.recipe.integration.RagiumMekRecipeProvider
import hiiragi283.ragium.data.recipe.integration.RagiumOritechRecipeProvider
import hiiragi283.ragium.data.tag.RagiumBlockTagsProvider
import hiiragi283.ragium.data.tag.RagiumFluidTagsProvider
import hiiragi283.ragium.data.tag.RagiumItemTagsProvider
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumDatagen {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val fileHelper: ExistingFileHelper = event.existingFileHelper
        event.createDatapackRegistryObjects(
            RegistrySetBuilder().add(Registries.ENCHANTMENT, HCEnchantments).also(RagiumWorldData::bootsrap),
        )
        // Server
        event.createProvider(::RagiumAdvancementProvider)
        event.createLootTables(::RagiumBlockLootProvider to LootContextParamSets.BLOCK)

        event.createProvider(::RagiumBasicRecipeProvider)
        event.createProvider(::RagiumChemicalRecipeProvider)
        event.createProvider(::RagiumColdRecipeProvider)
        event.createProvider(::RagiumFluidRecipeProvider)
        event.createProvider(::RagiumHeatRecipeProvider)
        event.createProvider(::RagiumMaterialRecipeProvider)
        event.createProvider(::RagiumArcaneRecipeProvider)
        event.createProvider(::RagiumBioRecipeBuilder)
        event.createProvider(::RagiumVanillaRecipeProvider)

        event.createProvider(::RagiumAERecipeProvider)
        event.createProvider(::RagiumIERecipeProvider)
        event.createProvider(::RagiumMekRecipeProvider)
        event.createProvider(::RagiumOritechRecipeProvider)

        event.createProviderWithHelper(::RagiumFluidTagsProvider)
        event.createBlockAndItemTags(
            { output: PackOutput, future: CompletableFuture<HolderLookup.Provider> -> RagiumBlockTagsProvider(fileHelper, output, future) },
            { output: PackOutput, future: CompletableFuture<HolderLookup.Provider>, future1: CompletableFuture<TagsProvider.TagLookup<Block>> -> RagiumItemTagsProvider(fileHelper, output, future, future1) },
        )

        event.createProvider(::RagiumDataMapProvider)
        // Client
        event.createProvider(::RagiumEnglishLangProvider)
        event.createProvider(::RagiumJapaneseLangProvider)

        event.createProviderWithHelper(::RagiumSpriteSourceProvider)

        event.createProviderWithHelper(::RagiumBlockStateProvider)
        event.createProvider(::RagiumItemModelProvider)
    }
}
