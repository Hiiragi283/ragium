package hiiragi283.ragium.data.lang

import hiiragi283.lib.collection.forEach
import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.data.lang.HTLangPatternProvider
import hiiragi283.lib.data.lang.HTLangProvider
import hiiragi283.lib.data.lang.HTLangType
import hiiragi283.lib.material.CommonMaterials
import hiiragi283.lib.material.HTMaterial
import hiiragi283.lib.material.VanillaMaterials
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.text.HTCommonTranslation
import hiiragi283.lib.text.HTHasTranslationKey
import hiiragi283.ragium.api.tag.HTBlockPart
import hiiragi283.ragium.api.tag.HTItemPart
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems

interface RagiumLangProvider {
    fun addPatternTranslations(provider: HTLangProvider) {
        val langType: HTLangType = provider.langType
        // Block
        val waxedCopper = HTLangPatternProvider("Waxed %s", "錆止めされた%s")
        RagiumBlocks.MATERIAL_BLOCKS.forEach { (part: HTBlockPart, material: HTMaterial, block: HTHasTranslationKey) ->
            provider.add(block, part, material)
        }
        // Fluid
        val dyePattern = HTLangPatternProvider("%s Dye", "%sの染料")
        for ((color: HTLangName, content: HTFluidContent) in RagiumFluids.DYES.asSequenceWithColor()) {
            provider.addFluid(content, dyePattern.translate(langType, color))
        }
        // Item
        RagiumItems.MATERIAL_ITEMS.forEach { (part: HTItemPart, material: HTMaterial, item: HTHasTranslationKey) ->
            var patternProvider: HTLangPatternProvider = part
            if (part == HTItemPart.DUST) {
                if (material == VanillaMaterials.WOOD || material == VanillaMaterials.PAPER) {
                    patternProvider = HTLangPatternProvider("%s Pulp", "%sパルプ")
                }
            }
            provider.add(item, patternProvider, material)
        }
        provider.add(RagiumItems.COAL_COKE, CommonMaterials.COAL_COKE)
        // Text
        // API - Constants
        provider.add(HTCommonTranslation.TRUE, "True")
        provider.add(HTCommonTranslation.FALSE, "False")
        // API - GUI
        provider.add(HTCommonTranslation.CAPACITY, $$"Capacity: %1$s")
        provider.add(HTCommonTranslation.CAPACITY_MB, $$"Capacity: %1$s mB")
        provider.add(HTCommonTranslation.CAPACITY_FE, $$"Capacity: %1$s FE")

        provider.add(HTCommonTranslation.STORED, $$"%1$s: %2$s")
        provider.add(HTCommonTranslation.STORED_MB, $$"%1$s: %2$s mB")
        provider.add(HTCommonTranslation.STORED_FE, $$"%1$s FE")
        provider.add(HTCommonTranslation.STORED_EXP, $$"%1$s Exp")

        provider.add(HTCommonTranslation.FRACTION, $$"%1$s / %2$s")
        provider.add(HTCommonTranslation.PERCENTAGE, $$"%1$s %%")

        provider.add(HTCommonTranslation.TICK, $$"%1$s ticks")
    }
}
