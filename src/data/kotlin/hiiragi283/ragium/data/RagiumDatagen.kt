package hiiragi283.ragium.data

import hiiragi283.core.api.data.createAdvancements
import hiiragi283.core.api.data.createLootTables
import hiiragi283.core.api.data.createProviderWithHelper
import hiiragi283.core.api.function.partially1
import hiiragi283.core.data.bootsrap.HCEnchantmentProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.data.advancement.RagiumAdvancementProvider
import hiiragi283.ragium.data.lang.RagiumEnglishLangProvider
import hiiragi283.ragium.data.lang.RagiumJapaneseLangProvider
import hiiragi283.ragium.data.loot.RagiumBlockLootProvider
import hiiragi283.ragium.data.model.RagiumBlockStateProvider
import hiiragi283.ragium.data.model.RagiumItemModelProvider
import hiiragi283.ragium.data.tag.RagiumBlockTagsProvider
import hiiragi283.ragium.data.tag.RagiumFluidTagsProvider
import hiiragi283.ragium.data.tag.RagiumItemTagsProvider
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
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
            RegistrySetBuilder().add(Registries.ENCHANTMENT, HCEnchantmentProvider),
        )
        // Server
        event.createAdvancements(listOf(RagiumAdvancementProvider))
        event.createLootTables(::RagiumBlockLootProvider to LootContextParamSets.BLOCK)

        event.createProvider(::RagiumRecipeProvider)

        event.createProviderWithHelper(::RagiumFluidTagsProvider)
        event.createBlockAndItemTags(::RagiumBlockTagsProvider.partially1(fileHelper), ::RagiumItemTagsProvider.partially1(fileHelper))

        event.createProvider(::RagiumDataMapProvider)
        // Client
        event.createProvider(::RagiumEnglishLangProvider)
        event.createProvider(::RagiumJapaneseLangProvider)

        event.createProviderWithHelper(::RagiumSpriteSourceProvider)

        event.createProviderWithHelper(::RagiumBlockStateProvider)
        event.createProviderWithHelper(::RagiumItemModelProvider)
    }
}
