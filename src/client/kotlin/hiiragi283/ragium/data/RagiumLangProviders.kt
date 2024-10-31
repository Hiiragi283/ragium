package hiiragi283.ragium.data

import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.content.HTEntryDelegated
import hiiragi283.ragium.api.content.HTTranslationFormatter
import hiiragi283.ragium.api.data.HTLangType
import hiiragi283.ragium.api.extension.splitWith
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.item.HTCrafterHammerItem
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder
import net.minecraft.block.Block
import net.minecraft.enchantment.Enchantment
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

object RagiumLangProviders {
    @JvmStatic
    fun init(pack: FabricDataGenerator.Pack) {
        pack.addProvider(RagiumLangProviders::EnglishLang)
        pack.addProvider(RagiumLangProviders::JapaneseLang)
    }

    @JvmName("addBlock")
    fun TranslationBuilder.add(entry: HTEntryDelegated<Block>, value: String) {
        add(entry.value, value)
    }

    @JvmName("addItem")
    fun TranslationBuilder.add(entry: HTEntryDelegated<Item>, value: String) {
        add(entry.value, value)
    }

    fun TranslationBuilder.add(enchantment: RegistryKey<Enchantment>, value: String) {
        add("enchantment.${enchantment.value.splitWith('.')}", value)
    }

    fun TranslationBuilder.add(tier: HTMachineTier, name: String, prefix: String) {
        add(tier.translationKey, name)
        add(tier.prefixKey, prefix)
    }

    fun TranslationBuilder.add(type: HTMachineConvertible, value: String) {
        add(type.key.translationKey, value)
    }

    @JvmStatic
    private fun translateContents(builder: TranslationBuilder, type: HTLangType) {
        // blocks
        buildList {
            addAll(RagiumContents.Ores.entries)
            addAll(RagiumContents.StorageBlocks.entries)
            addAll(RagiumContents.Hulls.entries)
            addAll(RagiumContents.Coils.entries)
        }.forEach { block: HTContent.Material<Block> -> builder.add(block, block.getTranslation(type)) }
        // items
        buildList {
            addAll(RagiumContents.Dusts.entries)
            addAll(RagiumContents.Gems.entries)
            addAll(RagiumContents.Ingots.entries)
            addAll(RagiumContents.Plates.entries)
            addAll(RagiumContents.RawMaterials.entries)
            addAll(RagiumContents.Armors.entries)
            addAll(RagiumContents.Tools.entries)
        }.forEach { item: HTContent.Material<Item> -> builder.add(item, item.getTranslation(type)) }
        // exporters
        RagiumContents.Exporters.entries.forEach { exporter: RagiumContents.Exporters ->
            builder.add(
                exporter,
                ExporterFormatter.getTranslation(type, exporter),
            )
        }
        // circuits
        RagiumContents.CircuitBoards.entries.forEach { boards: RagiumContents.CircuitBoards ->
            builder.add(
                boards,
                BoardFormatter.getTranslation(type, boards),
            )
        }
        RagiumContents.Circuits.entries.forEach { circuit: RagiumContents.Circuits ->
            builder.add(
                circuit,
                CircuitFormatter.getTranslation(type, circuit),
            )
        }
        // fluids
        RagiumContents.Fluids.entries.forEach { fluid: RagiumContents.Fluids ->
            builder.add(
                fluid.translationKey,
                fluid.getTranslation(type),
            )
        }
    }

    private data object ExporterFormatter : HTTranslationFormatter {
        override val enPattern: String = "%s Exporter"
        override val jaPattern: String = "%s搬出機"
    }

    private data object BoardFormatter : HTTranslationFormatter {
        override val enPattern: String = "%s Circuit Board"
        override val jaPattern: String = "%s回路基板"
    }

    private data object CircuitFormatter : HTTranslationFormatter {
        override val enPattern: String = "%s Circuit"
        override val jaPattern: String = "%s回路"
    }

    //    English    //

