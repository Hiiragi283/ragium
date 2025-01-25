package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.data.add
import hiiragi283.ragium.integration.RagiumMekIntegration
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

        add(RagiumBlocks.CATALYST_ADDON, "触媒アドオン")
        add(RagiumBlocks.ENERGY_NETWORK_INTERFACE, "E.N.I.")

        add(RagiumBlocks.Decorations.RAGI_ALLOY_BLOCK, "ラギ合金ブロック（装飾）")
        add(RagiumBlocks.Decorations.RAGI_STEEL_BLOCK, "ラギスチールブロック（装飾）")
        add(RagiumBlocks.Decorations.REFINED_RAGI_STEEL_BLOCK, "精製ラギスチールブロック（装飾）")

        add(RagiumBlocks.Decorations.BASIC_CASING, "基本外装（装飾）")
        add(RagiumBlocks.Decorations.ADVANCED_CASING, "発展外装（装飾）")
        add(RagiumBlocks.Decorations.ELITE_CASING, "精鋭外装（装飾）")
        add(RagiumBlocks.Decorations.ULTIMATE_CASING, "究極外装（装飾）")

        add(RagiumBlocks.Decorations.BASIC_HULL, "基本筐体（装飾）")
        add(RagiumBlocks.Decorations.ADVANCED_HULL, "発展筐体（装飾）")
        add(RagiumBlocks.Decorations.ELITE_HULL, "精鋭筐体（装飾）")
        add(RagiumBlocks.Decorations.ULTIMATE_HULL, "究極筐体（装飾）")

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
        add(RagiumTranslationKeys.CIRCUIT, "回路")
        add(RagiumTranslationKeys.COIL, "コイル")
        add(RagiumTranslationKeys.CRATE, "クレート")
        add(RagiumTranslationKeys.DRUM, "ドラム")
        add(RagiumTranslationKeys.GRATE, "格子")
        add(RagiumTranslationKeys.HULL, "筐体")
        add(RagiumTranslationKeys.PLASTIC, "プラスチック")

        add(HTOreVariant.OVERWORLD, "%s鉱石")
        add(HTOreVariant.DEEP, "深層%s鉱石")
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

        // add(RagiumItems.HEATING_CATALYST, "加熱触媒")
        // add(RagiumItems.COOLING_CATALYST, "冷却触媒")
        add(RagiumItems.OXIDIZATION_CATALYST, "酸化触媒")
        add(RagiumItems.REDUCTION_CATALYST, "還元触媒")
        add(RagiumItems.DEHYDRATION_CATALYST, "脱水触媒")

        add(RagiumItems.BEE_WAX, "蜜蠟")
        add(RagiumItems.CIRCUIT_BOARD, "回路基板")
        add(RagiumItems.CALCIUM_CARBIDE, "炭化カルシウム")
        add(RagiumItems.COAL_CHIP, "石炭チップ")
        add(RagiumItems.CRIMSON_CRYSTAL, "深紅の結晶")
        add(RagiumItems.DEEPANT, "ディーパント")
        add(RagiumItems.ENGINE, "V8エンジン")
        // add(RagiumItems.GLASS_SHARD, "ガラスの破片")
        add(RagiumItems.LED, "L.E.D.")
        add(RagiumItems.LUMINESCENCE_DUST, "ルミネッセンスダスト")
        add(RagiumItems.OBSIDIAN_TEAR, "黒曜石の涙")
        add(RagiumItems.RAGI_ALLOY_COMPOUND, "ラギ合金混合物")
        add(RagiumItems.RESIDUAL_COKE, "残渣油コークス")
        add(RagiumItems.SILKY_CRYSTAL, "シルキー結晶")
        add(RagiumItems.SLAG, "スラグ")
        add(RagiumItems.SOAP, "石鹸")
        add(RagiumItems.SOLAR_PANEL, "太陽光パネル")
        // add(RagiumItems.STELLA_PLATE, "S.T.E.L.L.A.板")
        add(RagiumItems.TALLOW, "獣脂")
        add(RagiumItems.WARPED_CRYSTAL, "歪んだ結晶")

        add(RagiumItems.Radioactives.URANIUM_FUEL, "ウラン燃料")
        add(RagiumItems.Radioactives.PLUTONIUM_FUEL, "プルトニウム燃料")
        add(RagiumItems.Radioactives.YELLOW_CAKE, "イエローケーキ")
        add(RagiumItems.Radioactives.YELLOW_CAKE_PIECE, "一切れのイエローケーキ")
        add(RagiumItems.Radioactives.NUCLEAR_WASTE, "核廃棄物")

        add(RagiumItems.RAGI_TICKET, "らぎチケット")

        // Machine
        add(HTMachineTier.BASIC, "基本", "基本%s")
        add(HTMachineTier.ADVANCED, "発展", "発展%s")
        add(HTMachineTier.ELITE, "精鋭", "精鋭%s")
        add(HTMachineTier.ULTIMATE, "究極", "究極%s")

        add(RagiumTranslationKeys.MACHINE_COST, "- 処理コスト: %s FE/回")
        add(RagiumTranslationKeys.MACHINE_NAME, "- 機械: %s")
        add(RagiumTranslationKeys.MACHINE_TIER, "- ティア: %s")

        add(RagiumTranslationKeys.MULTI_SHAPE_ERROR, "次の条件を満たしていません: %s (座標: %s)")
        add(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS, "機械の構造は有効です！")

        add(RagiumTranslationKeys.COOLING_CONDITION, "次の範囲の冷却が必要です: %s - %s")
        add(RagiumTranslationKeys.HEATING_CONDITION, "次の範囲の加熱が必要です: %s - %s")
        add(RagiumTranslationKeys.CATALYST_CONDITION, "触媒アイテムが必要です")
        // Machine Type
        add(RagiumMachineKeys.BEDROCK_MINER, "岩盤採掘機", "岩盤から鉱物を採掘する")
        add(RagiumMachineKeys.BIOMASS_FERMENTER, "バイオマス発酵槽", "植物からバイオマスを生産する")
        add(RagiumMachineKeys.DRAIN, "排水溝", "正面から液体を，上から経験値を，スロット内の液体キューブから中身を吸い取る")
        add(RagiumMachineKeys.FLUID_DRILL, "液体採掘機", "特定のバイオームから液体を汲み上げる")
        add(RagiumMachineKeys.GAS_PLANT, "ガス採集機", "特定のバイオームからガスを集める")
        add(RagiumMachineKeys.ROCK_GENERATOR, "岩石生成機", "水と溶岩を少なくとも一つずつ隣接させる")

        add(RagiumMachineKeys.COMBUSTION_GENERATOR, "燃焼発電機", "液体燃料から発電する")
        add(RagiumMachineKeys.GAS_TURBINE, "ガスタービン", "気体燃料から発電する")
        add(RagiumMachineKeys.NUCLEAR_REACTOR, "原子炉", "放射性燃料から発電する")
        add(RagiumMachineKeys.SOLAR_GENERATOR, "太陽光発電機", "日中に発電する")
        add(RagiumMachineKeys.STEAM_GENERATOR, "蒸気発電機", "水と石炭類から発電する")
        add(RagiumMachineKeys.THERMAL_GENERATOR, "地熱発電機", "高温の液体から発電する")
        add(RagiumMachineKeys.VIBRATION_GENERATOR, "音波発電機", "エッチなのはダメ!死刑!")

        add(RagiumMachineKeys.ASSEMBLER, "組立機", "おれぁ悪魔博士だよ")
        add(RagiumMachineKeys.BLAST_FURNACE, "大型高炉", "複数の素材を一つに焼き上げる")
        add(RagiumMachineKeys.CHEMICAL_REACTOR, "化学反応槽", "Are You Ready?")
        // add(RagiumMachineKeys.CONDENSER, "冷却器")
        add(RagiumMachineKeys.COMPRESSOR, "圧縮機", "saves.zip.zip")
        add(RagiumMachineKeys.CUTTING_MACHINE, "裁断機", "")
        add(RagiumMachineKeys.DISTILLATION_TOWER, "蒸留塔", "原油を処理する")
        // add(RagiumMachineKeys.ELECTROLYZER, "電解槽", "エレキ オン")
        add(RagiumMachineKeys.EXTRACTOR, "抽出器", "遠心分離機みたいなやつ")
        add(RagiumMachineKeys.GRINDER, "粉砕機", "クラッシュ・アップ")
        add(RagiumMachineKeys.GROWTH_CHAMBER, "成長チャンバー")
        // add(RagiumMachineKeys.INFUSER, "注入機", "遠心分離機じゃないみたいなやつ")
        // add(RagiumMachineKeys.LARGE_CHEMICAL_REACTOR, "大型化学反応槽", "大は小を兼ねません")
        add(RagiumMachineKeys.LASER_TRANSFORMER, "レーザー変換機")
        add(RagiumMachineKeys.MULTI_SMELTER, "並列精錬機", "複数のアイテムを一度に製錬する")
        add(RagiumMachineKeys.MIXER, "ミキサー", "ゲノミクス...")
        // Material
        add(RagiumMaterialKeys.ALKALI, "アルカリ")
        add(RagiumMaterialKeys.ALUMINUM, "アルミニウム")
        add(RagiumMaterialKeys.AMETHYST, "アメシスト")
        add(RagiumMaterialKeys.ASH, "灰")
        add(RagiumMaterialKeys.BAUXITE, "ボーキサイト")
        add(RagiumMaterialKeys.BRASS, "真鍮")
        add(RagiumMaterialKeys.BRONZE, "青銅")
        add(RagiumMaterialKeys.CARBON, "炭素")
        add(RagiumMaterialKeys.CINNABAR, "辰砂")
        add(RagiumMaterialKeys.COAL, "石炭")
        add(RagiumMaterialKeys.COPPER, "銅")
        add(RagiumMaterialKeys.CRUDE_RAGINITE, "粗製ラギナイト")
        add(RagiumMaterialKeys.CRYOLITE, "氷晶石")
        add(RagiumMaterialKeys.DEEP_STEEL, "深層鋼")
        add(RagiumMaterialKeys.DIAMOND, "ダイアモンド")
        add(RagiumMaterialKeys.DRAGONIUM, "ドラゴニウム")
        add(RagiumMaterialKeys.ECHORIUM, "エコリウム")
        add(RagiumMaterialKeys.ELECTRUM, "琥珀金")
        add(RagiumMaterialKeys.EMERALD, "エメラルド")
        add(RagiumMaterialKeys.FIERIUM, "ファイアリウム")
        add(RagiumMaterialKeys.FLUORITE, "蛍石")
        add(RagiumMaterialKeys.GALENA, "方鉛鉱")
        add(RagiumMaterialKeys.GOLD, "金")
        add(RagiumMaterialKeys.INVAR, "不変鋼")
        add(RagiumMaterialKeys.IRIDIUM, "イリジウム")
        add(RagiumMaterialKeys.IRON, "鉄")
        add(RagiumMaterialKeys.LAPIS, "ラピス")
        add(RagiumMaterialKeys.LEAD, "鉛")
        add(RagiumMaterialKeys.NETHERITE, "ネザライト")
        add(RagiumMaterialKeys.NETHERITE_SCRAP, "ネザライトの欠片")
        add(RagiumMaterialKeys.NICKEL, "ニッケル")
        add(RagiumMaterialKeys.NITER, "硝石")
        add(RagiumMaterialKeys.PERIDOT, "ペリドット")
        add(RagiumMaterialKeys.PLATINUM, "白金")
        add(RagiumMaterialKeys.PLUTONIUM, "プルトニウム")
        add(RagiumMaterialKeys.PYRITE, "黄鉄鉱")
        add(RagiumMaterialKeys.QUARTZ, "水晶")
        add(RagiumMaterialKeys.RAGI_ALLOY, "ラギ合金")
        add(RagiumMaterialKeys.RAGI_CRYSTAL, "ラギクリスタリル")
        add(RagiumMaterialKeys.RAGI_STEEL, "ラギスチール")
        add(RagiumMaterialKeys.RAGINITE, "ラギナイト")
        add(RagiumMaterialKeys.RAGIUM, "ラギウム")
        add(RagiumMaterialKeys.REDSTONE, "レッドストーン")
        add(RagiumMaterialKeys.REFINED_RAGI_STEEL, "精製ラギスチール")
        add(RagiumMaterialKeys.RUBY, "ルビー")
        add(RagiumMaterialKeys.SALT, "塩")
        add(RagiumMaterialKeys.SAPPHIRE, "サファイア")
        add(RagiumMaterialKeys.SILVER, "銀")
        add(RagiumMaterialKeys.SPHALERITE, "閃亜鉛鉱")
        add(RagiumMaterialKeys.STEEL, "スチール")
        add(RagiumMaterialKeys.SULFUR, "硫黄")
        add(RagiumMaterialKeys.TIN, "スズ")
        add(RagiumMaterialKeys.TITANIUM, "チタン")
        add(RagiumMaterialKeys.TUNGSTEN, "タングステン")
        add(RagiumMaterialKeys.URANIUM, "ウラニウム")
        add(RagiumMaterialKeys.WOOD, "木材")
        add(RagiumMaterialKeys.ZINC, "亜鉛")

        add(RagiumMekIntegration.OSMIUM, "オスミウム")
        add(RagiumMekIntegration.REFINED_GLOWSTONE, "精製グロウストーン")
        add(RagiumMekIntegration.REFINED_OBSIDIAN, "精製黒曜石")
        // Tag Prefix
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
        add(HTTagPrefix.STORAGE_BLOCK, "%sブロック")
        add(HTTagPrefix.WIRE, "%sワイヤ")
        // Misc
        add(RagiumTranslationKeys.FORMATTED_FLUID, "液体量: %s mb")

        add("config.jade.plugin_ragium.energy_network", "エネルギーネットワーク")
    }
}
