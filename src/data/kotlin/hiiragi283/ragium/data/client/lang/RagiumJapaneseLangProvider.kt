package hiiragi283.ragium.data.client.lang

import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumTags
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.upgrade.HTUpgradeKeys
import hiiragi283.ragium.common.upgrade.RagiumUpgradeKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.data.PackOutput

class RagiumJapaneseLangProvider(output: PackOutput) : HTLangProvider.Japanese(output, RagiumAPI.MOD_ID) {
    override fun addTranslations() {
        addMaterials()
        RagiumCommonTranslation.addTranslations(this)

        // Block
        add(RagiumBlocks.MEAT_BLOCK, "骨付き肉ブロック")
        add(RagiumBlocks.COOKED_MEAT_BLOCK, "骨付き焼肉ブロック")

        add(RagiumBlocks.ALLOY_SMELTER, "合金炉")
        add(RagiumBlocks.CRUSHER, "粉砕機")
        add(RagiumBlocks.CUTTING_MACHINE, "切断機")

        add(RagiumBlocks.DRYER, "乾燥機")
        add(RagiumBlocks.MELTER, "溶融炉")
        add(RagiumBlocks.MIXER, "混合機")
        add(RagiumBlocks.PYROLYZER, "熱分解室")

        add(RagiumBlocks.PLANTER, "栽培機")

        add(RagiumBlocks.BATTERY, "可変バッテリー")
        add(RagiumBlocks.CRATE, "可変クレート")
        add(RagiumBlocks.TANK, "可変タンク")
        add(RagiumBlocks.RESONANT_INTERFACE, "共振インターフェース")
        add(RagiumBlocks.UNIVERSAL_CHEST, "共有チェスト")

        add(RagiumBlocks.IMITATION_SPAWNER, "スポナーの模造品")
        // Fluid
        addFluid(RagiumFluids.CRUDE_BIO, "未加工バイオ")
        addFluid(RagiumFluids.ETHANOL, "エタノール")
        addFluid(RagiumFluids.BIOFUEL, "バイオ燃料")
        addFluid(RagiumFluids.FERTILIZER, "液体肥料")

        addFluid(RagiumFluids.CRUDE_OIL, "原油")
        addFluid(RagiumFluids.NAPHTHA, "ナフサ")
        addFluid(RagiumFluids.FUEL, "燃料")
        addFluid(RagiumFluids.LUBRICANT, "潤滑油")

        addFluid(RagiumFluids.MOLTEN_RAGINITE, "不安定化ラギナイト")
        addFluid(RagiumFluids.COOLANT, "冷却液")
        addFluid(RagiumFluids.CREOSOTE, "クレオソート")

        // Item
        add(RagiumItems.RAGI_ALLOY_COMPOUND, "ラギ合金混合物")
        add(RagiumItems.TAR, "タール")

        add(RagiumItems.EMPTY_CAN, "空の缶詰")

        add(RagiumItems.BLANK_DISC, "空のレコード")
        add(RagiumItems.LOCATION_TICKET, "座標チケット")
        add(RagiumItems.LOOT_TICKET, "らぎチケット")
        add(RagiumItems.POTION_DROP, "ポーションの雫")

        // Recipe
        add(RagiumRecipeTypes.ALLOYING, "合金")
        add(RagiumRecipeTypes.CRUSHING, "粉砕")
        add(RagiumRecipeTypes.CUTTING, "切断")
        add(RagiumRecipeTypes.DRYING, "乾燥")
        add(RagiumRecipeTypes.MELTING, "溶融")
        add(RagiumRecipeTypes.MIXING, "混合")
        add(RagiumRecipeTypes.PLANTING, "栽培")
        add(RagiumRecipeTypes.PRESSING, "プレス加工")
        add(RagiumRecipeTypes.PYROLYZING, "熱分解")
        add(RagiumRecipeTypes.REFINING, "精製")
        add(RagiumRecipeTypes.SIMULATING, "シミュレーション")
        add(RagiumRecipeTypes.SOLIDIFYING, "成型")

        // Tag
        add(RagiumTags.Items.FOODS_CAN, "缶詰の食料")
        add(RagiumTags.Items.MOLDS, "鋳型")

        add(RagiumTags.Items.GENERATOR_UPGRADABLE, "発電機")
        add(RagiumTags.Items.PROCESSOR_UPGRADABLE, "処理装置")
        add(RagiumTags.Items.MACHINE_UPGRADABLE, "機械")
        add(RagiumTags.Items.DEVICE_UPGRADABLE, "設備")
        add(RagiumTags.Items.EXTRA_VOIDING_UPGRADABLE, "追加の出力スロットをもつ処理機械")
        add(RagiumTags.Items.EFFICIENT_CRUSHING_UPGRADABLE, "粉砕機または破砕機")
        add(RagiumTags.Items.ENERGY_CAPACITY_UPGRADABLE, "エネルギーストレージ")
        add(RagiumTags.Items.FLUID_CAPACITY_UPGRADABLE, "液体ストレージ")
        add(RagiumTags.Items.ITEM_CAPACITY_UPGRADABLE, "アイテムストレージ")
        add(RagiumTags.Items.SMELTING_UPGRADABLE, "電動かまどまたは並列製錬炉")

        add(RagiumTags.Items.EXTRACTOR_EXCLUSIVE, "抽出機に対応したアップグレード")
        add(RagiumTags.Items.SMELTER_EXCLUSIVE, "製錬機械に対応したアップグレード")
        // Text
        add(RagiumTranslation.RAGIUM, "ラギウム")

        add(RagiumTranslation.CONFIG_ENERGY_CAPACITY, "エネルギー容量")
        add(RagiumTranslation.CONFIG_ENERGY_RATE, "エネルギー使用速度")
        add(RagiumTranslation.CONFIG_FLUID_FIRST_INPUT, "1番目の入力タンクの容量")
        add(RagiumTranslation.CONFIG_FLUID_SECOND_INPUT, "2番目の入力タンクの容量")
        add(RagiumTranslation.CONFIG_FLUID_FIRST_OUTPUT, "1番目の出力タンクの容量")
        add(RagiumTranslation.CONFIG_FLUID_SECOND_OUTPUT, "2番目の出力タンクの容量")
        add(RagiumTranslation.CONFIG_FLUID_THIRD_OUTPUT, "3番目の出力タンクの容量")

        add(RagiumTranslation.GUI_SLOT_BOTH, "%s: 双方")
        add(RagiumTranslation.GUI_SLOT_INPUT, "%s: 入力")
        add(RagiumTranslation.GUI_SLOT_OUTPUT, "%s: 出力")
        add(RagiumTranslation.GUI_SLOT_EXTRA_INPUT, "%s: 追加の入力")
        add(RagiumTranslation.GUI_SLOT_EXTRA_OUTPUT, "%s: 追加の出力")
        add(RagiumTranslation.GUI_SLOT_NONE, "%s: なし")

        add(RagiumTranslation.DRYER, "乾燥によって材料を変換する機械です。")
        add(RagiumTranslation.MELTER, "アイテムを融かして液体にする機械です。")
        add(RagiumTranslation.PYROLYZER, "熱によって材料を変換する機械です。")

        add(RagiumTranslation.BATTERY, "アップグレードで容量を拡張可能なエネルギーストレージです。")
        add(RagiumTranslation.CRATE, "アップグレードで容量を拡張可能なアイテムストレージです。")
        add(RagiumTranslation.TANK, "アップグレードで容量を拡張可能な液体ストレージです。")
        add(RagiumTranslation.BUFFER, "9つのスロット，3つのタンク，1つのバッテリーを併せ持つストレージです。")
        add(RagiumTranslation.UNIVERSAL_CHEST, "色ごとに中身を共有するチェストです。")

        add(RagiumTranslation.TOOLTIP_BLOCK_POS, $$"座標: [%1$s, %2$s, %3$s]")
        add(RagiumTranslation.TOOLTIP_CHARGE_POWER, $$"威力: %1$s")
        add(RagiumTranslation.TOOLTIP_DIMENSION, $$"次元: %1$s")
        add(RagiumTranslation.TOOLTIP_LOOT_TABLE_ID, $$"ルートテーブル: %1$s")
        add(RagiumTranslation.TOOLTIP_UPGRADE_TARGET, $$"アップグレードの対象: %1$s")
        add(RagiumTranslation.TOOLTIP_UPGRADE_EXCLUSIVE, $$"競合するアップグレード: %1$s")
        // Upgrade
        add(HTUpgradeKeys.BASE_MULTIPLIER, $$"- 基本倍率: %1$s")
        add(HTUpgradeKeys.IS_CREATIVE, "- クリエイティブ")

        add(HTUpgradeKeys.ENERGY_EFFICIENCY, $$"- エネルギー効率: %1$s")
        add(HTUpgradeKeys.ENERGY_GENERATION, $$"- エネルギー生産率: %1$s")
        add(HTUpgradeKeys.SPEED, $$"- 処理速度: %1$s")

        add(HTUpgradeKeys.ENERGY_CAPACITY, $$"- エネルギー容量: %1$s")
        add(HTUpgradeKeys.FLUID_CAPACITY, $$"- 液体容量: %1$s")
        add(HTUpgradeKeys.ITEM_CAPACITY, $$"- アイテム容量: %1$s")

        add(RagiumUpgradeKeys.BLASTING, "- 溶鉱炉レシピのみを処理")
        add(RagiumUpgradeKeys.SMOKING, "- 燻製器レシピのみを処理")
        add(RagiumUpgradeKeys.VOID_EXTRA, "- 追加の生産物を無効化")
        add(RagiumUpgradeKeys.USE_LUBRICANT, "- 処理ごとに潤滑油を消費")
    }
}
