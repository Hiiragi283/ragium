package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.add
import hiiragi283.ragium.api.extension.addEnchantment
import hiiragi283.ragium.api.extension.addFluid
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.integration.RagiumMekIntegration
import net.minecraft.data.PackOutput
import net.minecraft.world.item.DyeColor
import net.neoforged.neoforge.common.data.LanguageProvider

class RagiumJapaneseProvider(output: PackOutput) : LanguageProvider(output, RagiumAPI.MOD_ID, "ja_jp") {
    override fun addTranslations() {
        // Block
        addBlock(RagiumBlocks.SOUL_MAGMA_BLOCK, "ソウルマグマブロック")
        addBlock(RagiumBlocks.CRUDE_OIL, "原油")

        addBlock(RagiumBlocks.SLAG_BLOCK, "スラグブロック")

        addBlock(RagiumBlocks.RAGI_BRICKS, "らぎレンガ")
        addBlock(RagiumBlocks.RAGI_BRICK_FAMILY.stairs, "らぎレンガの階段")
        addBlock(RagiumBlocks.RAGI_BRICK_FAMILY.slab, "らぎレンガのハーフブロック")
        addBlock(RagiumBlocks.RAGI_BRICK_FAMILY.wall, "らぎレンガの壁")

        addBlock(RagiumBlocks.PLASTIC_BLOCK, "プラスチックブロック")
        addBlock(RagiumBlocks.PLASTIC_FAMILY.stairs, "プラスチックブロックの階段")
        addBlock(RagiumBlocks.PLASTIC_FAMILY.slab, "プラスチックブロックのハーフブロック")
        addBlock(RagiumBlocks.PLASTIC_FAMILY.wall, "プラスチックブロックの壁")

        addBlock(RagiumBlocks.SHAFT, "シャフト")

        addBlock(RagiumBlocks.CHEMICAL_GLASS, "化学ガラス")
        addBlock(RagiumBlocks.MOB_GLASS, "モブガラス")
        addBlock(RagiumBlocks.OBSIDIAN_GLASS, "黒曜石ガラス")
        addBlock(RagiumBlocks.SOUL_GLASS, "ソウルガラス")

        addBlock(RagiumBlocks.SPONGE_CAKE, "スポンジケーキ")
        addBlock(RagiumBlocks.SWEET_BERRIES_CAKE, "スイートベリーケーキ")

        addBlock(RagiumBlocks.MANUAL_GRINDER, "らぎ臼")
        addBlock(RagiumBlocks.PRIMITIVE_BLAST_FURNACE, "らぎ高炉")
        addBlock(RagiumBlocks.DISENCHANTING_TABLE, "ディスエンチャント台")

        addBlock(RagiumBlocks.COPPER_DRUM, "銅のドラム")

        addBlock(RagiumBlocks.ENERGY_NETWORK_INTERFACE, "E.N.I.")
        addBlock(RagiumBlocks.SLAG_COLLECTOR, "スラグ回収器")

        addBlock(RagiumBlocks.MAGMA_BURNER, "マグマバーナー")
        addBlock(RagiumBlocks.SOUL_BURNER, "ソウルバーナー")
        addBlock(RagiumBlocks.FIERY_BURNER, "燃え盛るバーナー")

        addBlock(RagiumBlocks.getLedBlock(DyeColor.RED), "LEDブロック（赤）")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.GREEN), "LEDブロック（緑）")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.BLUE), "LEDブロック（青）")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.CYAN), "LEDブロック（シアン）")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.MAGENTA), "LEDブロック（マゼンタ）")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.YELLOW), "LEDブロック（黄色）")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.WHITE), "LEDブロック")

        add(RagiumTranslationKeys.MOB_GLASS, "モブのみ通過可能")
        add(RagiumTranslationKeys.SOUL_GLASS, "プレイヤーのみ通過可能")

        add(RagiumTranslationKeys.SPONGE_CAKE, "干草の俵と同じだけ落下ダメージを軽減する")

        add(RagiumTranslationKeys.MANUAL_GRINDER, "右クリックで回転")
        add(RagiumTranslationKeys.MANUAL_GRINDER_1, "ホッパーやパイプで材料を投入")

        add(RagiumTranslationKeys.PRIMITIVE_BLAST_FURNACE, "1x 鉄インゴット + 4x 石炭 -> 1x 鋼鉄インゴット")
        add(RagiumTranslationKeys.PRIMITIVE_BLAST_FURNACE_1, "マルチブロックが必要")

        add(RagiumTranslationKeys.ENERGY_NETWORK_INTERFACE, "エネルギーネットワークに接続する")
        add(RagiumTranslationKeys.SLAG_COLLECTOR, "隣接した大型高炉が処理を行った際にスラグを生成する")
        // Chemical
        add(RagiumMekIntegration.RAGINITE_SLURRY.cleanSlurry.translationKey, "純粋なラギナイトの懸濁液")
        add(RagiumMekIntegration.RAGINITE_SLURRY.dirtySlurry.translationKey, "汚れたラギナイトの懸濁液")
        // Content
        add(HTOreVariant.OVERWORLD, "%s鉱石")
        add(HTOreVariant.DEEPSLATE, "深層%s鉱石")
        add(HTOreVariant.NETHER, "ネザー%s鉱石")
        add(HTOreVariant.END, "エンド%s鉱石")
        // Enchantment
        addEnchantment(RagiumEnchantments.CAPACITY, "容量増加", "アイテムや液体ストレージの容量を拡張します")
        // Fluids
        addFluid(RagiumFluids.HONEY, "蜂蜜")
        addFluid(RagiumFluids.SNOW, "粉雪")

        addFluid(RagiumVirtualFluids.MUSHROOM_STEW, "キノコシチュー")

        addFluid(RagiumVirtualFluids.NITROGEN, "窒素")
        addFluid(RagiumVirtualFluids.AMMONIA, "アンモニア")
        addFluid(RagiumVirtualFluids.NITRIC_ACID, "硝酸")

        addFluid(RagiumVirtualFluids.OXYGEN, "酸素")

        addFluid(RagiumVirtualFluids.SULFUR_DIOXIDE, "二酸化硫黄")
        addFluid(RagiumVirtualFluids.SULFUR_TRIOXIDE, "三酸化硫黄")
        addFluid(RagiumVirtualFluids.SULFURIC_ACID, "硫酸")

        addFluid(RagiumVirtualFluids.HYDROFLUORIC_ACID, "グロウ酸")

        addFluid(RagiumVirtualFluids.LAPIS_SOLUTION, "ラピス溶液")

        addFluid(RagiumVirtualFluids.NAPHTHA, "ナフサ")
        addFluid(RagiumVirtualFluids.FUEL, "燃料")
        addFluid(RagiumVirtualFluids.NITRO_FUEL, "ニトロ燃料")

        addFluid(RagiumVirtualFluids.PLANT_OIL, "植物油")
        addFluid(RagiumVirtualFluids.BIOMASS, "バイオマス")
        addFluid(RagiumVirtualFluids.ETHANOL, "エタノール")
        addFluid(RagiumVirtualFluids.BIODIESEL, "バイオディーゼル")

        addFluid(RagiumVirtualFluids.SAP, "樹液")
        addFluid(RagiumVirtualFluids.CRIMSON_SAP, "深紅の樹液")
        addFluid(RagiumVirtualFluids.WARPED_SAP, "歪んだ樹液")

        addFluid(RagiumVirtualFluids.RAGIUM_SOLUTION, "ラギウム溶液")
        // Items
        addItem(RagiumItems.SWEET_BERRIES_CAKE_PIECE, "一切れのスイートベリーケーキ")
        addItem(RagiumItems.MELON_PIE, "メロンパイ")

        addItem(RagiumItems.BUTTER, "バター")
        addItem(RagiumItems.DOUGH, "生地")
        addItem(RagiumItems.FLOUR, "小麦粉")

        addItem(RagiumItems.CHOCOLATE, "チョコレート")
        addItem(RagiumItems.CHOCOLATE_APPLE, "チョコリンゴ")
        addItem(RagiumItems.CHOCOLATE_BREAD, "チョコパン")
        addItem(RagiumItems.CHOCOLATE_COOKIE, "チョコレートクッキー")

        addItem(RagiumItems.MINCED_MEAT, "ひき肉")
        addItem(RagiumItems.MEAT_INGOT, "生肉インゴット")
        addItem(RagiumItems.COOKED_MEAT_INGOT, "焼肉インゴット")
        addItem(RagiumItems.CANNED_COOKED_MEAT, "焼肉缶詰")

        addItem(RagiumItems.AMBROSIA, "アンブロシア")

        addItem(RagiumItems.FORGE_HAMMER, "鍛造ハンマー")
        addItem(RagiumItems.SILKY_PICKAXE, "シルキーピッケル")

        addItem(RagiumItems.DEFOLIANT, "枯葉剤")
        addItem(RagiumItems.DYNAMITE, "ダイナマイト")
        addItem(RagiumItems.MAGNET, "マグネット")
        addItem(RagiumItems.SOAP, "石鹸")

        addItem(RagiumItems.ALUMINUM_CAN, "アルミ缶")
        addItem(RagiumItems.POTION_CAN, "ポーション缶")

        addItem(RagiumItems.BASIC_CIRCUIT, "基本回路")
        addItem(RagiumItems.ADVANCED_CIRCUIT, "発展回路")
        addItem(RagiumItems.ELITE_CIRCUIT, "精鋭回路")
        addItem(RagiumItems.ULTIMATE_CIRCUIT, "究極回路")

        addItem(RagiumItems.getPressMold(HTTagPrefix.GEAR), "プレス型（歯車）")
        addItem(RagiumItems.getPressMold(HTTagPrefix.PLATE), "プレス型（板材）")
        addItem(RagiumItems.getPressMold(HTTagPrefix.ROD), "プレス型（棒材）")
        addItem(RagiumItems.getPressMold(HTTagPrefix.WIRE), "プレス型（ワイヤー）")

        addItem(RagiumItems.REDSTONE_LENS, "レッドストーンレンズ")
        addItem(RagiumItems.GLOW_LENS, "グロウレンズ")
        addItem(RagiumItems.PRISMARINE_LENS, "プリズマリンレンズ")
        addItem(RagiumItems.MAGICAL_LENS, "魔術的レンズ")

        addItem(RagiumItems.ALKALI_REAGENT, "アルカリ試薬")
        addItem(RagiumItems.BLAZE_REAGENT, "ブレイズ試薬")
        addItem(RagiumItems.CREEPER_REAGENT, "クリーパー試薬")
        addItem(RagiumItems.DEEPANT_REAGENT, "ディーパント試薬")
        addItem(RagiumItems.ENDER_REAGENT, "エンダー試薬")
        addItem(RagiumItems.FROZEN_REAGENT, "フローズン試薬")
        addItem(RagiumItems.GLOW_REAGENT, "グロウ試薬")
        addItem(RagiumItems.MAGICAL_REAGENT, "魔術的試薬")
        addItem(RagiumItems.PRISMARINE_REAGENT, "プリズマリン試薬")
        addItem(RagiumItems.SCULK_REAGENT, "スカルク試薬")
        addItem(RagiumItems.WITHER_REAGENT, "ウィザー試薬")

        addItem(RagiumItems.BEE_WAX, "蜜蠟")
        addItem(RagiumItems.CHEMICAL_MACHINE_CASING, "化学機械筐体")
        addItem(RagiumItems.CIRCUIT_BOARD, "回路基板")
        addItem(RagiumItems.CRIMSON_CRYSTAL, "深紅の結晶")
        addItem(RagiumItems.CRUDE_OIL_BUCKET, "原油入りバケツ")
        addItem(RagiumItems.ENGINE, "V8エンジン")
        addItem(RagiumItems.LED, "L.E.D.")
        addItem(RagiumItems.MACHINE_CASING, "機械筐体")
        addItem(RagiumItems.PLASTIC_PLATE, "プラスチック板")
        addItem(RagiumItems.POLYMER_RESIN, "高分子樹脂")
        addItem(RagiumItems.PRECISION_MACHINE_CASING, "精密機械筐体")
        addItem(RagiumItems.RAGI_ALLOY_COMPOUND, "ラギ合金混合物")
        addItem(RagiumItems.RAGI_TICKET, "らぎチケット")
        addItem(RagiumItems.SILKY_CRYSTAL, "シルキー結晶")
        addItem(RagiumItems.SLAG, "スラグ")
        addItem(RagiumItems.SOLAR_PANEL, "太陽光パネル")
        addItem(RagiumItems.WARPED_CRYSTAL, "歪んだ結晶")
        addItem(RagiumItems.YELLOW_CAKE, "イエローケーキ")
        addItem(RagiumItems.YELLOW_CAKE_PIECE, "一切れのイエローケーキ")

        add(RagiumTranslationKeys.BEE_WAX, "ハニカムと同様に使える")

        add(RagiumTranslationKeys.AMBROSIA, "無限に食べられちまうんだ！")

        add(RagiumTranslationKeys.SILKY_PICKAXE, "常にシルクタッチが発動する")

        add(RagiumTranslationKeys.DEFOLIANT, "9x9x9の範囲を荒れ地に変える")
        add(RagiumTranslationKeys.DYNAMITE, "着弾時に爆発を起こす")
        add(RagiumTranslationKeys.MAGNET, "周囲のアイテムをひきつける")
        add(RagiumTranslationKeys.SOAP, "右クリックで対象のブロックを洗う")
        // Machine
        add(RagiumTranslationKeys.MACHINE_COST, "- 処理コスト: %s FE/回")
        add(RagiumTranslationKeys.MACHINE_NAME, "- 機械: %s")
        add(RagiumTranslationKeys.MACHINE_TIER, "- ティア: %s")

        add(RagiumTranslationKeys.MACHINE_OWNER, "- 所有者: %s")
        add(RagiumTranslationKeys.MACHINE_PREVIEW, "- プレビューの表示: %s")
        add(RagiumTranslationKeys.MACHINE_TICK_RATE, "- 処理時間: %s ティック（%s秒）")
        add(RagiumTranslationKeys.MACHINE_WORKING, "- 稼働中: %s")

        add(RagiumTranslationKeys.MULTI_SHAPE_ERROR, "次の条件を満たしていません: %s (座標: %s)")
        add(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS, "機械の構造は有効です！")
        // Machine Type
        add(HTMachineType.BEDROCK_MINER, "岩盤採掘機", "岩盤から鉱物を採掘する")
        add(HTMachineType.FISHER, "自動釣り機", "下の水源から魚を釣る")
        add(HTMachineType.LOOT_SPAWNER, "ルートスポナー", "壊れたスポナーからモブドロップを生成する")

        add(HTMachineType.COMBUSTION_GENERATOR, "燃焼発電機", "液体燃料から発電する")
        add(HTMachineType.SOLAR_GENERATOR, "太陽光発電機", "日中に発電する")
        add(HTMachineType.STIRLING_GENERATOR, "スターリング発電機", "固体燃料と水から発電する")
        add(HTMachineType.THERMAL_GENERATOR, "地熱発電機", "高温の液体から発電する")

        add(HTMachineType.ALCHEMICAL_BREWERY, "錬金醸造機", "連続した醸造")
        add(HTMachineType.ARCANE_ENCHANTER, "神秘的エンチャント機", "安定したエンチャント")
        add(HTMachineType.ASSEMBLER, "組立機", "君こそが天才だ!")
        add(HTMachineType.BLAST_FURNACE, "大型高炉", "複数の素材を一つに焼き上げる")
        add(HTMachineType.COMPRESSOR, "圧縮機", "saves.zip.zip")
        add(HTMachineType.EXTRACTOR, "抽出器", "遠心分離機みたいなやつ")
        add(HTMachineType.GRINDER, "粉砕機", "ダイヤモンドは壊れない")
        add(HTMachineType.GROWTH_CHAMBER, "成長チャンバー", "成長バーチャン")
        add(HTMachineType.INFUSER, "注入機", "遠心分離機みたくないやつ")
        add(HTMachineType.LASER_ASSEMBLY, "レーザーアセンブリ", "レーザーオン…")
        add(HTMachineType.MULTI_SMELTER, "並列精錬機", "複数のアイテムを一度に製錬する")
        add(HTMachineType.MIXER, "ミキサー", "ベストマッチ!")
        add(HTMachineType.REFINERY, "精製機", "プロジェクト・ビルド")
        add(HTMachineType.SOLIDIFIER, "固体成形機", "アンコントールスイッチ！ブラックハザード！")
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
        add(RagiumMaterials.DEEP_STEEL, "深層鋼")
        add(RagiumMaterials.ECHORIUM, "エコリウム")
        add(RagiumMaterials.FIERY_COAL, "燃え盛る石炭")
        add(RagiumMaterials.RAGI_ALLOY, "ラギ合金")
        add(RagiumMaterials.RAGI_CRYSTAL, "ラギクリスタリル")
        add(RagiumMaterials.RAGINITE, "ラギナイト")
        add(RagiumMaterials.RAGIUM, "ラギウム")
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
        add(HTTagPrefix.CLUMP, "%sの凝塊")
        add(HTTagPrefix.COIL, "%sのコイル")
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
        add(RagiumItemTags.ADVANCED_CIRCUIT, "発展回路")
        add(RagiumItemTags.BASIC_CIRCUIT, "基本回路")
        add(RagiumItemTags.COAL_COKE, "石炭コークス")
        add(RagiumItemTags.DOUGH, "生地")
        add(RagiumItemTags.ELITE_CIRCUIT, "精鋭回路")
        add(RagiumItemTags.PLASTICS, "プラスチック")
        add(RagiumItemTags.SLAG, "スラグ")
        add(RagiumItemTags.ULTIMATE_CIRCUIT, "究極回路")

        add(RagiumItemTags.DIRT_SOILS, "土壌")
        add(RagiumItemTags.END_SOILS, "エンドの土壌")
        add(RagiumItemTags.GEAR_MOLDS, "プレス型（歯車）")
        add(RagiumItemTags.LED_BLOCKS, "LEDブロック")
        add(RagiumItemTags.MUSHROOM_SOILS, "キノコの土壌")
        add(RagiumItemTags.NETHER_SOILS, "ネザーの土壌")
        add(RagiumItemTags.PLATE_MOLDS, "プレス型（板材）")
        add(RagiumItemTags.ROD_MOLDS, "プレス型（棒材）")
        add(RagiumItemTags.SOLAR_PANELS, "太陽光パネル")
        add(RagiumItemTags.WIRE_MOLDS, "プレス型（ワイヤー）")

        add(RagiumFluidTags.CREOSOTE, "クレオソート")
        add(RagiumFluidTags.MEAT, "液体肉")

        add(RagiumFluidTags.NITRO_FUEL, "ニトロ系燃料")
        add(RagiumFluidTags.NON_NITRO_FUEL, "非ニトロ系燃料")
        add(RagiumFluidTags.THERMAL_FUEL, "発熱燃料")
        // Misc
        add(RagiumTranslationKeys.FLUID_AMOUNT, "液体量: %s mb")
        add(RagiumTranslationKeys.FLUID_CAPACITY, "容量: %s mb")

        add("config.jade.plugin_ragium.energy_network", "エネルギーネットワーク")
        add("config.jade.plugin_ragium.machine_info", "機械の情報")
        add("config.jade.plugin_ragium.error_message", "エラーメッセージ")
    }
}
