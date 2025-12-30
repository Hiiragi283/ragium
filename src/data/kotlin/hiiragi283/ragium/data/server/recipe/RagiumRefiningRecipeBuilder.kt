package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTRefiningRecipeBuilder
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.world.item.Items

object RagiumRefiningRecipeBuilder : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Creosote -> Tar + Lubricant
        HTRefiningRecipeBuilder
            .create(
                fluidCreator.fromTagKey(RagiumFluids.CREOSOTE, 1000),
                fluidResult.create(RagiumFluids.LUBRICANT, 500),
            ).setResult(itemResult.create(HCItems.TAR))
            .saveSuffixed(output, "_from_creosote")

        // Crude Oil -> Naphtha + Tar
        HTRefiningRecipeBuilder
            .create(
                fluidCreator.fromTagKey(RagiumFluids.CRUDE_OIL, 1000),
                fluidResult.create(RagiumFluids.NAPHTHA, 750),
            ).setResult(itemResult.create(HCItems.TAR))
            .saveSuffixed(output, "_from_oil")
        // Naphtha -> Sulfur + Fuel
        HTRefiningRecipeBuilder
            .create(
                fluidCreator.fromTagKey(RagiumFluids.NAPHTHA, 750),
                fluidResult.create(RagiumFluids.FUEL, 500),
            ).setResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Minerals.SULFUR))
            .saveSuffixed(output, "_from_naphtha")

        // Crude Bio -> Ethanol
        HTRefiningRecipeBuilder
            .create(
                fluidCreator.fromTagKey(RagiumFluids.CRUDE_BIO, 1000),
                fluidResult.create(RagiumFluids.ETHANOL, 750),
            ).setResult(itemResult.create(Items.BONE_MEAL))
            .saveSuffixed(output, "_from_bio")
        // Ethanol + Sulfuric Acid -> Biofuel
    }
}
