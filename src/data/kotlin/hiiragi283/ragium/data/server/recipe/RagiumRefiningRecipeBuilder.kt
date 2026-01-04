package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTRefiningRecipeBuilder
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems

object RagiumRefiningRecipeBuilder : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Crude Oil -> Naphtha + Tar
        HTRefiningRecipeBuilder
            .create(
                fluidCreator.fromTagKey(RagiumFluids.CRUDE_OIL, 1000),
                fluidResult.create(RagiumFluids.NAPHTHA, 750),
            ).setResult(itemResult.create(RagiumItems.TAR))
            .saveSuffixed(output, "_from_oil")
        // Naphtha -> Sulfur + Fuel
        HTRefiningRecipeBuilder
            .create(
                fluidCreator.fromTagKey(RagiumFluids.NAPHTHA, 750),
                fluidResult.create(RagiumFluids.FUEL, 500),
            ).setResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Minerals.SULFUR))
            .saveSuffixed(output, "_from_naphtha")

        // Crude Bio -> Ethanol + Fertilizer
        HTRefiningRecipeBuilder
            .create(
                fluidCreator.fromTagKey(RagiumFluids.CRUDE_BIO, 1000),
                fluidResult.create(RagiumFluids.ETHANOL, 750),
            ).setResult(fluidResult.create(RagiumFluids.FERTILIZER, 250))
            .saveSuffixed(output, "_from_bio")
    }
}
