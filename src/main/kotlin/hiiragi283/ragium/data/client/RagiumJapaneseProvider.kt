package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.add
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.common.init.*
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.LanguageProvider

class RagiumJapaneseProvider(output: PackOutput) : LanguageProvider(output, RagiumAPI.MOD_ID, "ja_jp") {
    override fun addTranslations() {
        // Block
        add(RagiumBlocks.SOUL_MAGMA_BLOCK, "ソウルマグマブロック")

        add(RagiumBlocks.CHEMICAL_GLASS, "化学ガラス")
        add(RagiumBlocks.SHAFT, "シャフト")

        add(RagiumBlocks.PLASTIC_BLOCK, "プラスチックブロック")

        add(RagiumBlocks.SPONGE_CAKE, "スポンジケーキ")
        add(RagiumBlocks.SWEET_BERRIES_CAKE, "スイートベリーケーキ")

        add(RagiumBlocks.MANUAL_GRINDER, "らぎ臼")
        add(RagiumBlocks.PRIMITIVE_BLAST_FURNACE, "らぎ高炉")

        add(RagiumBlocks.CATALYST_ADDON, "触媒アドオン")
        add(RagiumBlocks.ENERGY_NETWORK_INTERFACE, "E.N.I.")
        add(RagiumBlocks.SUPERCONDUCTIVE_COOLANT, "超伝導冷却材")

        add(RagiumBlocks.Decorations.RAGI_ALLOY_BLOCK, "ラギ合金ブロック（装飾）")
        add(RagiumBlocks.Decorations.RAGI_STEEL_BLOCK, "ラギスチールブロック（装飾）")

        add(RagiumBlocks.Decorations.BASIC_CASING, "基本外装（装飾）")
        add(RagiumBlocks.Decorations.ADVANCED_CASING, "発展外装（装飾）")
        add(RagiumBlocks.Decorations.ELITE_CASING, "精鋭外装（装飾）")
        add(RagiumBlocks.Decorations.ULTIMATE_CASING, "究極外装（装飾）")

        add(RagiumBlocks.Decorations.BASIC_COIL, "基本コイル（装飾）")
        add(RagiumBlocks.Decorations.ADVANCED_COIL, "発展コイル（装飾）")
        add(RagiumBlocks.Decorations.ELITE_COIL, "精鋭コイル（装飾）")
        add(RagiumBlocks.Decorations.ULTIMATE_COIL, "究極コイル（装飾）")

        add(RagiumBlocks.LEDBlocks.RED, "LEDブロック（赤）")
        add(RagiumBlocks.LEDBlocks.GREEN, "LEDブロック（緑）")
        add(RagiumBlocks.LEDBlocks.BLUE, "LEDブロック（青）")
        add(RagiumBlocks.LEDBlocks.CYAN, "LEDブロック（シアン）")
        add(RagiumBlocks.LEDBlocks.MAGENTA, "LEDブロック（マゼンタ）")
        add(RagiumBlocks.LEDBlocks.YELLOW, "LEDブロック（黄色）")
        add(RagiumBlocks.LEDBlocks.WHITE, "LEDブロック")

        // Content
        add(RagiumTranslationKeys.BURNER, "バーナー")
        add(RagiumTranslationKeys.CASING, "外装")
        add(RagiumTranslationKeys.CASING_WALL, "外装の壁")
        add(RagiumTranslationKeys.CIRCUIT, "回路")
        add(RagiumTranslationKeys.COIL, "コイル")
        add(RagiumTranslationKeys.CRATE, "クレート")
        add(RagiumTranslationKeys.DRUM, "ドラム")
        add(RagiumTranslationKeys.GRATE, "格子")
        add(RagiumTranslationKeys.HULL, "筐体")
        add(RagiumTranslationKeys.PLASTIC, "プラスチック")

        add(HTOreVariant.OVERWORLD, "%s鉱石")
        add(HTOreVariant.DEEPSLATE, "深層%s鉱石")
        add(HTOreVariant.NETHER, "ネザー%s鉱石")
        add(HTOreVariant.END, "エンド%s鉱石")
        // Fluids
        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            add(fluid.typeHolder.get().descriptionId, fluid.jaName)
        }
        // Items
        add(RagiumItems.SWEET_BERRIES_CAKE_PIECE, "一切れのスイートベリーケーキ")
        add(RagiumItems.MELON_PIE, "メロンパイ")

