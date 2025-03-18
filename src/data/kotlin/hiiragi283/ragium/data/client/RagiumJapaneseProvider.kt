package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.add
import hiiragi283.ragium.api.extension.addEnchantment
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

        addBlock(RagiumBlocks.SPONGE_CAKE, "スポンジケーキ")
        addBlock(RagiumBlocks.SPONGE_CAKE_SLAB, "スポンジケーキのハーフブロック")
        addBlock(RagiumBlocks.SWEET_BERRIES_CAKE, "スイートベリーケーキ")

        addBlock(RagiumBlocks.DEVICE_CASING, "デバイス筐体")
        addBlock(RagiumBlocks.MACHINE_CASING, "機械筐体")
        addBlock(RagiumBlocks.STONE_CASING, "石材筐体")
        addBlock(RagiumBlocks.WOODEN_CASING, "木材筐体")

        // addBlock(RagiumBlocks.ENERGY_NETWORK_INTERFACE, "E.N.I.")
        // addBlock(RagiumBlocks.TELEPORT_ANCHOR, "テレポートアンカー")

        addBlock(RagiumBlocks.getLedBlock(DyeColor.RED), "LEDブロック（赤）")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.GREEN), "LEDブロック（緑）")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.BLUE), "LEDブロック（青）")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.CYAN), "LEDブロック（シアン）")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.MAGENTA), "LEDブロック（マゼンタ）")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.YELLOW), "LEDブロック（黄色）")
        addBlock(RagiumBlocks.getLedBlock(DyeColor.WHITE), "LEDブロック")
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
        add(CommonMaterials.COAL_COKE, "石炭コークス")
        add(CommonMaterials.CONSTANTAN, "コンスタンタン")
        add(CommonMaterials.CRYOLITE, "氷晶石")
        add(CommonMaterials.ELECTRUM, "琥珀金")
        add(CommonMaterials.FLUORITE, "蛍石")
        add(CommonMaterials.INVAR, "不変鋼")
        add(CommonMaterials.IRIDIUM, "イリジウム")
        add(CommonMaterials.LEAD, "鉛")
        add(CommonMaterials.NICKEL, "ニッケル")
        add(CommonMaterials.NIOBIUM, "ニオブ")
        add(CommonMaterials.OSMIUM, "オスミウム")
        add(CommonMaterials.PERIDOT, "ペリドット")
        add(CommonMaterials.PLATINUM, "白金")
        add(CommonMaterials.PLUTONIUM, "プルトニウム")
        add(CommonMaterials.PYRITE, "金")
        add(CommonMaterials.RUBY, "ルビー")
        add(CommonMaterials.SALT, "塩")
        add(CommonMaterials.SALTPETER, "硝石")
        add(CommonMaterials.SAPPHIRE, "サファイア")
        add(CommonMaterials.SILICON, "シリコン")
        add(CommonMaterials.SILVER, "銀")
        add(CommonMaterials.SOLDERING_ALLOY, "半田合金")
        add(CommonMaterials.STAINLESS_STEEL, "ステンレス鋼")
        add(CommonMaterials.STEEL, "スチール")
        add(CommonMaterials.SULFUR, "硫黄")
        add(CommonMaterials.TIN, "スズ")
        add(CommonMaterials.SUPERCONDUCTOR, "超伝導体")
        add(CommonMaterials.TITANIUM, "チタン")
        add(CommonMaterials.TUNGSTEN, "タングステン")
        add(CommonMaterials.URANIUM, "ウラニウム")
        add(CommonMaterials.ZINC, "亜鉛")
        // AA
        add(IntegrationMaterials.BLACK_QUARTZ, "黒水晶")
        // AE2
        add(IntegrationMaterials.CERTUS_QUARTZ, "ケルタスクォーツ")
        add(IntegrationMaterials.FLUIX, "フルーシュ")
        // Create
        add(IntegrationMaterials.ANDESITE_ALLOY, "安山岩合金")
        add(IntegrationMaterials.CARDBOARD, "段ボール")
        add(IntegrationMaterials.ROSE_QUARTZ, "ローズクォーツ")
        // EIO
        add(IntegrationMaterials.COPPER_ALLOY, "銅合金")
        add(IntegrationMaterials.ENERGETIC_ALLOY, "エナジェティック合金")
        add(IntegrationMaterials.VIBRANT_ALLOY, "ヴァイブラント合金")
        add(IntegrationMaterials.REDSTONE_ALLOY, "レッドストーン合金")
        add(IntegrationMaterials.CONDUCTIVE_ALLOY, "伝導合金")
        add(IntegrationMaterials.PULSATING_ALLOY, "脈動合金")
        add(IntegrationMaterials.DARK_STEEL, "ダークスチール")
        add(IntegrationMaterials.SOULARIUM, "ソウラリウム")
        add(IntegrationMaterials.END_STEEL, "エンドスチール")
        // EvilCraft
        add(IntegrationMaterials.DARK_GEM, "ダークジェム")
        // IE
        add(IntegrationMaterials.HOP_GRAPHITE, "高配向パイログラファイト")
        // Mek
        add(IntegrationMaterials.REFINED_GLOWSTONE, "精製グロウストーン")
        add(IntegrationMaterials.REFINED_OBSIDIAN, "精製黒曜石")
        // Twilight
        add(IntegrationMaterials.CARMINITE, "カーミナイト")
        add(IntegrationMaterials.FIERY_METAL, "灼熱の")
        add(IntegrationMaterials.IRONWOOD, "樹鉄")
        add(IntegrationMaterials.KNIGHTMETAL, "ナイトメタル")
        add(IntegrationMaterials.STEELEAF, "葉鋼")
        // Ragium
        add(RagiumMaterials.ADVANCED_RAGI_ALLOY, "発展ラギ合金")
        add(RagiumMaterials.AZURE_STEEL, "紺鉄")
        add(RagiumMaterials.CRIMSON_CRYSTAL, "深紅の結晶")
        add(RagiumMaterials.DEEP_STEEL, "深層鋼")
        add(RagiumMaterials.RAGI_ALLOY, "ラギ合金")
        add(RagiumMaterials.RAGI_CRYSTAL, "ラギクリスタリル")
        add(RagiumMaterials.RAGINITE, "ラギナイト")
        add(RagiumMaterials.RAGIUM, "ラギウム")
        add(RagiumMaterials.WARPED_CRYSTAL, "歪んだ結晶")
        // Vanilla
        add(VanillaMaterials.AMETHYST, "アメシスト")
        add(VanillaMaterials.CALCITE, "方解石")
        add(VanillaMaterials.COAL, "石炭")
        add(VanillaMaterials.COPPER, "銅")
        add(VanillaMaterials.DIAMOND, "ダイアモンド")
        add(VanillaMaterials.EMERALD, "エメラルド")
        add(VanillaMaterials.GLOWSTONE, "グロウストーン")
        add(VanillaMaterials.GOLD, "金")
        add(VanillaMaterials.IRON, "鉄")
        add(VanillaMaterials.LAPIS, "ラピス")
        add(VanillaMaterials.NETHERITE, "ネザライト")
        add(VanillaMaterials.NETHERITE_SCRAP, "ネザライトの欠片")
        add(VanillaMaterials.OBSIDIAN, "黒曜石")
        add(VanillaMaterials.QUARTZ, "水晶")
        add(VanillaMaterials.REDSTONE, "レッドストーン")
        add(VanillaMaterials.WOOD, "木材")
    }

    private fun tagPrefix() {
        add(HTTagPrefix.BLOCK, "%sブロック")
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
        add(HTTagPrefix.TINY_DUST, "小さな%sの粉")
        add(HTTagPrefix.WIRE, "%sのワイヤー")
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
        add(HTOreVariant.OVERWORLD, "%s鉱石")
        add(HTOreVariant.DEEPSLATE, "深層%s鉱石")
        add(HTOreVariant.NETHER, "ネザー%s鉱石")
        add(HTOreVariant.END, "エンド%s鉱石")
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
