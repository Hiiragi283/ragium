package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.block.attribute.HTFluidBlockAttribute
import hiiragi283.ragium.api.data.lang.HTLanguageProvider
import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.client.integration.jade.provider.HTBlockConfigurationDataProvider
import hiiragi283.ragium.client.integration.jade.provider.HTBlockMachinePropertiesProvider
import hiiragi283.ragium.client.integration.jade.provider.HTBlockOwnerProvider
import hiiragi283.ragium.common.text.RagiumCommonTranslation
import hiiragi283.ragium.data.server.advancement.RagiumAdvancements
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumEnchantments
import hiiragi283.ragium.setup.RagiumEntityTypes
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.data.PackOutput

class RagiumJapaneseProvider(output: PackOutput) : HTLanguageProvider.Japanese(output) {
    override fun addTranslations() {
        addPatterned()

        advancement()
        block()
        enchantment()
        entity()
        fluid()
        item()
        keyMapping()
        modTags()
        recipeType()
        text()

        emi()
        jade()
    }

    private fun advancement() {
        addAdvancement(RagiumAdvancements.ROOT, "Ragium", "Welcome to ようこそ Ragium へ！")
        addAdvancement(RagiumAdvancements.CRAFTABLE_TEMPLATES, "いとも容易く作られる簡素な強化", "Ragiumで追加された鍛冶型を作る")
        // Raginite
        addAdvancement(RagiumAdvancements.RAGINITE, "レッドストーンではない", "地下にあるラギナイト鉱石からラギナイトの粉を手に入れる")
        addAdvancement(RagiumAdvancements.RAGI_CHERRY, "双子の果物", "らぎチェリーを食べる")
        addAdvancement(RagiumAdvancements.RAGI_CHERRY_TOAST, "最後の朝食", "らぎチェリーのトーストタワーを手に入れる")

        addAdvancement(RagiumAdvancements.RAGI_ALLOY, "0xFF003F", "ラギ合金インゴットを手に入れる")
        addAdvancement(RagiumAdvancements.ALLOY_SMELTER, "アル-ケミストリー", "合金炉を手に入れる")
        addAdvancement(RagiumAdvancements.AMBROSIA, "禁断の果実", "スティー…アンブロシアを食べる")

        addAdvancement(RagiumAdvancements.ADV_RAGI_ALLOY, "赤色です。", "発展ラギ合金インゴットを手に入れる")
        addAdvancement(RagiumAdvancements.MELTER, "溶融とあなた", "溶融炉を手に入れる")
        addAdvancement(RagiumAdvancements.REFINERY, "リファイナリーでは…リファイナリーだこれ！", "精製機を手に入れる")
        addAdvancement(RagiumAdvancements.PLASTIC, "Plus-Tic", "プラスチックを手に入れる")
        addAdvancement(RagiumAdvancements.POTION_BUNDLE, "バンドルのバックポート", "ポーションバンドルを使う")

        addAdvancement(RagiumAdvancements.RAGI_CRYSTAL, "エナジウムではない", "ラギクリスタリルを手に入れる")
        addAdvancement(RagiumAdvancements.RAGI_CRYSTAL_HAMMER, "らぎ分解機", "ラギクリスタリルのハンマーを手に入れる")
        addAdvancement(RagiumAdvancements.RAGI_TICKET, "古き良きあの頃", "らぎチケットを手に入れてお宝チェストを開く")
        // Azure
        addAdvancement(RagiumAdvancements.AZURE_SHARD, "ラズライトではない", "紺碧の欠片を手に入れる")
        addAdvancement(RagiumAdvancements.AZURE_STEEL, "鋼は青かった", "紺鉄インゴットを手に入れる")
        addAdvancement(RagiumAdvancements.AZURE_GEARS, "Wake up! Azure Dragon!", "紺鉄インゴットで作られたツールか装備を手に入れる")
        addAdvancement(RagiumAdvancements.MIXER, "混ぜる，混ぜる，それから混ぜる", "ミキサーを手に入れる")
        // Deep
        addAdvancement(RagiumAdvancements.RESONANT_DEBRIS, "「古代」の残骸", "共振の残骸を手に入れる")
        addAdvancement(RagiumAdvancements.DEEP_STEEL, "深く，深く，なお深く。", "深層鋼を手に入れる")
        addAdvancement(RagiumAdvancements.BEHEAD_MOB, "帯電なんていらない", "雷撃エンチャントが付与された武器でモブの頭を切り落とす")

        addAdvancement(RagiumAdvancements.ECHO_STAR, "ソニック・ザ・ブーム", "残響の星を手に入れる")
        // Night Metal
        addAdvancement(RagiumAdvancements.NIGHT_METAL, "ナイト・オブ・ナイツ", "夜金インゴットを手に入れる")
        addAdvancement(RagiumAdvancements.SIMULATOR, "1 + 2 + 3 = 1 * 2 * 3", "シミュレーション室を手に入れる")
        // Crimson
        addAdvancement(RagiumAdvancements.CRIMSON_CRYSTAL, "チャオ！", "深紅のクリスタルを手に入れる")
        addAdvancement(RagiumAdvancements.CRIMSON_SOIL, "バラが赤い理由", "ソウルソイルに血塗られたチケットを使って深紅の土壌を手に入れる")
        // Warped
        addAdvancement(RagiumAdvancements.WARPED_CRYSTAL, "安定した歪み", "歪んだクリスタリルを手に入れる")
        addAdvancement(RagiumAdvancements.DIM_ANCHOR, "リモートワーク", "次元アンカーを置いて，そのチャンクを常に読み込ませる")
        addAdvancement(RagiumAdvancements.TELEPORT_KEY, "ロックオープン！", "転位の鍵を使い，紐づけた座標に飛ぶ")
        addAdvancement(RagiumAdvancements.WARPED_WART, "Industrialなウォート", "歪んだウォートを食べる")
        // Eldritch
        addAdvancement(RagiumAdvancements.ELDRITCH_PEARL, "始原ではない", "異質な真珠を手に入れる")
        addAdvancement(RagiumAdvancements.ELDRITCH_EGG, "ガッチャ！", "異質な卵を投げてモブを捕まえる")
        addAdvancement(RagiumAdvancements.MYSTERIOUS_OBSIDIAN, "隕石を落としているのは誰？", "")
        // Iridescent
        addAdvancement(RagiumAdvancements.IRIDESCENT_POWDER, "虹色の錬金術師", "七色の粉を手に入れる")
        addAdvancement(RagiumAdvancements.ETERNAL_COMPONENT, "さあ，地獄を楽しみな！", "ツールを不可壊にするためにエターナル構造体を手に入れる")
    }

