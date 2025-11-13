package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.data.lang.HTLanguageProvider
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.client.RagiumKeyMappings
import hiiragi283.ragium.client.integration.jade.provider.HTBlockConfigurationDataProvider
import hiiragi283.ragium.client.integration.jade.provider.HTBlockOwnerProvider
import hiiragi283.ragium.client.integration.jade.provider.HTExperienceHandlerProvider
import hiiragi283.ragium.client.text.RagiumClientTranslation
import hiiragi283.ragium.common.integration.RagiumDelightAddon
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.text.RagiumCommonTranslation
import hiiragi283.ragium.common.tier.HTDrumTier
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
        itemGroup()
        keyMapping()
        modTags()
        recipeType()
        text()

        delight()
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
        addAdvancement(RagiumAdvancements.BUDDING_AZURE, "青に染まる", "青の知識を芽生えたアメジストに使う")
        addAdvancement(RagiumAdvancements.AZURE_SHARD, "ラズライトではない", "紺碧の欠片を手に入れる")
        addAdvancement(RagiumAdvancements.AZURE_STEEL, "鋼は青かった", "紺鉄インゴットを手に入れる")
        addAdvancement(RagiumAdvancements.AZURE_GEARS, "Wake up! Azure Dragon!", "紺鉄インゴットで作られたツールか装備を手に入れる")
        addAdvancement(RagiumAdvancements.SIMULATOR, "1 + 2 + 3 = 1 * 2 * 3", "シミュレーション室を手に入れる")
        // Deep
        addAdvancement(RagiumAdvancements.RESONANT_DEBRIS, "「古代」の残骸", "共振の残骸を手に入れる")
        addAdvancement(RagiumAdvancements.DEEP_STEEL, "深く，深く，なお深く。", "深層鋼を手に入れる")
        addAdvancement(RagiumAdvancements.BEHEAD_MOB, "帯電なんていらない", "雷撃エンチャントが付与された武器でモブの頭を切り落とす")

        addAdvancement(RagiumAdvancements.ECHO_STAR, "取れないブームがあるものか", "残響の星を手に入れる")
        // Night Metal
        addAdvancement(RagiumAdvancements.NIGHT_METAL, "ナイト・オブ・ナイツ", "夜金インゴットを手に入れる")
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
        // Iridescentium
        addAdvancement(RagiumAdvancements.IRIDESCENTIUM, "虹色の錬金術師", "七色金インゴットを手に入れる")
        addAdvancement(RagiumAdvancements.ETERNAL_COMPONENT, "さあ，地獄を楽しみな！", "ツールを不可壊にするためにエターナル構造体を手に入れる")
    }

    private fun block() {
        add(RagiumBlocks.SILT, "シルト")

        add(RagiumBlocks.BUDDING_AZURE, "芽生えた紺碧")
        add(RagiumBlocks.AZURE_CLUSTER, "紺碧の塊")
        add(RagiumBlocks.RESONANT_DEBRIS, "共振の残骸")
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
        add(RagiumBlocks.STONE_CASING, "石材筐体")
        add(RagiumBlocks.REINFORCED_STONE_CASING, "強化石材筐体")
        add(RagiumBlocks.WOODEN_CASING, "木材筐体")
        // Generators
        add(RagiumBlocks.THERMAL_GENERATOR, "火力発電機")

        add(RagiumBlocks.COMBUSTION_GENERATOR, "燃焼発電機")

        add(RagiumBlocks.SOLAR_PANEL_CONTROLLER, "太陽光パネルコントローラー")

        add(RagiumBlocks.ENCHANTMENT_GENERATOR, "エンチャント発電機")
        add(RagiumBlocks.NUCLEAR_REACTOR, "原子炉")
        // Consumers
        add(RagiumBlocks.AUTO_SMITHING_TABLE, "自動鍛冶台")
        add(RagiumBlocks.AUTO_STONECUTTER, "自動石切台")

        add(RagiumBlocks.ALLOY_SMELTER, "合金炉")
        add(RagiumBlocks.BLOCK_BREAKER, "採掘機")
        add(RagiumBlocks.COMPRESSOR, "圧縮機")
        add(RagiumBlocks.CUTTING_MACHINE, "裁断機")
        add(RagiumBlocks.EXTRACTOR, "抽出機")
        add(RagiumBlocks.PULVERIZER, "粉砕機")

        add(RagiumBlocks.CRUSHER, "破砕機")
        add(RagiumBlocks.MELTER, "溶融炉")
        add(RagiumBlocks.REFINERY, "精製機")
        add(RagiumBlocks.WASHER, "洗浄機")

        add(RagiumBlocks.BREWERY, "醸造機")
        add(RagiumBlocks.MULTI_SMELTER, "並列製錬炉")
        add(RagiumBlocks.PLANTER, "栽培室")
        add(RagiumBlocks.SIMULATOR, "シミュレーション室")
        // Devices
        add(RagiumBlocks.ITEM_BUFFER, "アイテムバッファ")
        add(RagiumBlocks.MILK_COLLECTOR, "搾乳機")
        add(RagiumBlocks.WATER_COLLECTOR, "水収集機")

        add(RagiumBlocks.EXP_COLLECTOR, "経験値収集機")
        add(RagiumBlocks.LAVA_COLLECTOR, "溶岩収集機")

        add(RagiumBlocks.DIM_ANCHOR, "次元アンカー")
        add(RagiumBlocks.ENI, "E.N.I.")

        add(RagiumBlocks.MOB_CAPTURER, "モブ捕獲機")
        add(RagiumBlocks.TELEPAD, "テレパッド")

        add(RagiumBlocks.CEU, "C.E.U.")
        // Storage
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
        add(RagiumEntityTypes.BLAST_CHARGE, "ブラストチャージ")
        add(RagiumEntityTypes.ELDRITCH_EGG, "異質な卵")

        for (tier: HTDrumTier in HTDrumTier.entries) {
            val value: String = tier.translate(type, "%s付きトロッコ")
            add(tier.getEntityType(), value)
            add(tier.getMinecartItem(), value)
        }

        // add(RagiumEntityTypes.DYNAMITE, "ダイナマイト")
        // add(RagiumEntityTypes.DEFOLIANT_DYNAMITE, "枯葉剤ダイナマイト")
        // add(RagiumEntityTypes.FLATTEN_DYNAMITE, "整地用ダイナマイト")
        // add(RagiumEntityTypes.NAPALM_DYNAMITE, "ナパームダイナマイト")
        // add(RagiumEntityTypes.POISON_DYNAMITE, "毒ガスダイナマイト")
    }

    private fun fluid() {
        addFluid(RagiumFluidContents.HONEY, "蜂蜜")
        addFluid(RagiumFluidContents.EXPERIENCE, "液体経験値")
        addFluid(RagiumFluidContents.MUSHROOM_STEW, "キノコシチュー")

        addFluid(RagiumFluidContents.CHOCOLATE, "チョコレート")
        addFluid(RagiumFluidContents.MEAT, "液体肉")
        addFluid(RagiumFluidContents.ORGANIC_MUTAGEN, "有機的変異原")

        addFluid(RagiumFluidContents.CRUDE_OIL, "原油")
        addFluid(RagiumFluidContents.NATURAL_GAS, "天然ガス")
        addFluid(RagiumFluidContents.NAPHTHA, "ナフサ")
        addFluid(RagiumFluidContents.LUBRICANT, "潤滑油")

        addFluid(RagiumFluidContents.FUEL, "燃料")
        addFluid(RagiumFluidContents.CRIMSON_FUEL, "深紅の燃料")
        addFluid(RagiumFluidContents.GREEN_FUEL, "グリーン燃料")

        addFluid(RagiumFluidContents.SAP, "樹液")
        addFluid(RagiumFluidContents.CRIMSON_SAP, "深紅の樹液")
        addFluid(RagiumFluidContents.WARPED_SAP, "歪んだ樹液")
    }

    private fun item() {
        // Material
        add(RagiumItems.COMPRESSED_SAWDUST, "圧縮したおがくず")
        add(RagiumItems.ECHO_STAR, "残響の星")
        add(RagiumItems.ELDER_HEART, "エルダーの心臓")
        add(RagiumItems.RAGI_ALLOY_COMPOUND, "ラギ合金混合物")
        add(RagiumItems.RAGI_COKE, "らぎコークス")
        add(RagiumItems.RESIN, "樹脂")
        add(RagiumItems.TAR, "タール")
        add(RagiumItems.WITHER_DOLl, "ウィザー人形")

        add(RagiumItems.POTATO_SPROUTS, "ジャガイモの芽")
        add(RagiumItems.GREEN_CAKE, "グリーンケーキ")
        add(RagiumItems.GREEN_CAKE_DUST, "グリーンケーキの粉")
        add(RagiumItems.GREEN_PELLET, "グリーンペレット")
        // Armor
        add(RagiumItems.NIGHT_VISION_GOGGLES, "暗視ゴーグル")
        // Tool
        add(RagiumItems.MEDIUM_DRUM_UPGRADE, "ドラム強化（中）")
        add(RagiumItems.LARGE_DRUM_UPGRADE, "ドラム強化（大）")
        add(RagiumItems.HUGE_DRUM_UPGRADE, "ドラム強化（特大）")

        add(RagiumItems.DRILL, "電動ドリル")

        add(RagiumItems.ADVANCED_MAGNET, "発展らぎマグネット")
        add(RagiumItems.BLAST_CHARGE, "ブラストチャージ")
        add(RagiumItems.BLUE_KNOWLEDGE, "青の知識")
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
        add(RagiumItems.ICE_CREAM, "アイスクリーム")
        add(RagiumItems.ICE_CREAM_SODA, "クリームソーダ")

        add(RagiumItems.CANNED_COOKED_MEAT, "焼肉缶詰")

        add(RagiumItems.SWEET_BERRIES_CAKE_SLICE, "カットスイートベリーケーキ")
        add(RagiumItems.MELON_PIE, "メロンパイ")

        add(RagiumItems.RAGI_CHERRY, "らぎチェリー")
        add(RagiumItems.RAGI_CHERRY_JAM, "らぎチェリージャム")
        add(RagiumItems.RAGI_CHERRY_PULP, "らぎチェリーの果肉")
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
        add(RagiumItems.getMold(CommonMaterialPrefixes.STORAGE_BLOCK), "ブロックの鋳型")
        add(RagiumItems.getMold(CommonMaterialPrefixes.GEM), "宝石の鋳型")
        add(RagiumItems.getMold(CommonMaterialPrefixes.INGOT), "インゴットの鋳型")

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
    }

    private fun itemGroup() {
        add(RagiumCommonTranslation.CREATIVE_TAB_BLOCKS, "Ragium - ブロック")
        add(RagiumCommonTranslation.CREATIVE_TAB_INGREDIENTS, "Ragium - 素材")
        add(RagiumCommonTranslation.CREATIVE_TAB_ITEMS, "Ragium - アイテム")
    }

    private fun keyMapping() {
        add(RagiumClientTranslation.KEY_CATEGORY, "Ragium")

        add(RagiumKeyMappings.OPEN_UNIVERSAL_BUNDLE, "共有バンドルを開く")
    }

    private fun modTags() {
        add(RagiumModTags.Blocks.LED_BLOCKS, "LEDブロック")
        add(RagiumModTags.Blocks.RESONANT_DEBRIS_REPLACEABLES, "共振の残骸で置換可能")
        add(RagiumModTags.Blocks.WIP, "開発中")

        add(RagiumModTags.EntityTypes.CAPTURE_BLACKLIST, "捕獲できるモブのブラックリスト")
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
        add(RagiumRecipeTypes.COMPRESSING, "圧縮")
        add(RagiumRecipeTypes.CRUSHING, "破砕")
        add(RagiumRecipeTypes.CUTTING, "裁断")
        add(RagiumRecipeTypes.ENCHANTING, "エンチャント")
        add(RagiumRecipeTypes.EXTRACTING, "抽出")
        add(RagiumRecipeTypes.FLUID_TRANSFORM, "液体変換")
        add(RagiumRecipeTypes.MELTING, "融解")
        add(RagiumRecipeTypes.MIXING, "混合")
        add(RagiumRecipeTypes.PLANTING, "栽培")
        add(RagiumRecipeTypes.SIMULATING, "シミュレーション")
        add(RagiumRecipeTypes.WASHING, "洗浄")
    }

    private fun text() {
        add(HTAccessConfig.INPUT_ONLY, "モード：搬入")
        add(HTAccessConfig.OUTPUT_ONLY, "モード：搬出")
        add(HTAccessConfig.BOTH, "モード：双方")
        add(HTAccessConfig.DISABLED, "モード：無効")

        // API - Constants
        add(RagiumTranslation.ERROR, "エラー")
        add(RagiumTranslation.INFINITE, "無限")
        add(RagiumTranslation.NONE, "なし")
        add(RagiumTranslation.EMPTY, "空")
        // API - Error
        add(RagiumTranslation.EMPTY_TAG_KEY, $$"空のタグ: %1$s")
        add(RagiumTranslation.INVALID_PACKET_S2C, $$"サーバー側からの不正なパケットを受信しました: %1$s")
        add(RagiumTranslation.INVALID_PACKET_C2S, $$"クライアント側からの不正なパケットを受信しました: %1$s")
        add(RagiumTranslation.MISSING_REGISTRY, $$"不明なレジストリ: %1$s")
        add(RagiumTranslation.MISSING_KEY, $$"不明なキー: %1$s")
        // API - Item
        add(RagiumTranslation.ITEM_POTION, $$"%1$sのポーション")

        add(RagiumTranslation.TOOLTIP_BLOCK_POS, $$"座標: [%1$s, %2$s, %3$s]")
        add(RagiumTranslation.TOOLTIP_DIMENSION, $$"次元: %1$s")
        add(RagiumTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT, $$"常に少なくとも%1$sがあります")
        add(RagiumTranslation.TOOLTIP_LOOT_TABLE_ID, $$"ルートテーブル: %1$s")

        add(RagiumTranslation.TOOLTIP_SHOW_DESCRIPTION, "シフトキーを押して説明を表示")
        add(RagiumTranslation.TOOLTIP_SHOW_DETAILS, "シフトキーを押して詳細を表示")
        add(RagiumTranslation.TOOLTIP_WIP, "この要素は開発中です！！")
        // Common
        add(RagiumCommonTranslation.WARPED_WART, "食べるとランダムにデバフを一つだけ消します。")
        add(RagiumCommonTranslation.EXP_BERRIES, "食べると経験値を付与します。")

        add(RagiumCommonTranslation.THERMAL_GENERATOR, "かまど燃料や高温の液体から発電します。")
        add(RagiumCommonTranslation.COMBUSTION_GENERATOR, "液体燃料から発電します。")
        add(RagiumCommonTranslation.SOLAR_PANEL_CONTROLLER, "接続された太陽光パネルから発電します。")
        add(RagiumCommonTranslation.ENCHANTMENT_GENERATOR, "エンチャント本や液体経験値から発電します。")
        add(RagiumCommonTranslation.NUCLEAR_REACTOR, "核燃料から発電します。")

        add(RagiumCommonTranslation.MOB_CAPTURER, "周囲のモブをスポーンエッグに変換します。")
        add(RagiumCommonTranslation.CEU, "クリエイティブ用のバッテリー。")

        add(RagiumCommonTranslation.CRATE, "1種類のアイテムを保管します。")
        add(RagiumCommonTranslation.DRUM, "1種類の液体を保管します。")
        add(RagiumCommonTranslation.EXP_DRUM, "液体経験値と経験値を相互に保管します。")

        add(RagiumCommonTranslation.COMMAND_ENERGY_ADD, $$"エネルギーネットワークに%1$s FEを追加しました。")
        add(RagiumCommonTranslation.COMMAND_ENERGY_GET, $$"エネルギーネットワークに%1$s FEだけ保持しています。")
        add(RagiumCommonTranslation.COMMAND_ENERGY_SET, $$"エネルギーネットワークの量を%1$s FEに指定しました。")

        add(RagiumCommonTranslation.NO_DESTINATION, "移動先が指定されていません。")
        add(RagiumCommonTranslation.UNKNOWN_DIMENSION, $$"未知の次元です: %1$s")
        add(RagiumCommonTranslation.FUEL_SHORTAGE, $$"燃料不足: %1$s mB必要です。")

        add(RagiumCommonTranslation.ELDER_HEART, "エルダーガーディアンからドロップします。")

        add(RagiumCommonTranslation.BLAST_CHARGE, "作業台で火薬を用いて強化することができます。")
        add(RagiumCommonTranslation.DYNAMIC_LANTERN, "範囲内の暗所に光源を設置します。")
        add(RagiumCommonTranslation.ELDRITCH_EGG, "右クリックで投げることができ，モブに当たるとスポーンエッグになります。")
        add(RagiumCommonTranslation.MAGNET, "範囲内のドロップアイテムを回収します。")
        add(RagiumCommonTranslation.SLOT_COVER, "機械のスロットに入れることでレシピ判定から無視されます。")
        add(RagiumCommonTranslation.TRADER_CATALOG, "行商人からドロップします。右クリックで行商人との取引を行えます。")

        add(RagiumCommonTranslation.AMBROSIA, "いつでも食べられる上，いくら食べてもなくなりません！")
        add(RagiumCommonTranslation.ICE_CREAM, "食べると鎮火します。")
        add(RagiumCommonTranslation.RAGI_CHERRY, "リンゴと同様にサクラの葉からドロップします。")
    }

    //    Addon    //

    private fun delight() {
        add(RagiumDelightAddon.RAGI_CHERRY_PIE, "らぎチェリーパイ")
        add(RagiumDelightAddon.RAGI_CHERRY_TOAST_BLOCK, "らぎチェリーのトーストタワー")

        add(RagiumDelightAddon.RAGI_CHERRY_PIE_SLICE, "カットらぎチェリーパイ")
    }

    private fun jade() {
        add(HTBlockConfigurationDataProvider, "アクセス制御")
        add(HTExperienceHandlerProvider.ForBlocks, "経験値")
        add(HTBlockOwnerProvider, "ブロックの所有者")

        add(RagiumClientTranslation.JADE_EXP_STORAGE, "経験値: %s")
    }
}
