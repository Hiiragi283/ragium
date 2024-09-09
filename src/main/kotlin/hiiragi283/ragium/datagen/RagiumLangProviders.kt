package hiiragi283.ragium.datagen

import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.recipe.HTMachineType
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

object RagiumLangProviders {

    @JvmStatic
    fun init(pack: FabricDataGenerator.Pack) {
        pack.addProvider(::EnglishLang)
        pack.addProvider(::JapaneseLang)
    }

    fun TranslationBuilder.add(type: HTMachineType, value: String) {
        add(type.translationKey, value)
    }

    private class EnglishLang(
        output: FabricDataOutput,
        registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>,
    ) : FabricLanguageProvider(output, registryLookup) {
        override fun generateTranslations(
            registryLookup: RegistryWrapper.WrapperLookup,
            builder: TranslationBuilder,
        ) {
            // Blocks
            builder.add(RagiumBlocks.RAGINITE_ORE, "Raginite Ore")
            builder.add(RagiumBlocks.DEEPSLATE_RAGINITE_ORE, "Deepslate Raginite Ore")
            builder.add(RagiumBlocks.CREATIVE_SOURCE, "Creative Power Source")
            builder.add(RagiumBlocks.MANUAL_GRINDER, "Manual Grinder")
            // Items
            builder.add(RagiumItems.POWER_METER, "Power Meter")

            builder.add(RagiumItems.RAW_RAGINITE, "Raw Raginite")
            builder.add(RagiumItems.RAW_RAGINITE_DUST, "Raw Raginite Dust")
            builder.add(RagiumItems.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound ")
            builder.add(RagiumItems.RAGI_ALLOY_INGOT, "Ragi-Alloy Ingot")

            builder.add(RagiumItems.RAGI_STEEL_INGOT, "Ragi-Steel Ingot")

            builder.add(RagiumItems.REFINED_RAGI_STEEL_INGOT, "Refined Ragi-Steel Ingot")
            // Machine Type
            builder.add(HTMachineType.GRINDER, "Grinder")
            builder.add(HTMachineType.ALLOY_FURNACE, "Alloy Furnace")
        }
    }

    class JapaneseLang(
        output: FabricDataOutput,
        registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>,
    ) : FabricLanguageProvider(output, "ja_jp", registryLookup) {
        override fun generateTranslations(
            registryLookup: RegistryWrapper.WrapperLookup,
            builder: TranslationBuilder,
        ) {
            // Blocks
            builder.add(RagiumBlocks.RAGINITE_ORE, "ラギナイト鉱石")
            builder.add(RagiumBlocks.DEEPSLATE_RAGINITE_ORE, "深層ラギナイト鉱石")
            builder.add(RagiumBlocks.CREATIVE_SOURCE, "クリエイティブ用エネルギー源")
            builder.add(RagiumBlocks.MANUAL_GRINDER, "石臼")
            // Items
            builder.add(RagiumItems.POWER_METER, "パワー測定器")

            builder.add(RagiumItems.RAW_RAGINITE, "ラギナイトの原石")
            builder.add(RagiumItems.RAW_RAGINITE_DUST, "ラギナイトの原石の粉")
            builder.add(RagiumItems.RAGI_ALLOY_COMPOUND, "ラギ合金混合物")
            builder.add(RagiumItems.RAGI_ALLOY_INGOT, "ラギ合金インゴット")

            builder.add(RagiumItems.RAGI_STEEL_INGOT, "ラギスチールインゴット")

            builder.add(RagiumItems.REFINED_RAGI_STEEL_INGOT, "精製ラギスチールインゴット")
            // Machine Type
            builder.add(HTMachineType.GRINDER, "粉砕機")
            builder.add(HTMachineType.ALLOY_FURNACE, "合金かまど")
        }
    }

}