    private fun block() {
        add(RagiumBlocks.SILT, "シルト")

        add(RagiumBlocks.BUDDING_QUARTZ, "芽生えた水晶")
        add(RagiumBlocks.QUARTZ_CLUSTER, "水晶の塊")
        add(RagiumBlocks.RESONANT_DEBRIS, "共振の残骸")
        add(RagiumBlocks.SMOOTH_BLOCKSTONE, "なめらかなブラックストーン")
        add(RagiumBlocks.SOOTY_COBBLESTONE, "煤けた丸石")

        add(RagiumBlocks.CRIMSON_SOIL, "深紅の土壌")

        add(RagiumBlocks.WARPED_WART, "歪んだウォート")

        add(RagiumBlocks.EXP_BERRIES, "経験値ベリーの茂み", "経験値ベリー")
        add(RagiumBlocks.MYSTERIOUS_OBSIDIAN, "神秘的な黒曜石")

        add(RagiumBlocks.RAGI_BRICKS, "らぎレンガ")
        add(RagiumBlocks.AZURE_TILES, "紺碧のタイル")
        add(RagiumBlocks.ELDRITCH_STONE, "エルドリッチストーン")
        add(RagiumBlocks.ELDRITCH_STONE_BRICKS, "エルドリッチストーンレンガ")
        add(RagiumBlocks.PLASTIC_BRICKS, "プラスチックレンガ")
        add(RagiumBlocks.PLASTIC_TILES, "プラスチックタイル")
        add(RagiumBlocks.BLUE_NETHER_BRICKS, "青いネザーレンガ")
        add(RagiumBlocks.SPONGE_CAKE, "スポンジケーキ")

        add(RagiumBlocks.SWEET_BERRIES_CAKE, "スイートベリーケーキ")
        // Parts
        add(RagiumBlocks.DEVICE_CASING, "デバイス筐体")
        // Generators
        add(RagiumBlocks.THERMAL_GENERATOR, "火力発電機")

        add(RagiumBlocks.CULINARY_GENERATOR, "料理発電機")

        add(RagiumBlocks.COMBUSTION_GENERATOR, "燃焼発電機")
        add(RagiumBlocks.SOLAR_PANEL_UNIT, "太陽光パネルユニット")
        add(RagiumBlocks.SOLAR_PANEL_CONTROLLER, "太陽光パネルコントローラー")

        add(RagiumBlocks.ENCHANTMENT_GENERATOR, "エンチャント発電機")
        add(RagiumBlocks.NUCLEAR_REACTOR, "原子炉")
        // Processors
        add(RagiumBlocks.ALLOY_SMELTER, "合金炉")
        add(RagiumBlocks.BLOCK_BREAKER, "採掘機")
        add(RagiumBlocks.COMPRESSOR, "圧縮機")
        add(RagiumBlocks.CUTTING_MACHINE, "裁断機")
        add(RagiumBlocks.ELECTRIC_FURNACE, "電動かまど")
        add(RagiumBlocks.EXTRACTOR, "抽出機")
        add(RagiumBlocks.PULVERIZER, "粉砕機")

        add(RagiumBlocks.CRUSHER, "破砕機")
        add(RagiumBlocks.MELTER, "溶融炉")
        add(RagiumBlocks.MIXER, "ミキサー")
        add(RagiumBlocks.REFINERY, "精製機")

        add(RagiumBlocks.ADVANCED_MIXER, "発展型ミキサー")
        add(RagiumBlocks.BREWERY, "醸造機")
        add(RagiumBlocks.MULTI_SMELTER, "並列製錬炉")
        add(RagiumBlocks.PLANTER, "栽培室")

        add(RagiumBlocks.ENCHANTER, "エンチャンター")
        add(RagiumBlocks.MOB_CRUSHER, "モブ粉砕機")
        add(RagiumBlocks.SIMULATOR, "シミュレーション室")
        // Devices
        add(RagiumBlocks.FLUID_COLLECTOR, "液体収集機")
        add(RagiumBlocks.ITEM_COLLECTOR, "アイテム収集機")

        add(RagiumBlocks.DIM_ANCHOR, "次元アンカー")
        add(RagiumBlocks.ENI, "E.N.I.")

        add(RagiumBlocks.TELEPAD, "テレパッド")

        add(RagiumBlocks.CEU, "C.E.U.")
        // Storage
        add(RagiumBlocks.OPEN_CRATE, "オープンクレート")

        add(RagiumBlocks.EXP_DRUM, "経験値ドラム")
    }