        add(RagiumItems.BUTTER, "バター")
        add(RagiumItems.CARAMEL, "キャラメル")
        add(RagiumItems.DOUGH, "生地")
        add(RagiumItems.FLOUR, "小麦粉")

        add(RagiumItems.CHOCOLATE, "チョコレート")
        add(RagiumItems.CHOCOLATE_APPLE, "チョコリンゴ")
        add(RagiumItems.CHOCOLATE_BREAD, "チョコパン")
        add(RagiumItems.CHOCOLATE_COOKIE, "チョコレートクッキー")

        add(RagiumItems.CINNAMON_STICK, "シナモンスティック")
        add(RagiumItems.CINNAMON_POWDER, "シナモンパウダー")
        add(RagiumItems.CINNAMON_ROLL, "シナモンロール")

        add(RagiumItems.MINCED_MEAT, "ひき肉")
        add(RagiumItems.MEAT_INGOT, "生肉インゴット")
        add(RagiumItems.COOKED_MEAT_INGOT, "焼肉インゴット")
        add(RagiumItems.CANNED_COOKED_MEAT, "焼肉缶詰")

        add(RagiumItems.AMBROSIA, "アンブロシア")

        add(RagiumItems.FORGE_HAMMER, "鍛造ハンマー")
        add(RagiumItems.SILKY_PICKAXE, "シルキーピッケル")

        add(RagiumItems.DYNAMITE, "ダイナマイト")
        add(RagiumItems.SLOT_LOCK, "スロットロック")

        add(RagiumItems.GEAR_PRESS_MOLD, "プレス型（歯車）")
        add(RagiumItems.PLATE_PRESS_MOLD, "プレス型（板）")
        add(RagiumItems.ROD_PRESS_MOLD, "プレス型（棒）")

        add(RagiumItems.ALKALI_REAGENT, "アルカリ試薬")
        add(RagiumItems.BLAZE_REAGENT, "ブレイズ試薬")
        add(RagiumItems.CREEPER_REAGENT, "クリーパー試薬")
        add(RagiumItems.DEEPANT_REAGENT, "ディーパント試薬")
        add(RagiumItems.ENDER_REAGENT, "エンダー試薬")
        add(RagiumItems.NETHER_REAGENT, "ネザー試薬")
        add(RagiumItems.PRISMARINE_REAGENT, "プリズマリン試薬")
        add(RagiumItems.SCULK_REAGENT, "スカルク試薬")
        add(RagiumItems.WITHER_REAGENT, "ウィザー試薬")

        add(RagiumItems.BEE_WAX, "蜜蠟")
        add(RagiumItems.CALCIUM_CARBIDE, "炭化カルシウム")
        add(RagiumItems.CIRCUIT_BOARD, "回路基板")
        add(RagiumItems.CRIMSON_CRYSTAL, "深紅の結晶")
        add(RagiumItems.ENGINE, "V8エンジン")
        add(RagiumItems.LED, "L.E.D.")
        add(RagiumItems.LUMINESCENCE_DUST, "ルミネッセンスダスト")
        add(RagiumItems.OBSIDIAN_TEAR, "黒曜石の涙")
        add(RagiumItems.PLASTIC_PLATE, "プラスチック板")
        add(RagiumItems.POLYMER_RESIN, "高分子樹脂")
        add(RagiumItems.RAGI_ALLOY_COMPOUND, "ラギ合金混合物")
        add(RagiumItems.SILKY_CRYSTAL, "シルキー結晶")
        add(RagiumItems.SOAP, "石鹸")
        add(RagiumItems.SOLAR_PANEL, "太陽光パネル")
        add(RagiumItems.WARPED_CRYSTAL, "歪んだ結晶")

