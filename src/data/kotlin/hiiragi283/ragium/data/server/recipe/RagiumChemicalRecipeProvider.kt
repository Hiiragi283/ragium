package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTagPrefixes
import hiiragi283.ragium.common.data.recipe.HTChemicalRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemOrFluidRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTMeltingRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items

object RagiumChemicalRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        overworld()
        nether()
        end()
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

    //    Overworld    //

    @JvmStatic
    private fun overworld() {
        coal()
        breeze()
        slime()
    }

    @JvmStatic
    private fun coal() {
        // Log -> Charcoal
        pyrolyzing {
            ingredient += inputCreator.create(ItemTags.LOGS_THAT_BURN, 8)
            result += resultCreator.material(CommonParts.FUEL, VanillaMaterialKeys.CHARCOAL, 8)
            result += resultCreator.create(RagiumFluids.CREOSOTE, 1000)
            recipeId suffix "_from_log"
        }
        // Compressed Sawdust -> Charcoal
        pyrolyzing {
            ingredient += inputCreator.create(RagiumTagPrefixes.PELLET, VanillaMaterialKeys.WOOD, 8)
            result += resultCreator.material(CommonParts.FUEL, VanillaMaterialKeys.CHARCOAL, 8)
            result += resultCreator.create(RagiumFluids.CREOSOTE, 500)
            time /= 3
            recipeId suffix "_from_sawdust"
        }

        // Coal -> Coke + Creosote
        pyrolyzing {
            ingredient += inputCreator.create(CommonTagPrefixes.FUEL, VanillaMaterialKeys.COAL, 8)
            result += resultCreator.material(CommonParts.FUEL, CommonMaterialKeys.COAL_COKE, 8)
            result += resultCreator.create(RagiumFluids.CREOSOTE, 2000)
        }
        pyrolyzing {
            ingredient += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.COAL, 8)
            result += resultCreator.material(CommonParts.DUST, CommonMaterialKeys.COAL_COKE, 8)
            result += resultCreator.create(RagiumFluids.CREOSOTE, 2000)
        }
        pyrolyzing {
            ingredient += inputCreator.create(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.COAL)
            result += resultCreator.material(CommonParts.BLOCK, CommonMaterialKeys.COAL_COKE)
            result += resultCreator.create(RagiumFluids.CREOSOTE, 2000)
        }

        // Coal + Water -> Synthetic Gas
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.COAL))
            itemIngredients += inputCreator.create(Items.BLAZE_POWDER, amount = 0)
            fluidIngredients += inputCreator.water()

            fluidResults += resultCreator.create(RagiumFluids.SYNTHETIC_GAS, 250)
            recipeId suffix "_from_coal"
        }
        // Synthetic Gas + H2O -> CO2 + 2x H2
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.PLATINUM, amount = 0)
            fluidIngredients += inputCreator.create(RagiumFluids.SYNTHETIC_GAS, 1000)
            fluidIngredients += inputCreator.water()

            fluidResults += resultCreator.create(RagiumFluids.HYDROGEN, 2000)
            recipeId replace RagiumAPI.id("water_gas_shift_reaction")
        }
        // Coal -> Synthetic Oil
        HTMeltingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(baseOrDust(VanillaMaterialKeys.COAL))
            result = resultCreator.create(RagiumFluids.SYNTHETIC_OIL, 125)
        }
        // Synthetic Oil -> Fuel
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(RagiumFluids.SYNTHETIC_OIL, 500)
            result += resultCreator.create(RagiumFluids.FUEL, 200)
            recipeId suffix "_from_synthetic_oil"
        }
    }

    @JvmStatic
    private fun breeze() {
        // Wind Charge -> N2
        HTMeltingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.WIND_CHARGE)
            result = resultCreator.create(RagiumFluids.NITROGEN, 125)
        }
        // Wind Charge + Blue Ice -> Cryo-Charge

        // Cryo-Charge -> liq N2
    }

    @JvmStatic
    private fun slime() {
        // Slimeball + H2O -> NaOH aq
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(Items.SLIME_BALL)
            ingredient += inputCreator.water()
            result += resultCreator.create(RagiumFluids.NAOH_SOLUTION)
        }
        // H2SO4 + 2x NaOH aq -> Na2SO4 + 2x H2O
        HTChemicalRecipeBuilder.mixing(output) {
            fluidIngredients += inputCreator.create(RagiumFluids.SULFURIC_ACID)
            fluidIngredients += inputCreator.create(RagiumFluids.NAOH_SOLUTION)
            itemResults += resultCreator.create(Items.MAGMA_CREAM)
            fluidResults += resultCreator.water(2000)
            recipeId replace id("magma_cream_from_neutralization")
        }
    }

    //    Nether    //

    @JvmStatic
    private fun nether() {
        crudeOil()
        crimson()
        warped()

        ghast()
        explosive()
        blaze()
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
        // Soul Sand -> Sand + Crude Oil
        pyrolyzing {
            ingredient += inputCreator.create(Items.SOUL_SAND, 4)
            result += resultCreator.create(Items.SAND, 4)
            result += resultCreator.create(RagiumFluids.CRUDE_OIL)
            recipeId replace id("crude_oil_from_soul")
        }

        // Crude Oil -> Petroleum Coke + Naphtha
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(RagiumFluids.CRUDE_OIL, 500)
            result += resultCreator.material(CommonParts.FUEL, RagiumMaterialKeys.PETROLEUM_COKE)
            result += resultCreator.create(RagiumFluids.NAPHTHA, 300)
            recipeId suffix "_from_crude_oil"
        }
        // Naphtha -> Polymer Resin + Fuel
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(RagiumFluids.NAPHTHA, 500)
            result += resultCreator.create(HCItems.POLYMER_RESIN)
            result += resultCreator.create(RagiumFluids.FUEL, 300)
            recipeId suffix "_from_naphtha"
        }
        // Polymer Resin + Water -> Plastic
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.water(250)
            ingredient += inputCreator.create(HCItems.POLYMER_RESIN)
            result += resultCreator.material(CommonParts.PLATE, CommonMaterialKeys.PLASTIC, 2)
        }

        // CH4 + H2O -> Synthetic Gas
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.NICKEL, amount = 0)
            fluidIngredients += inputCreator.create(RagiumFluids.METHANE, 1000)
            fluidIngredients += inputCreator.water()

            fluidResults += resultCreator.create(RagiumFluids.SYNTHETIC_GAS, 2000)
            recipeId suffix "_from_methane"
        }
    }

    @JvmStatic
    private fun crimson() {
        // Crimson Stem -> Crimson Blood
        pyrolyzing {
            ingredient += inputCreator.create(ItemTags.CRIMSON_STEMS, 8)
            result += resultCreator.molten(HCMaterialKeys.CRIMSON_CRYSTAL)
            recipeId suffix "_from_crimson_stem"
        }
        // Crimson Dust + Lava -> Blaze Powder
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(CommonTagPrefixes.DUST, HCMaterialKeys.CRIMSON_CRYSTAL)
            ingredient += inputCreator.lava(250)

            result += resultCreator.create(Items.BLAZE_POWDER)
            recipeId suffix "_from_crimson"
        }
    }

    @JvmStatic
    private fun warped() {
        // Warped Stem -> Dew of the Warp
        pyrolyzing {
            ingredient += inputCreator.create(ItemTags.WARPED_STEMS, 8)
            result += resultCreator.molten(HCMaterialKeys.WARPED_CRYSTAL)
            recipeId suffix "_from_warped_stem"
        }
        // Warped Dust + Lava -> Ender Pearl
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(CommonTagPrefixes.DUST, HCMaterialKeys.WARPED_CRYSTAL)
            ingredient += inputCreator.lava(250)

            result += resultCreator.create(Items.ENDER_PEARL)
            recipeId suffix "_from_warped"
        }
    }

    @JvmStatic
    private fun ghast() {
        // Soul Soil -> KNO3

        // 2x KNO3 + H2SO4 -> 2x HNO3 + K2SO4
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SALTPETER, 2)
            fluidIngredients += inputCreator.create(RagiumFluids.SULFURIC_ACID)
            fluidResults += resultCreator.create(RagiumFluids.NITRIC_ACID, 2000)
            recipeId suffix "_from_saltpeter"
        }

        // Ghast Tear -> NO2
        HTItemOrFluidRecipeBuilder.pyrolyzing(output) {
            ingredient += inputCreator.create(Items.GHAST_TEAR)
            result += resultCreator.create(RagiumFluids.NITROGEN_DIOXIDE)
        }
        // NO2 + H2O -> HNO3
        HTChemicalRecipeBuilder.mixing(output) {
            fluidIngredients += inputCreator.create(RagiumFluids.NITROGEN_DIOXIDE)
            fluidIngredients += inputCreator.water()
            fluidResults += resultCreator.create(RagiumFluids.NITRIC_ACID)
        }
    }

    @JvmStatic
    private fun explosive() {
        // HNO3 + H2SO4 -> Mixture Acid
        HTChemicalRecipeBuilder.mixing(output) {
            fluidIngredients += inputCreator.create(RagiumFluids.NITRIC_ACID, 500)
            fluidIngredients += inputCreator.create(RagiumFluids.SULFURIC_ACID, 500)
            fluidResults += resultCreator.create(RagiumFluids.MIXTURE_ACID)
        }
    }

    @JvmStatic
    private fun blaze() {
        // Blaze Powder -> SO2
        HTItemOrFluidRecipeBuilder.pyrolyzing(output) {
            ingredient += inputCreator.create(Items.BLAZE_POWDER)
            result += resultCreator.create(RagiumFluids.SULFUR_DIOXIDE)
        }
        // SO2 + H2O -> H2SO4
        HTChemicalRecipeBuilder.mixing(output) {
            fluidIngredients += inputCreator.create(RagiumFluids.SULFUR_DIOXIDE)
            fluidIngredients += inputCreator.water()
            fluidResults += resultCreator.create(RagiumFluids.SULFURIC_ACID)
        }
    }

    //    The End    //

    @JvmStatic
    private fun end() {
    }
}