    private fun enchantment() {
        addEnchantment(RagiumEnchantments.CAPACITY, "容量増加", "アイテムや液体ストレージの容量を拡張します。")
        addEnchantment(RagiumEnchantments.RANGE, "範囲増加", "収集の範囲を拡張します。")

        addEnchantment(RagiumEnchantments.NOISE_CANCELING, "ノイズキャンセリング", "ウォーデンなどのスカルク系モンスターに対してのダメージを増加させます。")
        addEnchantment(RagiumEnchantments.STRIKE, "雷撃", "モブが頭をドロップします。")

        addEnchantment(RagiumEnchantments.SONIC_PROTECTION, "音響耐性", "ソニックブームなどの音響攻撃を無効にします。")
    }

    private fun entity() {
        add(RagiumEntityTypes.ELDRITCH_EGG, "異質な卵")
        // add(RagiumEntityTypes.DYNAMITE, "ダイナマイト")
        // add(RagiumEntityTypes.DEFOLIANT_DYNAMITE, "枯葉剤ダイナマイト")
        // add(RagiumEntityTypes.FLATTEN_DYNAMITE, "整地用ダイナマイト")
        // add(RagiumEntityTypes.NAPALM_DYNAMITE, "ナパームダイナマイト")
        // add(RagiumEntityTypes.POISON_DYNAMITE, "毒ガスダイナマイト")
    }

