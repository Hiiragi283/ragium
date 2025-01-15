package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import hiiragi283.ragium.data.add
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.LanguageProvider

class RagiumJapaneseProvider(output: PackOutput) : LanguageProvider(output, RagiumAPI.MOD_ID, "ja_jp") {
    override fun addTranslations() {
        // Block
        add(RagiumBlocks.ENERGY_NETWORK_INTERFACE, "E.N.I.")

        add(RagiumBlocks.LEDBlocks.RED, "LEDブロック（赤）")
        add(RagiumBlocks.LEDBlocks.GREEN, "LEDブロック（緑）")
        add(RagiumBlocks.LEDBlocks.BLUE, "LEDブロック（青）")
        add(RagiumBlocks.LEDBlocks.CYAN, "LEDブロック（シアン）")
        add(RagiumBlocks.LEDBlocks.MAGENTA, "LEDブロック（マゼンタ）")
        add(RagiumBlocks.LEDBlocks.YELLOW, "LEDブロック（黄色）")
        add(RagiumBlocks.LEDBlocks.WHITE, "LEDブロック")

        // Content
        add(RagiumTranslationKeys.CASING, "外装")
        add(RagiumTranslationKeys.CIRCUIT, "回路")
        add(RagiumTranslationKeys.COIL, "コイル")
        add(RagiumTranslationKeys.CRATE, "クレート")
        add(RagiumTranslationKeys.DRUM, "ドラム")
        add(RagiumTranslationKeys.GRATE, "格子")
        add(RagiumTranslationKeys.HULL, "筐体")
        add(RagiumTranslationKeys.PLASTIC, "プラスチック")
        // Fluids
        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            add(fluid.get().fluidType.descriptionId, fluid.jaName)
        }
        // Machine
        add(HTMachineTier.PRIMITIVE, "原始", "原始%s")
        add(HTMachineTier.SIMPLE, "簡易", "簡易%s")
        add(HTMachineTier.BASIC, "基本", "基本%s")
        add(HTMachineTier.ADVANCED, "発展", "発展%s")
        add(HTMachineTier.ELITE, "精鋭", "精鋭%s")
        // Machine Type
        add(RagiumMachineKeys.BEDROCK_MINER, "岩盤採掘機", "岩盤から鉱物を採掘する")
        add(RagiumMachineKeys.BIOMASS_FERMENTER, "バイオマス発酵槽", "植物からバイオマスを生産する")
        add(RagiumMachineKeys.DRAIN, "排水溝", "正面から液体を，上から経験値を，スロット内の液体キューブから中身を吸い取る")
        add(RagiumMachineKeys.FLUID_DRILL, "液体採掘機", "特定のバイオームから液体を汲み上げる")
        add(RagiumMachineKeys.GAS_PLANT, "ガス採集機", "特定のバイオームからガスを集める")
        add(RagiumMachineKeys.ROCK_GENERATOR, "岩石生成機", "水と溶岩を少なくとも一つずつ隣接させる")

        add(RagiumMachineKeys.COMBUSTION_GENERATOR, "燃焼発電機", "液体燃料から発電する")
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
        // add(RagiumMaterialKeys.NETHER_STAR, "ネザースター")
        add(RagiumMaterialKeys.NETHERITE, "ネザライト")
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
        // add(RagiumMaterialKeys.STONE, "石材")
        add(RagiumMaterialKeys.SULFUR, "硫黄")
        add(RagiumMaterialKeys.TIN, "スズ")
        add(RagiumMaterialKeys.TITANIUM, "チタン")
        add(RagiumMaterialKeys.TUNGSTEN, "タングステン")
        add(RagiumMaterialKeys.URANIUM, "ウラニウム")
        // add(RagiumMaterialKeys.WOOD, "木材")
        add(RagiumMaterialKeys.ZINC, "亜鉛")
        // Tag Prefix
        add(HTTagPrefix.DUST, "%sの粉")
        add(HTTagPrefix.GEAR, "%sの歯車")
        add(HTTagPrefix.GEM, "%s")
        add(HTTagPrefix.INGOT, "%sインゴット")
        add(HTTagPrefix.NUGGET, "%sのナゲット")
        add(HTTagPrefix.ORE, "%s鉱石")
        add(HTTagPrefix.PLATE, "%s板")
        add(HTTagPrefix.RAW_MATERIAL, "%sの原石")
        add(HTTagPrefix.ROD, "%s棒")
        add(HTTagPrefix.STORAGE_BLOCK, "%sブロック")
        add(HTTagPrefix.WIRE, "%sワイヤ")
    }
}
