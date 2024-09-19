package hiiragi283.ragium.data

import hiiragi283.ragium.common.data.HTLangType
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItemGroup
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import hiiragi283.ragium.common.machine.HTMachineType
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

object RagiumLangProviders {
    @JvmStatic
    fun init(pack: FabricDataGenerator.Pack) {
        pack.addProvider(RagiumLangProviders::EnglishLang)
        pack.addProvider(RagiumLangProviders::JapaneseLang)
    }

    fun TranslationBuilder.add(type: HTMachineType, value: String) {
        add(type.translationKey, value)
    }

    private class EnglishLang(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricLanguageProvider(output, registryLookup) {
        override fun generateTranslations(registryLookup: RegistryWrapper.WrapperLookup, builder: TranslationBuilder) {
            // Advancements
            RagiumAdvancementProvider.register.generateLang(HTLangType.EN_US, builder)
            // Blocks
            RagiumBlocks.REGISTER.generateLang(HTLangType.EN_US, builder)
            // Items
            RagiumItems.REGISTER.generateLang(HTLangType.EN_US, builder)
            // Item Group
            builder.add(RagiumItemGroup.ITEM, "Ragium Items")
            // Machine
            builder.add(RagiumTranslationKeys.MULTI_SHAPE_ERROR, "Not matching condition; %s at %ss")
            builder.add(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS, "The structure is valid!")
            // Machine Type
            builder.add(HTMachineType.Single.ALLOY_FURNACE, "Alloy Furnace")

            builder.add(HTMachineType.Single.COMPRESSOR, "Compressor")
            builder.add(HTMachineType.Single.EXTRACTOR, "Extractor")
            builder.add(HTMachineType.Single.GRINDER, "Grinder")
            builder.add(HTMachineType.Single.METAL_FORMER, "Metal Former")
            builder.add(HTMachineType.Single.MIXER, "Mixer")

            builder.add(HTMachineType.Single.CENTRIFUGE, "Centrifuge")
            builder.add(HTMachineType.Single.CHEMICAL_REACTOR, "Chemical Reactor")
            builder.add(HTMachineType.Single.ELECTROLYZER, "Electrolyzer")

            builder.add(HTMachineType.Multi.BRICK_BLAST_FURNACE, "Brick Blast Furnace")
            builder.add(HTMachineType.Multi.BLAZING_BLAST_FURNACE, "Blazing Blast Furnace")
            builder.add(HTMachineType.Multi.DISTILLATION_TOWER, "Distillation Tower")

            // builder.add(HTMachineType.Single.ALCHEMICAL_INFUSER, "Alchemical Infuser")
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
            // Advancements
            RagiumAdvancementProvider.register.generateLang(HTLangType.JA_JP, builder)
            // Blocks
            RagiumBlocks.REGISTER.generateLang(HTLangType.JA_JP, builder)
            // Items
            RagiumItems.REGISTER.generateLang(HTLangType.JA_JP, builder)
            // Item Group
            builder.add(RagiumItemGroup.ITEM, "Ragium - アイテム")
            // Machine
            builder.add(RagiumTranslationKeys.MULTI_SHAPE_ERROR, "次の条件を満たしていません; %s (座標 %s)")
            builder.add(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS, "構造物は有効です！")
            // Machine Type
            builder.add(HTMachineType.Single.ALLOY_FURNACE, "合金かまど")

            builder.add(HTMachineType.Single.COMPRESSOR, "圧縮機")
            builder.add(HTMachineType.Single.EXTRACTOR, "抽出器")
            builder.add(HTMachineType.Single.GRINDER, "粉砕機")
            builder.add(HTMachineType.Single.METAL_FORMER, "金属加工機")
            builder.add(HTMachineType.Single.MIXER, "ミキサー")

            builder.add(HTMachineType.Single.CENTRIFUGE, "遠心分離機")
            builder.add(HTMachineType.Single.CHEMICAL_REACTOR, "化学反応槽")
            builder.add(HTMachineType.Single.ELECTROLYZER, "電解槽")

            builder.add(HTMachineType.Multi.BRICK_BLAST_FURNACE, "レンガ高炉")
            builder.add(HTMachineType.Multi.BLAZING_BLAST_FURNACE, "ブレイズ高炉")
            builder.add(HTMachineType.Multi.DISTILLATION_TOWER, "蒸留塔")

            // builder.add(HTMachineType.Single.ALCHEMICAL_INFUSER, "錬金注入機")
            // Mod Menu
            builder.add(RagiumTranslationKeys.CONFIG_IS_HARD_MODE, "ハードモードを有効")
            // Jade
            builder.add(RagiumTranslationKeys.CONFIG_JADE_BURNING_BOX, "燃焼室")
            // REI
        }
    }
}
