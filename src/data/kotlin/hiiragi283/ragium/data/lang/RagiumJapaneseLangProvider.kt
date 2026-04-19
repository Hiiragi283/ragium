package hiiragi283.ragium.data.lang

import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.data.lang.HTLangTypes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.data.advancement.RagiumAdvancementKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.data.PackOutput

class RagiumJapaneseLangProvider(output: PackOutput) : HTLangProvider(output, RagiumAPI.MOD_ID, HTLangTypes.JA_JP) {
    override fun addTranslations() {
        RagiumCommonTranslation.addTranslations(this)

        // Advancement
        add(RagiumAdvancementKeys.ROOT, "Ragium", "Welcome to ようこそRagiumパーク！")

        add(RagiumAdvancementKeys.RAGI_ALLOY, "0xFF003F", "ラギ合金インゴットを手に入れる")
        add(RagiumAdvancementKeys.ALLOY_SMELTER, "アル-ケミストリー", "合金炉を手に入れる")

        add(RagiumAdvancementKeys.THERMOMETER, "Heat And Cool", "温度計を手に入れる")
        add(RagiumAdvancementKeys.ADVANCED_RAGI_ALLOY, "多分赤色", "発展ラギ合金インゴットを手に入れる")
        add(RagiumAdvancementKeys.REFINERY, "かなりリファイナリーだよこれ", "精製機を手に入れる")
        add(RagiumAdvancementKeys.PLASTIC, "Plus-TiC", "プラスチックを手に入れる")
        add(RagiumAdvancementKeys.REFINED_SILICON, "精製シリコン", "シリコンの粉を手に入れる")
        add(RagiumAdvancementKeys.PYROLYZER, "電動コークス炉", "熱分解炉を手に入れる")
        add(RagiumAdvancementKeys.CRIMSON_CRYSTAL, "チャオ！", "深紅のクリスタルを手に入れる")
        add(RagiumAdvancementKeys.WARPED_CRYSTAL, "安定した歪み", "歪んだのクリスタルを手に入れる")

        add(RagiumAdvancementKeys.RAGI_CRYSTAL, "エナジウムではない", "ラギクリスタリルを手に入れる")
        add(RagiumAdvancementKeys.STAINLESS_STEEL, "HV時代ではない", "ステンレス鋼インゴットを手に入れる")
        add(RagiumAdvancementKeys.ELECTRIC_CIRCUIT, "PCB: プリチーでカッコイイボード", "電子回路を手に入れる")
        add(RagiumAdvancementKeys.BREWERY, "全自動醸造機", "醸造機を手に入れる")
        add(RagiumAdvancementKeys.MIXER, "ベストマッチ！", "混合機を手に入れる")
        // Block
        add(RagiumBlocks.MEAT_BLOCK, "骨付き肉ブロック")
        add(RagiumBlocks.COOKED_MEAT_BLOCK, "骨付き焼肉ブロック")
        add(RagiumBlocks.INDUSTRIAL_TNT, "工業用TNT")
        // Generator
        add(RagiumBlocks.BOILER, "ボイラー")
        // Machine
        add(RagiumBlocks.ALLOY_SMELTER, "合金炉")
        add(RagiumBlocks.ASSEMBLER, "組立機")
        add(RagiumBlocks.AUTO_CHISEL, "自動石切台")
        add(RagiumBlocks.CRUSHER, "粉砕機")
        add(RagiumBlocks.CUTTING_MACHINE, "切断機")
        add(RagiumBlocks.ELECTRIC_FURNACE, "電動精錬機")
        add(RagiumBlocks.PLANTER, "栽培機")

        add(RagiumBlocks.FREEZER, "冷却機")
        add(RagiumBlocks.MELTER, "溶融炉")
        add(RagiumBlocks.PYROLYZER, "熱分解室")
        add(RagiumBlocks.REFINERY, "精製室")
        add(RagiumBlocks.WASHER, "洗浄機")

        add(RagiumBlocks.BREWERY, "醸造機")
        add(RagiumBlocks.CHEMICAL_WASHER, "化学洗浄機")
        add(RagiumBlocks.FLUID_MIXER, "液体混合機")
        add(RagiumBlocks.MIXER, "混合機")

        add(RagiumBlocks.FLUID_DUPLICATOR, "液体複製機")
        // Device
        add(RagiumBlocks.ENCHANTER, "エンチャンター")
        // Storage
        add(RagiumBlocks.UNIVERSAL_CHEST, "共有チェスト")

        add(RagiumBlocks.BATTERY, "可変バッテリー")
        add(RagiumBlocks.CRATE, "可変クレート")
        add(RagiumBlocks.TANK, "可変タンク")

        add(RagiumBlocks.VOID_TANK, "消滅タンク")

        add(RagiumBlocks.IMITATION_SPAWNER, "スポナーの模造品")

        add(RagiumBlocks.CREATIVE_BATTERY, "クリエイティブバッテリー")
        add(RagiumBlocks.CREATIVE_CRATE, "クリエイティブクレート")
        add(RagiumBlocks.CREATIVE_TANK, "クリエイティブタンク")
        // Fluid
        addFluid(RagiumFluids.HYDROGEN, "水素")
        addFluid(RagiumFluids.STEAM, "蒸気")

        addFluid(RagiumFluids.OXYGEN, "酸素")

        addFluid(RagiumFluids.CREOSOTE, "クレオソート")
        addFluid(RagiumFluids.SYNTHETIC_GAS, "合成ガス")
        addFluid(RagiumFluids.SYNTHETIC_OIL, "合成石油")

        addFluid(RagiumFluids.GLUE, "液体ノリ")
        addFluid(RagiumFluids.METHANE, "メタン")
        addFluid(RagiumFluids.CRUDE_BIO, "未加工バイオマス")
        addFluid(RagiumFluids.ETHANOL, "エタノール")
        addFluid(RagiumFluids.BIOFUEL, "バイオ燃料")

        addFluid(RagiumFluids.NITROGEN, "窒素")
        addFluid(RagiumFluids.LIQUID_NITROGEN, "液体窒素")

        addFluid(RagiumFluids.NAOH_SOLUTION, "アルカリ水溶液")

        addFluid(RagiumFluids.MERCURY, "水銀")

        addFluid(RagiumFluids.CRUDE_OIL, "原油")
        addFluid(RagiumFluids.NAPHTHA, "ナフサ")
        addFluid(RagiumFluids.FUEL, "燃料")

        addFluid(RagiumFluids.NITROGEN_DIOXIDE, "二酸化窒素")
        addFluid(RagiumFluids.AMMONIA, "アンモニア")
        addFluid(RagiumFluids.NITRIC_ACID, "硝酸")

        addFluid(RagiumFluids.SULFUR_DIOXIDE, "二酸化硫黄")
        addFluid(RagiumFluids.SULFUR_TRIOXIDE, "三酸化硫黄")
        addFluid(RagiumFluids.SULFURIC_ACID, "硫酸")

        addFluid(RagiumFluids.HELIUM, "ヘリウム")

        addFluid(RagiumFluids.RAGI_MATTER, "らぎマター")
        // Item
        add(RagiumItems.RAGI_ALLOY_COMPOUND, "ラギ合金混合物")
        add(RagiumItems.CARBON_COMPOUND, "炭素混合物")
        add(RagiumItems.CRYO_CHARGE, "クライオチャージ")

        add(RagiumItems.CRUDE_SILICON, "粗製シリコン")
        add(RagiumItems.GLYCEROL_DROP, "グリセロール")
        add(RagiumItems.NITROGLYCERIN, "ニトログリセリン")
        add(RagiumItems.NITROCELLULOSE, "ニトロセルロース")
        add(RagiumItems.SMOKELESS_POWDER, "無煙火薬")

        add(RagiumItems.MERCURY_BOTTLE, "水銀入り瓶")
        add(RagiumItems.THERMOMETER, "温度計")
        add(RagiumItems.SILICON_WAFER, "シリコンウェハ")
        add(RagiumItems.CIRCUIT_CHIP, "回路チップ")
        add(RagiumItems.CIRCUIT_BOARD, "回路基板")
        add(RagiumItems.ELECTRIC_CIRCUIT, "電子回路")

        add(RagiumItems.ARTIFICIAL_ARTIFACT, "人工遺物")

        add(RagiumItems.EMPTY_CAN, "空の缶詰")

        add(RagiumItems.BLANK_DISC, "空のレコード")
        add(RagiumItems.CRYSTAL_BATTERY, "クリスタルバッテリー")
        add(RagiumItems.DYNAMITE, "ダイナマイト")
        add(RagiumItems.ELECTRIC_IGNITER, "電動着火器")
        add(RagiumItems.LOCATION_TICKET, "座標チケット")

        add(RagiumItems.RAGI_MATTER, "らぎマター")
        add(RagiumItems.RAGI_TICKET, "らぎチケット")
        // Recipe
        add(RagiumRecipeLookups.ALLOYING, "合金")
        add(RagiumRecipeLookups.ASSEMBLING, "組立")
        add(RagiumRecipeLookups.CUTTING, "切断")
        add(RagiumRecipeLookups.PLANTING, "栽培")

        add(RagiumRecipeLookups.FREEZING, "冷却")
        add(RagiumRecipeLookups.IMPLODING, "内爆圧縮")
        add(RagiumRecipeLookups.MELTING, "溶融")
        add(RagiumRecipeLookups.PYROLYZING, "熱分解")
        add(RagiumRecipeLookups.REFINING, "精製")
        add(RagiumRecipeLookups.WASHING, "洗浄")

        add(RagiumRecipeLookups.CHEMICAL_WASHING, "化学洗浄")
        add(RagiumRecipeLookups.MIXING, "混合")

        add(RagiumRecipeLookups.ENCHANTING, "エンチャント")

        // Tag
        add(RagiumTags.Fluids.ALCOHOL, "アルコール")
        add(RagiumTags.Fluids.BIODIESEL, "バイオディーゼル")
        add(RagiumTags.Fluids.DIESEL, "ディーゼル")
        add(RagiumTags.Fluids.LIQUID_MATTER, "液体マター")

        add(RagiumTags.Items.FOODS_CAN, "缶詰の食料")
        // Text
        add(RagiumTranslation.RAGIUM, "ラギウム")

        add(RagiumTranslation.CONFIG_ENERGY_CAPACITY, "エネルギー容量")
        add(RagiumTranslation.CONFIG_ENERGY_RATE, "エネルギー使用速度")

        add(RagiumTranslation.GUI_SLOT_BOTH, "%s: 双方")
        add(RagiumTranslation.GUI_SLOT_INPUT, "%s: 入力")
        add(RagiumTranslation.GUI_SLOT_OUTPUT, "%s: 出力")
        add(RagiumTranslation.GUI_SLOT_EXTRA_INPUT, "%s: 追加の入力")
        add(RagiumTranslation.GUI_SLOT_EXTRA_OUTPUT, "%s: 追加の出力")
        add(RagiumTranslation.GUI_SLOT_NONE, "%s: なし")

        add(RagiumTranslation.ALLOY_SMELTER, "複数のアイテムを一つに焼き上げる機械です。")
        add(RagiumTranslation.CRUSHER, "アイテムを粉にする機械です。")
        add(RagiumTranslation.CUTTING_MACHINE, "原木や木製アイテムを木材に加工する機械です。")
        add(RagiumTranslation.ELECTRIC_FURNACE, "エネルギーでアイテムを焼く機械です。")

        add(RagiumTranslation.MELTER, "アイテムを融かしたり，液体を加熱したりする機械です。")
        add(RagiumTranslation.PYROLYZER, "原木や石炭を木炭やコークスに加工する機械です。")

        add(RagiumTranslation.FREEZER, "アイテムを凍らせたり，液体を冷却したりする機械です。")
        add(RagiumTranslation.WASHER, "砕いた鉱石と液体からアイテムを生産する機械です。")

        add(RagiumTranslation.BREWERY, "液体のポーションを醸造する機械です。")
        add(RagiumTranslation.CHEMICAL_WASHER, "アイテムを化学物質で洗浄する機械です。")
        add(RagiumTranslation.MIXER, "複数のアイテムや液体を混ぜる機械です。")

        add(RagiumTranslation.FLUID_DUPLICATOR, "液体のマターを消費して任意の液体を複製する機械です。")

        add(RagiumTranslation.BATTERY, "クラフトで合体させることで容量を拡張可能なエネルギーストレージです。")
        add(RagiumTranslation.CRATE, "クラフトで合体させることで容量を拡張可能なアイテムストレージです。")
        add(RagiumTranslation.TANK, "クラフトで合体させることで容量を拡張可能な液体ストレージです。")
        add(RagiumTranslation.BUFFER, "9つのスロット，3つのタンク，1つのバッテリーを併せ持つストレージです。")
        add(RagiumTranslation.UNIVERSAL_CHEST, "色ごとに中身を共有するチェストです。")

        add(RagiumTranslation.TOOLTIP_BLOCK_POS, $$"座標: [%1$s, %2$s, %3$s]")
        add(RagiumTranslation.TOOLTIP_CHARGE_POWER, $$"威力: %1$s")
        add(RagiumTranslation.TOOLTIP_DIMENSION, $$"次元: %1$s")
        add(RagiumTranslation.TOOLTIP_LOOT_TABLE_ID, $$"ルートテーブル: %1$s")
    }
}
