package hiiragi283.ragium.data

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.data.HTLangType
import hiiragi283.ragium.common.init.RagiumItemGroup
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.util.HTBlockContent
import hiiragi283.ragium.common.util.HTItemContent
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
    private fun translateContents(builder: TranslationBuilder, type: HTLangType) {
        // blocks
        buildList<HTBlockContent> {
            addAll(RagiumContents.StorageBlocks.entries)
            addAll(RagiumContents.Hulls.entries)
            addAll(RagiumContents.Coils.entries)
        }.forEach { block: HTBlockContent -> builder.add(block.block, block.getTranslation(type)) }
        // elements
        RagiElement.entries.forEach { element: RagiElement ->
            val translatedName: String = element.getTranslation(type)
            builder.add(element.translationKey, translatedName)
            when (type) {
                HTLangType.EN_US -> {
                    builder.add(element.buddingBlock, "Budding $translatedName")
                    builder.add(element.clusterBlock, "$translatedName Cluster")
                    builder.add(element.dustItem, "$translatedName Dust")
                }

                HTLangType.JA_JP -> {
                    builder.add(element.buddingBlock, "芽生えた$translatedName")
                    builder.add(element.clusterBlock, "${translatedName}の塊")
                    builder.add(element.dustItem, "${translatedName}の粉")
                }
            }
        }
        // dusts
        // items
        buildList<HTItemContent> {
            addAll(RagiumContents.Dusts.entries)
            addAll(RagiumContents.Ingots.entries)
            addAll(RagiumContents.Plates.entries)
        }.forEach { item: HTItemContent -> builder.add(item.item, item.getTranslation(type)) }
        // fluids
        RagiumContents.Fluids.entries.forEach { fluid: RagiumContents.Fluids ->
            builder.add(
                fluid.asItem(),
                FluidFormatter.getTranslation(type, fluid),
            )
        }
    }

    private data object FluidFormatter : HTTranslationFormatter {
        override val enPattern: String = "Fluid Cube (%s)"
        override val jaPattern: String = "液体キューブ（%s）"
    }

    //    English    //

    private class EnglishLang(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricLanguageProvider(output, registryLookup) {
        override fun generateTranslations(registryLookup: RegistryWrapper.WrapperLookup, builder: TranslationBuilder) {
            translateContents(builder, HTLangType.EN_US)
            // Advancements
            RagiumAdvancementProvider.register.generateLang(HTLangType.EN_US, builder)
            // Blocks
            builder.add(RagiumContents.RAGINITE_ORE, "Raginite Ore")
            builder.add(RagiumContents.DEEPSLATE_RAGINITE_ORE, "Deep Raginite Ore")

            builder.add(RagiumContents.RUBBER_LOG, "Rubber Log")
            builder.add(RagiumContents.RUBBER_LEAVES, "Rubber Leaves")
            builder.add(RagiumContents.RUBBER_SAPLING, "Rubber Sapling")

            builder.add(RagiumContents.CREATIVE_SOURCE, "Creative Power Source")
            builder.add(RagiumContents.MANUAL_GRINDER, "Manual Grinder")
            builder.add(RagiumContents.BRICK_ALLOY_FURNACE, "Brick Alloy Furnace")
            builder.add(RagiumContents.BURNING_BOX, "Burning Box")
            builder.add(RagiumContents.WATER_GENERATOR, "Water Generator")
            builder.add(RagiumContents.WIND_GENERATOR, "Wind Generator")
            builder.add(RagiumContents.SHAFT, "Shaft")
            builder.add(RagiumContents.CABLE, "Cable")
            builder.add(RagiumContents.GEAR_BOX, "Gear Box")
            builder.add(RagiumContents.BLAZING_BOX, "Blazing Box")
            builder.add(RagiumContents.ALCHEMICAL_INFUSER, "Alchemical Infuser")
            builder.add(RagiumContents.ITEM_DISPLAY, "Item Display")
            // Items
            builder.add(RagiumContents.FORGE_HAMMER, "Forge Hammer")
            builder.add(RagiumContents.STEEL_SWORD, "Steel Sword")
            builder.add(RagiumContents.STEEL_SHOVEL, "Steel Shovel")
            builder.add(RagiumContents.STEEL_PICKAXE, "Steel Pickaxe")
            builder.add(RagiumContents.STEEL_AXE, "Steel Axe")
            builder.add(RagiumContents.STEEL_HOE, "Steel Hoe")
            builder.add(RagiumContents.BACKPACK, "Backpack")
            builder.add(RagiumContents.LARGE_BACKPACK, "Large Backpack")
            builder.add(RagiumContents.ENDER_BACKPACK, "Ender Backpack")

            builder.add(RagiumContents.RAW_RAGINITE, "Raw Raginite")
            builder.add(RagiumContents.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound")
            builder.add(RagiumContents.EMPTY_FLUID_CUBE, "Fluid Cube (Empty)")
            builder.add(RagiumContents.SOAP_INGOT, "Soap Ingot")
            builder.add(RagiumContents.RAW_RUBBER_BALL, "Raw Rubber Ball")
            builder.add(RagiumContents.RAGI_CRYSTAL, "Ragi-Crystal")
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
            builder.add(HTMachineType.Single.ROCK_GENERATOR, "Rock Generator")

            builder.add(HTMachineType.Single.CENTRIFUGE, "Centrifuge")
            builder.add(HTMachineType.Single.CHEMICAL_REACTOR, "Chemical Reactor")
            builder.add(HTMachineType.Single.ELECTROLYZER, "Electrolyzer")

            builder.add(HTMachineType.Multi.BRICK_BLAST_FURNACE, "Brick Blast Furnace")
            builder.add(HTMachineType.Multi.BLAZING_BLAST_FURNACE, "Blazing Blast Furnace")
            builder.add(HTMachineType.Multi.ELECTRIC_BLAST_FURNACE, "Electric Blast Furnace")
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
            translateContents(builder, HTLangType.JA_JP)
            // Advancements
            RagiumAdvancementProvider.register.generateLang(HTLangType.JA_JP, builder)
            // Blocks
            builder.add(RagiumContents.RAGINITE_ORE, "ラギナイト鉱石")
            builder.add(RagiumContents.DEEPSLATE_RAGINITE_ORE, "深層ラギナイト鉱石")

            builder.add(RagiumContents.RUBBER_LOG, "ゴムノキの原木")
            builder.add(RagiumContents.RUBBER_LEAVES, "ゴムノキの葉")
            builder.add(RagiumContents.RUBBER_SAPLING, "ゴムノキの苗木")

            builder.add(RagiumContents.CREATIVE_SOURCE, "クリエイティブ用エネルギー源")
            builder.add(RagiumContents.MANUAL_GRINDER, "石臼")
            builder.add(RagiumContents.BRICK_ALLOY_FURNACE, "レンガ合金かまど")
            builder.add(RagiumContents.BURNING_BOX, "燃焼室")
            builder.add(RagiumContents.WATER_GENERATOR, "水力発電機")
            builder.add(RagiumContents.WIND_GENERATOR, "風力発電機")
            builder.add(RagiumContents.SHAFT, "シャフト")
            builder.add(RagiumContents.CABLE, "ケーブル")
            builder.add(RagiumContents.GEAR_BOX, "ギアボックス")
            builder.add(RagiumContents.BLAZING_BOX, "豪炎室")
            builder.add(RagiumContents.ALCHEMICAL_INFUSER, "錬金注入機")
            builder.add(RagiumContents.ITEM_DISPLAY, "アイテムティスプレイ")
            // Items
            builder.add(RagiumContents.FORGE_HAMMER, "鍛造ハンマー")
            builder.add(RagiumContents.STEEL_SWORD, "鋼鉄の剣")
            builder.add(RagiumContents.STEEL_SHOVEL, "鋼鉄のシャベル")
            builder.add(RagiumContents.STEEL_PICKAXE, "鋼鉄のツルハシ")
            builder.add(RagiumContents.STEEL_AXE, "鋼鉄の斧")
            builder.add(RagiumContents.STEEL_HOE, "鋼鉄のクワ")
            builder.add(RagiumContents.BACKPACK, "バックパック")
            builder.add(RagiumContents.LARGE_BACKPACK, "大型パックパック")
            builder.add(RagiumContents.ENDER_BACKPACK, "エンダーパックパック")

            builder.add(RagiumContents.RAW_RAGINITE, "ラギナイトの原石")
            builder.add(RagiumContents.RAGI_ALLOY_COMPOUND, "ラギ合金混合物")
            builder.add(RagiumContents.EMPTY_FLUID_CUBE, "液体キューブ（なし）")
            builder.add(RagiumContents.SOAP_INGOT, "石鹸インゴット")
            builder.add(RagiumContents.RAW_RUBBER_BALL, "生ゴムボール")
            builder.add(RagiumContents.RAGI_CRYSTAL, "ラギクリスタリル")
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
            builder.add(HTMachineType.Single.ROCK_GENERATOR, "岩石生成機")

            builder.add(HTMachineType.Single.CENTRIFUGE, "遠心分離機")
            builder.add(HTMachineType.Single.CHEMICAL_REACTOR, "化学反応槽")
            builder.add(HTMachineType.Single.ELECTROLYZER, "電解槽")

            builder.add(HTMachineType.Multi.BRICK_BLAST_FURNACE, "レンガ高炉")
            builder.add(HTMachineType.Multi.BLAZING_BLAST_FURNACE, "ブレイズ高炉")
            builder.add(HTMachineType.Multi.ELECTRIC_BLAST_FURNACE, "電気高炉")
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
