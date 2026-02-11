package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.data.recipe.HTFluidWithItemRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTPyrolyzingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTReactingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items

object RagiumOrganicRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        charcoal()

        coal()
        crudeOil()
        methane()

        crimson()
        warped()

        // Organic Oil + Alcohol -> Biofuel + Glycerol
        HTReactingRecipeBuilder.create(output) {
            fluidIngredients += inputCreator.create(RagiumFluids.SUNFLOWER_OIL)
            fluidIngredients += inputCreator.create(RagiumTags.Fluids.ALCOHOL, 3000)
            fluidResults += resultCreator.create(RagiumFluids.BIOFUEL, 3000)
            fluidResults += resultCreator.create(RagiumFluids.GLYCEROL)
        }
    }

    @HTBuilderMarker
    @JvmStatic
    private inline fun pyrolyzing(builderAction: HTPyrolyzingRecipeBuilder.() -> Unit) {
        // Without Nitrogen
        HTPyrolyzingRecipeBuilder.create(output, builderAction)
        // With Nitrogen
        HTPyrolyzingRecipeBuilder.create(output) {
            builderAction()
            fluidIngredient = inputCreator.create(RagiumFluids.NITROGEN)
            time /= 2
            recipeId suffix "_with_nitrogen"
        }
    }

    @JvmStatic
    private fun charcoal() {
        // Log -> Charcoal
        pyrolyzing {
            itemIngredient = inputCreator.create(ItemTags.LOGS_THAT_BURN, 8)
            itemResult = resultCreator.material(CommonTagPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL, 8)
            fluidResult = resultCreator.create(RagiumFluids.CREOSOTE, 1000)
            recipeId suffix "_from_log"
        }
        // Compressed Sawdust -> Charcoal
        pyrolyzing {
            itemIngredient = inputCreator.create(HCItems.COMPRESSED_SAWDUST, 8)
            itemResult = resultCreator.material(CommonTagPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL, 8)
            fluidResult = resultCreator.create(RagiumFluids.CREOSOTE, 500)
            time /= 3
            recipeId suffix "_from_sawdust"
        }
    }

    @JvmStatic
    private fun coal() {
        // Coal -> Coke + Creosote
        pyrolyzing {
            itemIngredient = inputCreator.create(CommonTagPrefixes.FUEL, VanillaMaterialKeys.COAL, 8)
            itemResult = resultCreator.material(CommonTagPrefixes.FUEL, CommonMaterialKeys.COAL_COKE, 8)
            fluidResult = resultCreator.create(RagiumFluids.CREOSOTE, 2000)
        }

        pyrolyzing {
            itemIngredient = inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.COAL, 8)
            itemResult = resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.COAL_COKE, 8)
            fluidResult = resultCreator.create(RagiumFluids.CREOSOTE, 2000)
        }

        pyrolyzing {
            itemIngredient = inputCreator.create(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.COAL)
            itemResult = resultCreator.material(CommonTagPrefixes.BLOCK, CommonMaterialKeys.COAL_COKE)
            fluidResult = resultCreator.create(RagiumFluids.CREOSOTE, 2000)
        }
        // Creosote + Redstone -> Lubricant
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE)
            fluidIngredients += inputCreator.create(RagiumFluids.CREOSOTE, 1000)
            result += resultCreator.create(RagiumFluids.LUBRICANT, 500)
            time /= 2
            recipeId suffix "_from_creosote_with_redstone"
        }
        // Creosote + Raginite -> Lubricant
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            fluidIngredients += inputCreator.create(RagiumFluids.CREOSOTE, 1000)
            result += resultCreator.create(RagiumFluids.LUBRICANT, 750)
            time /= 2
            recipeId suffix "_from_creosote_with_raginite"
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
            fluidIngredients += inputCreator.create(RagiumFluids.SYNTHETIC_GAS, 1000)
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

        // Naphtha -> Fuel
        HTSingleRecipeBuilder.refining(output) {
            ingredient = inputCreator.create(RagiumFluids.NAPHTHA, 500)
            result = resultCreator.create(RagiumFluids.FUEL, 250)
            recipeId suffix "_from_naphtha"
        }
        // Naphtha -> C2H4 + C4H6 + Fuel

        // Residue Oil -> Lubricant  Asphalt
    }

    @JvmStatic
    private fun methane() {
        // CH4 + Water -> Methanol
        // CH4 + CO2 -> Synthetic Gas
        HTReactingRecipeBuilder.create(output) {
            fluidIngredients += inputCreator.create(RagiumFluids.METHANE, 1000)
            fluidIngredients += inputCreator.create(RagiumFluids.CARBON_DIOXIDE, 1000)
            catalyst = listOf(CommonMaterialKeys.RUTHENIUM, CommonMaterialKeys.RHODIUM)
                .map(CommonTagPrefixes.DUST::itemTagKey)
                .let(inputCreator::create)
            fluidResults += resultCreator.create(RagiumFluids.SYNTHETIC_GAS, 2000)
            recipeId replace RagiumAPI.id("dry_reforming")
        }
        // CH4 + H2O -> CO2 + 4x H2
        HTReactingRecipeBuilder.create(output) {
            fluidIngredients += inputCreator.create(RagiumFluids.METHANE, 1000)
            fluidIngredients += inputCreator.water(1000)
            catalyst = inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.NICKEL)
            fluidResults += resultCreator.create(RagiumFluids.CARBON_DIOXIDE, 1000)
            fluidResults += resultCreator.create(RagiumFluids.HYDROGEN, 4000)
            recipeId suffix "_from_methane"
        }
    }

    @JvmStatic
    private fun crimson() {
        // Crimson Stem -> Crimson Blood
        pyrolyzing {
            itemIngredient = inputCreator.create(ItemTags.CRIMSON_STEMS, 8)
            itemResult = resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.CARBON, 4)
            fluidResult = resultCreator.molten(HCMaterialKeys.CRIMSON_CRYSTAL)
            recipeId suffix "_from_crimson_stem"
        }
        // Crimson Dust + Lava -> Blaze Powder
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(CommonTagPrefixes.DUST, HCMaterialKeys.CRIMSON_CRYSTAL)
            fluidIngredient = inputCreator.lava(250)
            result = resultCreator.create(Items.BLAZE_POWDER)
            recipeId suffix "_from_crimson"
        }
    }

    @JvmStatic
    private fun warped() {
        // Warped Stem -> Dew of the Warp
        pyrolyzing {
            itemIngredient = inputCreator.create(ItemTags.WARPED_STEMS, 8)
            itemResult = resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.CARBON, 4)
            fluidResult = resultCreator.molten(HCMaterialKeys.WARPED_CRYSTAL)
            recipeId suffix "_from_warped_stem"
        }
        // Warped Dust + Lava -> Ender Pearl
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(CommonTagPrefixes.DUST, HCMaterialKeys.WARPED_CRYSTAL)
            fluidIngredient = inputCreator.lava(250)
            result = resultCreator.create(Items.ENDER_PEARL)
            recipeId suffix "_from_warped"
        }
    }
}
