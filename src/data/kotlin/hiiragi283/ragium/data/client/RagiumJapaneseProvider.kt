package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.addEnchantment
import hiiragi283.ragium.api.extension.addMaterialKey
import hiiragi283.ragium.api.extension.addOreVariant
import hiiragi283.ragium.api.extension.addTagPrefix
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumEnchantments
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.data.PackOutput
import net.minecraft.world.item.DyeColor
import net.neoforged.neoforge.common.data.LanguageProvider

class RagiumJapaneseProvider(output: PackOutput) : LanguageProvider(output, RagiumAPI.MOD_ID, "ja_jp") {
    override fun addTranslations() {
        block()
        enchantment()
        entity()
        // fluid()
        item()
        material()
        tagPrefix()
        tag()
        tooltips()
        misc()

        mekanism()
        jade()
    }

    private fun block() {
        addBlock(RagiumBlocks.SILT, "シルト")

        RagiumBlocks.RAGI_BRICK_SETS.addTranslationJp("らぎレンガ", this)
        RagiumBlocks.AZURE_TILE_SETS.addTranslationJp("紺碧のタイル", this)
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

        addBlock(RagiumBlocks.SPONGE_CAKE, "スポンジケーキ")
        addBlock(RagiumBlocks.SPONGE_CAKE_SLAB, "スポンジケーキのハーフブロック")
        addBlock(RagiumBlocks.SWEET_BERRIES_CAKE, "スイートベリーケーキ")

        addBlock(RagiumBlocks.DEVICE_CASING, "デバイス筐体")
        addBlock(RagiumBlocks.MACHINE_CASING, "機械筐体")
        addBlock(RagiumBlocks.STONE_CASING, "石材筐体")
        addBlock(RagiumBlocks.WOODEN_CASING, "木材筐体")

        // addBlock(RagiumBlocks.ENERGY_NETWORK_INTERFACE, "E.N.I.")
        // addBlock(RagiumBlocks.TELEPORT_ANCHOR, "テレポートアンカー")

        addBlock(RagiumBlocks.CRUSHER, "粉砕機")
    }

    private fun enchantment() {
        addEnchantment(RagiumEnchantments.CAPACITY, "容量増加", "アイテムや液体ストレージの容量を拡張します")
    }

    private fun entity() {
        // addEntityType(RagiumEntityTypes.DYNAMITE, "ダイナマイト")
        // addEntityType(RagiumEntityTypes.DEFOLIANT_DYNAMITE, "枯葉剤ダイナマイト")
        // addEntityType(RagiumEntityTypes.FLATTEN_DYNAMITE, "整地用ダイナマイト")
        // addEntityType(RagiumEntityTypes.NAPALM_DYNAMITE, "ナパームダイナマイト")
        // addEntityType(RagiumEntityTypes.POISON_DYNAMITE, "毒ガスダイナマイト")
    }

