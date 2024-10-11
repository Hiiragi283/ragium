package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTLangType
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.util.splitWith
import hiiragi283.ragium.client.RagiumKeyBinds
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.util.HTBlockContent
import hiiragi283.ragium.common.util.HTItemContent
import hiiragi283.ragium.common.util.HTTranslationFormatter
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder
import net.minecraft.enchantment.Enchantment
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

object RagiumLangProviders {
    @JvmStatic
    fun init(pack: FabricDataGenerator.Pack) {
        pack.addProvider(RagiumLangProviders::EnglishLang)
        pack.addProvider(RagiumLangProviders::JapaneseLang)
    }

    fun TranslationBuilder.add(enchantment: RegistryKey<Enchantment>, value: String) {
        add("enchantment.${enchantment.value.splitWith('.')}", value)
    }

    fun TranslationBuilder.add(tier: HTMachineTier, name: String, prefix: String) {
        add(tier.translationKey, name)
        add(tier.prefixKey, prefix)
    }

    fun TranslationBuilder.add(type: HTMachineConvertible, value: String) {
        add(type.asMachine().translationKey, value)
    }

    @JvmStatic
    private fun translateContents(builder: TranslationBuilder, type: HTLangType) {
        // blocks
        buildList<HTBlockContent> {
            addAll(RagiumContents.getOres())
            addAll(RagiumContents.StorageBlocks.entries)
            addAll(RagiumContents.Hulls.entries)
            addAll(RagiumContents.Coils.entries)
        }.forEach { block: HTBlockContent -> builder.add(block.block, block.getTranslation(type)) }
        // elements
        RagiumContents.Element.entries.forEach { element: RagiumContents.Element ->
            val translatedName: String = element.getTranslation(type)
            builder.add(element.translationKey, translatedName)
            when (type) {
                HTLangType.EN_US -> {
                    builder.add(element.buddingBlock, "Budding $translatedName")
                    builder.add(element.clusterBlock, "$translatedName Cluster")
                    builder.add(element.dustItem, "$translatedName Dust")
                    builder.add(element.pendantItem, "$translatedName Pendant")
                    builder.add(element.ringItem, "$translatedName Ring")
                }

                HTLangType.JA_JP -> {
                    builder.add(element.buddingBlock, "芽生えた$translatedName")
                    builder.add(element.clusterBlock, "${translatedName}の塊")
                    builder.add(element.dustItem, "${translatedName}の粉")
                    builder.add(element.pendantItem, "${translatedName}のペンダント")
                    builder.add(element.ringItem, "${translatedName}の指輪")
                }
            }
        }
        // items
        buildList<HTItemContent> {
            addAll(RagiumContents.Dusts.entries)
            addAll(RagiumContents.Ingots.entries)
            addAll(RagiumContents.Plates.entries)
            addAll(RagiumContents.RawMaterials.entries)
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
            builder.add(RagiumContents.POROUS_NETHERRACK, "Porous Netherrack")
            builder.add(RagiumContents.OBLIVION_CLUSTER, "Oblivion Cluster")

            builder.add(RagiumContents.CREATIVE_SOURCE, "Creative Power Source")
            builder.add(RagiumContents.BASIC_CASING, "Basic Casing")
            builder.add(RagiumContents.ADVANCED_CASING, "Advanced Casing")
            builder.add(RagiumContents.DATA_DRIVE, "Data Drive")
            builder.add(RagiumContents.DRIVE_SCANNER, "Drive Scanner")
            builder.add(RagiumContents.ITEM_DISPLAY, "Item Display")
            builder.add(RagiumContents.MANUAL_GRINDER, "Manual Grinder")
            builder.add(RagiumContents.META_MACHINE, "Machine")
            builder.add(RagiumContents.NETWORK_INTERFACE, "E.N.I.")
            builder.add(RagiumContents.SHAFT, "Shaft")

            builder.add(RagiumContents.ALCHEMICAL_INFUSER, "Alchemical Infuser")
            // Enchantment
            builder.add(RagiumEnchantments.SMELTING, "Smelting")
            builder.add(RagiumEnchantments.SLEDGE_HAMMER, "Sledge Hammer")
            builder.add(RagiumEnchantments.BUZZ_SAW, "Buzz Saw")
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

            builder.add(RagiumContents.STEEL_HELMET, "Steel Helmet")
            builder.add(RagiumContents.STEEL_CHESTPLATE, "Steel Chestplate")
            builder.add(RagiumContents.STEEL_LEGGINGS, "Steel Leggings")
            builder.add(RagiumContents.STEEL_BOOTS, "Steel Boots")
            builder.add(RagiumContents.DIVING_GOGGLES, "Diving Goggle")
            builder.add(RagiumContents.NIGHT_VISION_GOGGLES, "Night Vision Goggle")
            builder.add(RagiumContents.PISTON_BOOTS, "Piston Boots")
            builder.add(RagiumContents.PARACHUTE, "Parachute")

            builder.add(RagiumContents.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound")
            builder.add(RagiumContents.EMPTY_FLUID_CUBE, "Fluid Cube (Empty)")
            builder.add(RagiumContents.SOAP_INGOT, "Soap Ingot")
            builder.add(RagiumContents.BASALT_FIBER, "Basalt Fiber")
            builder.add(RagiumContents.SOLAR_PANEL, "Solar Panel")
            builder.add(RagiumContents.RAGI_CRYSTAL, "Ragi-Crystal")
            builder.add(RagiumContents.OBLIVION_CRYSTAL, "Oblivion Crystal")

            builder.add(RagiumContents.BEE_WAX, "Bee Wax")
            builder.add(RagiumContents.BUTTER, "Butter")
            builder.add(RagiumContents.CHOCOLATE, "Chocolate")
            builder.add(RagiumContents.FLOUR, "Flour")
            builder.add(RagiumContents.DOUGH, "Dough")
            builder.add(RagiumContents.MINCED_MEAT, "Minced Meat")
            builder.add(RagiumContents.PULP, "Pulp")

            builder.add(RagiumFluids.PETROLEUM.bucketItem, "Petroleum Bucket")
            // Item Group
            builder.add(RagiumItemGroup.ITEM_KEY, "Ragium - Items")
            builder.add(RagiumItemGroup.MACHINE_KEY, "Ragium - Machines")
            // Key Binds
            builder.add(RagiumKeyBinds.CATEGORY, RagiumAPI.MOD_NAME)

            builder.add(RagiumKeyBinds.OPEN_BACKPACK.translationKey, "Open Backpack")
            // Machine
            builder.add(RagiumTranslationKeys.MACHINE_NAME, "Name: %s")
            builder.add(RagiumTranslationKeys.MACHINE_TIER, "Tier: %s")
            builder.add(RagiumTranslationKeys.MACHINE_RECIPE_COST, "Recipe cost: %s E")
            // builder.add(RagiumTranslationKeys.MACHINE_ENERGY_CAPACITY, "Energy Capacity: %s E")

            builder.add(RagiumTranslationKeys.MULTI_SHAPE_ERROR, "Not matching condition; %s at %ss")
            builder.add(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS, "The structure is valid!")
            // Machine Tier
            // builder.add(HTMachineTier.NONE, "None", "%s")
            builder.add(HTMachineTier.PRIMITIVE, "Primitive", "Primitive %s")
            builder.add(HTMachineTier.BASIC, "Basic", "Basic %s")
            builder.add(HTMachineTier.ADVANCED, "Advanced", "Advanced %s")
            // Machine Type
            builder.add(HTMachineType.DEFAULT, "Default Machine")

            builder.add(RagiumMachineTypes.Generator.COMBUSTION, "Combustion Generator")
            builder.add(RagiumMachineTypes.Generator.THERMAL, "Thermal Generator")
            builder.add(RagiumMachineTypes.Generator.SOLAR, "Solar Generator")
            builder.add(RagiumMachineTypes.Generator.WATER, "Water Generator")

            builder.add(RagiumMachineTypes.HEAT_GENERATOR, "Heat Generator")

            builder.add(RagiumMachineTypes.Processor.ALLOY_FURNACE, "Alloy Furnace")
            builder.add(RagiumMachineTypes.Processor.ASSEMBLER, "Assembler")
            builder.add(RagiumMachineTypes.Processor.CHEMICAL_REACTOR, "Chemical Reactor")
            builder.add(RagiumMachineTypes.Processor.COMPRESSOR, "Compressor")
            builder.add(RagiumMachineTypes.Processor.DECOMPRESSOR, "Decompressor")
            builder.add(RagiumMachineTypes.Processor.ELECTROLYZER, "Electrolyzer")
            builder.add(RagiumMachineTypes.Processor.EXTRACTOR, "Extractor")
            builder.add(RagiumMachineTypes.Processor.GRINDER, "Grinder")
            builder.add(RagiumMachineTypes.Processor.METAL_FORMER, "Metal Former")
            builder.add(RagiumMachineTypes.Processor.MIXER, "Mixer")
            builder.add(RagiumMachineTypes.Processor.ROCK_GENERATOR, "Rock Generator")

            builder.add(RagiumMachineTypes.BLAST_FURNACE, "Large Blast Furnace")
            builder.add(RagiumMachineTypes.DISTILLATION_TOWER, "Distillation Tower")
            builder.add(RagiumMachineTypes.FLUID_DRILL, "Fluid Drill")
            builder.add(RagiumMachineTypes.MOB_EXTRACTOR, "Mob Extractor")
            builder.add(RagiumMachineTypes.SAW_MILL, "Saw Mill")
            // Mod Menu
            builder.add(RagiumTranslationKeys.CONFIG_TILE, "Ragium - Config")
            builder.add(RagiumTranslationKeys.CONFIG_IS_HARD_MODE, "Enable Hard Mode (Run `/reload` command to apply)")
            // Jade
            builder.add(RagiumTranslationKeys.CONFIG_JADE_MACHINE, "Machines")
            builder.add(RagiumTranslationKeys.CONFIG_JADE_NETWORK_INTERFACE, "E.N.I")
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
            builder.add(RagiumContents.POROUS_NETHERRACK, "多孔質ネザーラック")
            builder.add(RagiumContents.OBLIVION_CLUSTER, "忘却の芽")

            builder.add(RagiumContents.CREATIVE_SOURCE, "クリエイティブ用エネルギー源")
            builder.add(RagiumContents.BASIC_CASING, "基本型外装")
            builder.add(RagiumContents.ADVANCED_CASING, "発展型外装")
            builder.add(RagiumContents.DATA_DRIVE, "データドライブ")
            builder.add(RagiumContents.DRIVE_SCANNER, "ドライブスキャナ")
            builder.add(RagiumContents.ITEM_DISPLAY, "アイテムティスプレイ")
            builder.add(RagiumContents.MANUAL_GRINDER, "石臼")
            builder.add(RagiumContents.META_MACHINE, "機械")
            builder.add(RagiumContents.NETWORK_INTERFACE, "E.N.I.")
            builder.add(RagiumContents.SHAFT, "シャフト")

            builder.add(RagiumContents.ALCHEMICAL_INFUSER, "錬金注入機")
            // Enchantment
            builder.add(RagiumEnchantments.SMELTING, "精錬")
            builder.add(RagiumEnchantments.SLEDGE_HAMMER, "粉砕")
            builder.add(RagiumEnchantments.BUZZ_SAW, "製材")
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

            builder.add(RagiumContents.STEEL_HELMET, "鋼鉄のヘルメット")
            builder.add(RagiumContents.STEEL_CHESTPLATE, "鋼鉄のチェストプレート")
            builder.add(RagiumContents.STEEL_LEGGINGS, "鋼鉄のレギンス")
            builder.add(RagiumContents.STEEL_BOOTS, "鋼鉄のブーツ")
            builder.add(RagiumContents.DIVING_GOGGLES, "潜水ゴーグル")
            builder.add(RagiumContents.NIGHT_VISION_GOGGLES, "暗視ゴーグル")
            builder.add(RagiumContents.PISTON_BOOTS, "ピストンブーツ")
            builder.add(RagiumContents.PARACHUTE, "パラシュート")

            builder.add(RagiumContents.RAGI_ALLOY_COMPOUND, "ラギ合金混合物")
            builder.add(RagiumContents.EMPTY_FLUID_CUBE, "液体キューブ（なし）")
            builder.add(RagiumContents.SOAP_INGOT, "石鹸インゴット")
            builder.add(RagiumContents.BASALT_FIBER, "玄武岩繊維")
            builder.add(RagiumContents.SOLAR_PANEL, "太陽光パネル")
            builder.add(RagiumContents.RAGI_CRYSTAL, "ラギクリスタリル")
            builder.add(RagiumContents.OBLIVION_CRYSTAL, "忘却の結晶")

            builder.add(RagiumContents.BEE_WAX, "蜜蠟")
            builder.add(RagiumContents.BUTTER, "バター")
            builder.add(RagiumContents.CHOCOLATE, "チョコレート")
            builder.add(RagiumContents.FLOUR, "小麦粉")
            builder.add(RagiumContents.DOUGH, "生地")
            builder.add(RagiumContents.MINCED_MEAT, "ひき肉")
            builder.add(RagiumContents.PULP, "パルプ")

            builder.add(RagiumFluids.PETROLEUM.bucketItem, "石油バケツ")
            // Item Group
            builder.add(RagiumItemGroup.ITEM_KEY, "Ragium - アイテム")
            builder.add(RagiumItemGroup.MACHINE_KEY, "Ragium - 機械")
            // Key Binds
            builder.add(RagiumKeyBinds.CATEGORY, RagiumAPI.MOD_NAME)

            builder.add(RagiumKeyBinds.OPEN_BACKPACK.translationKey, "バックパックを開く")
            // Machine
            builder.add(RagiumTranslationKeys.MACHINE_NAME, "名称: %s")
            builder.add(RagiumTranslationKeys.MACHINE_TIER, "ティア: %s")
            builder.add(RagiumTranslationKeys.MACHINE_RECIPE_COST, "処理コスト: %s E")
            // builder.add(RagiumTranslationKeys.MACHINE_ENERGY_CAPACITY, "バッテリー容量: %s E")

            builder.add(RagiumTranslationKeys.MULTI_SHAPE_ERROR, "次の条件を満たしていません; %s (座標 %s)")
            builder.add(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS, "構造物は有効です！")
            // Machine Tier
            // builder.add(HTMachineTier.NONE, "なし", "%s")
            builder.add(HTMachineTier.PRIMITIVE, "簡易", "簡易型%s")
            builder.add(HTMachineTier.BASIC, "基本", "基本型%s")
            builder.add(HTMachineTier.ADVANCED, "発展", "発展型%s")
            // Machine Type
            builder.add(HTMachineType.DEFAULT, "デフォルト機械")

            builder.add(RagiumMachineTypes.Generator.COMBUSTION, "燃焼発電機")
            builder.add(RagiumMachineTypes.Generator.SOLAR, "太陽光発電機")
            builder.add(RagiumMachineTypes.Generator.THERMAL, "地熱発電機")
            builder.add(RagiumMachineTypes.Generator.WATER, "水力発電機")

            builder.add(RagiumMachineTypes.HEAT_GENERATOR, "火力発電機")

            builder.add(RagiumMachineTypes.Processor.ALLOY_FURNACE, "合金かまど")
            builder.add(RagiumMachineTypes.Processor.ASSEMBLER, "組立機")
            builder.add(RagiumMachineTypes.Processor.CHEMICAL_REACTOR, "化学反応槽")
            builder.add(RagiumMachineTypes.Processor.COMPRESSOR, "圧縮機")
            builder.add(RagiumMachineTypes.Processor.DECOMPRESSOR, "減圧機")
            builder.add(RagiumMachineTypes.Processor.ELECTROLYZER, "電解槽")
            builder.add(RagiumMachineTypes.Processor.EXTRACTOR, "抽出器")
            builder.add(RagiumMachineTypes.Processor.GRINDER, "粉砕機")
            builder.add(RagiumMachineTypes.Processor.METAL_FORMER, "金属加工機")
            builder.add(RagiumMachineTypes.Processor.MIXER, "ミキサー")
            builder.add(RagiumMachineTypes.Processor.ROCK_GENERATOR, "岩石生成機")

            builder.add(RagiumMachineTypes.BLAST_FURNACE, "大型高炉")
            builder.add(RagiumMachineTypes.DISTILLATION_TOWER, "蒸留塔")
            builder.add(RagiumMachineTypes.FLUID_DRILL, "液体ドリル")
            builder.add(RagiumMachineTypes.MOB_EXTRACTOR, "モブ抽出器")
            builder.add(RagiumMachineTypes.SAW_MILL, "製材機")
            // Mod Menu
            builder.add(RagiumTranslationKeys.CONFIG_TILE, "Ragium - Config")
            builder.add(RagiumTranslationKeys.CONFIG_IS_HARD_MODE, "ハードモードの切り替え（reloadコマンドで反映）")
            // Jade
            builder.add(RagiumTranslationKeys.CONFIG_JADE_MACHINE, "機械")
            builder.add(RagiumTranslationKeys.CONFIG_JADE_NETWORK_INTERFACE, "E.N.I")
            // REI
            builder.add(RagiumTranslationKeys.REI_RECIPE_INFO, "レシピ情報")
            builder.add(RagiumTranslationKeys.REI_RECIPE_REQUIRE_SCAN, "スキャンが必要です！")
        }
    }
}