        add(RagiumItems.NUCLEAR_WASTE, "核廃棄物")
        add(RagiumItems.PLUTONIUM_FUEL, "プルトニウム燃料")
        add(RagiumItems.URANIUM_FUEL, "ウラン燃料")
        add(RagiumItems.YELLOW_CAKE, "イエローケーキ")
        add(RagiumItems.YELLOW_CAKE_PIECE, "一切れのイエローケーキ")

        add(RagiumItems.RAGI_TICKET, "らぎチケット")

        // Machine
        add(HTMachineTier.BASIC, "基本", "基本%s")
        add(HTMachineTier.ADVANCED, "発展", "発展%s")
        add(HTMachineTier.ELITE, "精鋭", "精鋭%s")
        add(HTMachineTier.ULTIMATE, "究極", "究極%s")

        add(RagiumTranslationKeys.MACHINE_COST, "- 処理コスト: %s FE/回")
        add(RagiumTranslationKeys.MACHINE_NAME, "- 機械: %s")
        add(RagiumTranslationKeys.MACHINE_TIER, "- ティア: %s")

        add(RagiumTranslationKeys.MACHINE_PREVIEW, "- プレビューの表示: %s")
        add(RagiumTranslationKeys.MACHINE_TICK_RATE, "- 処理時間: %s ティック（%s秒）")
        add(RagiumTranslationKeys.MACHINE_WORKING, "- 稼働中: %s")

        add(RagiumTranslationKeys.MULTI_SHAPE_ERROR, "次の条件を満たしていません: %s (座標: %s)")
        add(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS, "機械の構造は有効です！")

        add(RagiumTranslationKeys.BIOME_CONDITION, "次のバイオームの範囲内で動作が必要です: %s")
        add(RagiumTranslationKeys.CATALYST_CONDITION, "触媒アイテムが必要です")
        add(RagiumTranslationKeys.ENCHANTMENT_CONDITION, "次のエンチャントが必要です: %s")
        add(RagiumTranslationKeys.ROCK_GENERATOR_CONDITION, "周囲に水源と溶岩源，そして触媒アイテムが必要です")
        add(RagiumTranslationKeys.SOURCE_CONDITION, "次のソース「%s」が%s側から必要です")
        // Machine Type
        add(RagiumMachineKeys.BEDROCK_MINER, "岩盤採掘機", "岩盤から鉱物を採掘する")
        add(RagiumMachineKeys.DRAIN, "排水溝", "正面から液体を，上から経験値を，スロット内の液体キューブから中身を吸い取る")

        add(RagiumMachineKeys.COMBUSTION_GENERATOR, "燃焼発電機", "液体燃料から発電する")
        add(RagiumMachineKeys.GAS_TURBINE, "ガスタービン", "気体燃料から発電する")
        add(RagiumMachineKeys.NUCLEAR_REACTOR, "原子炉", "放射性燃料から発電する")
        add(RagiumMachineKeys.SOLAR_GENERATOR, "太陽光発電機", "日中に発電する")
        add(RagiumMachineKeys.STEAM_TURBINE, "蒸気タービン", "蒸気から発電する")
        add(RagiumMachineKeys.THERMAL_GENERATOR, "地熱発電機", "高温の液体から発電する")
        add(RagiumMachineKeys.VIBRATION_GENERATOR, "音波発電機", "エッチなのはダメ!死刑!")

