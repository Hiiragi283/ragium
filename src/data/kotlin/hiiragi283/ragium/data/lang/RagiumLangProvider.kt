package hiiragi283.ragium.data.lang

import hiiragi283.lib.collection.forEach
import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.data.lang.HTLangPatternProvider
import hiiragi283.lib.data.lang.HTLangProvider
import hiiragi283.lib.data.lang.HTLangType
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.text.HTCommonTranslation
import hiiragi283.lib.text.HTHasTranslationKey
import hiiragi283.ragium.api.tag.HTItemPart
import hiiragi283.ragium.api.tag.HTMaterial
import hiiragi283.ragium.fluid.RagiumFluids
import hiiragi283.ragium.item.RagiumItems

interface RagiumLangProvider {
    fun addPatternTranslations(provider: HTLangProvider) {
        val langType: HTLangType = provider.langType
        // Block
        val waxedCopper = HTLangPatternProvider("Waxed %s", "錆止めされた%s")
        val copperBasin = HTLangName("Copper Basin", "銅の鉢")
        /*for (phase: HTCopperPhase in HTCopperPhase.entries) {
            val (weathering: HTHasTranslationKey, waxed: HTHasTranslationKey) = HCBlocks.COPPER_BASIN[phase]
            provider.add(weathering, phase.translate(langType, copperBasin))
            provider.add(waxed, waxedCopper.translate(langType, phase.translate(langType, copperBasin)))
        }*/
        // Fluid
        val dyePattern = HTLangPatternProvider("%s Dye", "%sの染料")
        for ((color: HTLangName, content: HTFluidContent) in RagiumFluids.DYES.asSequenceWithColor()) {
            provider.addFluid(content, dyePattern.translate(langType, color))
        }
        // Item
        RagiumItems.MATERIAL_ITEMS.forEach { (part: HTItemPart, material: HTMaterial, item: HTHasTranslationKey) ->
            if (part == HTItemPart.DUST && material == HTMaterial.Other.WOOD) {
                provider.add(item, HTLangName("Sawdust", "おがくず"))
            } else {
                provider.add(item, part, material)
            }
        }
        provider.add(RagiumItems.COAL_COKE, HTMaterial.Fuel.COAL_COKE)
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