    /*private fun fluid() {
        addFluid(RagiumFluids.GLASS, "溶融ガラス")
        addFluid(RagiumFluids.HONEY, "蜂蜜")
        addFluid(RagiumFluids.SNOW, "粉雪")

        addFluid(RagiumVirtualFluids.CHOCOLATE, "チョコレート")
        addFluid(RagiumVirtualFluids.MUSHROOM_STEW, "キノコシチュー")

        addFluid(RagiumVirtualFluids.AIR, "空気")
        addFluid(RagiumVirtualFluids.STEAM, "蒸気")

        addFluid(RagiumVirtualFluids.HYDROGEN, "水素")

        addFluid(RagiumVirtualFluids.NITROGEN, "窒素")
        addFluid(RagiumVirtualFluids.AMMONIA, "アンモニア")
        addFluid(RagiumVirtualFluids.NITRIC_ACID, "硝酸")
        addFluid(RagiumVirtualFluids.MIXTURE_ACID, "混酸")

        addFluid(RagiumVirtualFluids.OXYGEN, "酸素")
        addFluid(RagiumVirtualFluids.ROCKET_FUEL, "ロケット燃料")

        addFluid(RagiumVirtualFluids.HYDROFLUORIC_ACID, "フッ化水素酸")

        addFluid(RagiumVirtualFluids.ALKALI_SOLUTION, "アルカリ溶液")

        addFluid(RagiumVirtualFluids.ALUMINA_SOLUTION, "アルミナ溶液")

        addFluid(RagiumVirtualFluids.SULFUR_DIOXIDE, "二酸化硫黄")
        addFluid(RagiumVirtualFluids.SULFUR_TRIOXIDE, "三酸化硫黄")
        addFluid(RagiumVirtualFluids.SULFURIC_ACID, "硫酸")

        addFluid(RagiumVirtualFluids.NAPHTHA, "ナフサ")
        addFluid(RagiumVirtualFluids.FUEL, "燃料")
        addFluid(RagiumVirtualFluids.NITRO_FUEL, "ニトロ燃料")

        addFluid(RagiumVirtualFluids.AROMATIC_COMPOUND, "芳香族化合物")

        addFluid(RagiumVirtualFluids.PLANT_OIL, "植物油")
        addFluid(RagiumVirtualFluids.BIOMASS, "バイオマス")
        addFluid(RagiumVirtualFluids.ETHANOL, "エタノール")

        addFluid(RagiumVirtualFluids.CRUDE_BIODIESEL, "未精製のバイオディーゼル")
        addFluid(RagiumVirtualFluids.BIODIESEL, "バイオディーゼル")
        addFluid(RagiumVirtualFluids.GLYCEROL, "グリセロール")
        addFluid(RagiumVirtualFluids.NITROGLYCERIN, "ニトログリセリン")

        addFluid(RagiumVirtualFluids.SAP, "樹液")
        addFluid(RagiumVirtualFluids.CRIMSON_SAP, "深紅の樹液")
        addFluid(RagiumVirtualFluids.WARPED_SAP, "歪んだ樹液")
        addFluid(RagiumVirtualFluids.LATEX, "ラテックス")

        addFluid(RagiumVirtualFluids.LIQUID_GLOW, "リキッド・グロウ")
        addFluid(RagiumVirtualFluids.RAGIUM_SOLUTION, "ラギウム溶液")
    }*/

