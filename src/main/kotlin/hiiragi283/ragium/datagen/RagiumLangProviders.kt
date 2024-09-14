package hiiragi283.ragium.datagen

import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluids
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

            builder.add(RagiumBlocks.RAGI_ALLOY_BLOCK, "Block of Ragi-Alloy")
            builder.add(RagiumBlocks.RAGI_ALLOY_HULL, "Ragi-Alloy Hull")
            builder.add(RagiumBlocks.MANUAL_GRINDER, "Manual Grinder")
            builder.add(RagiumBlocks.WATER_COLLECTOR, "Water Collector")
            builder.add(RagiumBlocks.BURNING_BOX, "Burning Box")

            builder.add(RagiumBlocks.RAGI_STEEL_BLOCK, "Block of Ragi-Steel")
            builder.add(RagiumBlocks.RAGI_STEEL_HULL, "Ragi-Steel Hull")

            builder.add(RagiumBlocks.REFINED_RAGI_STEEL_BLOCK, "Block of Refined Ragi-Steel")
            builder.add(RagiumBlocks.REFINED_RAGI_STEEL_HULL, "Refined Ragi-Steel Hull")
            // Fluids
            builder.add(RagiumFluids.OIL.block, "Oil")
            // Items
            builder.add(RagiumItems.POWER_METER, "Power Meter")
            builder.add(RagiumItems.FORGE_HAMMER, "Forge Hammer")

            builder.add(RagiumItems.RAW_RAGINITE, "Raw Raginite")
            builder.add(RagiumItems.RAW_RAGINITE_DUST, "Raw Raginite Dust")
            builder.add(RagiumItems.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound ")
            builder.add(RagiumItems.RAGI_ALLOY_INGOT, "Ragi-Alloy Ingot")
            builder.add(RagiumItems.RAGI_ALLOY_PLATE, "Ragi-Alloy Plate")
            builder.add(RagiumItems.RAGI_ALLOY_ROD, "Ragi-Alloy Rod")

            builder.add(RagiumItems.RAGI_STEEL_INGOT, "Ragi-Steel Ingot")

            builder.add(RagiumItems.REFINED_RAGI_STEEL_INGOT, "Refined Ragi-Steel Ingot")
            // ItemGroups
            builder.add(RagiumTranslationKeys.ITEM_GROUP_BLOCK, "Ragium Blocks")
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

            builder.add(RagiumBlocks.RAGI_ALLOY_BLOCK, "ラギ合金ブロック")
            builder.add(RagiumBlocks.RAGI_ALLOY_HULL, "ラギ合金筐体")
            builder.add(RagiumBlocks.MANUAL_GRINDER, "石臼")
            builder.add(RagiumBlocks.WATER_COLLECTOR, "水収集器")
            builder.add(RagiumBlocks.BURNING_BOX, "燃焼室")

            builder.add(RagiumBlocks.RAGI_STEEL_BLOCK, "ラギスチールブロック")
            builder.add(RagiumBlocks.RAGI_STEEL_HULL, "ラギスチール筐体")

            builder.add(RagiumBlocks.REFINED_RAGI_STEEL_BLOCK, "精製ラギスチールブロック")
            builder.add(RagiumBlocks.REFINED_RAGI_STEEL_HULL, "精製ラギスチール筐体")
            // Fluids
            builder.add(RagiumFluids.OIL.block, "石油")
            // Items
            builder.add(RagiumItems.POWER_METER, "パワー測定器")
            builder.add(RagiumItems.FORGE_HAMMER, "鍛造ハンマー")

            builder.add(RagiumItems.RAW_RAGINITE, "ラギナイトの原石")
            builder.add(RagiumItems.RAW_RAGINITE_DUST, "ラギナイトの原石の粉")
            builder.add(RagiumItems.RAGI_ALLOY_COMPOUND, "ラギ合金混合物")
            builder.add(RagiumItems.RAGI_ALLOY_INGOT, "ラギ合金インゴット")
            builder.add(RagiumItems.RAGI_ALLOY_PLATE, "ラギ合金の板")
            builder.add(RagiumItems.RAGI_ALLOY_ROD, "ラギ合金の棒")

            builder.add(RagiumItems.RAGI_STEEL_INGOT, "ラギスチールインゴット")

            builder.add(RagiumItems.REFINED_RAGI_STEEL_INGOT, "精製ラギスチールインゴット")
            // ItemGroups
            builder.add(RagiumTranslationKeys.ITEM_GROUP_BLOCK, "Ragium - ブロック")
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