    private fun fluid() {
        addFluid(RagiumFluidContents.HONEY, "蜂蜜")
        addFluid(RagiumFluidContents.MUSHROOM_STEW, "キノコシチュー")
        addFluid(RagiumFluidContents.CREAM, "クリーム")
        addFluid(RagiumFluidContents.CHOCOLATE, "チョコレート")
        addFluid(RagiumFluidContents.RAGI_CHERRY_JUICE, "らぎチェリージュース")

        addFluid(RagiumFluidContents.SLIME, "スライム")
        addFluid(RagiumFluidContents.GELLED_EXPLOSIVE, "ゲル状爆薬")
        addFluid(RagiumFluidContents.ORGANIC_MUTAGEN, "有機的変異原")

        addFluid(RagiumFluidContents.CRUDE_OIL, "原油")
        addFluid(RagiumFluidContents.NATURAL_GAS, "天然ガス")
        addFluid(RagiumFluidContents.NAPHTHA, "ナフサ")
        addFluid(RagiumFluidContents.LUBRICANT, "潤滑油")

        addFluid(RagiumFluidContents.FUEL, "燃料")

        addFluid(RagiumFluidContents.SAP, "樹液")
        addFluid(RagiumFluidContents.SPRUCE_RESIN, "松脂")
        addFluid(RagiumFluidContents.CRIMSON_SAP, "深紅の樹液")
        addFluid(RagiumFluidContents.WARPED_SAP, "歪んだ樹液")

        addFluid(RagiumFluidContents.DESTABILIZED_RAGINITE, "不安定化ラギナイト")

        addFluid(RagiumFluidContents.EXPERIENCE, "液体経験値")
        addFluid(RagiumFluidContents.SULFURIC_ACID, "硫酸")
        addFluid(RagiumFluidContents.COOLANT, "冷却液")
    }

    private fun item() {
        // Material
        add(RagiumItems.COAL_CHIP, "石炭チップ")
        add(RagiumItems.COAL_CHUNK, "石炭の塊")
        add(RagiumItems.COMPRESSED_SAWDUST, "圧縮したおがくず")
        add(RagiumItems.ECHO_STAR, "残響の星")
        add(RagiumItems.ELDER_HEART, "エルダーの心臓")
        add(RagiumItems.IRIDESCENT_POWDER, "七色の粉")
        add(RagiumItems.MAGMA_SHARD, "マグマシャード")
        add(RagiumItems.POTION_DROP, "ポーションの雫")
        add(RagiumItems.RAGI_ALLOY_COMPOUND, "ラギ合金混合物")
        add(RagiumItems.RAGI_COKE, "らぎコークス")
        add(RagiumItems.ROSIN, "ロジン")
        add(RagiumItems.SPAWNER_FRAGMENT, "スポナーの欠片")
        add(RagiumItems.TAR, "タール")
        add(RagiumItems.WITHER_DOLl, "ウィザー人形")
        add(RagiumItems.WITHER_STAR, "ウィザースター")

        add(RagiumItems.POTATO_SPROUTS, "ジャガイモの芽")
        add(RagiumItems.GREEN_CAKE, "グリーンケーキ")
        add(RagiumItems.GREEN_CAKE_DUST, "グリーンケーキの粉")
        add(RagiumItems.GREEN_PELLET, "グリーンペレット")
        // Armor
        add(RagiumItems.NIGHT_VISION_GOGGLES, "暗視ゴーグル")
        // Tool
        add(RagiumItems.DRILL, "電動ドリル")

        add(RagiumItems.ADVANCED_MAGNET, "発展らぎマグネット")
        add(RagiumItems.DYNAMIC_LANTERN, "らぎランタン")
        add(RagiumItems.ELDRITCH_EGG, "異質な卵")
        add(RagiumItems.LOOT_TICKET, "らぎチケット")
        add(RagiumItems.MAGNET, "らぎマグネット")
        add(RagiumItems.POTION_BUNDLE, "ポーションバンドル")
        add(RagiumItems.SLOT_COVER, "スロットカバー")
        add(RagiumItems.TELEPORT_KEY, "転位の鍵")
        add(RagiumItems.TRADER_CATALOG, "行商人のカタログ")
        add(RagiumItems.UNIVERSAL_BUNDLE, "共有バンドル")
        // Food
        add(RagiumItems.CREAM_BOWL, "クリーム入りボウル")

        add(RagiumItems.ICE_CREAM, "アイスクリーム")
        add(RagiumItems.ICE_CREAM_SODA, "クリームソーダ")

        add(RagiumItems.CANNED_COOKED_MEAT, "焼肉缶詰")

        add(RagiumItems.SWEET_BERRIES_CAKE_SLICE, "カットスイートベリーケーキ")
        add(RagiumItems.MELON_PIE, "メロンパイ")

        add(RagiumItems.RAGI_CHERRY, "らぎチェリー")
        add(RagiumItems.RAGI_CHERRY_PULP, "らぎチェリーの果肉")
        add(RagiumItems.RAGI_CHERRY_JUICE, "らぎチェリージュース")
        add(RagiumItems.RAGI_CHERRY_JAM, "らぎチェリージャム")
        add(RagiumItems.RAGI_CHERRY_PIE, "らぎチェリーパイ")
        add(RagiumItems.RAGI_CHERRY_PIE_SLICE, "カットらぎチェリーパイ")
        add(RagiumItems.RAGI_CHERRY_TOAST, "らぎチェリーのトースト")
        add(RagiumItems.FEVER_CHERRY, "フィーバーチェリー")

        add(RagiumItems.BOTTLED_BEE, "瓶詰めのハチ")
        add(RagiumItems.AMBROSIA, "アンブロシア")

        // Mold
        // add(RagiumItems.Molds.BLANK, "成形型（なし）")
        // add(RagiumItems.Molds.BALL, "成形型（ボール）")
        // add(RagiumItems.Molds.BLOCK, "成形型（ブロック）")
        // add(RagiumItems.Molds.GEAR, "成形型（歯車）")
        // add(RagiumItems.Molds.INGOT, "成形型（インゴット）")
        // add(RagiumItems.Molds.PLATE, "成形型（板材）")
        // add(RagiumItems.Molds.ROD, "成形型（棒材）")
        // add(RagiumItems.Molds.WIRE, "成形型（ワイヤー）")
        // Parts
        add(RagiumItems.ADVANCED_CIRCUIT, "発展回路")
        add(RagiumItems.BASIC_CIRCUIT, "基本回路")
        add(RagiumItems.CIRCUIT_BOARD, "回路基板")
        add(RagiumItems.GRAVITATIONAL_UNIT, "重力制御ユニット")
        add(RagiumItems.LED, "発光ダイオード")
        add(RagiumItems.LUMINOUS_PASTE, "蛍光ペースト")
        add(RagiumItems.POLYMER_CATALYST, "重合触媒")
        add(RagiumItems.POLYMER_RESIN, "高分子樹脂")
        add(RagiumItems.REDSTONE_BOARD, "レッドストーン基板")
        add(RagiumItems.SOLAR_PANEL, "太陽光パネル")
        add(RagiumItems.SYNTHETIC_FIBER, "合成繊維")
        add(RagiumItems.SYNTHETIC_LEATHER, "合成革")
        // Upgrades
        add(RagiumItems.ETERNAL_COMPONENT, "永久構造体")

        add(RagiumItems.EFFICIENT_CRUSH_UPGRADE, "効率的粉砕アップグレード")
        add(RagiumItems.FORTUNE_UPGRADE, "幸運アップグレード")
        add(RagiumItems.PRIMARY_ONLY_UPGRADE, "主産物特化アップグレード")

        add(RagiumItems.EXP_COLLECTOR_UPGRADE, "経験値収集アップグレード")
        add(RagiumItems.FISHING_UPGRADE, "釣りアップグレード")
        add(RagiumItems.MOB_CAPTURE_UPGRADE, "モブ捕獲アップグレード")

        add(RagiumItems.CREATIVE_UPGRADE, "クリエイティブ用アップグレード")
    }

