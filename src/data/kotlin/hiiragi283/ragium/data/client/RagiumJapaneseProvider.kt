package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumEnchantments
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.data.PackOutput
import net.minecraft.world.item.DyeColor
import net.neoforged.neoforge.common.data.LanguageProvider

class RagiumJapaneseProvider(output: PackOutput) : LanguageProvider(output, RagiumAPI.MOD_ID, "ja_jp") {
    override fun addTranslations() {
        advancement()
        block()
        enchantment()
        entity()
        fluid()
        item()
        material()
        // tagPrefix()
        tag()
        text()

        delight()
        mekanism()
        jade()
        emi()
    }

    private fun advancement() {
        add(RagiumTranslationKeys.ADV_ROOT_DESC, "ラギナイト鉱石からラギナイトの原石を手に入れてRagiumを始めよう")
        addItemAdvDesc(RagiumItems.RAGI_ALLOY_INGOT, "ラギ合金インゴットを手に入れる")
        add(RagiumTranslationKeys.ADV_FORGE_HAMMER_TITLE, "RagiForge")
        add(RagiumTranslationKeys.ADV_FORGE_HAMMER_DESC, "鍛造ハンマーを手に入れる")

        addAdvDesc(RagiumItems.AZURE_SHARD, "アメジストの塊にラピスを使って紺碧の欠片を手に入れる")
        addItemAdvDesc(RagiumItems.AZURE_STEEL_INGOT, "紺鉄インゴットを手に入れる")
    }

