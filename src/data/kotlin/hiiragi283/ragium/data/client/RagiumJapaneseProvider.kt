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
import hiiragi283.ragium.common.integration.RagiumDelightAddon
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.data.server.advancement.RagiumAdvancements
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumCreativeTabs
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
        information()

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
        add(RagiumItems.BASALT_MESH, "玄武岩メッシュ")
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
        add(RagiumItems.ADVANCED_CIRCUIT_BOARD, "玄武岩強化回路基板")
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
        addItemGroup(RagiumCreativeTabs.BLOCKS, "Ragium - ブロック")
        addItemGroup(RagiumCreativeTabs.INGREDIENTS, "Ragium - 素材")
        addItemGroup(RagiumCreativeTabs.ITEMS, "Ragium - アイテム")
    }

    private fun keyMapping() {
        add(RagiumTranslation.KEY_CATEGORY, "Ragium")

        add(RagiumKeyMappings.OPEN_UNIVERSAL_BUNDLE, "共有バンドルを開く")
    }

    /*private fun material() {
        // Common
        addMaterialKey(CommonMaterials.ALUMINA, "アルミナ")
        addMaterialKey(CommonMaterials.ALUMINUM, "アルミニウム")
        addMaterialKey(CommonMaterials.ANTIMONY, "アンチモン")
        addMaterialKey(CommonMaterials.BERYLLIUM, "ベリリウム")
        addMaterialKey(CommonMaterials.ASH, "灰")
        addMaterialKey(CommonMaterials.BAUXITE, "ボーキサイト")
        addMaterialKey(CommonMaterials.BRASS, "真鍮")
        addMaterialKey(CommonMaterials.BRONZE, "青銅")
        addMaterialKey(CommonMaterials.CADMIUM, "カドミウム")
        addMaterialKey(CommonMaterials.CARBON, "炭素")
        addMaterialKey(CommonMaterials.CHEESE, "チーズ")
        addMaterialKey(CommonMaterials.CHOCOLATE, "チョコレート")
        addMaterialKey(CommonMaterials.CHROMIUM, "クロム")
        addMaterialKey(CommonMaterials.COAL_COKE, "石炭コークス")
        addMaterialKey(CommonMaterials.CONSTANTAN, "コンスタンタン")
        addMaterialKey(CommonMaterials.CRYOLITE, "氷晶石")
        addMaterialKey(CommonMaterials.ELECTRUM, "琥珀金")
        addMaterialKey(CommonMaterials.FLUORITE, "蛍石")
        addMaterialKey(CommonMaterials.INVAR, "不変鋼")
        addMaterialKey(CommonMaterials.IRIDIUM, "イリジウム")
        addMaterialKey(CommonMaterials.LEAD, "鉛")
        addMaterialKey(CommonMaterials.NICKEL, "ニッケル")
        addMaterialKey(CommonMaterials.NIOBIUM, "ニオブ")
        addMaterialKey(CommonMaterials.OSMIUM, "オスミウム")
        addMaterialKey(CommonMaterials.PERIDOT, "ペリドット")
        addMaterialKey(CommonMaterials.PLATINUM, "白金")
        addMaterialKey(CommonMaterials.PLUTONIUM, "プルトニウム")
        addMaterialKey(CommonMaterials.PYRITE, "金")
        addMaterialKey(CommonMaterials.RUBY, "ルビー")
        addMaterialKey(CommonMaterials.SALT, "塩")
        addMaterialKey(CommonMaterials.SALTPETER, "硝石")
        addMaterialKey(CommonMaterials.SAPPHIRE, "サファイア")
        addMaterialKey(CommonMaterials.SILICON, "シリコン")
        addMaterialKey(CommonMaterials.SILVER, "銀")
        addMaterialKey(CommonMaterials.SOLDERING_ALLOY, "半田合金")
        addMaterialKey(CommonMaterials.STAINLESS_STEEL, "ステンレス鋼")
        addMaterialKey(CommonMaterials.STEEL, "スチール")
        addMaterialKey(CommonMaterials.SULFUR, "硫黄")
        addMaterialKey(CommonMaterials.TIN, "スズ")
        addMaterialKey(CommonMaterials.SUPERCONDUCTOR, "超伝導体")
        addMaterialKey(CommonMaterials.TITANIUM, "チタン")
        addMaterialKey(CommonMaterials.TUNGSTEN, "タングステン")
        addMaterialKey(CommonMaterials.URANIUM, "ウラニウム")
        addMaterialKey(CommonMaterials.ZINC, "亜鉛")
        // AA
        addMaterialKey(IntegrationMaterials.BLACK_QUARTZ, "黒水晶")
        // AE2
        addMaterialKey(IntegrationMaterials.CERTUS_QUARTZ, "ケルタスクォーツ")
        addMaterialKey(IntegrationMaterials.FLUIX, "フルーシュ")
        // Create
        addMaterialKey(IntegrationMaterials.ANDESITE_ALLOY, "安山岩合金")
        addMaterialKey(IntegrationMaterials.CARDBOARD, "段ボール")
        addMaterialKey(IntegrationMaterials.ROSE_QUARTZ, "ローズクォーツ")
        // EIO
        addMaterialKey(IntegrationMaterials.COPPER_ALLOY, "銅合金")
        addMaterialKey(IntegrationMaterials.ENERGETIC_ALLOY, "エナジェティック合金")
        addMaterialKey(IntegrationMaterials.VIBRANT_ALLOY, "ヴァイブラント合金")
        addMaterialKey(IntegrationMaterials.REDSTONE_ALLOY, "レッドストーン合金")
        addMaterialKey(IntegrationMaterials.CONDUCTIVE_ALLOY, "伝導合金")
        addMaterialKey(IntegrationMaterials.PULSATING_ALLOY, "脈動合金")
        addMaterialKey(IntegrationMaterials.DARK_STEEL, "ダークスチール")
        addMaterialKey(IntegrationMaterials.SOULARIUM, "ソウラリウム")
        addMaterialKey(IntegrationMaterials.END_STEEL, "エンドスチール")
        // EvilCraft
        addMaterialKey(IntegrationMaterials.DARK_GEM, "ダークジェム")
        // IE
        addMaterialKey(IntegrationMaterials.HOP_GRAPHITE, "高配向パイログラファイト")
        // Mek
        addMaterialKey(IntegrationMaterials.REFINED_GLOWSTONE, "精製グロウストーン")
        addMaterialKey(IntegrationMaterials.REFINED_OBSIDIAN, "精製黒曜石")
        // Twilight
        addMaterialKey(IntegrationMaterials.CARMINITE, "カーミナイト")
        addMaterialKey(IntegrationMaterials.FIERY_METAL, "灼熱の")
        addMaterialKey(IntegrationMaterials.IRONWOOD, "樹鉄")
        addMaterialKey(IntegrationMaterials.KNIGHTMETAL, "ナイトメタル")
        addMaterialKey(IntegrationMaterials.STEELEAF, "葉鋼")
        // Ragium
        addMaterialKey(RagiumMaterials.ADVANCED_RAGI_ALLOY, "発展ラギ合金")
        addMaterialKey(RagiumMaterials.AZURE_STEEL, "紺鉄")
        addMaterialKey(RagiumMaterials.CRIMSON_CRYSTAL, "深紅の結晶")
        addMaterialKey(RagiumMaterials.DEEP_STEEL, "深層鋼")
        addMaterialKey(RagiumMaterials.RAGI_ALLOY, "ラギ合金")
        addMaterialKey(RagiumMaterials.RAGI_CRYSTAL, "ラギクリスタリル")
        addMaterialKey(RagiumMaterials.RAGINITE, "ラギナイト")
        addMaterialKey(RagiumMaterials.WARPED_CRYSTAL, "歪んだクリスタリル")
        // Vanilla
        addMaterialKey(VanillaMaterials.AMETHYST, "アメシスト")
        addMaterialKey(VanillaMaterials.CALCITE, "方解石")
        addMaterialKey(VanillaMaterials.COAL, "石炭")
        addMaterialKey(VanillaMaterials.COPPER, "銅")
        addMaterialKey(VanillaMaterials.DIAMOND, "ダイアモンド")
        addMaterialKey(VanillaMaterials.EMERALD, "エメラルド")
        addMaterialKey(VanillaMaterials.ENDER_PEARL, "エンダーパール")
        addMaterialKey(VanillaMaterials.GLOWSTONE, "グロウストーン")
        addMaterialKey(VanillaMaterials.GOLD, "金")
        addMaterialKey(VanillaMaterials.IRON, "鉄")
        addMaterialKey(VanillaMaterials.LAPIS, "ラピス")
        addMaterialKey(VanillaMaterials.NETHERITE, "ネザライト")
        addMaterialKey(VanillaMaterials.NETHERITE_SCRAP, "ネザライトの欠片")
        addMaterialKey(VanillaMaterials.OBSIDIAN, "黒曜石")
        addMaterialKey(VanillaMaterials.QUARTZ, "水晶")
        addMaterialKey(VanillaMaterials.REDSTONE, "レッドストーン")
        addMaterialKey(VanillaMaterials.WOOD, "木材")
    }

    private fun tagPrefix() {
        addTagPrefix(HTTagPrefixes.CLUMP, "%sの凝塊")
        addTagPrefix(HTTagPrefixes.CRYSTAL, "%sの結晶")
        addTagPrefix(HTTagPrefixes.DIRTY_DUST, "汚れた%sの粉")
        addTagPrefix(HTTagPrefixes.DUST, "%sの粉")
        addTagPrefix(HTTagPrefixes.GEAR, "%sの歯車")
        addTagPrefix(HTTagPrefixes.GEM, "%s")
        addTagPrefix(HTTagPrefixes.INGOT, "%sインゴット")
        addTagPrefix(HTTagPrefixes.NUGGET, "%sのナゲット")
        addTagPrefix(HTTagPrefixes.ORE, "%s鉱石")
        addTagPrefix(HTTagPrefixes.PLATE, "%s板")
        addTagPrefix(HTTagPrefixes.RAW_MATERIAL, "%sの原石")
        addTagPrefix(HTTagPrefixes.RAW_STORAGE, "%sの原石ブロック")
        addTagPrefix(HTTagPrefixes.ROD, "%s棒")
        addTagPrefix(HTTagPrefixes.SHARD, "%sの欠片")
        addTagPrefix(HTTagPrefixes.SHEETMETAL, "%sの板金")
        addTagPrefix(HTTagPrefixes.STORAGE_BLOCK, "%sブロック")
        addTagPrefix(HTTagPrefixes.TINY_DUST, "小さな%sの粉")
        addTagPrefix(HTTagPrefixes.WIRE, "%sのワイヤー")
    }*/

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
        add(RagiumRecipeTypes.PLANTING, "栽培")
        add(RagiumRecipeTypes.SIMULATING, "シミュレーション")
        add(RagiumRecipeTypes.WASHING, "洗浄")
    }

    private fun text() {
        add(RagiumTranslation.AZURE_STEEL_UPGRADE, "紺鉄強化")
        add(RagiumTranslation.AZURE_STEEL_UPGRADE_APPLIES_TO, "紺鉄の装備品")
        add(RagiumTranslation.AZURE_STEEL_UPGRADE_INGREDIENTS, "紺鉄インゴット")
        add(RagiumTranslation.AZURE_STEEL_UPGRADE_BASE_SLOT_DESCRIPTION, "鉄製の防具，武器，道具を置いてください")
        add(RagiumTranslation.AZURE_STEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, "紺鉄インゴットを置いてください")

        add(RagiumTranslation.DEEP_STEEL_UPGRADE, "深層鋼強化")
        add(RagiumTranslation.DEEP_STEEL_UPGRADE_APPLIES_TO, "深層鋼の装備品")
        add(RagiumTranslation.DEEP_STEEL_UPGRADE_INGREDIENTS, "深層鋼インゴット")
        add(RagiumTranslation.DEEP_STEEL_UPGRADE_BASE_SLOT_DESCRIPTION, "ダイヤモンド製の防具，武器，道具を置いてください")
        add(RagiumTranslation.DEEP_STEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, "深層鋼インゴットを置いてください")

        add(RagiumTranslation.NIGHT_METAL_UPGRADE, "夜金強化")
        add(RagiumTranslation.NIGHT_METAL_UPGRADE_APPLIES_TO, "夜金の装備品")
        add(RagiumTranslation.NIGHT_METAL_UPGRADE_INGREDIENTS, "夜金インゴット")
        add(RagiumTranslation.NIGHT_METAL_UPGRADE_BASE_SLOT_DESCRIPTION, "金製の防具，武器，道具を置いてください")
        add(RagiumTranslation.NIGHT_METAL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, "夜金インゴットを置いてください")

        add(RagiumTranslation.ITEM_POTION, "%sのポーション")

        add(RagiumTranslation.TOOLTIP_EFFECT_RANGE, "有効半径: %s ブロック")
        add(RagiumTranslation.TOOLTIP_ENERGY_PERCENTAGE, "%s / %s FE")
        add(RagiumTranslation.TOOLTIP_EXP_PERCENTAGE, "%s / %s Exp")
        add(RagiumTranslation.TOOLTIP_FLUID_NAME, "%s : %s mb")
        add(RagiumTranslation.TOOLTIP_FLUID_NAME_EMPTY, "空")
        add(RagiumTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT, "常に少なくとも%sがあります")
        add(RagiumTranslation.TOOLTIP_LOOT_TABLE_ID, "ルートテーブル: %s")
        add(RagiumTranslation.TOOLTIP_SHOW_INFO, "シフトキーを押して情報を表示")
        add(RagiumTranslation.TOOLTIP_WIP, "この要素は開発中です！！")

        add(HTAccessConfig.INPUT_ONLY, "モード：搬入")
        add(HTAccessConfig.OUTPUT_ONLY, "モード：搬出")
        add(HTAccessConfig.BOTH, "モード：双方")
        add(HTAccessConfig.DISABLED, "モード：無効")
    }

    private fun information() {
        addInfo(RagiumBlocks.CRIMSON_SOIL, "このブロックの上で倒されたモブは経験値も落とします。")
        addInfo(RagiumBlocks.WARPED_WART, "食べるとランダムにデバフを一つだけ消します。")

        val nonSilkTouch = "シルクタッチなしで回収することが可能です。"
        addInfo(RagiumBlocks.getGlass(VanillaMaterialKeys.OBSIDIAN), "黒曜石とおなじ爆破耐性をもちます。", "また，$nonSilkTouch")
        addInfo(RagiumBlocks.getGlass(VanillaMaterialKeys.QUARTZ), nonSilkTouch)
        addInfo(RagiumBlocks.getGlass(VanillaMaterialKeys.SOUL), "プレイヤーのみ通過できます。", "また，$nonSilkTouch")

        addInfo(RagiumItems.AMBROSIA, "いつでも食べられる上，いくら食べてもなくなりません！")
        addInfo(RagiumItems.BLAST_CHARGE, "作業台で火薬を用いて強化することができます。")
        addInfo(RagiumItems.ELDER_HEART, "エルダーガーディアンからドロップします。")
        addInfo(
            RagiumItems.ELDRITCH_EGG,
            "右クリックで投げることができ，モブに当たるとスポーンエッグになります。",
            "ドロップしたスポーンエッグは直接インベントリに入ります。",
        )
        addInfo(RagiumItems.ICE_CREAM, "食べると鎮火します。")
        addInfo(RagiumItems.RAGI_CHERRY, "リンゴと同様にサクラの葉からドロップします。")
        addInfo(RagiumItems.DYNAMIC_LANTERN, "範囲内の暗所に光源を設置します。")
        addInfo(RagiumItems.MAGNET, "範囲内のドロップアイテムを回収します。")
        addInfo(RagiumItems.SLOT_COVER, "機械のスロットに入れることでレシピ判定から無視されます。")
        addInfo(RagiumItems.TRADER_CATALOG, "行商人からドロップします。")
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

        add(RagiumTranslation.JADE_EXP_STORAGE, "経験値: %s")
    }
}