    private fun keyMapping() {
        add(RagiumCommonTranslation.KEY_CATEGORY, "Ragium")

        add(RagiumCommonTranslation.KEY_OPEN_UNIVERSAL_BUNDLE, "共有バンドルを開く")
    }

    private fun modTags() {
        add(RagiumModTags.Blocks.LED_BLOCKS, "LEDブロック")
        add(RagiumModTags.Blocks.RESONANT_DEBRIS_REPLACEABLES, "共振の残骸で置換可能")
        add(RagiumModTags.Blocks.WIP, "開発中")

        add(RagiumModTags.EntityTypes.CAPTURE_BLACKLIST, "捕獲できるモブのブラックリスト")
        add(RagiumModTags.EntityTypes.CONFUSION_BLACKLIST, "混乱を起こすモブのブラックリスト")
        add(RagiumModTags.EntityTypes.GENERATE_RESONANT_DEBRIS, "共振の残骸を生成する")
        add(RagiumModTags.EntityTypes.SENSITIVE_TO_NOISE_CANCELLING, "ノイズキャンセリングに反応する")

        add(RagiumModTags.Items.ELDRITCH_PEARL_BINDER, "異質な真珠の結合剤")
        add(RagiumModTags.Items.LED_BLOCKS, "LEDブロック")
        add(RagiumModTags.Items.PLASTICS, "プラスチック")
        add(RagiumModTags.Items.POLYMER_RESIN, "高分子樹脂")
        add(RagiumModTags.Items.RAW_MEAT, "生肉")
        add(RagiumModTags.Items.WIP, "開発中")

        add(RagiumModTags.Items.CAPACITY_ENCHANTABLE, "容量増加をエンチャント可能")
        add(RagiumModTags.Items.RANGE_ENCHANTABLE, "範囲増加をエンチャント可能")
        add(RagiumModTags.Items.STRIKE_ENCHANTABLE, "雷撃をエンチャント可能")

        add(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC, "合金炉で使う基本融剤")
        add(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED, "合金炉で使う発展融剤")

        add(RagiumModTags.Items.TOOLS_DRILL, "ドリル")
        add(RagiumModTags.Items.TOOLS_HAMMER, "ハンマー")
    }

