package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.data.recipe.HTChemicalRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTDistillingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemOrFluidRecipeBuilder
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
        HTChemicalRecipeBuilder.mixing(output) {
            fluidIngredients += inputCreator.create(RagiumFluids.SUNFLOWER_OIL)
            fluidIngredients += inputCreator.create(RagiumTags.Fluids.ALCOHOL, 3000)
            fluidResults += resultCreator.create(RagiumFluids.BIOFUEL, 3000)
            fluidResults += resultCreator.create(RagiumFluids.GLYCEROL)
        }
    }

    @HTBuilderMarker
    @JvmStatic
    private inline fun pyrolyzing(builderAction: HTItemOrFluidRecipeBuilder.() -> Unit) {
        // Without Nitrogen
        HTItemOrFluidRecipeBuilder.pyrolyzing(output, builderAction)
        // With Nitrogen
        HTItemOrFluidRecipeBuilder.pyrolyzing(output) {
            builderAction()
            ingredient += inputCreator.create(RagiumFluids.NITROGEN)
            time /= 2
            recipeId suffix "_with_nitrogen"
        }
    }

    @JvmStatic
    private fun charcoal() {
        // Log -> Charcoal
        pyrolyzing {
            ingredient += inputCreator.create(ItemTags.LOGS_THAT_BURN, 8)
            result += resultCreator.material(CommonTagPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL, 8)
            result += resultCreator.create(RagiumFluids.CREOSOTE, 1000)
            recipeId suffix "_from_log"
        }
        // Compressed Sawdust -> Charcoal
        pyrolyzing {
            ingredient += inputCreator.create(HCItems.COMPRESSED_SAWDUST, 8)
            result += resultCreator.material(CommonTagPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL, 8)
            result += resultCreator.create(RagiumFluids.CREOSOTE, 500)
            time /= 3
            recipeId suffix "_from_sawdust"
        }
    }

    @JvmStatic
    private fun coal() {
        // Coal -> Coke + Creosote
        pyrolyzing {
            ingredient += inputCreator.create(CommonTagPrefixes.FUEL, VanillaMaterialKeys.COAL, 8)
            result += resultCreator.material(CommonTagPrefixes.FUEL, CommonMaterialKeys.COAL_COKE, 8)
            result += resultCreator.create(RagiumFluids.CREOSOTE, 2000)
        }

        pyrolyzing {
            ingredient += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.COAL, 8)
            result += resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.COAL_COKE, 8)
            result += resultCreator.create(RagiumFluids.CREOSOTE, 2000)
        }

        pyrolyzing {
            ingredient += inputCreator.create(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.COAL)
            result += resultCreator.material(CommonTagPrefixes.BLOCK, CommonMaterialKeys.COAL_COKE)
            result += resultCreator.create(RagiumFluids.CREOSOTE, 2000)
        }
        // Creosote + Formaldehyde -> Polymer Resin
        HTChemicalRecipeBuilder.mixing(output) {
            fluidIngredients += inputCreator.create(RagiumFluids.CREOSOTE)
            fluidIngredients += inputCreator.create(RagiumFluids.METHANAL)

            itemResults += resultCreator.create(HCItems.POLYMER_RESIN, 4)
        }
        // C + H2O -> CO + H2
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.COAL))
            itemIngredients += inputCreator.create(Items.BLAZE_POWDER, amount = 0)
            fluidIngredients += inputCreator.water(1000)

            fluidResults += resultCreator.create(RagiumFluids.CARBON_MONOXIDE)
            fluidResults += resultCreator.create(RagiumFluids.HYDROGEN)
            recipeId replace id("synthetic_gas_from_coal")
        }

        // Coal Dust + Residue Oil -> Synthetic Oil
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.COAL)
            fluidIngredients += inputCreator.create(RagiumFluids.RESIDUE_OIL, 500)
            fluidResults += resultCreator.create(RagiumFluids.SYNTHETIC_OIL, 500)
        }
        // Synthetic Oil -> Naphtha
        HTDistillingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(RagiumFluids.SYNTHETIC_OIL, 500)
            fluidResults += resultCreator.create(RagiumFluids.NAPHTHA, 250)
            recipeId suffix "_from_synthetic_oil"
        }
    }

    @JvmStatic
    private fun crudeOil() {
        // Oil Sand -> Sand + Crude Oil
        pyrolyzing {
            ingredient += inputCreator.create(HCBlocks.OIL_SAND, 4)
            result += resultCreator.create(Items.SAND, 4)
            result += resultCreator.create(RagiumFluids.CRUDE_OIL, 2000)
            recipeId replace id("crude_oil_from_sand")
        }
        // Oil Shale -> Clay + Crude Oil
        pyrolyzing {
            ingredient += inputCreator.create(HCBlocks.OIL_SHALE, 4)
            result += resultCreator.create(Items.CLAY_BALL, 16)
            result += resultCreator.create(RagiumFluids.CRUDE_OIL, 2000)
            recipeId replace id("crude_oil_from_shale")
        }
        // Soul Sand/Soil -> Sand + Crude Oil
        pyrolyzing {
            ingredient += inputCreator.create(ItemTags.SOUL_FIRE_BASE_BLOCKS, 4)
            result += resultCreator.create(Items.SAND, 4)
            result += resultCreator.create(RagiumFluids.CRUDE_OIL)
            recipeId replace id("crude_oil_from_soul")
        }

        // Crude Oil -> Naphtha + Residue Oil
        HTDistillingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(RagiumFluids.CRUDE_OIL, 1000)
            fluidResults += resultCreator.create(RagiumFluids.RESIDUE_OIL, 250)
            fluidResults += resultCreator.create(RagiumFluids.NAPHTHA, 750)
            recipeId suffix "_from_crude_oil"
        }
        // Naphtha -> C2H4 + Fuel
        HTDistillingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(RagiumFluids.NAPHTHA, 1000)
            itemResult = resultCreator.create(HCItems.POLYMER_RESIN)
            fluidResults += resultCreator.create(RagiumFluids.FUEL, 500)
            fluidResults += resultCreator.create(RagiumFluids.ETHENE, 250)
            recipeId suffix "_from_naphtha"
        }
        // Residue Oil -> Raw Rubber + Fuel
        HTDistillingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(RagiumFluids.RESIDUE_OIL, 1000)
            itemResult = resultCreator.create(HCItems.RAW_RUBBER, 3)
            fluidResults += resultCreator.create(RagiumFluids.FUEL, 250)
            time *= 3
            recipeId suffix "_from_residue_oil"
        }
    }

    @JvmStatic
    private fun methane() {
        // CH4 + H2O -> CO + 3x H2
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.NICKEL, amount = 0)
            fluidIngredients += inputCreator.create(RagiumFluids.METHANE)
            fluidIngredients += inputCreator.water(1000)

            fluidResults += resultCreator.create(RagiumFluids.CARBON_MONOXIDE)
            fluidResults += resultCreator.create(RagiumFluids.HYDROGEN, 3000)
            recipeId replace id("steam_methane_reforming")
        }
        // CO + H2O -> CO2 + H2
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.IRON, amount = 0)
            fluidIngredients += inputCreator.create(RagiumFluids.CARBON_MONOXIDE)
            fluidIngredients += inputCreator.water(1000)

            fluidResults += resultCreator.create(RagiumFluids.CARBON_DIOXIDE)
            fluidResults += resultCreator.create(RagiumFluids.HYDROGEN)
            recipeId replace id("water_gas_shift_reaction")
        }

        // CO + 2x H2 -[Cu]-> CH3OH
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.COPPER, amount = 0)
            fluidIngredients += inputCreator.create(RagiumFluids.CARBON_MONOXIDE)
            fluidIngredients += inputCreator.create(RagiumFluids.HYDROGEN, 2000)

            fluidResults += resultCreator.create(RagiumFluids.METHANOL)
        }
        // CH3OH -[Ag]-> HCHO + H2
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SILVER, amount = 0)
            fluidIngredients += inputCreator.create(RagiumFluids.METHANOL)

            fluidResults += resultCreator.create(RagiumFluids.METHANAL)
            fluidResults += resultCreator.create(RagiumFluids.HYDROGEN)
        }

        // CH3OH + CO -> HCOOCH3
        // HCOOCH3 + H2O -> HCOOH + CH3OH
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.PLATINUM, amount = 0)
            fluidIngredients += inputCreator.create(RagiumFluids.CARBON_MONOXIDE)
            fluidIngredients += inputCreator.water(1000)

            fluidResults += resultCreator.create(RagiumFluids.METHANOIC_ACID)
        }
        // Latex + HCOOH -> Raw Rubber
        HTChemicalRecipeBuilder.mixing(output) {
            fluidIngredients += inputCreator.create(HCFluids.LATEX)
            fluidIngredients += inputCreator.create(RagiumFluids.METHANOIC_ACID, 250)

            itemResults += resultCreator.create(HCItems.RAW_RUBBER, 4)
        }
    }

    @JvmStatic
    private fun crimson() {
        // Crimson Stem -> Crimson Blood
        pyrolyzing {
            ingredient += inputCreator.create(ItemTags.CRIMSON_STEMS, 8)
            result += resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.CARBON, 4)
            result += resultCreator.molten(HCMaterialKeys.CRIMSON_CRYSTAL)
            recipeId suffix "_from_crimson_stem"
        }
        // Crimson Dust + Lava -> Blaze Powder
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, HCMaterialKeys.CRIMSON_CRYSTAL)
            fluidIngredients += inputCreator.lava(250)

            itemResults += resultCreator.create(Items.BLAZE_POWDER)
            recipeId suffix "_from_crimson"
        }
    }

    @JvmStatic
    private fun warped() {
        // Warped Stem -> Dew of the Warp
        pyrolyzing {
            ingredient += inputCreator.create(ItemTags.WARPED_STEMS, 8)
            result += resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.CARBON, 4)
            result += resultCreator.molten(HCMaterialKeys.WARPED_CRYSTAL)
            recipeId suffix "_from_warped_stem"
        }
        // Warped Dust + Lava -> Ender Pearl
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, HCMaterialKeys.WARPED_CRYSTAL)
            fluidIngredients += inputCreator.lava(250)

            itemResults += resultCreator.create(Items.ENDER_PEARL)
            recipeId suffix "_from_warped"
        }
    }
}
