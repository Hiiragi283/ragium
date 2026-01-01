package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTComplexRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterial
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems

object RagiumMixingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Creosote + Redstone -> Lubricant
        HTComplexRecipeBuilder
            .mixing(
                itemCreator.fromTagKey(HCMaterialPrefixes.DUST, HCMaterial.Minerals.REDSTONE),
                fluidCreator.fromTagKey(RagiumFluids.CREOSOTE, 1000),
            ).setResult(fluidResult.create(RagiumFluids.LUBRICANT, 500))
            .saveSuffixed(output, "_from_creosote_with_redstone")
        // Creosote + Raginite -> Lubricant
        HTComplexRecipeBuilder
            .mixing(
                itemCreator.fromTagKey(HCMaterialPrefixes.DUST, RagiumMaterial.RAGINITE),
                fluidCreator.fromTagKey(RagiumFluids.CREOSOTE, 1000),
            ).setResult(fluidResult.create(RagiumFluids.LUBRICANT, 750))
            .saveSuffixed(output, "_from_creosote_with_raginite")

        // Ethanol + Seed Oil -> Biofuel
        HTComplexRecipeBuilder
            .mixing(
                itemCreator.fromItem(RagiumItems.SEED_OIL_DROP),
                fluidCreator.fromTagKey(RagiumFluids.ETHANOL, 750),
            ).setResult(fluidResult.create(RagiumFluids.BIOFUEL, 500))
            .saveSuffixed(output, "_from_ethanol")
    }
}