    private fun recipeType() {
        add(RagiumRecipeTypes.ALLOYING, "合金")
        add(RagiumRecipeTypes.BREWING, "醸造")
        add(RagiumRecipeTypes.COMPRESSING, "圧縮")
        add(RagiumRecipeTypes.CRUSHING, "破砕")
        add(RagiumRecipeTypes.CUTTING, "裁断")
        add(RagiumRecipeTypes.ENCHANTING, "エンチャント")
        add(RagiumRecipeTypes.EXTRACTING, "抽出")
        add(RagiumRecipeTypes.MELTING, "融解")
        add(RagiumRecipeTypes.MIXING, "混合")
        add(RagiumRecipeTypes.PLANTING, "栽培")
        add(RagiumRecipeTypes.REFINING, "精製")
        add(RagiumRecipeTypes.SIMULATING, "シミュレーション")
    }

    private fun text() {
        add(HTAccessConfig.INPUT_ONLY, "モード：搬入")
        add(HTAccessConfig.OUTPUT_ONLY, "モード：搬出")
        add(HTAccessConfig.BOTH, "モード：双方")
        add(HTAccessConfig.DISABLED, "モード：無効")

        add(HTFluidBlockAttribute.TankType.INPUT, "搬入タンクの容量")
        add(HTFluidBlockAttribute.TankType.OUTPUT, "搬出タンクの容量")
        add(HTFluidBlockAttribute.TankType.FIRST_INPUT, "1番目の搬入タンクの容量")
        add(HTFluidBlockAttribute.TankType.SECOND_INPUT, "2番目の搬入タンクの容量")

        add(HTMachineUpgrade.Key.ENERGY_CAPACITY, $$"- エネルギー容量: %1$s")
        add(HTMachineUpgrade.Key.ENERGY_EFFICIENCY, $$"- エネルギー効率: %1$s")
        add(HTMachineUpgrade.Key.ENERGY_GENERATION, $$"- エネルギー生産率: %1$s")
        add(HTMachineUpgrade.Key.SPEED, $$"- 処理速度: %1$s")

        add(HTMachineUpgrade.Key.SUBPRODUCT_CHANCE, $$"- 追加のチャンス: %1$s")
        // API - Constants
        add(RagiumTranslation.ERROR, "エラー")
        add(RagiumTranslation.INFINITE, "無限")
        add(RagiumTranslation.NONE, "なし")
        add(RagiumTranslation.EMPTY, "空")
        // API - Error
        add(RagiumTranslation.EMPTY_TAG_KEY, $$"空のタグ: %1$s")
        add(RagiumTranslation.INVALID_PACKET_S2C, $$"サーバー側からの不正なパケットを受信しました: %1$s")
        add(RagiumTranslation.INVALID_PACKET_C2S, $$"クライアント側からの不正なパケットを受信しました: %1$s")

        add(RagiumTranslation.MISSING_SERVER, "サーバーが見つかりません")
        add(RagiumTranslation.MISSING_REGISTRY, $$"不明なレジストリ: %1$s")
        add(RagiumTranslation.MISSING_KEY, $$"不明なキー: %1$s")
        // API - GUI
        add(RagiumTranslation.BURN_TIME, $$"燃焼時間: %1$s ticks")
        // API - Item
        add(RagiumTranslation.TOOLTIP_BLOCK_POS, $$"座標: [%1$s, %2$s, %3$s]")
        add(RagiumTranslation.TOOLTIP_CHARGE_POWER, $$"威力: %1$s")
        add(RagiumTranslation.TOOLTIP_DIMENSION, $$"次元: %1$s")
        add(RagiumTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT, $$"常に少なくとも%1$sがあります")
        add(RagiumTranslation.TOOLTIP_LOOT_TABLE_ID, $$"ルートテーブル: %1$s")

        add(RagiumTranslation.TOOLTIP_SHOW_DESCRIPTION, "シフトキーを押して説明を表示")
        add(RagiumTranslation.TOOLTIP_SHOW_DETAILS, "シフトキーを押して詳細を表示")
        add(RagiumTranslation.TOOLTIP_WIP, "この要素は開発中です！！")
        // Common
        add(RagiumCommonTranslation.WARPED_WART, "食べるとランダムにデバフを一つだけ消します。")
        add(RagiumCommonTranslation.EXP_BERRIES, "食べると経験値を付与します。")

        add(RagiumCommonTranslation.QUARTZ_GLASS, "シルクタッチがなくても回収できるガラスです。")
        add(RagiumCommonTranslation.OBSIDIAN_GLASS, "ネザーポータルのフレームに使用できるガラスです。")
        add(RagiumCommonTranslation.CRIMSON_GLASS, "上に乗るとマグマブロックと同じダメージを与えるガラスです。")
        add(RagiumCommonTranslation.WARPED_GLASS, "プレイヤーのみが通り抜けられるガラスです。")

        add(RagiumCommonTranslation.THERMAL_GENERATOR, "かまど燃料から発電する機械です。")
        add(RagiumCommonTranslation.CULINARY_GENERATOR, "食料から発電する機械です。")
        add(RagiumCommonTranslation.COMBUSTION_GENERATOR, "液体燃料から発電する機械です。")
        add(RagiumCommonTranslation.SOLAR_PANEL_UNIT, "太陽光パネルコントローラーと組み合わせて使用します。")
        add(RagiumCommonTranslation.SOLAR_PANEL_CONTROLLER, "9x9の範囲にある太陽光パネルユニットの台数から発電する機械です。")
        add(RagiumCommonTranslation.ENCHANTMENT_GENERATOR, "エンチャント本や液体経験値から発電する機械です。")
        add(RagiumCommonTranslation.NUCLEAR_REACTOR, "核燃料から発電する機械です。")

        add(RagiumCommonTranslation.ALLOY_SMELTER, "複数のアイテムを一つのアイテムに焼き上げる機械です。")
        add(RagiumCommonTranslation.BLOCK_BREAKER, "正面のブロックを採掘する機械です。")
        add(RagiumCommonTranslation.CUTTING_MACHINE, "アイテムを切断して別のアイテムにする機械です。")
        add(RagiumCommonTranslation.COMPRESSOR, "アイテムを圧縮して別のアイテムにする機械です。")
        add(RagiumCommonTranslation.EXTRACTOR, "アイテムから別のアイテムと液体を抽出する機械です。")
        add(RagiumCommonTranslation.PULVERIZER, "アイテムを粉砕して別のアイテムにする機械です。")

        add(RagiumCommonTranslation.CRUSHER, "粉砕機の上位版で，副産物も生産する機械です。")
        add(RagiumCommonTranslation.MELTER, "アイテムを融かして液体にする機械です。")
        add(RagiumCommonTranslation.MIXER, "複数のアイテムと液体を混合する機械です。")
        add(RagiumCommonTranslation.REFINERY, "液体を別の液体に変換する機械です。")

        add(RagiumCommonTranslation.BREWERY, "アイテムと液体からポーションを醸造する機械です。")
        add(RagiumCommonTranslation.MULTI_SMELTER, "複数のアイテムをまとめて精錬する機械です。")
        add(RagiumCommonTranslation.PLANTER, "種子や苗木から植物を成長さる機械です。")

        add(RagiumCommonTranslation.ENCHANTER, "アイテムからエンチャント本を作成する機械です。")
        add(RagiumCommonTranslation.SIMULATOR, "ブロックやモブの行動を再現して資源を生産する機械です。")

        add(RagiumCommonTranslation.FLUID_COLLECTOR, "周囲の水源やバイオームに応じて水を生産する設備です。")

        add(RagiumCommonTranslation.DIM_ANCHOR, "設置されたチャンクを強制的に読み込む設備です。")
        add(RagiumCommonTranslation.ENI, "エネルギーネットワークにアクセスする設備です。")

        add(RagiumCommonTranslation.CEU, "無制限にエネルギーを供給する設備です。")

        add(RagiumCommonTranslation.CRATE, "1種類のアイテムを保管するストレージです。")
        add(RagiumCommonTranslation.OPEN_CRATE, "搬入されたアイテムを真下に落とすストレージです。")
        add(RagiumCommonTranslation.DRUM, "1種類の液体を保管するストレージです。")
        add(RagiumCommonTranslation.EXP_DRUM, "液体経験値だけを保管するストレージです。")

        add(RagiumCommonTranslation.CONFIG_ENERGY_CAPACITY, "エネルギー容量")
        add(RagiumCommonTranslation.CONFIG_ENERGY_RATE, "エネルギー使用速度")

        add(RagiumCommonTranslation.COMMAND_ENERGY_ADD, $$"エネルギーネットワークに%1$s FEを追加しました。")
        add(RagiumCommonTranslation.COMMAND_ENERGY_GET, $$"エネルギーネットワークに%1$s FEだけ保持しています。")
        add(RagiumCommonTranslation.COMMAND_ENERGY_SET, $$"エネルギーネットワークの量を%1$s FEに指定しました。")

        add(RagiumCommonTranslation.NO_DESTINATION, "移動先が指定されていません。")
        add(RagiumCommonTranslation.UNKNOWN_DIMENSION, $$"未知の次元です: %1$s")
        add(RagiumCommonTranslation.FUEL_SHORTAGE, $$"燃料不足: %1$s mB必要です。")

        add(RagiumCommonTranslation.ELDER_HEART, "エルダーガーディアンからドロップします。")
        add(RagiumCommonTranslation.IRIDESCENT_POWDER, "時間経過やダメージで消滅しません。")

        add(RagiumCommonTranslation.BLAST_CHARGE, "着弾すると爆発するチャージです。")
        add(RagiumCommonTranslation.STRIKE_CHARGE, "着弾すると落雷を起こすチャージです。")
        add(RagiumCommonTranslation.NEUTRAL_CHARGE, "着弾すると周囲のモブから装備を奪うチャージです。")
        add(RagiumCommonTranslation.FISHING_CHARGE, "水中に着弾すると魚を釣るチャージです。")
        add(RagiumCommonTranslation.TELEPORT_CHARGE, "着弾すると周囲のモブをテレポートさせるチャージです。")
        add(RagiumCommonTranslation.CONFUSING_CHARGE, "着弾すると周囲のモブを混乱させるチャージです。")

        add(RagiumCommonTranslation.DYNAMIC_LANTERN, "範囲内の暗所に光源を設置します。")
        add(RagiumCommonTranslation.ELDRITCH_EGG, "右クリックで投げることができ，モブに当たるとスポーンエッグになります。")
        add(RagiumCommonTranslation.MAGNET, "範囲内のドロップアイテムを回収します。")
        add(RagiumCommonTranslation.SLOT_COVER, "機械のスロットに入れることでレシピ判定から無視されます。")
        add(RagiumCommonTranslation.TRADER_CATALOG, "行商人からドロップします。右クリックで行商人との取引を行えます。")

        add(RagiumCommonTranslation.AMBROSIA, "いつでも食べられる上，いくら食べてもなくなりません！")
        add(RagiumCommonTranslation.ICE_CREAM, "食べると鎮火します。")
        add(RagiumCommonTranslation.RAGI_CHERRY, "リンゴと同様にサクラの葉からドロップします。")

        add(RagiumCommonTranslation.EFFICIENT_CRUSH_UPGRADE, "潤滑油を消費することにより，短時間で粉砕するようになります。")
        add(RagiumCommonTranslation.PRIMARY_ONLY_UPGRADE, "副産物を生産しなくなります。")

        add(RagiumCommonTranslation.EXP_COLLECTOR_UPGRADE, "液体収集機が経験値オーブを集めるようになります。")
        add(RagiumCommonTranslation.FISHING_UPGRADE, "アイテム収集機が下の3x3の水源から魚を釣るようになります。")
        add(RagiumCommonTranslation.MOB_CAPTURE_UPGRADE, "アイテム収集機が異質な卵を消費してモブを捕まえるようになります。")
    }

    //    Addon    //

    private fun emi() {
        add(RagiumCommonTranslation.EMI_MACHINE_UPGRADE, "機械のアップグレード")
    }

    private fun jade() {
        add(HTBlockConfigurationDataProvider, "アクセス制御")
        add(HTBlockMachinePropertiesProvider, "機械のプロパティ")
        add(HTBlockOwnerProvider, "ブロックの所有者")

        add(RagiumCommonTranslation.JADE_MACHINE_TIER, $$"ティア: %1$s")
    }
}
