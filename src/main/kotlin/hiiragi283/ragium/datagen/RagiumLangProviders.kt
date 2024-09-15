package hiiragi283.ragium.datagen

import hiiragi283.ragium.common.data.HTLangType
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumTranslationKeys
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

    private class EnglishLang(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricLanguageProvider(output, registryLookup) {
        override fun generateTranslations(registryLookup: RegistryWrapper.WrapperLookup, builder: TranslationBuilder) {
            // Blocks
            RagiumBlocks.REGISTER.generateLang(HTLangType.EN_US, builder)
            // Items
            RagiumItems.REGISTER.generateLang(HTLangType.EN_US, builder)
            // Item Group
            builder.add(RagiumTranslationKeys.ITEM_GROUP_ITEM, "Ragium Items")
            // Machine
            builder.add(RagiumTranslationKeys.MULTI_SHAPE_ERROR, "Not matching condition; %s at %s")
            builder.add(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS, "The structure is valid!")
            // Machine Type
            builder.add(HTMachineType.Single.ALLOY_FURNACE, "Alloy Furnace")
            builder.add(HTMachineType.Multi.BRICK_BLAST_FURNACE, "Steam Blast Furnace")

            builder.add(HTMachineType.Single.COMPRESSOR, "Compressor")
            builder.add(HTMachineType.Single.EXTRACTOR, "Extractor")
            builder.add(HTMachineType.Single.GRINDER, "Grinder")
            builder.add(HTMachineType.Single.METAL_FORMER, "Metal Former")
            builder.add(HTMachineType.Single.MIXER, "Mixer")
            builder.add(HTMachineType.Single.WASHER, "Washer")
            // Mod Menu
            builder.add(RagiumTranslationKeys.CONFIG_IS_HARD_MODE, "Enable Hard Mode")
            // Jade
            builder.add(RagiumTranslationKeys.CONFIG_JADE_BURNING_BOX, "Burning Box")
            // REI
        }
    }

    class JapaneseLang(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricLanguageProvider(output, "ja_jp", registryLookup) {
        override fun generateTranslations(registryLookup: RegistryWrapper.WrapperLookup, builder: TranslationBuilder) {
            // Blocks
            RagiumBlocks.REGISTER.generateLang(HTLangType.JA_JP, builder)
            // Items
            RagiumItems.REGISTER.generateLang(HTLangType.JA_JP, builder)
            // Item Group
            builder.add(RagiumTranslationKeys.ITEM_GROUP_ITEM, "Ragium - アイテム")
            // Machine
            builder.add(RagiumTranslationKeys.MULTI_SHAPE_ERROR, "次の条件を満たしていません; %s (座標 %s)")
            builder.add(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS, "構造物は有効です！")
            // Machine Type
            builder.add(HTMachineType.Single.ALLOY_FURNACE, "合金かまど")
            builder.add(HTMachineType.Multi.BRICK_BLAST_FURNACE, "蒸気高炉")

            builder.add(HTMachineType.Single.COMPRESSOR, "圧縮機")
            builder.add(HTMachineType.Single.EXTRACTOR, "抽出器")
            builder.add(HTMachineType.Single.GRINDER, "粉砕機")
            builder.add(HTMachineType.Single.METAL_FORMER, "金属加工機")
            builder.add(HTMachineType.Single.MIXER, "ミキサー")
            builder.add(HTMachineType.Single.WASHER, "洗浄機")

            // Mod Menu
            builder.add(RagiumTranslationKeys.CONFIG_IS_HARD_MODE, "ハードモードを有効")
            // Jade
            builder.add(RagiumTranslationKeys.CONFIG_JADE_BURNING_BOX, "燃焼室")
            // REI
        }
    }
}