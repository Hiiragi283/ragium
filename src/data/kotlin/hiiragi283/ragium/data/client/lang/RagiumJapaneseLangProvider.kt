package hiiragi283.ragium.data.client.lang

import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.common.data.lang.HTMaterialTranslationHelper
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.material.RagiumMaterial
import hiiragi283.ragium.common.text.RagiumTranslation
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.data.PackOutput

class RagiumJapaneseLangProvider(output: PackOutput) : HTLangProvider.Japanese(output, RagiumAPI.MOD_ID) {
    override fun addTranslations() {
        for (material: RagiumMaterial in RagiumMaterial.entries) {
            // Block
            for ((prefix: HTMaterialPrefix, block: HTHasTranslationKey) in RagiumBlocks.MATERIALS.column(material)) {
                val name: String = HTMaterialTranslationHelper.translate(langType, prefix, material) ?: continue
                add(block, name)
            }
            // Item
            for ((prefix: HTMaterialPrefix, item: HTHasTranslationKey) in RagiumItems.MATERIALS.column(material)) {
                val name: String = HTMaterialTranslationHelper.translate(langType, prefix, material) ?: continue
                add(item, name)
            }
        }

        // Block
        add(RagiumBlocks.BATTERY, "可変バッテリー")
        add(RagiumBlocks.CRATE, "可変クレート")
        add(RagiumBlocks.TANK, "可変タンク")

        // Fluid
        addFluid(RagiumFluids.SALT_WATER, "塩水")

        addFluid(RagiumFluids.SLIME, "スライム")
        addFluid(RagiumFluids.GELLED_EXPLOSIVE, "ゲル状爆薬")
        addFluid(RagiumFluids.CRUDE_BIO, "未加工バイオ")
        addFluid(RagiumFluids.BIOFUEL, "バイオ燃料")

        addFluid(RagiumFluids.CRUDE_OIL, "原油")
        addFluid(RagiumFluids.NAPHTHA, "ナフサ")
        addFluid(RagiumFluids.FUEL, "燃料")
        addFluid(RagiumFluids.LUBRICANT, "潤滑油")

        addFluid(RagiumFluids.DESTABILIZED_RAGINITE, "不安定化ラギナイト")

        addFluid(RagiumFluids.COOLANT, "冷却液")
        addFluid(RagiumFluids.CREOSOTE, "クレオソート")

        // Item
        add(RagiumItems.RAGI_ALLOY_COMPOUND, "ラギ合金混合物")
        add(RagiumItems.RAGIUM_POWDER, "ラギウムパウダー")

        add(RagiumItems.LOOT_TICKET, "らぎチケット")
        add(RagiumItems.POTION_DROP, "ポーションの雫")
        add(RagiumItems.SLOT_COVER, "スロットカバー")
        add(RagiumItems.TRADER_CATALOG, "行商人のカタログ")

        // Recipe
        add(RagiumRecipeTypes.ALLOYING, "合金")
        add(RagiumRecipeTypes.DRYING, "乾燥")
        add(RagiumRecipeTypes.MELTING, "溶融")

        // Text
        add(RagiumTranslation.RAGIUM, "ラギウム")

        add(RagiumTranslation.BATTERY, "アップグレードで容量を拡張可能なエネルギーストレージです。")
        add(RagiumTranslation.CRATE, "アップグレードで容量を拡張可能なアイテムストレージです。")
        add(RagiumTranslation.TANK, "アップグレードで容量を拡張可能な液体ストレージです。")
        add(RagiumTranslation.BUFFER, "9つのスロット，3つのタンク，1つのバッテリーを併せ持つストレージです。")
        add(RagiumTranslation.UNIVERSAL_CHEST, "色ごとに中身を共有するチェストです。")

        add(RagiumTranslation.SLOT_COVER, "機械のスロットに入れることでレシピ判定から無視されます。")
        add(RagiumTranslation.TRADER_CATALOG, "行商人からドロップします。右クリックで行商人との取引を行えます。")

        add(RagiumTranslation.TOOLTIP_BLOCK_POS, $$"座標: [%1$s, %2$s, %3$s]")
        add(RagiumTranslation.TOOLTIP_CHARGE_POWER, $$"威力: %1$s")
        add(RagiumTranslation.TOOLTIP_DIMENSION, $$"次元: %1$s")
        add(RagiumTranslation.TOOLTIP_LOOT_TABLE_ID, $$"ルートテーブル: %1$s")
    }
}