        add(RagiumMachineKeys.ASSEMBLER, "組立機", "君こそが天才だ!")
        add(RagiumMachineKeys.BLAST_FURNACE, "大型高炉", "複数の素材を一つに焼き上げる")
        add(RagiumMachineKeys.CHEMICAL_REACTOR, "化学反応槽", "Are You Ready?")
        add(RagiumMachineKeys.COMPRESSOR, "圧縮機", "saves.zip.zip")
        add(RagiumMachineKeys.EXTRACTOR, "抽出器", "遠心分離機みたいなやつ")
        add(RagiumMachineKeys.GRINDER, "粉砕機", "ダイヤモンドは壊れない")
        add(RagiumMachineKeys.GROWTH_CHAMBER, "成長チャンバー", "成長バーチャン")
        add(RagiumMachineKeys.INFUSER, "Infuser", "遠心分離機みたくないやつ")
        add(RagiumMachineKeys.LASER_TRANSFORMER, "レーザー変換機", "レーザーオン…")
        add(RagiumMachineKeys.MULTI_SMELTER, "並列精錬機", "複数のアイテムを一度に製錬する")
        add(RagiumMachineKeys.MIXER, "ミキサー", "ベストマッチ!")
        add(RagiumMachineKeys.REFINERY, "精製機", "プロジェクト・ビルド")
        add(RagiumMachineKeys.RESOURCE_PLANT, "資源プラント", "チョコレートファウンテンのように")
        add(RagiumMachineKeys.STEAM_BOILER, "蒸気ボイラー", "スチールホッパー!")
        // Material
        add(CommonMaterials.ALUMINA, "アルミナ")
        add(CommonMaterials.ALUMINUM, "アルミニウム")
        add(CommonMaterials.ANTIMONY, "アンチモン")
        add(CommonMaterials.BERYLLIUM, "ベリリウム")
        add(CommonMaterials.ASH, "灰")
        add(CommonMaterials.BAUXITE, "ボーキサイト")
        add(CommonMaterials.BRASS, "真鍮")
        add(CommonMaterials.BRONZE, "青銅")
        add(CommonMaterials.CADMIUM, "カドミウム")
        add(CommonMaterials.CARBON, "炭素")
        add(CommonMaterials.CHROMIUM, "クロム")
        add(CommonMaterials.CRYOLITE, "氷晶石")
        add(CommonMaterials.ELECTRUM, "琥珀金")
        add(CommonMaterials.FLUORITE, "蛍石")
        add(CommonMaterials.INVAR, "不変鋼")
        add(CommonMaterials.IRIDIUM, "イリジウム")
        add(CommonMaterials.LEAD, "鉛")
        add(CommonMaterials.NICKEL, "ニッケル")
        add(CommonMaterials.OSMIUM, "オスミウム")
        add(CommonMaterials.PERIDOT, "ペリドット")
        add(CommonMaterials.PLATINUM, "白金")
        add(CommonMaterials.PLUTONIUM, "プルトニウム")
        add(CommonMaterials.RUBY, "ルビー")
        add(CommonMaterials.SALT, "塩")
        add(CommonMaterials.SALTPETER, "硝石")
        add(CommonMaterials.SAPPHIRE, "サファイア")
        add(CommonMaterials.SILICON, "シリコン")
        add(CommonMaterials.SILVER, "銀")
        add(CommonMaterials.STAINLESS_STEEL, "ステンレス鋼")
        add(CommonMaterials.STEEL, "スチール")
        add(CommonMaterials.SULFUR, "硫黄")
        add(CommonMaterials.TIN, "スズ")
        add(CommonMaterials.TITANIUM, "チタン")
        add(CommonMaterials.TUNGSTEN, "タングステン")
        add(CommonMaterials.URANIUM, "ウラニウム")
        add(CommonMaterials.WOOD, "木材")
        add(CommonMaterials.ZINC, "亜鉛")
        add(IntegrationMaterials.BLACK_QUARTZ, "黒水晶")
        add(IntegrationMaterials.DARK_GEM, "ダークジェム")
        add(IntegrationMaterials.REFINED_GLOWSTONE, "精製グロウストーン")
        add(IntegrationMaterials.REFINED_OBSIDIAN, "精製黒曜石")
        add(RagiumMaterials.CRUDE_RAGINITE, "粗製ラギナイト")
        add(RagiumMaterials.DEEP_STEEL, "深層鋼")
        add(RagiumMaterials.ECHORIUM, "エコリウム")
        add(RagiumMaterials.FIERY_COAL, "燃え盛る石炭")
        add(RagiumMaterials.RAGI_ALLOY, "ラギ合金")
        add(RagiumMaterials.RAGI_CRYSTAL, "ラギクリスタリル")
        add(RagiumMaterials.RAGI_STEEL, "ラギスチール")
        add(RagiumMaterials.RAGINITE, "ラギナイト")
        add(RagiumMaterials.RAGIUM, "ラギウム")
        add(RagiumMaterials.RESIDUAL_COKE, "残渣油コークス")
        add(RagiumMaterials.SLAG, "スラグ")
        add(VanillaMaterials.AMETHYST, "アメシスト")
        add(VanillaMaterials.COAL, "石炭")
        add(VanillaMaterials.COPPER, "銅")
        add(VanillaMaterials.DIAMOND, "ダイアモンド")
        add(VanillaMaterials.EMERALD, "エメラルド")
        add(VanillaMaterials.GOLD, "金")
        add(VanillaMaterials.IRON, "鉄")
        add(VanillaMaterials.LAPIS, "ラピス")
        add(VanillaMaterials.NETHERITE, "ネザライト")
        add(VanillaMaterials.NETHERITE_SCRAP, "ネザライトの欠片")
        add(VanillaMaterials.QUARTZ, "水晶")
        add(VanillaMaterials.REDSTONE, "レッドストーン")
        // Tag Prefix
        add(HTTagPrefix.CASING, "%s外装")
        add(HTTagPrefix.CLUMP, "%sの凝塊")
        add(HTTagPrefix.CRYSTAL, "%sの結晶")
        add(HTTagPrefix.DIRTY_DUST, "汚れた%sの粉")
        add(HTTagPrefix.DUST, "%sの粉")
        add(HTTagPrefix.GEAR, "%sの歯車")
        add(HTTagPrefix.GEM, "%s")
        add(HTTagPrefix.INGOT, "%sインゴット")
        add(HTTagPrefix.NUGGET, "%sのナゲット")
        add(HTTagPrefix.ORE, "%s鉱石")
        add(HTTagPrefix.PLATE, "%s板")
        add(HTTagPrefix.RAW_MATERIAL, "%sの原石")
        add(HTTagPrefix.RAW_STORAGE, "%sの原石ブロック")
        add(HTTagPrefix.ROD, "%s棒")
        add(HTTagPrefix.SHARD, "%sの欠片")
        add(HTTagPrefix.SHEETMETAL, "%sの板金")
        add(HTTagPrefix.STORAGE_BLOCK, "%sブロック")
        add(HTTagPrefix.TINY_DUST, "小さな%sの粉")
        add(HTTagPrefix.WIRE, "%sのワイヤー")
        // Tags
        add(RagiumItemTags.ALKALI_REAGENTS, "アルカリ試薬")
        add(RagiumItemTags.DOUGH, "生地")
        add(RagiumItemTags.PLASTICS, "プラスチック")

        add(RagiumItemTags.SOLAR_PANELS, "太陽光パネル")
        // Misc
        add(RagiumTranslationKeys.FLUID_AMOUNT, "液体量: %s mb")
        add(RagiumTranslationKeys.FLUID_CAPACITY, "容量: %s mb")

        add("config.jade.plugin_ragium.energy_network", "エネルギーネットワーク")
        add("config.jade.plugin_ragium.machine_info", "機械の情報")
    }
}
