package hiiragi283.ragium.data.lang

import hiiragi283.lib.collection.forEach
import hiiragi283.lib.color.HTColoredCollection
import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.data.lang.HTLangPatternProvider
import hiiragi283.lib.data.lang.HTLangProvider
import hiiragi283.lib.data.lang.HTLangType
import hiiragi283.lib.item.component.HTToolType
import hiiragi283.lib.text.HTCommonTranslation
import hiiragi283.lib.text.HTHasTranslationKey
import hiiragi283.ragium.api.material.HTBlockPart
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.world.item.DyeColor

interface RagiumLangProvider {
    fun addPatternTranslations(provider: HTLangProvider) {
        val langType: HTLangType = provider.langType
        // Block
        val waxedCopper = HTLangPatternProvider("Waxed %s", "錆止めされた%s")
        RagiumBlocks.MATERIAL_BLOCKS
            .forEach { (part: HTBlockPart, material: RagiumMaterial, block: HTHasTranslationKey) ->
                provider.add(block, part, material)
            }
        val casingPattern = HTLangPatternProvider("Machine Casing (%s)", "機械筐体 (%s)")
        for ((machineType: HTLangName, casing: HTHasTranslationKey) in RagiumBlocks.MACHINE_CASINGS) {
            provider.add(casing, casingPattern, machineType)
        }
        // Fluid
        val dyePattern = HTLangPatternProvider("%s Dye", "%sの染料")
        for (color: DyeColor in DyeColor.entries) {
            provider.addFluid(
                RagiumFluids.DYES[color],
                dyePattern.translate(langType, HTColoredCollection.TRANSLATED_NAMES[color])
            )
        }
        // Item
        RagiumItems.MATERIAL_ITEMS
            .forEach { (part: HTItemPart, material: RagiumMaterial, item: HTHasTranslationKey) ->
                var patternProvider: HTLangPatternProvider = part
                if (part == HTItemPart.DUST) {
                    if (material is RagiumMaterial.Other && material.isPulp) {
                        patternProvider = HTLangPatternProvider("%s Pulp", "%sパルプ")
                    }
                }
                provider.add(item, patternProvider, material)
            }
        provider.add(RagiumItems.COAL_COKE, RagiumMaterial.Fuel.COAL_COKE)

        val partsPattern = HTLangPatternProvider("Machine Parts (%s)", "機械部品 (%s)")
        for ((machineType: HTLangName, parts: HTHasTranslationKey) in RagiumItems.MACHINE_PARTS) {
            provider.add(parts, partsPattern, machineType)
        }
        for (toolType: HTToolType in HTToolType.entries) {
            provider.add(RagiumItems.SOOTY_IRON_TOOLS[toolType], toolType, RagiumMaterial.Metal.SOOTY_IRON)
        }
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
