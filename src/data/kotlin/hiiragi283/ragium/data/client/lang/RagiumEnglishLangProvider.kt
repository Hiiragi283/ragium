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
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

class RagiumEnglishLangProvider(output: PackOutput) : HTLangProvider.English(output, RagiumAPI.MOD_ID) {
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
        addFluid(RagiumFluids.SLIME, "Slime")
        addFluid(RagiumFluids.GELLED_EXPLOSIVE, "Gelled Explosive")
        addFluid(RagiumFluids.CRUDE_BIO, "Crude Bio")
        addFluid(RagiumFluids.BIOFUEL, "Biofuel")

        addFluid(RagiumFluids.CRUDE_OIL, "Crude Oil")
        addFluid(RagiumFluids.NAPHTHA, "Naphtha")
        addFluid(RagiumFluids.FUEL, "Fuel")
        addFluid(RagiumFluids.LUBRICANT, "Lubricant")

        addFluid(RagiumFluids.DESTABILIZED_RAGINITE, "Destabilized Raginite")
        addFluid(RagiumFluids.COOLANT, "Coolant")
        addFluid(RagiumFluids.CREOSOTE, "Creosote")

        // Item
        add(RagiumItems.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound")
        add(RagiumItems.RAGIUM_POWDER, "Ragium Powder")

        // Recipe
        add(RagiumRecipeTypes.ALLOYING, "Alloying")

        // Text
        add(RagiumTranslation.RAGIUM, "Ragium")
    }
}
