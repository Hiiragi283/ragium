package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.RagiumAdvancements
import hiiragi283.ragium.api.extension.addAdvancement
import hiiragi283.ragium.api.extension.addEnchantment
import hiiragi283.ragium.api.extension.addFluid
import hiiragi283.ragium.api.extension.addInfo
import hiiragi283.ragium.api.extension.addItemGroup
import hiiragi283.ragium.api.extension.addMatterType
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.integration.replication.RagiumReplicationAddon
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumEnchantments
import hiiragi283.ragium.setup.RagiumEntityTypes
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.LanguageProvider

class RagiumJapaneseProvider(output: PackOutput) : LanguageProvider(output, RagiumAPI.MOD_ID, "ja_jp") {
    override fun addTranslations() {
        advancement()
        block()
        enchantment()
        entity()
        fluid()
        item()
        itemGroup()
        modTags()
        text()
        information()

        delight()
        jade()
        mekanism()
        replication()
    }

    private fun advancement() {
        addAdvancement(RagiumAdvancements.ROOT, "Ragium", "Welcome to ようこそ Ragium へ！")
        addAdvancement(RagiumAdvancements.ETERNAL_TICKET, "さあ，地獄を楽しみな！", "ツールを不可壊にするために永遠のチケットを手に入れる")
        // Raginite
        addAdvancement(RagiumAdvancements.RAGINITE, "0xFF003F", "地下にあるラギナイト鉱石からラギナイトの粉を手に入れる")
        addAdvancement(RagiumAdvancements.RAGI_ALLOY, "赤い彗星", "ラギ合金インゴットを手に入れる")
        addAdvancement(RagiumAdvancements.RAGI_CRYSTAL, "エナジウムではない", "ラギクリスタリルを手に入れる")
        addAdvancement(RagiumAdvancements.RAGI_TICKET, "古き良きあの頃", "らぎチケットを手に入れてお宝チェストを開く")
        // Azure
        addAdvancement(RagiumAdvancements.AZURE_TICKET, "チケットは青かった", "紺碧のチケットを手に入れる")
        addAdvancement(RagiumAdvancements.AZURE_SHARD, "ラズライトではない", "アメジストの塊にラピスを使って紺碧の欠片を手に入れる")
        addAdvancement(RagiumAdvancements.AZURE_GEARS, "Wake up! Azure Dragon!", "紺鉄インゴットで作られたツールか装備を手に入れる")
        // Crimson
        addAdvancement(RagiumAdvancements.CRIMSON_CRYSTAL, "血と汗と樹液の結晶", "深紅の結晶を手に入れる")
        addAdvancement(RagiumAdvancements.CRIMSON_SOIL, "バラが赤い理由", "ソウルソイルに血塗られたチケットを使って深紅の土壌を手に入れる")
        // Warped
        addAdvancement(RagiumAdvancements.WARPED_CRYSTAL, "不正な歪み", "歪んだ結晶を手に入れる")
        addAdvancement(RagiumAdvancements.TELEPORT_TICKET, "片道切符", "テレポートアンカーに紐づいたテレポートチケットを使う")
        // Eldritch
        addAdvancement(RagiumAdvancements.ELDRITCH_PEARL, "始原ではない", "異質な真珠を手に入れる")
        addAdvancement(RagiumAdvancements.MYSTERIOUS_OBSIDIAN, "隕石を落としているのは誰？", "泣く黒曜石に異質なチケットを使って神秘的な黒曜石を手に入れる")
    }

