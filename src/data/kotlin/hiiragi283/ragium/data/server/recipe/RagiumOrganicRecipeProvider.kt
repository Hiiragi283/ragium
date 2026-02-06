package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTPyrolyzingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTReactingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items

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
            fluidResult = resultCreator.create(RagiumFluids.CREOSOTE, 500)
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
        // Coal + Water -> Synthetic Gas
        HTReactingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.COAL))
            fluidIngredients += inputCreator.water(1000)
            catalyst = inputCreator.create(Items.BLAZE_POWDER)
            fluidResults += resultCreator.create(RagiumFluids.SYNTHETIC_GAS, 250)
            recipeId suffix "_from_coal"
        }
        // Synthetic Gas + H2O -> CO2 + 2x H2
        HTReactingRecipeBuilder.create(output) {
            fluidIngredients += inputCreator.create(RagiumFluids.SYNTHETIC_GAS, 250)
            fluidIngredients += inputCreator.water(1000)
            catalyst = inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.PLATINUM)
            fluidResults += resultCreator.create(RagiumFluids.CARBON_DIOXIDE, 1000)
            fluidResults += resultCreator.create(RagiumFluids.HYDROGEN, 2000)
            recipeId replace RagiumAPI.id("water_gas_shift_reaction")
        }

        // Coal Dust + Residue Oil -> Synthetic Oil
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.COAL)
            fluidIngredients += inputCreator.create(RagiumFluids.RESIDUE_OIL, 500)
            result += resultCreator.create(RagiumFluids.SYNTHETIC_OIL, 500)
        }
        // Synthetic Oil -> Naphtha
        HTSingleRecipeBuilder.refining(output) {
            ingredient = inputCreator.create(RagiumFluids.SYNTHETIC_OIL, 500)
            result = resultCreator.create(RagiumFluids.NAPHTHA, 250)
            recipeId suffix "_from_synthetic_oil"
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
        // Crude Oil + C2H4 + Naphtha + Residue Oil

        // Naphtha -> Gasoline
        HTSingleRecipeBuilder.refining(output) {
            ingredient = inputCreator.create(RagiumFluids.NAPHTHA, 500)
            result = resultCreator.create(RagiumFluids.GASOLINE, 250)
            recipeId suffix "_from_naphtha"
        }
        // Naphtha -> C2H4 + C4H6 + Gasoline

        // Residue Oil -> Lubricant  Asphalt
    }

    @JvmStatic
    private fun crudeBio() {
    }
}
