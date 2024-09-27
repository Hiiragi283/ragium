package hiiragi283.ragium.data

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.data.HTLangType
import hiiragi283.ragium.common.init.RagiumItemGroup
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import hiiragi283.ragium.common.machine.HTMachineTier
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

    fun TranslationBuilder.add(tier: HTMachineTier, name: String, prefix: String) {
        add(tier.translationKey, name)
        add(tier.prefixKey, prefix)
    }

    fun TranslationBuilder.add(type: HTMachineType<*>, value: String) {
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
        // items
        buildList<HTItemContent> {
            addAll(RagiumContents.Dusts.entries)
            addAll(RagiumContents.Ingots.entries)
            addAll(RagiumContents.Plates.entries)
        }.forEach { item: HTItemContent -> builder.add(item.item, item.getTranslation(type)) }
        // circuits
        RagiumContents.Circuit.entries.forEach { circuit: RagiumContents.Circuit ->
            builder.add(
                circuit.asItem(),
                CircuitFormatter.getTranslation(type, circuit),
            )
        }
        // fluids
        RagiumContents.Fluids.entries.forEach { fluid: RagiumContents.Fluids ->
            builder.add(
                fluid.asItem(),
                FluidFormatter.getTranslation(type, fluid),
            )
        }
    }

    private data object CircuitFormatter : HTTranslationFormatter {
        override val enPattern: String = "%s Circuit"
        override val jaPattern: String = "%s回路"
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
            builder.add(RagiumContents.DATA_DRIVE, "Data Drive")
            builder.add(RagiumContents.DRIVE_SCANNER, "Drive Scanner")
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
            builder.add(RagiumTranslationKeys.MACHINE_NAME, "Name: %s")
            builder.add(RagiumTranslationKeys.MACHINE_TIER, "Tier: %s")
            builder.add(RagiumTranslationKeys.MACHINE_RECIPE_COST, "Recipe cost: %s E")
            builder.add(RagiumTranslationKeys.MACHINE_ENERGY_CAPACITY, "Energy Capacity: %s E")

            builder.add(RagiumTranslationKeys.MULTI_SHAPE_ERROR, "Not matching condition; %s at %ss")
            builder.add(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS, "The structure is valid!")
            // Machine Tier
            // builder.add(HTMachineTier.NONE, "None", "%s")
            builder.add(HTMachineTier.PRIMITIVE, "Primitive", "Primitive %s")
            builder.add(HTMachineTier.BASIC, "Basic", "Basic %s")
            builder.add(HTMachineTier.ADVANCED, "Advanced", "Advanced %s")
            // Machine Type
            builder.add(RagiumMachineTypes.Single.ALLOY_FURNACE, "Alloy Furnace")
            builder.add(RagiumMachineTypes.Single.ASSEMBLER, "Assembler")
            builder.add(RagiumMachineTypes.Single.CENTRIFUGE, "Centrifuge")
            builder.add(RagiumMachineTypes.Single.CHEMICAL_REACTOR, "Chemical Reactor")
            builder.add(RagiumMachineTypes.Single.COMPRESSOR, "Compressor")
            builder.add(RagiumMachineTypes.Single.ELECTROLYZER, "Electrolyzer")
            builder.add(RagiumMachineTypes.Single.EXTRACTOR, "Extractor")
            builder.add(RagiumMachineTypes.Single.GRINDER, "Grinder")
            builder.add(RagiumMachineTypes.Single.METAL_FORMER, "Metal Former")
            builder.add(RagiumMachineTypes.Single.MIXER, "Mixer")
            builder.add(RagiumMachineTypes.Single.ROCK_GENERATOR, "Rock Generator")

            builder.add(RagiumMachineTypes.BLAST_FURNACE, "Large Blast Furnace")
            builder.add(RagiumMachineTypes.DISTILLATION_TOWER, "Distillation Tower")
            // Mod Menu
            builder.add(RagiumTranslationKeys.CONFIG_TILE, "Ragium - Config")
            builder.add(RagiumTranslationKeys.CONFIG_IS_HARD_MODE, "Enable Hard Mode (Run `/reload` command to apply)")
            // Jade
            builder.add(RagiumTranslationKeys.CONFIG_JADE_BURNING_BOX, "Burning Box")
            // REI
            builder.add(RagiumTranslationKeys.REI_RECIPE_INFO, "Recipe Info")
            builder.add(RagiumTranslationKeys.REI_RECIPE_REQUIRE_SCAN, "Required Scanning!")
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
            builder.add(RagiumContents.DATA_DRIVE, "データドライブ")
            builder.add(RagiumContents.DRIVE_SCANNER, "ドライブスキャナ")
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
            builder.add(RagiumTranslationKeys.MACHINE_NAME, "名称: %s")
            builder.add(RagiumTranslationKeys.MACHINE_TIER, "ティア: %s")
            builder.add(RagiumTranslationKeys.MACHINE_RECIPE_COST, "処理コスト: %s E")
            builder.add(RagiumTranslationKeys.MACHINE_ENERGY_CAPACITY, "バッテリー容量: %s E")

            builder.add(RagiumTranslationKeys.MULTI_SHAPE_ERROR, "次の条件を満たしていません; %s (座標 %s)")
            builder.add(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS, "構造物は有効です！")
            // Machine Tier
            // builder.add(HTMachineTier.NONE, "なし", "%s")
            builder.add(HTMachineTier.PRIMITIVE, "簡易", "簡易型%s")
            builder.add(HTMachineTier.BASIC, "基本", "基本型%s")
            builder.add(HTMachineTier.ADVANCED, "発展", "発展型%s")
            // Machine Type
            builder.add(RagiumMachineTypes.Single.ALLOY_FURNACE, "合金かまど")
            builder.add(RagiumMachineTypes.Single.ASSEMBLER, "組立機")
            builder.add(RagiumMachineTypes.Single.CENTRIFUGE, "遠心分離機")
            builder.add(RagiumMachineTypes.Single.CHEMICAL_REACTOR, "化学反応槽")
            builder.add(RagiumMachineTypes.Single.COMPRESSOR, "圧縮機")
            builder.add(RagiumMachineTypes.Single.ELECTROLYZER, "電解槽")
            builder.add(RagiumMachineTypes.Single.EXTRACTOR, "抽出器")
            builder.add(RagiumMachineTypes.Single.GRINDER, "粉砕機")
            builder.add(RagiumMachineTypes.Single.METAL_FORMER, "金属加工機")
            builder.add(RagiumMachineTypes.Single.MIXER, "ミキサー")
            builder.add(RagiumMachineTypes.Single.ROCK_GENERATOR, "岩石生成機")

            builder.add(RagiumMachineTypes.BLAST_FURNACE, "大型高炉")
            builder.add(RagiumMachineTypes.DISTILLATION_TOWER, "蒸留塔")
            // Mod Menu
            builder.add(RagiumTranslationKeys.CONFIG_TILE, "Ragium - Config")
            builder.add(RagiumTranslationKeys.CONFIG_IS_HARD_MODE, "ハードモードの切り替え（reloadコマンドで反映）")
            // Jade
            builder.add(RagiumTranslationKeys.CONFIG_JADE_BURNING_BOX, "燃焼室")
            // REI
            builder.add(RagiumTranslationKeys.REI_RECIPE_INFO, "レシピ情報")
            builder.add(RagiumTranslationKeys.REI_RECIPE_REQUIRE_SCAN, "スキャンが必要です！")
        }
    }
}