    private fun block() {
        addBlock(RagiumBlocks.SILT, "シルト")
        addBlock(RagiumBlocks.MYSTERIOUS_OBSIDIAN, "神秘的な黒曜石")
        addBlock(RagiumBlocks.CRIMSON_SOIL, "深紅の土壌")

        addBlock(RagiumBlocks.ASH_LOG, "灰化した原木")
        addBlock(RagiumBlocks.EXP_BERRY_BUSH, "経験値ベリーの茂み")
        addBlock(RagiumBlocks.WARPED_WART, "歪んだウォート")

        addBlock(RagiumBlocks.RaginiteOres.STONE, "ラギナイト鉱石")
        addBlock(RagiumBlocks.RaginiteOres.DEEP, "深層ラギナイト鉱石")
        addBlock(RagiumBlocks.RaginiteOres.NETHER, "ネザーラギナイト鉱石")
        addBlock(RagiumBlocks.RaginiteOres.END, "エンドラギナイト鉱石")

        addBlock(RagiumBlocks.RagiCrystalOres.STONE, "ラギクリスタリル鉱石")
        addBlock(RagiumBlocks.RagiCrystalOres.DEEP, "深層ラギクリスタリル鉱石")
        addBlock(RagiumBlocks.RagiCrystalOres.NETHER, "ネザーラギクリスタリル鉱石")
        addBlock(RagiumBlocks.RagiCrystalOres.END, "エンドラギクリスタリル鉱石")

        addBlock(RagiumBlocks.RESONANT_DEBRIS, "共振の残骸")

        addBlock(RagiumBlocks.StorageBlocks.RAGI_CRYSTAL, "ラギクリスタリルブロック")
        addBlock(RagiumBlocks.StorageBlocks.CRIMSON_CRYSTAL, "深紅の結晶ブロック")
        addBlock(RagiumBlocks.StorageBlocks.WARPED_CRYSTAL, "歪んだ結晶ブロック")
        addBlock(RagiumBlocks.StorageBlocks.ELDRITCH_PEARL, "異質な真珠ブロック")

        addBlock(RagiumBlocks.StorageBlocks.RAGI_ALLOY, "ラギ合金ブロック")
        addBlock(RagiumBlocks.StorageBlocks.ADVANCED_RAGI_ALLOY, "発展ラギ合金ブロック")
        addBlock(RagiumBlocks.StorageBlocks.AZURE_STEEL, "紺鉄ブロック")
        addBlock(RagiumBlocks.StorageBlocks.DEEP_STEEL, "深層鋼ブロック")

        addBlock(RagiumBlocks.StorageBlocks.CHOCOLATE, "チョコレートブロック")
        addBlock(RagiumBlocks.StorageBlocks.MEAT, "肉ブロック")
        addBlock(RagiumBlocks.StorageBlocks.COOKED_MEAT, "焼肉ブロック")

        RagiumBlocks.RAGI_STONE_SETS.addTranslationJp("らぎストーン", this)
        RagiumBlocks.RAGI_STONE_BRICKS_SETS.addTranslationJp("らぎストーンレンガ", this)
        RagiumBlocks.RAGI_STONE_SQUARE_SETS.addTranslationJp("らぎストーン（正方）", this)
        RagiumBlocks.AZURE_TILE_SETS.addTranslationJp("紺碧のタイル", this)
        RagiumBlocks.EMBER_STONE_SETS.addTranslationJp("熾火石", this)
        RagiumBlocks.PLASTIC_SETS.addTranslationJp("プラスチックブロック", this)
        RagiumBlocks.BLUE_NETHER_BRICK_SETS.addTranslationJp("青いネザーレンガ", this)
        RagiumBlocks.SPONGE_CAKE_SETS.addTranslationJp("スポンジケーキ", this)

        addBlock(RagiumBlocks.Glasses.QUARTZ, "クォーツガラス")
        addBlock(RagiumBlocks.Glasses.SOUL, "ソウルガラス")
        addBlock(RagiumBlocks.Glasses.OBSIDIAN, "黒曜石ガラス")

        addBlock(RagiumBlocks.LEDBlocks.RED, "LEDブロック（赤）")
        addBlock(RagiumBlocks.LEDBlocks.GREEN, "LEDブロック（緑）")
        addBlock(RagiumBlocks.LEDBlocks.BLUE, "LEDブロック（青）")
        addBlock(RagiumBlocks.LEDBlocks.CYAN, "LEDブロック（シアン）")
        addBlock(RagiumBlocks.LEDBlocks.MAGENTA, "LEDブロック（マゼンタ）")
        addBlock(RagiumBlocks.LEDBlocks.YELLOW, "LEDブロック（黄色）")
        addBlock(RagiumBlocks.LEDBlocks.WHITE, "LEDブロック")

        addBlock(RagiumBlocks.SWEET_BERRIES_CAKE, "スイートベリーケーキ")
        // Dynamo
        addBlock(RagiumBlocks.Dynamos.STIRLING, "スターリングダイナモ")
        // Machine
        addBlock(RagiumBlocks.Frames.ADVANCED, "発展機械フレーム")
        addBlock(RagiumBlocks.Frames.BASIC, "基本機械フレーム")
        addBlock(RagiumBlocks.Frames.ELITE, "精鋭機械フレーム")

        addBlock(RagiumBlocks.Machines.CRUSHER, "粉砕機")
        addBlock(RagiumBlocks.Machines.BLOCK_BREAKER, "ブロック採掘機")
        addBlock(RagiumBlocks.Machines.EXTRACTOR, "抽出機")

        addBlock(RagiumBlocks.Machines.ALLOY_SMELTER, "合金炉")
        addBlock(RagiumBlocks.Machines.FORMING_PRESS, "成型機")
        addBlock(RagiumBlocks.Machines.MELTER, "溶融炉")
        addBlock(RagiumBlocks.Machines.REFINERY, "精製機")
        addBlock(RagiumBlocks.Machines.SOLIDIFIER, "成型機")

        addBlock(RagiumBlocks.Machines.INFUSER, "神秘的注入機")
        // Device
        addBlock(RagiumBlocks.Casings.DEVICE, "デバイス筐体")
        addBlock(RagiumBlocks.Casings.STONE, "石材筐体")
        addBlock(RagiumBlocks.Casings.WOODEN, "木材筐体")

        addBlock(RagiumBlocks.Devices.CEU, "C.E.U")
        addBlock(RagiumBlocks.Devices.DIM_ANCHOR, "次元アンカー")
        addBlock(RagiumBlocks.Devices.ENI, "E.N.I.")
        addBlock(RagiumBlocks.Devices.EXP_COLLECTOR, "経験値収集機")
        addBlock(RagiumBlocks.Devices.ITEM_BUFFER, "アイテムバッファ")
        addBlock(RagiumBlocks.Devices.LAVA_COLLECTOR, "溶岩収集機")
        addBlock(RagiumBlocks.Devices.MILK_DRAIN, "牛乳シンク")
        addBlock(RagiumBlocks.Devices.SPRINKLER, "スプリンクラー")
        addBlock(RagiumBlocks.Devices.WATER_COLLECTOR, "水収集機")
        // Storage
        addBlock(RagiumBlocks.Drums.SMALL, "ドラム（小）")
        addBlock(RagiumBlocks.Drums.MEDIUM, "ドラム（中）")
        addBlock(RagiumBlocks.Drums.LARGE, "ドラム（大）")
        addBlock(RagiumBlocks.Drums.HUGE, "ドラム（特大）")
    }

