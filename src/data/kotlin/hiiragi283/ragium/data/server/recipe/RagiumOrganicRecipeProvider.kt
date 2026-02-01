package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTPyrolyzingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.tags.ItemTags

object RagiumOrganicRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        charcoal()
        coal()
        crudeOil()
        crudeBio()
    }

    @JvmStatic
    private fun charcoal() {
        // Log -> Charcoal
        HTPyrolyzingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(ItemTags.LOGS_THAT_BURN, 8)
            itemResult = resultCreator.material(CommonTagPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL, 8)
            fluidResult = resultCreator.create(RagiumFluids.CREOSOTE, 1000)
            recipeId suffix "_from_log"
        }
        // Compressed Sawdust -> Charcoal
        HTPyrolyzingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(HCItems.COMPRESSED_SAWDUST, 8)
            itemResult = resultCreator.material(CommonTagPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL, 8)
            fluidResult = resultCreator.create(RagiumFluids.CREOSOTE, 1000)
            time /= 3
            recipeId suffix "_from_sawdust"
        }
    }

    @JvmStatic
    private fun coal() {
        // Coal -> Coke + Creosote
        HTPyrolyzingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.FUEL, VanillaMaterialKeys.COAL, 8)
            itemResult = resultCreator.material(CommonTagPrefixes.FUEL, CommonMaterialKeys.COAL_COKE, 8)
            fluidResult = resultCreator.create(RagiumFluids.CREOSOTE, 2000)
        }

        HTPyrolyzingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.COAL, 8)
            itemResult = resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.COAL_COKE, 8)
            fluidResult = resultCreator.create(RagiumFluids.CREOSOTE, 2000)
        }

        HTPyrolyzingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.COAL)
            itemResult = resultCreator.material(CommonTagPrefixes.BLOCK, CommonMaterialKeys.COAL_COKE)
            fluidResult = resultCreator.create(RagiumFluids.CREOSOTE, 2000)
        }

        // Coal + Water -> Coal Gas

        // Coal Gas -> LPG
        HTSingleRecipeBuilder.refining(output) {
            ingredient = inputCreator.create(RagiumFluids.COAL_GAS, 750)
            result = resultCreator.create(RagiumFluids.LPG, 250)
            recipeId suffix "_from_coal_gas"
        }

        // Coal Dust + Residue Oil -> Coal Liquid
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.COAL)
            fluidIngredients += inputCreator.create(RagiumFluids.RESIDUE_OIL, 500)
            result += resultCreator.create(RagiumFluids.COAL_LIQUID, 500)
        }
        // Coal Liquid -> Naphtha
        HTSingleRecipeBuilder.refining(output) {
            ingredient = inputCreator.create(RagiumFluids.COAL_LIQUID, 750)
            result = resultCreator.create(RagiumFluids.NAPHTHA, 500)
            recipeId suffix "_from_coal_liquid"
        }
    }

    @JvmStatic
    private fun crudeOil() {
        // Crude Oil -> Naphtha
        HTSingleRecipeBuilder.refining(output) {
            ingredient = inputCreator.create(RagiumFluids.CRUDE_OIL, 500)
            result = resultCreator.create(RagiumFluids.NAPHTHA, 250)
            recipeId suffix "_from_crude_oil"
        }
        // Crude Oil + LPG + Naphtha + Residue Oil

        // LPG -> Ethylene
        HTSingleRecipeBuilder.refining(output) {
            ingredient = inputCreator.create(RagiumFluids.LPG, 500)
            result = resultCreator.create(RagiumFluids.ETHYLENE, 250)
            recipeId suffix "_from_lpg"
        }
        // LPG -> Methane + Ethylene + Butadiene

        // Naphtha -> Gasoline
        HTSingleRecipeBuilder.refining(output) {
            ingredient = inputCreator.create(RagiumFluids.NAPHTHA, 500)
            result = resultCreator.create(RagiumFluids.GASOLINE, 250)
            recipeId suffix "_from_naphtha"
        }
        // Naphtha -> Ethylene + Gasoline + Lubricant
    }

    @JvmStatic
    private fun crudeBio() {
    }
}
