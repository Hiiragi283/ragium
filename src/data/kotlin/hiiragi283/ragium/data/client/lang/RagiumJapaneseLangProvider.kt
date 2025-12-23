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

        // Fluid
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

        // Text
        add(RagiumTranslation.RAGIUM, "ラギウム")
    }
}