    private fun enchantment() {
        addEnchantment(RagiumEnchantments.CAPACITY, "容量増加", "アイテムや液体ストレージの容量を拡張します。")
        addEnchantment(RagiumEnchantments.NOISE_CANCELING, "ノイズキャンセリング", "ウォーデンなどのスカルク系モンスターに対してのダメージを増加させます。")
        addEnchantment(RagiumEnchantments.SONIC_PROTECTION, "音響耐性", "ソニックブームなどの音響攻撃を無効にします。")
    }

    private fun entity() {
        addEntityType(RagiumEntityTypes.BLAST_CHARGE, "ブラストチャージ")
        addEntityType(RagiumEntityTypes.ELDRITCH_EGG, "異質な卵")

        // addEntityType(RagiumEntityTypes.DYNAMITE, "ダイナマイト")
        // addEntityType(RagiumEntityTypes.DEFOLIANT_DYNAMITE, "枯葉剤ダイナマイト")
        // addEntityType(RagiumEntityTypes.FLATTEN_DYNAMITE, "整地用ダイナマイト")
        // addEntityType(RagiumEntityTypes.NAPALM_DYNAMITE, "ナパームダイナマイト")
        // addEntityType(RagiumEntityTypes.POISON_DYNAMITE, "毒ガスダイナマイト")
    }