    private fun item() {
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
        addItem(RagiumItems.MEAT_SANDWICH, "ミートサンドイッチ")

        addItem(RagiumItems.WARPED_WART, "歪んだウォート")

        addItem(RagiumItems.AMBROSIA, "アンブロシア")

        /*addItem(RagiumItems.DIVING_GOGGLE, "ダイビングゴーグル")
        addItem(RagiumItems.JETPACK, "ジェットパック")
        addItem(RagiumItems.CLIMBING_LEGGINGS, "登山用レギンス")
        addItem(RagiumItems.SLIME_BOOTS, "スライムブーツ")

        RagiumItems.AZURE_STEEL_ARMORS.addTranslationJp("紺鉄", this)
        RagiumItems.DURALUMIN_ARMORS.addTranslationJp("ジュラルミン", this)

        addItem(RagiumItems.FEVER_PICKAXE, "フィーバーピッケル")
        addItem(RagiumItems.RAGI_LANTERN, "らぎランタン")
        addItem(RagiumItems.SILKY_PICKAXE, "シルキーピッケル")

        RagiumItems.RAGI_ALLOY_TOOLS.addTranslationJp("ラギ合金", this)
        RagiumItems.AZURE_STEEL_TOOLS.addTranslationJp("紺鉄", this)
        RagiumItems.DURALUMIN_TOOLS.addTranslationJp("ジュラルミン", this)

        addItem(RagiumItems.POTION_BUNDLE, "ポーションバンドル")
        addItem(RagiumItems.DURALUMIN_CASE, "ジュラルミンケース")

        addItem(RagiumItems.ITEM_MAGNET, "アイテムマグネット")
        addItem(RagiumItems.EXP_MAGNET, "経験値マグネット")

        addItem(RagiumItems.DYNAMITE, "ダイナマイト")
        addItem(RagiumItems.DEFOLIANT_DYNAMITE, "枯葉剤ダイナマイト")
        addItem(RagiumItems.FLATTEN_DYNAMITE, "整地用ダイナマイト")
        addItem(RagiumItems.NAPALM_DYNAMITE, "ナパームダイナマイト")
        addItem(RagiumItems.POISON_DYNAMITE, "毒ガスダイナマイト")

        addItem(RagiumItems.BLANK_PRESS_MOLD, "プレス型（なし）")
        addItem(RagiumItems.BALL_PRESS_MOLD, "プレス型（ボール）")
        addItem(RagiumItems.BLOCK_PRESS_MOLD, "プレス型（ブロック）")
        addItem(RagiumItems.GEAR_PRESS_MOLD, "プレス型（歯車）")
        addItem(RagiumItems.INGOT_PRESS_MOLD, "プレス型（インゴット）")
        addItem(RagiumItems.PLATE_PRESS_MOLD, "プレス型（板材）")
        addItem(RagiumItems.ROD_PRESS_MOLD, "プレス型（棒材）")
        addItem(RagiumItems.WIRE_PRESS_MOLD, "プレス型（ワイヤー）")

        addItem(RagiumItems.BASIC_CIRCUIT, "基本回路")
        addItem(RagiumItems.ADVANCED_CIRCUIT, "発展回路")
        addItem(RagiumItems.ELITE_CIRCUIT, "精鋭回路")

        addItem(RagiumItems.REDSTONE_LENS, "レッドストーンレンズ")
        addItem(RagiumItems.GLOWSTONE_LENS, "グロウストーンレンズ")
        addItem(RagiumItems.DIAMOND_LENS, "ダイヤモンドレンズ")
        addItem(RagiumItems.EMERALD_LENS, "エメラルドレンズ")
        addItem(RagiumItems.AMETHYST_LENS, "アメシストレンズ")*/

        addItem(RagiumItems.AZURE_STEEL_COMPOUND, "紺鉄混合物")
        // addItem(RagiumItems.BEE_WAX, "蜜蠟")
        // addItem(RagiumItems.CIRCUIT_BOARD, "回路基板")
        // addItem(RagiumItems.CRUDE_OIL_BUCKET, "原油入りバケツ")
        addItem(RagiumItems.ENGINE, "V8エンジン")
        addItem(RagiumItems.LED, "L.E.D.")
        addItem(RagiumItems.PLASTIC_PLATE, "プラスチック板")
        addItem(RagiumItems.POLYMER_RESIN, "高分子樹脂")
        addItem(RagiumItems.RAGI_ALLOY_COMPOUND, "ラギ合金混合物")
        addItem(RagiumItems.SOAP, "石鹸")
        addItem(RagiumItems.SOLAR_PANEL, "太陽光パネル")
        addItem(RagiumItems.STONE_BOARD, "石版")
        addItem(RagiumItems.TAR, "タール")
        addItem(RagiumItems.YELLOW_CAKE, "イエローケーキ")
        addItem(RagiumItems.YELLOW_CAKE_PIECE, "一切れのイエローケーキ")

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
        addMaterialKey(RagiumMaterials.RAGIUM, "ラギウム")
        addMaterialKey(RagiumMaterials.WARPED_CRYSTAL, "歪んだ結晶")
        // Vanilla
        addMaterialKey(VanillaMaterials.AMETHYST, "アメシスト")
        addMaterialKey(VanillaMaterials.CALCITE, "方解石")
        addMaterialKey(VanillaMaterials.COAL, "石炭")
        addMaterialKey(VanillaMaterials.COPPER, "銅")
        addMaterialKey(VanillaMaterials.DIAMOND, "ダイアモンド")
        addMaterialKey(VanillaMaterials.EMERALD, "エメラルド")
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
        addTagPrefix(HTTagPrefix.CLUMP, "%sの凝塊")
        addTagPrefix(HTTagPrefix.CRYSTAL, "%sの結晶")
        addTagPrefix(HTTagPrefix.DIRTY_DUST, "汚れた%sの粉")
        addTagPrefix(HTTagPrefix.DUST, "%sの粉")
        addTagPrefix(HTTagPrefix.GEAR, "%sの歯車")
        addTagPrefix(HTTagPrefix.GEM, "%s")
        addTagPrefix(HTTagPrefix.INGOT, "%sインゴット")
        addTagPrefix(HTTagPrefix.NUGGET, "%sのナゲット")
        addTagPrefix(HTTagPrefix.ORE, "%s鉱石")
        addTagPrefix(HTTagPrefix.PLATE, "%s板")
        addTagPrefix(HTTagPrefix.RAW_MATERIAL, "%sの原石")
        addTagPrefix(HTTagPrefix.RAW_STORAGE, "%sの原石ブロック")
        addTagPrefix(HTTagPrefix.ROD, "%s棒")
        addTagPrefix(HTTagPrefix.SHARD, "%sの欠片")
        addTagPrefix(HTTagPrefix.SHEETMETAL, "%sの板金")
        addTagPrefix(HTTagPrefix.STORAGE_BLOCK, "%sブロック")
        addTagPrefix(HTTagPrefix.TINY_DUST, "小さな%sの粉")
        addTagPrefix(HTTagPrefix.WIRE, "%sのワイヤー")
    }

