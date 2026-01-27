package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTRefiningRecipeBuilder
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems

object RagiumRefiningRecipeBuilder : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Crude Oil -> Naphtha + Tar
        HTRefiningRecipeBuilder.create(output) {
            ingredient = inputCreator.create(RagiumFluids.CRUDE_OIL, 1000)
            primalResult = resultCreator.create(RagiumFluids.NAPHTHA, 750)
            result += resultCreator.create(RagiumItems.TAR)
            recipeId suffix "_from_oil"
        }
        // Naphtha -> Sulfur + Fuel
        HTRefiningRecipeBuilder.create(output) {
            ingredient = inputCreator.create(RagiumFluids.NAPHTHA, 750)
            primalResult = resultCreator.create(RagiumFluids.FUEL, 500)
            result += resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR)
            recipeId suffix "_from_naphtha"
        }

        // Crude Bio -> Ethanol + Fertilizer
        HTRefiningRecipeBuilder.create(output) {
            ingredient = inputCreator.create(RagiumFluids.CRUDE_BIO, 1000)
            primalResult = resultCreator.create(RagiumFluids.ETHANOL, 750)
            result += resultCreator.create(RagiumFluids.FERTILIZER, 250)
            recipeId suffix "_from_bio"
        }
    }
}