    private fun fluid() {
        addFluid(RagiumFluidContents.HONEY, "蜂蜜")
        addFluid(RagiumFluidContents.EXPERIENCE, "液体経験値")
        addFluid(RagiumFluidContents.MUSHROOM_STEW, "キノコシチュー")

        addFluid(RagiumFluidContents.CRUDE_OIL, "原油")
        addFluid(RagiumFluidContents.LPG, "LPG")
        addFluid(RagiumFluidContents.NAPHTHA, "ナフサ")
        addFluid(RagiumFluidContents.DIESEL, "ディーゼル")
        addFluid(RagiumFluidContents.CRIMSON_DIESEL, "深紅のディーゼル")
        addFluid(RagiumFluidContents.LUBRICANT, "潤滑油")

        addFluid(RagiumFluidContents.SAP, "樹液")
        addFluid(RagiumFluidContents.SYRUP, "シロップ")
        addFluid(RagiumFluidContents.CRIMSON_SAP, "深紅の樹液")
        addFluid(RagiumFluidContents.WARPED_SAP, "歪んだ樹液")
    }

    private fun item() {
        // Tickets
        addItem(RagiumItems.Tickets.BLANK, "ブランクチケット")

        addItem(RagiumItems.Tickets.RAGI, "らぎチケット")
        addItem(RagiumItems.Tickets.AZURE, "紺碧のチケット")
        addItem(RagiumItems.Tickets.BLOODY, "血塗られたチケット")
        addItem(RagiumItems.Tickets.TELEPORT, "テレポートチケット")
        addItem(RagiumItems.Tickets.ELDRITCH, "異質なチケット")

        addItem(RagiumItems.Tickets.DAYBREAK, "暁のチケット")
        addItem(RagiumItems.Tickets.ETERNAL, "永遠のチケット")
        // Material
        addItem(RagiumItems.COMPRESSED_SAWDUST, "圧縮したおがくず")
        addItem(RagiumItems.DEEP_SCRAP, "深層の欠片")
        addItem(RagiumItems.ELDER_HEART, "エルダーの心臓")
        addItem(RagiumItems.ELDRITCH_ORB, "異質な宝玉")
        addItem(RagiumItems.RAGI_COKE, "らぎコークス")
        addItem(RagiumItems.TAR, "タール")

        addItem(RagiumItems.Gems.RAGI_CRYSTAL, "ラギクリスタリル")
        addItem(RagiumItems.Gems.AZURE_SHARD, "紺碧の欠片")
        addItem(RagiumItems.Gems.CRIMSON_CRYSTAL, "深紅の結晶")
        addItem(RagiumItems.Gems.WARPED_CRYSTAL, "歪んだ結晶")
        addItem(RagiumItems.Gems.ELDRITCH_PEARL, "異質な真珠")

        addItem(RagiumItems.Compounds.RAGI_ALLOY, "ラギ合金混合物")
        addItem(RagiumItems.Compounds.ADVANCED_RAGI_ALLOY, "発展ラギ合金混合物")
        addItem(RagiumItems.Compounds.AZURE_STEEL, "紺鉄混合物")

        addItem(RagiumItems.Ingots.RAGI_ALLOY, "ラギ合金インゴット")
        addItem(RagiumItems.Ingots.ADVANCED_RAGI_ALLOY, "発展ラギ合金インゴット")
        addItem(RagiumItems.Ingots.AZURE_STEEL, "紺鉄インゴット")
        addItem(RagiumItems.Ingots.DEEP_STEEL, "深層鋼インゴット")

        addItem(RagiumItems.Nuggets.RAGI_ALLOY, "ラギ合金ナゲット")
        addItem(RagiumItems.Nuggets.ADVANCED_RAGI_ALLOY, "発展ラギ合金ナゲット")
        addItem(RagiumItems.Nuggets.AZURE_STEEL, "紺鉄ナゲット")
        addItem(RagiumItems.Nuggets.DEEP_STEEL, "深層鋼ナゲット")

        addItem(RagiumItems.Dusts.ASH, "灰")
        addItem(RagiumItems.Dusts.CINNABAR, "辰砂の粉")
        addItem(RagiumItems.Dusts.OBSIDIAN, "黒曜石の粉")
        addItem(RagiumItems.Dusts.RAGINITE, "ラギナイトの粉")
        addItem(RagiumItems.Dusts.SALTPETER, "硝石の粉")
        addItem(RagiumItems.Dusts.SAW, "おがくず")
        addItem(RagiumItems.Dusts.SULFUR, "硫黄の粉")

        // Armor
        RagiumItems.AZURE_STEEL_ARMORS.addTranslationJp("紺鉄", this)
        RagiumItems.DEEP_STEEL_ARMORS.addTranslationJp("深層鋼", this)
        // Tool
        addItem(RagiumItems.ADVANCED_RAGI_ALLOY_UPGRADE_SMITHING_TEMPLATE, "発展ラギ合金強化")
        addItem(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE, "紺鉄強化")
        addItem(RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE, "深層鋼強化")
        RagiumItems.AZURE_STEEL_TOOLS.addTranslationJp("紺鉄", this)
        RagiumItems.DEEP_STEEL_TOOLS.addTranslationJp("深層鋼", this)

        addItem(RagiumItems.DRILL, "電動ドリル")

        addItem(RagiumItems.ForgeHammers.IRON, "鉄の鍛造ハンマー")
        addItem(RagiumItems.ForgeHammers.DIAMOND, "ダイヤモンドの鍛造ハンマー")
        addItem(RagiumItems.ForgeHammers.NETHERITE, "ネザライトの鍛造ハンマー")
        addItem(RagiumItems.ForgeHammers.RAGI_ALLOY, "ラギ合金の鍛造ハンマー")
        addItem(RagiumItems.ForgeHammers.AZURE_STEEL, "紺鉄の鍛造ハンマー")
        addItem(RagiumItems.ForgeHammers.DEEP_STEEL, "深層鋼の鍛造ハンマー")

        addItem(RagiumItems.ADVANCED_RAGI_MAGNET, "発展らぎマグネット")
        addItem(RagiumItems.BLAST_CHARGE, "ブラストチャージ")
        addItem(RagiumItems.ELDRITCH_EGG, "異質な卵")
        addItem(RagiumItems.ENDER_BUNDLE, "エンダーバンドル")
        addItem(RagiumItems.POTION_BUNDLE, "ポーションバンドル")
        addItem(RagiumItems.RAGI_LANTERN, "らぎランタン")
        addItem(RagiumItems.RAGI_MAGNET, "らぎマグネット")
        addItem(RagiumItems.SLOT_COVER, "スロットカバー")
        addItem(RagiumItems.TRADER_CATALOG, "行商人のカタログ")
        // Food
        addItem(RagiumItems.CHOCOLATE_INGOT, "チョコレートインゴット")
        addItem(RagiumItems.ICE_CREAM, "アイスクリーム")
        addItem(RagiumItems.ICE_CREAM_SODA, "クリームソーダ")

        addItem(RagiumItems.MINCED_MEAT, "ひき肉")
        addItem(RagiumItems.MEAT_INGOT, "生肉インゴット")
        addItem(RagiumItems.COOKED_MEAT_INGOT, "焼肉インゴット")
        addItem(RagiumItems.CANNED_COOKED_MEAT, "焼肉缶詰")

        addItem(RagiumItems.SWEET_BERRIES_CAKE_SLICE, "カットスイートベリーケーキ")
        addItem(RagiumItems.MELON_PIE, "メロンパイ")

        addItem(RagiumItems.RAGI_CHERRY, "らぎチェリー")
        addItem(RagiumItems.RAGI_CHERRY_JAM, "らぎチェリージャム")
        addItem(RagiumItems.FEVER_CHERRY, "フィーバーチェリー")

        addItem(RagiumItems.BOTTLED_BEE, "瓶詰めのハチ")
        addItem(RagiumItems.EXP_BERRIES, "経験値ベリー")
        addItem(RagiumItems.WARPED_WART, "歪んだウォート")
        addItem(RagiumItems.AMBROSIA, "アンブロシア")

        // Mold
        // addItem(RagiumItems.Molds.BLANK, "成形型（なし）")
        // addItem(RagiumItems.Molds.BALL, "成形型（ボール）")
        // addItem(RagiumItems.Molds.BLOCK, "成形型（ブロック）")
        // addItem(RagiumItems.Molds.GEAR, "成形型（歯車）")
        // addItem(RagiumItems.Molds.INGOT, "成形型（インゴット）")
        // addItem(RagiumItems.Molds.PLATE, "成形型（板材）")
        // addItem(RagiumItems.Molds.ROD, "成形型（棒材）")
        // addItem(RagiumItems.Molds.WIRE, "成形型（ワイヤー）")
        // Parts
        addItem(RagiumItems.CIRCUIT_BOARD, "回路基板")
        addItem(RagiumItems.Circuits.ADVANCED, "発展回路")
        addItem(RagiumItems.Circuits.BASIC, "基本回路")
        addItem(RagiumItems.Circuits.ELITE, "精鋭回路")
        addItem(RagiumItems.Circuits.ULTIMATE, "究極回路")
        addItem(RagiumItems.LED, "発光ダイオード")
        addItem(RagiumItems.LUMINOUS_PASTE, "蛍光ペースト")
        addItem(RagiumItems.PLASTIC_PLATE, "プラスチック板")
        addItem(RagiumItems.POLYMER_RESIN, "高分子樹脂")
        addItem(RagiumItems.REDSTONE_BOARD, "レッドストーン基板")
        addItem(RagiumItems.SOLAR_PANEL, "太陽光パネル")
        addItem(RagiumItems.SYNTHETIC_FIBER, "合成繊維")
        addItem(RagiumItems.SYNTHETIC_LEATHER, "合成革")
    }