    private fun tag() {
        // Item
        add(RagiumItemTags.COAL_COKE, "石炭コークス")
        add(RagiumItemTags.PAPER, "紙")
        add(RagiumItemTags.PLASTICS, "プラスチック")
        add(RagiumItemTags.SILICON, "シリコン")
        add(RagiumItemTags.SLAG, "スラグ")

        add(RagiumItemTags.CROPS_WARPED_WART, "歪んだウォート")
        add(RagiumItemTags.FLOURS, "小麦粉")
        add(RagiumItemTags.FOOD_CHOCOLATE, "チョコレート")
        add(RagiumItemTags.FOOD_DOUGH, "生地")

        add(RagiumItemTags.CIRCUITS, "回路")
        add(RagiumItemTags.CIRCUITS_ADVANCED, "発展回路")
        add(RagiumItemTags.CIRCUITS_BASIC, "基本回路")
        add(RagiumItemTags.CIRCUITS_ELITE, "精鋭回路")

        add(RagiumItemTags.GLASS_BLOCKS_OBSIDIAN, "黒曜石ガラス")
        add(RagiumItemTags.GLASS_BLOCKS_QUARTZ, "クォーツガラス")

        add(RagiumItemTags.DYNAMITES, "ダイナマイト")
        add(RagiumItemTags.LED_BLOCKS, "LEDブロック")

        add(RagiumItemTags.MOLDS, "プレス型")
        add(RagiumItemTags.MOLDS_BALL, "プレス型（ボール）")
        add(RagiumItemTags.MOLDS_BLOCK, "プレス型（ブロック）")
        add(RagiumItemTags.MOLDS_GEAR, "プレス型（歯車）")
        add(RagiumItemTags.MOLDS_INGOT, "プレス型（インゴット）")
        add(RagiumItemTags.MOLDS_PLATE, "プレス型（板材）")
        add(RagiumItemTags.MOLDS_ROD, "プレス型（棒材）")
        add(RagiumItemTags.MOLDS_WIRE, "プレス型（ワイヤー）")

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

    private fun tooltips() {}

    private fun misc() {
        // Ore Variant
        addOreVariant(HTOreVariant.OVERWORLD, "%s鉱石")
        addOreVariant(HTOreVariant.DEEPSLATE, "深層%s鉱石")
        addOreVariant(HTOreVariant.NETHER, "ネザー%s鉱石")
        addOreVariant(HTOreVariant.END, "エンド%s鉱石")
    }

    private fun mekanism() {
        // add(RagiumMekAddon.RAGINITE_SLURRY.cleanSlurry.translationKey, "純粋なラギナイトの懸濁液")
        // add(RagiumMekAddon.RAGINITE_SLURRY.dirtySlurry.translationKey, "汚れたラギナイトの懸濁液")
    }

    private fun jade() {
        add("config.jade.plugin_ragium.boiler", "ボイラー")
        add("config.jade.plugin_ragium.enchantable_block", "エンチャント可能なブロック")
        add("config.jade.plugin_ragium.energy_network", "エネルギーネットワーク")
        add("config.jade.plugin_ragium.error_message", "エラーメッセージ")
        add("config.jade.plugin_ragium.firebox", "火室")
        add("config.jade.plugin_ragium.heat_source", "熱源")
        add("config.jade.plugin_ragium.machine_info", "機械の情報")
        add("config.jade.plugin_ragium.rock_generator", "岩石生成機")
        add("config.jade.plugin_ragium.steam_furnace", "蒸気かまど")
    }
}