    private fun block() {
        addBlock(RagiumBlocks.SILT, "シルト")
        addBlock(RagiumBlocks.MYSTERIOUS_OBSIDIAN, "神秘的な黒曜石")

        addBlock(RagiumBlocks.ASH_LOG, "灰化した原木")
        addBlock(RagiumBlocks.EXP_BERRY_BUSH, "経験値ベリーの茂み")

        RagiumBlocks.RAGINITE_ORES.addTranslationJp("ラギナイト", this)
        RagiumBlocks.RAGI_CRYSTAL_ORES.addTranslationJp("ラギクリスタリル", this)

        addBlock(RagiumBlocks.StorageBlocks.RAGI_ALLOY.holder, "ラギ合金ブロック")
        addBlock(RagiumBlocks.StorageBlocks.ADVANCED_RAGI_ALLOY.holder, "発展ラギ合金ブロック")
        addBlock(RagiumBlocks.StorageBlocks.RAGI_CRYSTAL.holder, "ラギクリスタリルブロック")
        addBlock(RagiumBlocks.StorageBlocks.AZURE_STEEL.holder, "紺鉄ブロック")
        addBlock(RagiumBlocks.StorageBlocks.DEEP_STEEL.holder, "深層鋼ブロック")

        addBlock(RagiumBlocks.StorageBlocks.CRIMSON_CRYSTAL.holder, "深紅の結晶ブロック")
        addBlock(RagiumBlocks.StorageBlocks.WARPED_CRYSTAL.holder, "歪んだ結晶ブロック")

        addBlock(RagiumBlocks.StorageBlocks.CHEESE.holder, "チーズブロック")
        addBlock(RagiumBlocks.StorageBlocks.CHOCOLATE.holder, "チョコレートブロック")

        RagiumBlocks.RAGI_STONE_SETS.addTranslationJp("らぎストーン", this)
        RagiumBlocks.RAGI_STONE_BRICKS_SETS.addTranslationJp("らぎストーンレンガ", this)
        RagiumBlocks.RAGI_STONE_SQUARE_SETS.addTranslationJp("らぎストーン（正方）", this)
        RagiumBlocks.AZURE_TILE_SETS.addTranslationJp("紺碧のタイル", this)
        RagiumBlocks.EMBER_STONE_SETS.addTranslationJp("熾火石", this)
        RagiumBlocks.PLASTIC_SETS.addTranslationJp("プラスチックブロック", this)
        RagiumBlocks.BLUE_NETHER_BRICK_SETS.addTranslationJp("青いネザーレンガ", this)

        addBlock(RagiumBlocks.OBSIDIAN_GLASS, "黒曜石ガラス")
        addBlock(RagiumBlocks.QUARTZ_GLASS, "クォーツガラス")
        addBlock(RagiumBlocks.SOUL_GLASS, "ソウルガラス")

        addBlock(RagiumBlocks.getLedBlock(DyeColor.RED), "LEDブロック（赤）")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.GREEN), "LEDブロック（緑）")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.BLUE), "LEDブロック（青）")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.CYAN), "LEDブロック（シアン）")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.MAGENTA), "LEDブロック（マゼンタ）")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.YELLOW), "LEDブロック（黄色）")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.WHITE), "LEDブロック")

        addBlock(RagiumBlocks.COOKED_MEAT_ON_THE_BONE, "骨付き焼肉ブロック")
        addBlock(RagiumBlocks.SPONGE_CAKE, "スポンジケーキ")
        addBlock(RagiumBlocks.SPONGE_CAKE_SLAB, "スポンジケーキのハーフブロック")
        addBlock(RagiumBlocks.SWEET_BERRIES_CAKE, "スイートベリーケーキ")

        addBlock(RagiumBlocks.ADVANCED_MACHINE_CASING, "発展機械筐体")
        addBlock(RagiumBlocks.DEVICE_CASING, "デバイス筐体")
        addBlock(RagiumBlocks.MACHINE_CASING, "機械筐体")
        addBlock(RagiumBlocks.STONE_CASING, "石材筐体")
        addBlock(RagiumBlocks.WOODEN_CASING, "木材筐体")

        // addBlock(RagiumBlocks.ENERGY_NETWORK_INTERFACE, "E.N.I.")
        // addBlock(RagiumBlocks.TELEPORT_ANCHOR, "テレポートアンカー")
        // Machine
        addBlock(RagiumBlocks.CRUSHER, "粉砕機")
        addBlock(RagiumBlocks.EXTRACTOR, "抽出機")

        addBlock(RagiumBlocks.ADVANCED_CRUSHER, "発展型粉砕機")
        addBlock(RagiumBlocks.ADVANCED_EXTRACTOR, "発展型抽出機")
        addBlock(RagiumBlocks.INFUSER, "注入機")
        addBlock(RagiumBlocks.REFINERY, "精製機")
        // Device
        addBlock(RagiumBlocks.ENI, "E.N.I.")
        addBlock(RagiumBlocks.EXP_COLLECTOR, "経験値収集機")
        addBlock(RagiumBlocks.ITEM_COLLECTOR, "アイテム収集機")
        addBlock(RagiumBlocks.LAVA_COLLECTOR, "溶岩収集機")
        addBlock(RagiumBlocks.MILK_DRAIN, "牛乳シンク")
        addBlock(RagiumBlocks.SOUL_SPIKE, "魂の尖頭")
        addBlock(RagiumBlocks.SPRINKLER, "スプリンクラー")
        addBlock(RagiumBlocks.TELEPORT_ANCHOR, "テレポートアンカー")
        addBlock(RagiumBlocks.WATER_COLLECTOR, "水収集機")
    }

    private fun enchantment() {
        addEnchantment(RagiumEnchantments.CAPACITY, "容量増加", "アイテムや液体ストレージの容量を拡張します。")
    }

    private fun entity() {
        // addEntityType(RagiumEntityTypes.DYNAMITE, "ダイナマイト")
        // addEntityType(RagiumEntityTypes.DEFOLIANT_DYNAMITE, "枯葉剤ダイナマイト")
        // addEntityType(RagiumEntityTypes.FLATTEN_DYNAMITE, "整地用ダイナマイト")
        // addEntityType(RagiumEntityTypes.NAPALM_DYNAMITE, "ナパームダイナマイト")
        // addEntityType(RagiumEntityTypes.POISON_DYNAMITE, "毒ガスダイナマイト")
    }

    private fun fluid() {
        addFluid(RagiumFluidContents.HONEY, "蜂蜜")
        addFluid(RagiumFluidContents.EXPERIENCE, "液体経験値")
        addFluid(RagiumFluidContents.CHOCOLATE, "チョコレート")
        addFluid(RagiumFluidContents.MUSHROOM_STEW, "キノコシチュー")

        addFluid(RagiumFluidContents.HYDROGEN, "水素")

        addFluid(RagiumFluidContents.NITROGEN, "窒素")
        addFluid(RagiumFluidContents.AMMONIA, "アンモニア")
        addFluid(RagiumFluidContents.NITRIC_ACID, "硝酸")
        addFluid(RagiumFluidContents.MIXTURE_ACID, "混酸")

        addFluid(RagiumFluidContents.OXYGEN, "酸素")
        addFluid(RagiumFluidContents.ROCKET_FUEL, "ロケット燃料")

        addFluid(RagiumFluidContents.ALKALI_SOLUTION, "アルカリ溶液")

        addFluid(RagiumFluidContents.SULFUR_DIOXIDE, "二酸化硫黄")
        addFluid(RagiumFluidContents.SULFUR_TRIOXIDE, "三酸化硫黄")
        addFluid(RagiumFluidContents.SULFURIC_ACID, "硫酸")

        addFluid(RagiumFluidContents.CRUDE_OIL, "原油")
        addFluid(RagiumFluidContents.NAPHTHA, "ナフサ")
        addFluid(RagiumFluidContents.FUEL, "燃料")
        addFluid(RagiumFluidContents.NITRO_FUEL, "ニトロ燃料")
        addFluid(RagiumFluidContents.AROMATIC_COMPOUND, "芳香族化合物")

        addFluid(RagiumFluidContents.PLANT_OIL, "植物油")
        addFluid(RagiumFluidContents.BIOMASS, "バイオマス")

        addFluid(RagiumFluidContents.SAP, "樹液")
        addFluid(RagiumFluidContents.CRIMSON_SAP, "深紅の樹液")
        addFluid(RagiumFluidContents.WARPED_SAP, "歪んだ樹液")

        addFluid(RagiumFluidContents.MOLTEN_RAGIUM, "溶融ラギウム")
    }

    private fun item() {
        // Material
        addItem(RagiumItems.RAW_RAGINITE, "ラギナイトの原石")
        addItem(RagiumItems.RAGI_COKE, "らぎコークス")
        addItem(RagiumItems.AZURE_SHARD, "紺碧の欠片")
        addItem(RagiumItems.COMPRESSED_SAWDUST, "圧縮したおがくず")
        addItem(RagiumItems.TAR, "タール")

        addItem(RagiumItems.RAGI_CRYSTAL, "ラギクリスタリル")
        addItem(RagiumItems.CRIMSON_CRYSTAL, "深紅のクリスタル")
        addItem(RagiumItems.WARPED_CRYSTAL, "歪んだクリスタル")

        addItem(RagiumItems.RAGI_ALLOY_COMPOUND, "ラギ合金混合物")
        addItem(RagiumItems.RAGI_ALLOY_INGOT, "ラギ合金インゴット")
        addItem(RagiumItems.ADVANCED_RAGI_ALLOY_COMPOUND, "発展ラギ合金混合物")
        addItem(RagiumItems.ADVANCED_RAGI_ALLOY_INGOT, "発展ラギ合金インゴット")
        addItem(RagiumItems.AZURE_STEEL_COMPOUND, "紺鉄インゴット")
        addItem(RagiumItems.AZURE_STEEL_INGOT, "紺鉄混合物")
        addItem(RagiumItems.DEEP_STEEL_INGOT, "深層鋼インゴット")

        addItem(RagiumItems.ASH_DUST, "灰")
        addItem(RagiumItems.OBSIDIAN_DUST, "黒曜石の粉")
        addItem(RagiumItems.RAGINITE_DUST, "ラギナイトの粉")
        addItem(RagiumItems.SALTPETER_DUST, "硝石の粉")
        addItem(RagiumItems.SAWDUST, "おがくず")
        addItem(RagiumItems.SULFUR_DUST, "硫黄の粉")

        // Armor
        RagiumItems.AZURE_STEEL_ARMORS.addTranslationJp("紺鉄", this)
        // Tool
        RagiumItems.RAGI_ALLOY_TOOLS.addTranslationJp("ラギ合金", this)
        RagiumItems.AZURE_STEEL_TOOLS.addTranslationJp("紺鉄", this)

        addItem(RagiumItems.ENDER_BUNDLE, "エンダーバンドル")
        addItem(RagiumItems.EXP_MAGNET, "EXPマグネット")
        addItem(RagiumItems.ITEM_MAGNET, "アイテムマグネット")
        addItem(RagiumItems.TRADER_CATALOG, "行商人のカタログ")
        addItem(RagiumItems.TELEPORT_TICKET, "テレポートチケット")
        addItem(RagiumItems.RAGI_LANTERN, "らぎランタン")
        addItem(RagiumItems.RAGI_TICKET, "らぎチケット")
        // Food
        addItem(RagiumItems.ICE_CREAM, "アイスクリーム")
        addItem(RagiumItems.ICE_CREAM_SODA, "クリームソーダ")

        addItem(RagiumItems.MINCED_MEAT, "ひき肉")
        addItem(RagiumItems.MEAT_INGOT, "生肉インゴット")
        addItem(RagiumItems.COOKED_MEAT_INGOT, "焼肉インゴット")
        addItem(RagiumItems.CANNED_COOKED_MEAT, "焼肉缶詰")

        addItem(RagiumItems.SWEET_BERRIES_CAKE_PIECE, "一切れのスイートベリーケーキ")
        addItem(RagiumItems.MELON_PIE, "メロンパイ")

        addItem(RagiumItems.RAGI_CHERRY, "らぎチェリー")
        addItem(RagiumItems.RAGI_CHERRY_JAM, "らぎチェリージャム")
        addItem(RagiumItems.FEVER_CHERRY, "フィーバーチェリー")

        addItem(RagiumItems.BOTTLED_BEE, "瓶詰めのハチ")
        addItem(RagiumItems.EXP_BERRIES, "経験値ベリー")
        addItem(RagiumItems.WARPED_WART, "歪んだウォート")
        addItem(RagiumItems.AMBROSIA, "アンブロシア")

        /*addItem(RagiumItems.POTION_BUNDLE, "ポーションバンドル")
        addItem(RagiumItems.DURALUMIN_CASE, "ジュラルミンケース")
        addItem(RagiumItems.SILKY_PICKAXE, "シルキーピッケル")

        addItem(RagiumItems.ITEM_MAGNET, "アイテムマグネット")
        addItem(RagiumItems.EXP_MAGNET, "経験値マグネット")

        addItem(RagiumItems.DYNAMITE, "ダイナマイト")
        addItem(RagiumItems.DEFOLIANT_DYNAMITE, "枯葉剤ダイナマイト")
        addItem(RagiumItems.FLATTEN_DYNAMITE, "整地用ダイナマイト")
        addItem(RagiumItems.NAPALM_DYNAMITE, "ナパームダイナマイト")
        addItem(RagiumItems.POISON_DYNAMITE, "毒ガスダイナマイト")*/
        // Mold
        addItem(RagiumItems.Molds.BLANK, "成形型（なし）")
        addItem(RagiumItems.Molds.BALL, "成形型（ボール）")
        addItem(RagiumItems.Molds.BLOCK, "成形型（ブロック）")
        addItem(RagiumItems.Molds.GEAR, "成形型（歯車）")
        addItem(RagiumItems.Molds.INGOT, "成形型（インゴット）")
        addItem(RagiumItems.Molds.PLATE, "成形型（板材）")
        addItem(RagiumItems.Molds.ROD, "成形型（棒材）")
        addItem(RagiumItems.Molds.WIRE, "成形型（ワイヤー）")
        // Parts
        addItem(RagiumItems.ADVANCED_CIRCUIT, "発展回路")
        addItem(RagiumItems.AQUATIC_RAGIUM_ESSENCE, "海洋ラギウムエッセンス")
        addItem(RagiumItems.BASIC_CIRCUIT, "基本回路")
        addItem(RagiumItems.CHIPPED_RAGIUM_ESSENCE, "ラギウムエッセンスの欠片")
        addItem(RagiumItems.CRYSTAL_PROCESSOR, "クリスタルプロセッサ")
        addItem(RagiumItems.ELDER_HEART, "エルダーの心臓")
        addItem(RagiumItems.ENGINE, "V8エンジン")
        addItem(RagiumItems.INACTIVE_RAGIUM_ESSENCE, "不活性ラギウムエッセンス")
        addItem(RagiumItems.LED, "発光ダイオード")
        addItem(RagiumItems.PLASTIC_PLATE, "プラスチック板")
        addItem(RagiumItems.POLYMER_RESIN, "高分子樹脂")
        addItem(RagiumItems.RAGIUM_ESSENCE, "ラギウムエッセンス")
        addItem(RagiumItems.SOAP, "石鹸")
        addItem(RagiumItems.SOLAR_PANEL, "太陽光パネル")
        addItem(RagiumItems.STONE_BOARD, "石版")

        // addItem(RagiumItems.BLANK_TICKET, "空のチケット")
        // addItem(RagiumItems.TELEPORT_TICKET, "テレポートチケット")
        // addItem(RagiumItems.RAGI_TICKET, "らぎチケット")
    }

    private fun material() {
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
        addMaterialKey(RagiumMaterials.WARPED_CRYSTAL, "歪んだ結晶")
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

    /*private fun tagPrefix() {
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

    private fun tag() {
        // Item
        add(RagiumItemTags.COAL_COKE, "石炭コークス")
        add(RagiumItemTags.PAPER, "紙")
        add(RagiumItemTags.PLASTICS, "プラスチック")
        add(RagiumItemTags.SILICON, "シリコン")
        add(RagiumItemTags.SLAG, "スラグ")
        add(RagiumItemTags.TOOLS_FORGE_HAMMER, "鍛造ハンマー")

        add(RagiumItemTags.CROPS_WARPED_WART, "歪んだウォート")
        add(RagiumItemTags.FOODS_CHEESE, "チーズ")
        add(RagiumItemTags.FOODS_CHOCOLATE, "チョコレート")

        add(RagiumItemTags.CIRCUITS, "回路")
        add(RagiumItemTags.CIRCUITS_ADVANCED, "発展回路")
        add(RagiumItemTags.CIRCUITS_BASIC, "基本回路")
        add(RagiumItemTags.CIRCUITS_ELITE, "精鋭回路")

        add(RagiumItemTags.GLASS_BLOCKS_OBSIDIAN, "黒曜石ガラス")
        add(RagiumItemTags.GLASS_BLOCKS_QUARTZ, "クォーツガラス")

        add(RagiumItemTags.DYNAMITES, "ダイナマイト")
        add(RagiumItemTags.LED_BLOCKS, "LEDブロック")

        add(RagiumItemTags.MOLDS, "成形型")
        add(RagiumItemTags.MOLDS_BALL, "成形型（ボール）")
        add(RagiumItemTags.MOLDS_BLANK, "成形型（なし）")
        add(RagiumItemTags.MOLDS_BLOCK, "成形型（ブロック）")
        add(RagiumItemTags.MOLDS_GEAR, "成形型（歯車）")
        add(RagiumItemTags.MOLDS_INGOT, "成形型（インゴット）")
        add(RagiumItemTags.MOLDS_PLATE, "成形型（板材）")
        add(RagiumItemTags.MOLDS_ROD, "成形型（棒材）")
        add(RagiumItemTags.MOLDS_WIRE, "成形型（ワイヤー）")

        add(RagiumItemTags.DIRT_SOILS, "土壌")
        add(RagiumItemTags.END_SOILS, "エンドの土壌")
        add(RagiumItemTags.MUSHROOM_SOILS, "キノコの土壌")
        add(RagiumItemTags.NETHER_SOILS, "ネザーの土壌")
        // Fluid
        add(RagiumFluidTags.CHOCOLATES, "チョコレート")
        add(RagiumFluidTags.CREOSOTE, "クレオソート")
        add(RagiumFluidTags.MEAT, "液体肉")
        add(RagiumFluidTags.STEAM, "蒸気")

        add(RagiumFluidTags.NITRO_FUEL, "ニトロ系燃料")
        add(RagiumFluidTags.NON_NITRO_FUEL, "非ニトロ系燃料")
        add(RagiumFluidTags.THERMAL_FUEL, "発熱燃料")
    }

    private fun text() {
        add(RagiumTranslationKeys.TEXT_FLUID_NAME, "%s: %s mb")
        add(RagiumTranslationKeys.TEXT_FLUID_CAPACITY, "容量: %s mb")

        add(RagiumTranslationKeys.TEXT_EFFECT_RANGE, "有効半径: %s ブロック")
    }

    private fun delight() {
        addItem(RagiumDelightAddon.RAGI_CHERRY_PULP, "らぎチェリーの果肉")
    }

    private fun mekanism() {
        add(RagiumMekanismAddon.CHEMICAL_RAGINITE.translationKey, "ラギナイト")
        add(RagiumMekanismAddon.CHEMICAL_AZURE.translationKey, "紺碧エッセンス")
        add(
            RagiumMekanismAddon.CHEMICAL_RAGINITE_SLURRY.cleanSlurry
                .get()
                .translationKey,
            "純粋なラギナイトの懸濁液",
        )
        add(RagiumMekanismAddon.CHEMICAL_RAGINITE_SLURRY.get().translationKey, "汚れたラギナイトの懸濁液")

        addItem(RagiumMekanismAddon.ITEM_ENRICHED_RAGINITE, "濃縮ラギナイト")
        addItem(RagiumMekanismAddon.ITEM_DIRTY_RAGINITE_DUST, "汚れたラギナイトの粉")
        addItem(RagiumMekanismAddon.ITEM_RAGINITE_CLUMP, "ラギナイトの凝塊")
        addItem(RagiumMekanismAddon.ITEM_RAGINITE_SHARD, "ラギナイトの欠片")
        addItem(RagiumMekanismAddon.ITEM_RAGINITE_CRYSTAL, "ラギナイトの結晶")

        addItem(RagiumMekanismAddon.ITEM_ENRICHED_AZURE, "濃縮紺碧エッセンス")
    }

    private fun jade() {
        add("config.jade.plugin_ragium.advanced_crusher", "発展型粉砕機")
        add("config.jade.plugin_ragium.advanced_extractor", "発展型抽出機")
        add("config.jade.plugin_ragium.crusher", "粉砕機")
        add("config.jade.plugin_ragium.enchantable_block", "エンチャント可能なブロック")
        add("config.jade.plugin_ragium.extractor", "抽出機")
    }

    private fun emi() {
        add(RagiumTranslationKeys.EMI_AMBROSIA, "いつでも食べられる上，いくら食べてもなくなりません！")
        add(RagiumTranslationKeys.EMI_ASH_LOG, "壊すと灰の粉が手に入ります。")
        add(RagiumTranslationKeys.EMI_HARVESTABLE_GLASS, "このガラスはシルクタッチなしで回収することが可能です。")
        add(RagiumTranslationKeys.EMI_ICE_CREAM, "食べると鎮火します。")
        add(RagiumTranslationKeys.EMI_ITEM_MAGNET, "範囲内のドロップアイテムを回収します。")
        add(RagiumTranslationKeys.EMI_OBSIDIAN_GLASS, "黒曜石とおなじ爆破耐性をもちます。")
        add(RagiumTranslationKeys.EMI_RAGI_CHERRY, "リンゴと同様にサクラの葉からドロップします。")
        add(RagiumTranslationKeys.EMI_SOUL_GLASS, "プレイヤーのみ通過できます。")
        add(RagiumTranslationKeys.EMI_TRADER_CATALOG, "行商人を倒すことでも入手できます。")
        add(RagiumTranslationKeys.EMI_WARPED_WART, "食べるとランダムにデバフを一つだけ消します。")
    }
}