    private class EnglishLang(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricLanguageProvider(output, registryLookup) {
        override fun generateTranslations(registryLookup: RegistryWrapper.WrapperLookup, builder: TranslationBuilder) {
            translateContents(builder, HTLangType.EN_US)
            // Advancements
            RagiumAdvancementProvider.register.generateLang(HTLangType.EN_US, builder)
            // Blocks
            builder.add(RagiumBlocks.POROUS_NETHERRACK, "Porous Netherrack")
            // builder.add(RagiumBlocks.OBLIVION_CLUSTER, "Oblivion Cluster")

            builder.add(RagiumBlocks.SPONGE_CAKE, "Sponge Cake")
            builder.add(RagiumBlocks.SWEET_BERRIES_CAKE, "Sweet Berries Cake")

            builder.add(RagiumBlocks.CREATIVE_SOURCE, "Creative Power Source")
            builder.add(RagiumBlocks.BASIC_CASING, "Basic Casing")
            builder.add(RagiumBlocks.ADVANCED_CASING, "Advanced Casing")
            builder.add(RagiumBlocks.ITEM_DISPLAY, "Item Display")
            builder.add(RagiumBlocks.MANUAL_FORGE, "Ragi-Anvil")
            builder.add(RagiumBlocks.MANUAL_GRINDER, "Ragi-Grinder")
            builder.add(RagiumBlocks.MANUAL_MIXER, "Ragi-Mixing Basin")
            builder.add(RagiumBlocks.META_GENERATOR, "Generator")
            builder.add(RagiumBlocks.META_PROCESSOR, "Processor")
            builder.add(RagiumBlocks.NETWORK_INTERFACE, "E.N.I.")
            builder.add(RagiumBlocks.SHAFT, "Shaft")

            builder.add(RagiumContents.Pipes.IRON, "Iron Pipe")
            builder.add(RagiumContents.Pipes.WOODEN, "Wooden Pipe")
            builder.add(RagiumContents.Pipes.STEEL, "Steel Pipe")
            builder.add(RagiumContents.Pipes.COPPER, "Copper Pipe")
            builder.add(RagiumContents.Pipes.UNIVERSAL, "Universal Pipe")
            // Enchantment
            builder.add(RagiumEnchantments.SMELTING, "Smelting")
            builder.add(RagiumEnchantments.SLEDGE_HAMMER, "Sledge Hammer")
            builder.add(RagiumEnchantments.BUZZ_SAW, "Buzz Saw")
            // Items
            // builder.add(RagiumContents.Crops.CANOLA.cropBlock, "Canola")
            // builder.add(RagiumContents.Crops.CANOLA.seedItem, "Canola Seeds")
            // builder.add(RagiumContents.Crops.SWEET_POTATO.cropBlock, "Sweet Potatoes")
            // builder.add(RagiumContents.Crops.SWEET_POTATO.seedItem, "Sweet Potato")

            builder.add(RagiumContents.Foods.BEE_WAX, "Bee Wax")
            builder.add(RagiumContents.Foods.BUTTER, "Butter")
            builder.add(RagiumContents.Foods.CANDY_APPLE, "Candy Apple")
            builder.add(RagiumContents.Foods.CARAMEL, "Caramel")
            builder.add(RagiumContents.Foods.CHOCOLATE, "Chocolate")
            builder.add(RagiumContents.Foods.CHOCOLATE_APPLE, "Chocolate Apple")
            builder.add(RagiumContents.Foods.CHOCOLATE_BREAD, "Chocolate Bread")
            builder.add(RagiumContents.Foods.FLOUR, "Flour")
            builder.add(RagiumContents.Foods.DOUGH, "Dough")
            builder.add(RagiumContents.Foods.MINCED_MEAT, "Minced Meat")
            builder.add(RagiumContents.Foods.PULP, "Pulp")

            builder.add(RagiumContents.Misc.BACKPACK, "Backpack")
            builder.add(RagiumContents.Misc.BASALT_MESH, "Basalt Mesh")
            builder.add(RagiumContents.Misc.CRAFTER_HAMMER, "Crafter's Hammer")
            builder.add(RagiumContents.Misc.DYNAMITE, "Dynamite")
            builder.add(RagiumContents.Misc.EMPTY_FLUID_CUBE, "Fluid Cube (Empty)")
            builder.add(RagiumContents.Misc.FILLED_FLUID_CUBE, "Fluid Cube (%s)")
            builder.add(RagiumContents.Misc.ENGINE, "V8 Engine")
            builder.add(RagiumContents.Misc.FORGE_HAMMER, "Forge Hammer")
            builder.add(RagiumContents.Misc.HEART_OF_THE_NETHER, "Heart of the Nether")
            // builder.add(RagiumContents.Misc.OBLIVION_CUBE_SPAWN_EGG, "Spawn Oblivion Cube")
            builder.add(RagiumContents.Misc.POLYMER_RESIN, "Polymer Resin")
            builder.add(RagiumContents.Misc.PROCESSOR_SOCKET, "Processor Socket")
            builder.add(RagiumContents.Misc.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound")
            builder.add(RagiumContents.Misc.RAGI_CRYSTAL_PROCESSOR, "Ragi-Crystal Processor")
            builder.add(RagiumContents.Misc.RAGIUM, "Ragium")
            builder.add(RagiumContents.Misc.REMOVER_DYNAMITE, "Remover Dynamite")
            builder.add(RagiumContents.Misc.SOAP_INGOT, "Soap Ingot")
            builder.add(RagiumContents.Misc.SOLAR_PANEL, "Solar Panel")
            builder.add(RagiumContents.Misc.TRADER_CATALOG, "Trader Catalog")

            builder.add(HTCrafterHammerItem.Behavior.DEFAULT, "Hammer Module (Default)")
            builder.add(HTCrafterHammerItem.Behavior.AXE, "Hammer Module (Axe)")
            builder.add(HTCrafterHammerItem.Behavior.HOE, "Hammer Module (Hoe)")
            builder.add(HTCrafterHammerItem.Behavior.PICKAXE, "Hammer Module (Pickaxe)")
            builder.add(HTCrafterHammerItem.Behavior.SHOVEL, "Hammer Module (Shovel)")

            builder.add(RagiumTranslationKeys.CRAFTER_HAMMER_MODULE, "Module: %s")
            builder.add(RagiumTranslationKeys.DYNAMITE_DESTROY, "Destroy: %s")
            builder.add(RagiumTranslationKeys.DYNAMITE_POWER, "Power: %s")
            builder.add(RagiumTranslationKeys.REMOVER_DYNAMITE_MODE, "Mode: %s")
            // Item Group
            builder.add(RagiumItemGroup.ITEM_KEY, "Ragium - Items")
            builder.add(RagiumItemGroup.MACHINE_KEY, "Ragium - Machines")
            // Key Binds
            // builder.add(RagiumKeyBinds.CATEGORY, RagiumAPI.MOD_NAME)

            // builder.add(RagiumKeyBinds.OPEN_BACKPACK.translationKey, "Open Backpack")
            // Machine
            builder.add(RagiumTranslationKeys.MACHINE_NAME, "Name: %s")
            builder.add(RagiumTranslationKeys.MACHINE_TIER, "Tier: %s")
            builder.add(RagiumTranslationKeys.MACHINE_RECIPE_COST, "Recipe cost: %s E")
            builder.add(RagiumTranslationKeys.MACHINE_SHOW_PREVIEW, "Show preview: %s")
            builder.add(RagiumTranslationKeys.MACHINE_SLOT_COUNTS, "Input/Output Slots: %s")
            builder.add(RagiumTranslationKeys.MACHINE_TANK_CAPACITY, "Each Tank Capacity: %s Buckets")

            builder.add(RagiumTranslationKeys.MULTI_SHAPE_ERROR, "Not matching condition; %s at %ss")
            builder.add(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS, "The structure is valid!")
            // Machine Tier
            builder.add(HTMachineTier.PRIMITIVE, "Primitive", "Primitive %s")
            builder.add(HTMachineTier.BASIC, "Basic", "Basic %s")
            builder.add(HTMachineTier.ADVANCED, "Advanced", "Advanced %s")
            // Machine SizeType
            // builder.add(HTMachineType.Default, "Default Machine")

            builder.add(RagiumMachineTypes.Generator.COMBUSTION, "Combustion Generator")
            builder.add(RagiumMachineTypes.Generator.THERMAL, "Thermal Generator")
            builder.add(RagiumMachineTypes.Generator.SOLAR, "Solar Generator")
            builder.add(RagiumMachineTypes.Generator.WATER, "Water Generator")

            builder.add(RagiumMachineTypes.HEAT_GENERATOR, "Heat Generator")

            builder.add(RagiumMachineTypes.Processor.ALLOY_FURNACE, "Alloy Furnace")
            builder.add(RagiumMachineTypes.Processor.ASSEMBLER, "Assembler")
            builder.add(RagiumMachineTypes.Processor.CHEMICAL_REACTOR, "Chemical Reactor")
            // builder.add(RagiumMachineTypes.Processor.COMPRESSOR, "Compressor")
            // builder.add(RagiumMachineTypes.Processor.DECOMPRESSOR, "Decompressor")
            builder.add(RagiumMachineTypes.Processor.ELECTROLYZER, "Electrolyzer")
            builder.add(RagiumMachineTypes.Processor.EXTRACTOR, "Extractor")
            builder.add(RagiumMachineTypes.Processor.GRINDER, "Grinder")
            builder.add(RagiumMachineTypes.Processor.METAL_FORMER, "Metal Former")
            builder.add(RagiumMachineTypes.Processor.MIXER, "Mixer")
            builder.add(RagiumMachineTypes.Processor.ROCK_GENERATOR, "Rock Generator")

            builder.add(RagiumMachineTypes.BLAST_FURNACE, "Large Blast Furnace")
            builder.add(RagiumMachineTypes.DISTILLATION_TOWER, "Distillation Tower")
            // builder.add(RagiumMachineTypes.FLUID_DRILL, "Fluid Drill")
            // builder.add(RagiumMachineTypes.MOB_EXTRACTOR, "Mob Extractor")
            builder.add(RagiumMachineTypes.SAW_MILL, "Saw Mill")
            // Mod Menu
            builder.add(RagiumTranslationKeys.CONFIG_TILE, "Ragium - Config")
            builder.add(RagiumTranslationKeys.CONFIG_IS_HARD_MODE, "Enable Hard Mode (Run `/reload` command to apply)")
            // Jade
            builder.add(RagiumTranslationKeys.CONFIG_JADE_MACHINE, "Machines")
            builder.add(RagiumTranslationKeys.CONFIG_JADE_NETWORK_INTERFACE, "E.N.I")

            builder.add(RagiumTranslationKeys.PROVIDER_JADE_NETWORK_INTERFACE, "Stored Energy: %s Unit")
            // REI
            builder.add(RagiumTranslationKeys.REI_RECIPE_BIOME, "Found in the biome: %s")
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
            builder.add(RagiumBlocks.POROUS_NETHERRACK, "多孔質ネザーラック")
            // builder.add(RagiumBlocks.OBLIVION_CLUSTER, "忘却の芽")

            builder.add(RagiumBlocks.SPONGE_CAKE, "スポンジケーキ")
            builder.add(RagiumBlocks.SWEET_BERRIES_CAKE, "スイートベリーケーキ")

            builder.add(RagiumBlocks.CREATIVE_SOURCE, "クリエイティブ用エネルギー源")
            builder.add(RagiumBlocks.BASIC_CASING, "基本型外装")
            builder.add(RagiumBlocks.ADVANCED_CASING, "発展型外装")
            builder.add(RagiumBlocks.ITEM_DISPLAY, "アイテムティスプレイ")
            builder.add(RagiumBlocks.MANUAL_FORGE, "らぎ金床")
            builder.add(RagiumBlocks.MANUAL_GRINDER, "らぎ臼")
            builder.add(RagiumBlocks.MANUAL_MIXER, "らぎ釜")
            builder.add(RagiumBlocks.META_GENERATOR, "発電機")
            builder.add(RagiumBlocks.META_PROCESSOR, "加工機械")
            builder.add(RagiumBlocks.NETWORK_INTERFACE, "E.N.I.")
            builder.add(RagiumBlocks.SHAFT, "シャフト")

            builder.add(RagiumContents.Pipes.IRON, "鉄パイプ")
            builder.add(RagiumContents.Pipes.WOODEN, "木製パイプ")
            builder.add(RagiumContents.Pipes.STEEL, "鋼鉄パイプ")
            builder.add(RagiumContents.Pipes.COPPER, "銅パイプ")
            builder.add(RagiumContents.Pipes.UNIVERSAL, "万能パイプ")
            // Enchantment
            builder.add(RagiumEnchantments.SMELTING, "精錬")
            builder.add(RagiumEnchantments.SLEDGE_HAMMER, "粉砕")
            builder.add(RagiumEnchantments.BUZZ_SAW, "製材")
            // Items
            // builder.add(RagiumContents.Crops.CANOLA.cropBlock, "アブラナ")
            // builder.add(RagiumContents.Crops.CANOLA.seedItem, "菜種")
            // builder.add(RagiumContents.Crops.SWEET_POTATO.cropBlock, "サツマイモ")
            // builder.add(RagiumContents.Crops.SWEET_POTATO.seedItem, "サツマイモ")

            builder.add(RagiumContents.Foods.BEE_WAX, "蜜蠟")
            builder.add(RagiumContents.Foods.BUTTER, "バター")
            builder.add(RagiumContents.Foods.CANDY_APPLE, "りんご飴")
            builder.add(RagiumContents.Foods.CARAMEL, "キャラメル")
            builder.add(RagiumContents.Foods.CHOCOLATE, "チョコレート")
            builder.add(RagiumContents.Foods.CHOCOLATE_APPLE, "チョコリンゴ")
            builder.add(RagiumContents.Foods.CHOCOLATE_BREAD, "チョコパン")
            builder.add(RagiumContents.Foods.FLOUR, "小麦粉")
            builder.add(RagiumContents.Foods.DOUGH, "生地")
            builder.add(RagiumContents.Foods.MINCED_MEAT, "ひき肉")
            builder.add(RagiumContents.Foods.PULP, "パルプ")

            builder.add(RagiumContents.Misc.BACKPACK, "バックパック")
            builder.add(RagiumContents.Misc.BASALT_MESH, "玄武岩メッシュ")
            builder.add(RagiumContents.Misc.CRAFTER_HAMMER, "クラフターズ・ハンマー")
            builder.add(RagiumContents.Misc.DYNAMITE, "ダイナマイト")
            builder.add(RagiumContents.Misc.EMPTY_FLUID_CUBE, "液体キューブ（なし）")
            builder.add(RagiumContents.Misc.FILLED_FLUID_CUBE, "液体キューブ（%s）")
            builder.add(RagiumContents.Misc.ENGINE, "V8エンジン")
            builder.add(RagiumContents.Misc.FORGE_HAMMER, "鍛造ハンマー")
            builder.add(RagiumContents.Misc.HEART_OF_THE_NETHER, "地獄の心臓")
            // builder.add(RagiumContents.Misc.OBLIVION_CUBE_SPAWN_EGG, "スポーン 忘却の箱")
            builder.add(RagiumContents.Misc.POLYMER_RESIN, "高分子樹脂")
            builder.add(RagiumContents.Misc.PROCESSOR_SOCKET, "プロセッサソケット")
            builder.add(RagiumContents.Misc.RAGI_ALLOY_COMPOUND, "ラギ合金混合物")
            builder.add(RagiumContents.Misc.RAGI_CRYSTAL_PROCESSOR, "ラギクリスタリルプロセッサ")
            builder.add(RagiumContents.Misc.RAGIUM, "ラギウム")
            builder.add(RagiumContents.Misc.REMOVER_DYNAMITE, "削除用ダイナマイト")
            builder.add(RagiumContents.Misc.SOAP_INGOT, "石鹸インゴット")
            builder.add(RagiumContents.Misc.SOLAR_PANEL, "太陽光パネル")
            builder.add(RagiumContents.Misc.TRADER_CATALOG, "行商人カタログ")

            builder.add(HTCrafterHammerItem.Behavior.DEFAULT, "ハンマーモジュール（デフォルト）")
            builder.add(HTCrafterHammerItem.Behavior.AXE, "ハンマーモジュール（アックス）")
            builder.add(HTCrafterHammerItem.Behavior.HOE, "ハンマーモジュール（クワ）")
            builder.add(HTCrafterHammerItem.Behavior.PICKAXE, "ハンマーモジュール（ピッケル）")
            builder.add(HTCrafterHammerItem.Behavior.SHOVEL, "ハンマーモジュール（ショベル）")

            builder.add(RagiumTranslationKeys.CRAFTER_HAMMER_MODULE, "モジュール: %s")
            builder.add(RagiumTranslationKeys.DYNAMITE_DESTROY, "地形破壊: %s")
            builder.add(RagiumTranslationKeys.DYNAMITE_POWER, "威力: %s")
            builder.add(RagiumTranslationKeys.REMOVER_DYNAMITE_MODE, "モード: %s")
            // Item Group
            builder.add(RagiumItemGroup.ITEM_KEY, "Ragium - アイテム")
            builder.add(RagiumItemGroup.MACHINE_KEY, "Ragium - 機械")
            // Key Binds
            // builder.add(RagiumKeyBinds.CATEGORY, RagiumAPI.MOD_NAME)

            // builder.add(RagiumKeyBinds.OPEN_BACKPACK.translationKey, "バックパックを開く")
            // Machine
            builder.add(RagiumTranslationKeys.MACHINE_NAME, "名称: %s")
            builder.add(RagiumTranslationKeys.MACHINE_TIER, "ティア: %s")
            builder.add(RagiumTranslationKeys.MACHINE_RECIPE_COST, "処理コスト: %s E")
            builder.add(RagiumTranslationKeys.MACHINE_SHOW_PREVIEW, "プレビューの表示: %s")
            builder.add(RagiumTranslationKeys.MACHINE_SLOT_COUNTS, "入力/出力スロット数: %s")
            builder.add(RagiumTranslationKeys.MACHINE_TANK_CAPACITY, "各液体タンクの容量: %s バケツ")

            builder.add(RagiumTranslationKeys.MULTI_SHAPE_ERROR, "次の条件を満たしていません; %s (座標 %s)")
            builder.add(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS, "構造物は有効です！")
            // Machine Tier
            builder.add(HTMachineTier.PRIMITIVE, "簡易", "簡易型%s")
            builder.add(HTMachineTier.BASIC, "基本", "基本型%s")
            builder.add(HTMachineTier.ADVANCED, "発展", "発展型%s")
            // Machine SizeType
            // builder.add(HTMachineType.Default, "デフォルト機械")

            builder.add(RagiumMachineTypes.Generator.COMBUSTION, "燃焼発電機")
            builder.add(RagiumMachineTypes.Generator.SOLAR, "太陽光発電機")
            builder.add(RagiumMachineTypes.Generator.THERMAL, "地熱発電機")
            builder.add(RagiumMachineTypes.Generator.WATER, "水力発電機")

            builder.add(RagiumMachineTypes.HEAT_GENERATOR, "火力発電機")

            builder.add(RagiumMachineTypes.Processor.ALLOY_FURNACE, "合金かまど")
            builder.add(RagiumMachineTypes.Processor.ASSEMBLER, "組立機")
            builder.add(RagiumMachineTypes.Processor.CHEMICAL_REACTOR, "化学反応槽")
            // builder.add(RagiumMachineTypes.Processor.COMPRESSOR, "圧縮機")
            // builder.add(RagiumMachineTypes.Processor.DECOMPRESSOR, "減圧機")
            builder.add(RagiumMachineTypes.Processor.ELECTROLYZER, "電解槽")
            builder.add(RagiumMachineTypes.Processor.EXTRACTOR, "抽出器")
            builder.add(RagiumMachineTypes.Processor.GRINDER, "粉砕機")
            builder.add(RagiumMachineTypes.Processor.METAL_FORMER, "金属加工機")
            builder.add(RagiumMachineTypes.Processor.MIXER, "ミキサー")
            builder.add(RagiumMachineTypes.Processor.ROCK_GENERATOR, "岩石生成機")

            builder.add(RagiumMachineTypes.BLAST_FURNACE, "大型高炉")
            builder.add(RagiumMachineTypes.DISTILLATION_TOWER, "蒸留塔")
            // builder.add(RagiumMachineTypes.FLUID_DRILL, "液体ドリル")
            // builder.add(RagiumMachineTypes.MOB_EXTRACTOR, "モブ抽出器")
            builder.add(RagiumMachineTypes.SAW_MILL, "製材機")
            // Mod Menu
            builder.add(RagiumTranslationKeys.CONFIG_TILE, "Ragium - Config")
            builder.add(RagiumTranslationKeys.CONFIG_IS_HARD_MODE, "ハードモードの切り替え（reloadコマンドで反映）")
            // Jade
            builder.add(RagiumTranslationKeys.CONFIG_JADE_MACHINE, "機械")
            builder.add(RagiumTranslationKeys.CONFIG_JADE_NETWORK_INTERFACE, "E.N.I")

            builder.add(RagiumTranslationKeys.PROVIDER_JADE_NETWORK_INTERFACE, "エネルギー量: %s Unit")
            // REI
            builder.add(RagiumTranslationKeys.REI_RECIPE_BIOME, "次のバイオームで見つかる: %s")
            builder.add(RagiumTranslationKeys.REI_RECIPE_INFO, "レシピ情報")
            builder.add(RagiumTranslationKeys.REI_RECIPE_REQUIRE_SCAN, "スキャンが必要です！")
        }
    }
}
