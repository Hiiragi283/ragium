package hiiragi283.ragium.data

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.data.HTLangType
import hiiragi283.ragium.common.init.RagiumItemGroup
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.util.HTTranslationFormatter
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

    fun TranslationBuilder.add(element: RagiElement, value: String) {
        add(element.translationKey, value)
    }

    fun TranslationBuilder.add(type: HTMachineType, value: String) {
        add(type.translationKey, value)
    }

    @JvmStatic
    private fun translateStorageBlocks(builder: TranslationBuilder, type: HTLangType) {
        RagiumContents.StorageBlocks.entries.forEach { block: RagiumContents.StorageBlocks ->
            builder.add(
                block.block,
                block.getTranslation(type, block.material),
            )
        }
    }

    @JvmStatic
    private fun translateHulls(builder: TranslationBuilder, type: HTLangType) {
        RagiumContents.Hulls.entries.forEach { hull: RagiumContents.Hulls ->
            builder.add(
                hull.block,
                hull.getTranslation(type, hull.material),
            )
        }
    }

    @JvmStatic
    private fun translateElements(builder: TranslationBuilder, type: HTLangType) {
        RagiElement.entries.forEach { element ->
            when (type) {
                HTLangType.EN_US -> {
                    builder.add(element.buddingBlock, "Budding ${element.getTranslatedName(type)}")
                    builder.add(element.clusterBlock, "${element.getTranslatedName(type)} Cluster")
                }

                HTLangType.JA_JP -> {
                    builder.add(element.buddingBlock, "芽生えた${element.getTranslatedName(type)}")
                    builder.add(element.clusterBlock, "${element.getTranslatedName(type)}の塊")
                }
            }
        }
    }

    @JvmStatic
    private fun translateDusts(builder: TranslationBuilder, type: HTLangType) {
        RagiumItems.Dusts.entries.forEach { dust: RagiumItems.Dusts ->
            builder.add(
                dust.asItem(),
                when (type) {
                    HTLangType.EN_US -> dust.enName
                    HTLangType.JA_JP -> dust.jaName
                },
            )
        }
    }

    @JvmStatic
    private fun translateIngots(builder: TranslationBuilder, type: HTLangType) {
        RagiumItems.Ingots.entries.forEach { ingot: RagiumItems.Ingots ->
            builder.add(
                ingot.asItem(),
                ingot.getTranslation(type, ingot.material),
            )
        }
    }

    @JvmStatic
    private fun translatePlates(builder: TranslationBuilder, type: HTLangType) {
        RagiumItems.Plates.entries.forEach { plate: RagiumItems.Plates ->
            builder.add(
                plate.asItem(),
                plate.getTranslation(type, plate.material),
            )
        }
    }

    private data object FluidFormatter : HTTranslationFormatter {
        override val enPattern: String = "Fluid Cube (%s)"
        override val jaPattern: String = "液体キューブ（%s）"
    }

    @JvmStatic
    private fun translateFluids(builder: TranslationBuilder, type: HTLangType) {
        RagiumItems.Fluids.entries.forEach { fluid: RagiumItems.Fluids ->
            builder.add(
                fluid.asItem(),
                FluidFormatter.getTranslation(type, fluid),
            )
        }
    }

    //    English    //

    private class EnglishLang(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricLanguageProvider(output, registryLookup) {
        override fun generateTranslations(registryLookup: RegistryWrapper.WrapperLookup, builder: TranslationBuilder) {
            // Advancements
            RagiumAdvancementProvider.register.generateLang(HTLangType.EN_US, builder)
            // Blocks
            builder.add(RagiumContents.RAGINITE_ORE, "Raginite Ore")
            builder.add(RagiumContents.DEEPSLATE_RAGINITE_ORE, "Deep Raginite Ore")
            builder.add(RagiumContents.CREATIVE_SOURCE, "Creative Power Source")
            builder.add(RagiumContents.MANUAL_GRINDER, "Manual Grinder")
            builder.add(RagiumContents.BURNING_BOX, "Burning Box")
            builder.add(RagiumContents.WATER_GENERATOR, "Water Generator")
            builder.add(RagiumContents.WIND_GENERATOR, "Wind Generator")
            builder.add(RagiumContents.SHAFT, "Shaft")
            builder.add(RagiumContents.GEAR_BOX, "Gear Box")
            builder.add(RagiumContents.BLAZING_BOX, "Blazing Box")
            builder.add(RagiumContents.ALCHEMICAL_INFUSER, "Alchemical Infuser")
            builder.add(RagiumContents.ITEM_DISPLAY, "Item Display")

            translateStorageBlocks(builder, HTLangType.EN_US)
            translateHulls(builder, HTLangType.EN_US)
            translateElements(builder, HTLangType.EN_US)
            // Elements
            RagiElement.TRANSLATION_TABLE.column(HTLangType.EN_US).forEach { (element: RagiElement, value: String) ->
                builder.add(element, value)
            }
            // Items
            RagiumItems.REGISTER.generateLang(HTLangType.EN_US, builder)
            translateDusts(builder, HTLangType.EN_US)
            translateIngots(builder, HTLangType.EN_US)
            translatePlates(builder, HTLangType.EN_US)
            translateFluids(builder, HTLangType.EN_US)
            // Item Group
            builder.add(RagiumItemGroup.ITEM_KEY, "Ragium Items")
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

    //    Japanese    //

    class JapaneseLang(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricLanguageProvider(output, "ja_jp", registryLookup) {
        override fun generateTranslations(registryLookup: RegistryWrapper.WrapperLookup, builder: TranslationBuilder) {
            // Advancements
            RagiumAdvancementProvider.register.generateLang(HTLangType.JA_JP, builder)
            // Blocks
            builder.add(RagiumContents.RAGINITE_ORE, "ラギナイト鉱石")
            builder.add(RagiumContents.DEEPSLATE_RAGINITE_ORE, "深層ラギナイト鉱石")
            builder.add(RagiumContents.CREATIVE_SOURCE, "クリエイティブ用エネルギー源")
            builder.add(RagiumContents.MANUAL_GRINDER, "石臼")
            builder.add(RagiumContents.BURNING_BOX, "燃焼室")
            builder.add(RagiumContents.WATER_GENERATOR, "水力発電機")
            builder.add(RagiumContents.WIND_GENERATOR, "風力発電機")
            builder.add(RagiumContents.SHAFT, "シャフト")
            builder.add(RagiumContents.GEAR_BOX, "ギアボックス")
            builder.add(RagiumContents.BLAZING_BOX, "豪炎室")
            builder.add(RagiumContents.ALCHEMICAL_INFUSER, "錬金注入機")
            builder.add(RagiumContents.ITEM_DISPLAY, "アイテムティスプレイ")

            translateStorageBlocks(builder, HTLangType.JA_JP)
            translateHulls(builder, HTLangType.JA_JP)
            translateElements(builder, HTLangType.JA_JP)
            // Elements
            RagiElement.TRANSLATION_TABLE.column(HTLangType.JA_JP).forEach { (element: RagiElement, value: String) ->
                builder.add(element, value)
            }
            // Items
            RagiumItems.REGISTER.generateLang(HTLangType.JA_JP, builder)
            translateDusts(builder, HTLangType.JA_JP)
            translateIngots(builder, HTLangType.JA_JP)
            translatePlates(builder, HTLangType.JA_JP)
            translateFluids(builder, HTLangType.JA_JP)
            // Item Group
            builder.add(RagiumItemGroup.ITEM_KEY, "Ragium - アイテム")
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
