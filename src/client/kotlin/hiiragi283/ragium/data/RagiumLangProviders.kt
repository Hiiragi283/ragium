package hiiragi283.ragium.data

import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.content.HTRegistryContent
import hiiragi283.ragium.api.data.HTLangType
import hiiragi283.ragium.api.extension.splitWith
import hiiragi283.ragium.api.machine.HTMachine
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
    fun TranslationBuilder.add(entry: HTRegistryContent<Block>, value: String, desc: String? = null) {
        val block: Block = entry.value
        add(block, value)
        desc?.let { add("${block.translationKey}.description", it) }
    }

    @JvmName("addItem")
    fun TranslationBuilder.add(entry: HTRegistryContent<Item>, value: String) {
        add(entry.value, value)
    }

    fun TranslationBuilder.add(enchantment: RegistryKey<Enchantment>, value: String) {
        add("enchantment.${enchantment.value.splitWith('.')}", value)
    }

    fun TranslationBuilder.add(tier: HTMachineTier, name: String, prefix: String) {
        add(tier.translationKey, name)
        add(tier.prefixKey, prefix)
    }

    fun TranslationBuilder.add(type: HTMachine, value: String, desc: String? = null) {
        add(type.key.translationKey, value)
        desc?.let { add(type.key.descriptionKey, it) }
    }

    @JvmStatic
    private fun translateContents(builder: TranslationBuilder, type: HTLangType) {
        // blocks
        buildList {
            addAll(RagiumContents.Ores.entries)
            addAll(RagiumContents.StorageBlocks.entries)

            addAll(RagiumContents.Hulls.entries)
            addAll(RagiumContents.Coils.entries)
            addAll(RagiumContents.Exporters.entries)
        }.forEach { block: HTContent<Block> -> builder.add(block, block.getTranslation(type)) }
        // items
        buildList {
            addAll(RagiumContents.Dusts.entries)
            addAll(RagiumContents.Gems.entries)
            addAll(RagiumContents.Ingots.entries)
            addAll(RagiumContents.Plates.entries)
            addAll(RagiumContents.RawMaterials.entries)

            addAll(RagiumContents.CircuitBoards.entries)
            addAll(RagiumContents.Circuits.entries)

            addAll(HTCrafterHammerItem.Behavior.entries)
        }.forEach { item: HTContent<Item> -> builder.add(item, item.getTranslation(type)) }
        // fluids
        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            builder.add(
                fluid.translationKey,
                fluid.getTranslation(type),
            )
        }
    }

    //    English    //

    private class EnglishLang(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricLanguageProvider(output, registryLookup) {
        override fun generateTranslations(registryLookup: RegistryWrapper.WrapperLookup, builder: TranslationBuilder) {
            translateContents(builder, HTLangType.EN_US)
            // Advancements
            RagiumAdvancementProvider.register.generateLang(HTLangType.EN_US, builder)
            // Blocks
            builder.add(RagiumBlocks.SPONGE_CAKE, "Sponge Cake")
            builder.add(RagiumBlocks.SWEET_BERRIES_CAKE, "Sweet Berries Cake")

            builder.add(RagiumBlocks.ADVANCED_CASING, "Advanced Casing")
            builder.add(RagiumBlocks.BASIC_CASING, "Basic Casing")
            builder.add(RagiumBlocks.CREATIVE_SOURCE, "Creative Power Source")
            builder.add(RagiumBlocks.FIREBOX, "Firebox")
            builder.add(RagiumBlocks.ITEM_DISPLAY, "Item Display")
            builder.add(RagiumBlocks.MANUAL_FORGE, "Ragi-Anvil")
            builder.add(RagiumBlocks.MANUAL_GRINDER, "Ragi-Grinder")
            builder.add(RagiumBlocks.MANUAL_MIXER, "Ragi-Basin")
            builder.add(RagiumBlocks.NETWORK_INTERFACE, "E.N.I.")
            builder.add(RagiumBlocks.SHAFT, "Shaft")
            builder.add(RagiumBlocks.TRADER_STATION, "Trader Station")
            builder.add(RagiumBlocks.TRASH_BOX, "Trash Box")

            builder.add(RagiumContents.Armors.STEEL_HELMET, "Steel Helmet")
            builder.add(RagiumContents.Armors.STEEL_CHESTPLATE, "Steel Chestplate")
            builder.add(RagiumContents.Armors.STEEL_LEGGINGS, "Steel Leggings")
            builder.add(RagiumContents.Armors.STEEL_BOOTS, "Steel Boots")
            builder.add(RagiumContents.Armors.STELLA_GOGGLE, "S.T.E.L.L.A. Goggles")
            builder.add(RagiumContents.Armors.STELLA_JACKET, "S.T.E.L.L.A. Jacket")
            builder.add(RagiumContents.Armors.STELLA_LEGGINGS, "S.T.E.L.L.A. Leggings")
            builder.add(RagiumContents.Armors.STELLA_BOOTS, "S.T.E.L.L.A. Boots")

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
            builder.add(RagiumItems.BEE_WAX, "Bee Wax")
            builder.add(RagiumItems.BUTTER, "Butter")
            builder.add(RagiumItems.CARAMEL, "Caramel")
            builder.add(RagiumItems.CHOCOLATE, "Chocolate")
            builder.add(RagiumItems.CHOCOLATE_APPLE, "Chocolate Apple")
            builder.add(RagiumItems.CHOCOLATE_BREAD, "Chocolate Bread")
            builder.add(RagiumItems.FLOUR, "Flour")
            builder.add(RagiumItems.DOUGH, "Dough")
            builder.add(RagiumItems.MINCED_MEAT, "Minced Meat")
            builder.add(RagiumItems.PULP, "Pulp")

            builder.add(RagiumItems.BACKPACK, "Backpack")
            builder.add(RagiumItems.BASALT_MESH, "Basalt Mesh")
            builder.add(RagiumItems.CRAFTER_HAMMER, "Crafter's Hammer")
            builder.add(RagiumItems.DYNAMITE, "Dynamite")
            builder.add(RagiumItems.EMPTY_FLUID_CUBE, "Fluid Cube (Empty)")
            builder.add(RagiumItems.FILLED_FLUID_CUBE, "Fluid Cube (%s)")
            builder.add(RagiumItems.ENGINE, "V8 Engine")
            builder.add(RagiumItems.FORGE_HAMMER, "Forge Hammer")
            builder.add(RagiumItems.HEART_OF_THE_NETHER, "Heart of the Nether")
            builder.add(RagiumItems.POLYMER_RESIN, "Polymer Resin")
            builder.add(RagiumItems.PROCESSOR_SOCKET, "Processor Socket")
            builder.add(RagiumItems.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound")
            builder.add(RagiumItems.RAGI_CRYSTAL_PROCESSOR, "Ragi-Crystal Processor")
            builder.add(RagiumItems.REMOVER_DYNAMITE, "Remover Dynamite")
            builder.add(RagiumItems.SOAP_INGOT, "Soap Ingot")
            builder.add(RagiumItems.SOLAR_PANEL, "Solar Panel")
            builder.add(RagiumItems.TRADER_CATALOG, "Trader Catalog")

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
            builder.add(RagiumTranslationKeys.MACHINE_FLUID_AMOUNT, "Amount: %s Units")
            builder.add(RagiumTranslationKeys.MACHINE_NETWORK_ENERGY, "Network Energy: %s Units")
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
            builder.add(
                RagiumMachineKeys.DRAIN,
                "Drain",
                "Drains fluids from each side",
            )

            builder.add(
                RagiumMachineKeys.COMBUSTION_GENERATOR,
                "Combustion Generator",
                "Generate energy from liquid fuels",
            )
            builder.add(
                RagiumMachineKeys.SOLAR_PANEL,
                "Solar Generator",
                "Generate energy in daytime",
            )
            builder.add(
                RagiumMachineKeys.STEAM_GENERATOR,
                "Steam Generator",
                "Generate energy from water and below heat source",
            )
            builder.add(
                RagiumMachineKeys.THERMAL_GENERATOR,
                "Thermal Generator",
                "Generate energy from hot fluids",
            )
            builder.add(RagiumMachineKeys.WATER_GENERATOR, "Water Generator")

            builder.add(
                RagiumMachineKeys.ALLOY_FURNACE,
                "Alloy Furnace",
                "Smelt two ingredients into one",
            )
            builder.add(RagiumMachineKeys.ASSEMBLER, "Assembler")
            builder.add(
                RagiumMachineKeys.BLAST_FURNACE,
                "Large Blast Furnace",
                "Smelt multiple ingredients into one",
            )
            builder.add(RagiumMachineKeys.CHEMICAL_REACTOR, "Chemical Reactor")
            builder.add(
                RagiumMachineKeys.DISTILLATION_TOWER,
                "Distillation Tower",
                "Process Crude Oil",
            )
            builder.add(RagiumMachineKeys.ELECTROLYZER, "Electrolyzer")
            builder.add(RagiumMachineKeys.EXTRACTOR, "Extractor")
            builder.add(RagiumMachineKeys.GRINDER, "Grinder")
            builder.add(RagiumMachineKeys.METAL_FORMER, "Metal Former")
            builder.add(RagiumMachineKeys.MIXER, "Mixer")
            builder.add(RagiumMachineKeys.MULTI_SMELTER, "Multi Smelter")
            builder.add(RagiumMachineKeys.ROCK_GENERATOR, "Rock Generator")
            builder.add(
                RagiumMachineKeys.SAW_MILL,
                "Saw Mill",
                "Process Logs more efficiently",
            )
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

            builder.add(RagiumBlocks.SPONGE_CAKE, "スポンジケーキ")
            builder.add(RagiumBlocks.SWEET_BERRIES_CAKE, "スイートベリーケーキ")

            builder.add(RagiumBlocks.ADVANCED_CASING, "発展外装")
            builder.add(RagiumBlocks.BASIC_CASING, "基本外装")
            builder.add(RagiumBlocks.CREATIVE_SOURCE, "クリエイティブ用エネルギー源")
            builder.add(RagiumBlocks.FIREBOX, "火室")
            builder.add(RagiumBlocks.ITEM_DISPLAY, "アイテムティスプレイ")
            builder.add(RagiumBlocks.MANUAL_FORGE, "らぎ金床")
            builder.add(RagiumBlocks.MANUAL_GRINDER, "らぎ臼")
            builder.add(RagiumBlocks.MANUAL_MIXER, "らぎ釜")
            builder.add(RagiumBlocks.NETWORK_INTERFACE, "E.N.I.")
            builder.add(RagiumBlocks.SHAFT, "シャフト")
            builder.add(RagiumBlocks.TRADER_STATION, "貿易ステーション")
            builder.add(RagiumBlocks.TRASH_BOX, "ゴミ箱")

            builder.add(RagiumContents.Armors.STEEL_HELMET, "スチールのヘルメット")
            builder.add(RagiumContents.Armors.STEEL_CHESTPLATE, "スチールのチェストプレート")
            builder.add(RagiumContents.Armors.STEEL_LEGGINGS, "スチールのレギンス")
            builder.add(RagiumContents.Armors.STEEL_BOOTS, "スチールのブーツ")
            builder.add(RagiumContents.Armors.STELLA_GOGGLE, "S.T.E.L.L.A.ゴーグル")
            builder.add(RagiumContents.Armors.STELLA_JACKET, "S.T.E.L.L.A.ジャケット")
            builder.add(RagiumContents.Armors.STELLA_LEGGINGS, "S.T.E.L.L.A.レギンス")
            builder.add(RagiumContents.Armors.STELLA_BOOTS, "S.T.E.L.L.A.ブーツ")

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
            builder.add(RagiumItems.BEE_WAX, "蜜蠟")
            builder.add(RagiumItems.BUTTER, "バター")
            builder.add(RagiumItems.CARAMEL, "キャラメル")
            builder.add(RagiumItems.CHOCOLATE, "チョコレート")
            builder.add(RagiumItems.CHOCOLATE_APPLE, "チョコリンゴ")
            builder.add(RagiumItems.CHOCOLATE_BREAD, "チョコパン")
            builder.add(RagiumItems.FLOUR, "小麦粉")
            builder.add(RagiumItems.DOUGH, "生地")
            builder.add(RagiumItems.MINCED_MEAT, "ひき肉")
            builder.add(RagiumItems.PULP, "パルプ")

            builder.add(RagiumItems.BACKPACK, "バックパック")
            builder.add(RagiumItems.BASALT_MESH, "玄武岩メッシュ")
            builder.add(RagiumItems.CRAFTER_HAMMER, "クラフターズ・ハンマー")
            builder.add(RagiumItems.DYNAMITE, "ダイナマイト")
            builder.add(RagiumItems.EMPTY_FLUID_CUBE, "液体キューブ（なし）")
            builder.add(RagiumItems.FILLED_FLUID_CUBE, "液体キューブ（%s）")
            builder.add(RagiumItems.ENGINE, "V8エンジン")
            builder.add(RagiumItems.FORGE_HAMMER, "鍛造ハンマー")
            builder.add(RagiumItems.HEART_OF_THE_NETHER, "地獄の心臓")
            // builder.add(RagiumItems.OBLIVION_CUBE_SPAWN_EGG, "スポーン 忘却の箱")
            builder.add(RagiumItems.POLYMER_RESIN, "高分子樹脂")
            builder.add(RagiumItems.PROCESSOR_SOCKET, "プロセッサソケット")
            builder.add(RagiumItems.RAGI_ALLOY_COMPOUND, "ラギ合金混合物")
            builder.add(RagiumItems.RAGI_CRYSTAL_PROCESSOR, "ラギクリスタリルプロセッサ")
            builder.add(RagiumItems.REMOVER_DYNAMITE, "削除用ダイナマイト")
            builder.add(RagiumItems.SOAP_INGOT, "石鹸インゴット")
            builder.add(RagiumItems.SOLAR_PANEL, "太陽光パネル")
            builder.add(RagiumItems.TRADER_CATALOG, "行商人カタログ")

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
            builder.add(RagiumTranslationKeys.MACHINE_FLUID_AMOUNT, "液体量: %s ユニット")
            builder.add(RagiumTranslationKeys.MACHINE_NETWORK_ENERGY, "ネットワーク上のエネルギー量: %s ユニット")
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
            // Machine Type
            builder.add(RagiumMachineKeys.DRAIN, "排水溝", "各面から液体を吸い取る")

            builder.add(RagiumMachineKeys.COMBUSTION_GENERATOR, "燃焼発電機", "液体燃料から発電する")
            builder.add(RagiumMachineKeys.SOLAR_PANEL, "太陽光発電機", "日中に発電する")
            builder.add(RagiumMachineKeys.STEAM_GENERATOR, "蒸気発電機", "水と下部の熱源から発電する")
            builder.add(RagiumMachineKeys.THERMAL_GENERATOR, "地熱発電機", "高温の液体から発電する")
            builder.add(RagiumMachineKeys.WATER_GENERATOR, "水力発電機")

            builder.add(RagiumMachineKeys.ALLOY_FURNACE, "合金かまど", "二つの素材を一つに焼き上げる")
            builder.add(RagiumMachineKeys.ASSEMBLER, "組立機")
            builder.add(RagiumMachineKeys.BLAST_FURNACE, "大型高炉", "複数の素材を一つに焼き上げる")
            builder.add(RagiumMachineKeys.CHEMICAL_REACTOR, "化学反応槽")
            builder.add(RagiumMachineKeys.DISTILLATION_TOWER, "蒸留塔", "原油を処理する")
            builder.add(RagiumMachineKeys.ELECTROLYZER, "電解槽")
            builder.add(RagiumMachineKeys.EXTRACTOR, "抽出器")
            builder.add(RagiumMachineKeys.GRINDER, "粉砕機")
            builder.add(RagiumMachineKeys.METAL_FORMER, "金属加工機")
            builder.add(RagiumMachineKeys.MULTI_SMELTER, "並列精錬機")
            builder.add(RagiumMachineKeys.MIXER, "ミキサー")
            builder.add(RagiumMachineKeys.ROCK_GENERATOR, "岩石生成機", "岩石を生成する")
            builder.add(RagiumMachineKeys.SAW_MILL, "製材機", "より効率的に原木を加工する")
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