    private fun itemGroup() {
        addItemGroup(RagiumCreativeTabs.BLOCKS, "Ragium - ブロック")
        addItemGroup(RagiumCreativeTabs.INGREDIENTS, "Ragium - 素材")
        addItemGroup(RagiumCreativeTabs.ITEMS, "Ragium - アイテム")
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
        add(RagiumModTags.Items.POLYMER_RESIN, "高分子樹脂")
        add(RagiumModTags.Items.WIP, "開発中")

        add(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC, "合金炉で使う基本融剤")
        add(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED, "合金炉で使う発展融剤")
    }

    private fun text() {
        add(RagiumTranslationKeys.AZURE_STEEL_UPGRADE, "紺鉄強化")
        add(RagiumTranslationKeys.AZURE_STEEL_UPGRADE_APPLIES_TO, "紺鉄の装備品")
        add(RagiumTranslationKeys.AZURE_STEEL_UPGRADE_INGREDIENTS, "紺鉄インゴット")
        add(RagiumTranslationKeys.AZURE_STEEL_UPGRADE_BASE_SLOT_DESCRIPTION, "鉄製の防具，武器，道具を置いてください")
        add(RagiumTranslationKeys.AZURE_STEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, "紺鉄インゴットを置いてください")

        add(RagiumTranslationKeys.DEEP_STEEL_UPGRADE, "深層鋼強化")
        add(RagiumTranslationKeys.DEEP_STEEL_UPGRADE_APPLIES_TO, "深層鋼の装備品")
        add(RagiumTranslationKeys.DEEP_STEEL_UPGRADE_INGREDIENTS, "深層鋼インゴット")
        add(RagiumTranslationKeys.DEEP_STEEL_UPGRADE_BASE_SLOT_DESCRIPTION, "ダイヤモンド製の防具，武器，道具を置いてください")
        add(RagiumTranslationKeys.DEEP_STEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, "深層鋼インゴットを置いてください")

        add(RagiumTranslationKeys.TOOLTIP_EFFECT_RANGE, "有効半径: %s ブロック")
        add(RagiumTranslationKeys.TOOLTIP_ENERGY_PERCENTAGE, "%s / %s FE")
        add(RagiumTranslationKeys.TOOLTIP_FLUID_NAME, "%s : %s mb")
        add(RagiumTranslationKeys.TOOLTIP_FLUID_NAME_EMPTY, "空")
        add(RagiumTranslationKeys.TOOLTIP_INTRINSIC_ENCHANTMENT, "常に少なくとも%sがあります")
        add(RagiumTranslationKeys.TOOLTIP_LOOT_TABLE_ID, "ルートテーブル: %s")
        add(RagiumTranslationKeys.TOOLTIP_SHOW_INFO, "シフトキーを押して情報を表示")
        add(RagiumTranslationKeys.TOOLTIP_WIP, "この要素は開発中です！！")
    }

    private fun information() {
        addInfo(RagiumBlocks.ASH_LOG, "壊すと灰の粉が手に入ります。")
        addInfo(RagiumBlocks.CRIMSON_SOIL, "このブロックの上で倒されたモブは経験値も落とします。")

        addInfo(RagiumBlocks.Devices.CEU, "無限のパワー")
        addInfo(RagiumBlocks.Devices.DIM_ANCHOR, "設置されたチャンクを常に読み込みます。")
        addInfo(RagiumBlocks.Devices.ENI, "エネルギーネットワークにアクセスできます。")
        addInfo(RagiumBlocks.Devices.EXP_COLLECTOR, "周囲の経験値オーブを回収します。")
        addInfo(RagiumBlocks.Devices.ITEM_BUFFER, "9スロットのアイテムバッファとして機能します。")
        addInfo(
            RagiumBlocks.Devices.LAVA_COLLECTOR,
            "次の条件のうち全てを満たすとき，溶岩を生産します。",
            "- ネザーに設置されている",
            "- 周囲4ブロックが溶岩源で囲われている",
        )
        addInfo(RagiumBlocks.Devices.MILK_DRAIN, "牛を乗せると牛乳を搾り取ります。")
        addInfo(
            RagiumBlocks.Devices.WATER_COLLECTOR,
            "次の条件のうちいずれかを満たすとき，水を生産します。",
            "- 海洋または河川系バイオームに設置されている",
            "- 周囲2ブロック以上が水源で囲われている",
        )

        val nonSilkTouch = "シルクタッチなしで回収することが可能です。"
        addInfo(RagiumBlocks.Glasses.OBSIDIAN, "黒曜石とおなじ爆破耐性をもちます。", "また，$nonSilkTouch")
        addInfo(RagiumBlocks.Glasses.QUARTZ, nonSilkTouch)
        addInfo(RagiumBlocks.Glasses.SOUL, "プレイヤーのみ通過できます。", "また，$nonSilkTouch")

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
        addInfo(RagiumItems.RAGI_LANTERN, "範囲内の暗所に光源を設置します。")
        addInfo(RagiumItems.RAGI_MAGNET, "範囲内のドロップアイテムを回収します。")
        addInfo(RagiumItems.SLOT_COVER, "機械のスロットに入れることでレシピ判定から無視されます。")
        addInfo(RagiumItems.TRADER_CATALOG, "行商人からドロップします。")
        addInfo(RagiumItems.WARPED_WART, "食べるとランダムにデバフを一つだけ消します。")
    }

    private fun delight() {
        addItem(RagiumDelightAddon.RAGI_CHERRY_PULP, "らぎチェリーの果肉")
    }

    private fun jade() {
        add("config.jade.plugin_ragium.output_side", "搬出面")

        add(RagiumTranslationKeys.JADE_OUTPUT_SIDE, "搬出面: %s")
    }

    private fun mekanism() {
        add(RagiumMekanismAddon.CHEMICAL_RAGINITE.translationKey, "ラギナイト")
        add(RagiumMekanismAddon.CHEMICAL_AZURE.translationKey, "紺碧エッセンス")
        add(RagiumMekanismAddon.CHEMICAL_CRIMSON_SAP.translationKey, "深紅の樹液")
        add(RagiumMekanismAddon.CHEMICAL_WARPED_SAP.translationKey, "歪んだ樹液")

        addItem(RagiumMekanismAddon.ITEM_ENRICHED_RAGINITE, "濃縮ラギナイト")
        addItem(RagiumMekanismAddon.ITEM_ENRICHED_AZURE, "濃縮紺碧エッセンス")
    }

    private fun replication() {
        addMatterType(RagiumReplicationAddon.MATTER_RAGIUM.get(), "ラギウム")
    }
}
