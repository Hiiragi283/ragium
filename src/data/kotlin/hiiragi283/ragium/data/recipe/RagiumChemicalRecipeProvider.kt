package hiiragi283.ragium.data.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTagPrefixes
import hiiragi283.ragium.common.data.recipe.HTAssemblingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTChemicalReactingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTCombiningRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemOrFluidRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTMeltingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags

object RagiumChemicalRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        overworld()
        nether()
        end()
        endGame()
    }

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
    private fun catalyst(material: HTMaterialLike): Ingredient = itemCreator.create(CommonTagPrefixes.DUST, material)

    //    Overworld    //

    @JvmStatic
    private fun overworld() {
        coal()
        breeze()
        slime()
        rubber()

        // 2x H2O -> 2x H2 + O2
        HTChemicalReactingRecipeBuilder.create(output) {
            ingredients += inputCreator.water()
            catalyst += itemCreator.create(Items.HEART_OF_THE_SEA)
            fluidResults += resultCreator.create(RagiumFluids.HYDROGEN)
            fluidResults += resultCreator.create(RagiumFluids.OXYGEN, 500)
            recipeId suffix "_from_water"
        }
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

        // Coal + Steam -> Synthetic Gas
        HTItemOrFluidRecipeBuilder.chemicalWashing(output) {
            ingredient += inputCreator.create(baseOrDust(VanillaMaterialKeys.COAL))
            ingredient += inputCreator.create(RagiumFluids.STEAM, 250)

            result += resultCreator.create(RagiumFluids.SYNTHETIC_GAS, 250)
            recipeId suffix "_from_coal"
        }
        // Synthetic Gas + H2O -> CO2 + 2x H2
        HTChemicalReactingRecipeBuilder.create(output) {
            ingredients += inputCreator.create(RagiumFluids.SYNTHETIC_GAS, 1000)
            ingredients += inputCreator.water()
            catalyst += catalyst(CommonMaterialKeys.PLATINUM)

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
        // Cryo-Charge
        HTShapedRecipeBuilder.create(output) {
            cross8()
            define('A') += Items.PACKED_ICE
            define('B') += Items.BLUE_ICE
            define('C') += CommonTagPrefixes.DUST to VanillaMaterialKeys.BREEZE
            resultStack += RagiumItems.CRYO_CHARGE
        }
        // Cryo-Charge -> liq N2
        HTMeltingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(RagiumItems.CRYO_CHARGE)
            result = resultCreator.create(RagiumFluids.LIQUID_NITROGEN, 125)
        }
    }

    @JvmStatic
    private fun slime() {
        // H2SO4 + 2x NaOH aq -> Na2SO4 + 2x H2O
        HTItemOrFluidRecipeBuilder.chemicalWashing(output) {
            ingredient += inputCreator.create(Items.SLIME_BALL)
            ingredient += inputCreator.create(RagiumFluids.SULFURIC_ACID)
            result += resultCreator.create(Items.MAGMA_CREAM)
            result += resultCreator.water(2000)
            recipeId replace id("magma_cream_from_neutralization")
        }

        // Slimeball + Water -> Glue
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(Tags.Items.SLIME_BALLS)
            ingredient += inputCreator.water()
            result += resultCreator.create(RagiumFluids.GLUE)
            recipeId suffix "_from_slime"
        }
        // Polymer Resin + Water -> Glue
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(HCItems.POLYMER_RESIN)
            ingredient += inputCreator.water()
            result += resultCreator.create(RagiumFluids.GLUE)
            recipeId suffix "_from_polymer"
        }
        // Borax + Glue -> Slimeball
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(CommonTagPrefixes.DUST, RagiumMaterialKeys.BORAX)
            ingredient += inputCreator.create(RagiumFluids.GLUE)
            result += resultCreator.create(Items.SLIME_BALL, 4)
            recipeId suffix "_from_glue"
        }
    }

    @JvmStatic
    private fun rubber() {
        // Raw Rubber + Sulfur -> Rubber
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.create(HCItems.CURED_RUBBER, 2)
            ingredients += inputCreator.create(HCItems.RAW_RUBBER)
            ingredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR)
        }
        // Carbon Compound
        HTShapelessRecipeBuilder.create(output) {
            ingredients += CommonTagPrefixes.DUST to CommonMaterialKeys.CARBON
            ingredients += CommonTagPrefixes.DUST to CommonMaterialKeys.SULFUR
            resultStack += RagiumItems.CARBON_COMPOUND
        }
        // Raw Rubber + Sulfur + Carbon -> Rubber
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.create(HCItems.CURED_RUBBER, 3)
            ingredients += inputCreator.create(HCItems.RAW_RUBBER)
            ingredients += inputCreator.create(RagiumItems.CARBON_COMPOUND)
            recipeId suffix "_with_carbon"
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
        silicon()
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
        // Polymer Resin + Oxygen -> Plastic
        HTItemOrFluidRecipeBuilder.chemicalWashing(output) {
            ingredient += inputCreator.create(HCItems.POLYMER_RESIN)
            ingredient += inputCreator.create(RagiumFluids.OXYGEN, 250)
            result += resultCreator.material(CommonParts.PLATE, CommonMaterialKeys.PLASTIC, 2)
        }

        // CH4 + H2O -> Synthetic Gas
        HTChemicalReactingRecipeBuilder.create(output) {
            ingredients += inputCreator.create(RagiumFluids.METHANE, 1000)
            ingredients += inputCreator.water()
            catalyst += itemCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.NICKEL)

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
        HTItemOrFluidRecipeBuilder.chemicalWashing(output) {
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
        HTItemOrFluidRecipeBuilder.chemicalWashing(output) {
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
        HTItemOrFluidRecipeBuilder.chemicalWashing(output) {
            ingredient += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SALTPETER, 2)
            ingredient += inputCreator.create(RagiumFluids.SULFURIC_ACID)
            result += resultCreator.create(Items.MAGMA_CREAM)
            result += resultCreator.create(RagiumFluids.NITRIC_ACID, 2000)
            recipeId suffix "_from_saltpeter"
        }

        // 3x H2 + N2 -> 2x NH3
        HTChemicalReactingRecipeBuilder.create(output) {
            ingredients += inputCreator.create(RagiumFluids.HYDROGEN, 3000)
            ingredients += inputCreator.create(RagiumFluids.NITROGEN)
            catalyst += catalyst(VanillaMaterialKeys.IRON)
            fluidResults += resultCreator.create(RagiumFluids.AMMONIA, 2000)
        }
        // 4x NH3 + 7x O2 -> 4x NO2 + 6x H2O
        HTChemicalReactingRecipeBuilder.create(output) {
            ingredients += inputCreator.create(RagiumFluids.AMMONIA, 4000)
            ingredients += inputCreator.create(RagiumFluids.OXYGEN, 7000)
            catalyst += catalyst(CommonMaterialKeys.PLATINUM)
            fluidResults += resultCreator.create(RagiumFluids.NITROGEN_DIOXIDE, 4000)
        }
        // Ghast Tear -> NO2
        HTItemOrFluidRecipeBuilder.pyrolyzing(output) {
            ingredient += inputCreator.create(Items.GHAST_TEAR)
            result += resultCreator.create(RagiumFluids.NITROGEN_DIOXIDE)
        }
        // 3x NO2 + H2O -> 2x HNO3 + NO
        HTChemicalReactingRecipeBuilder.create(output) {
            ingredients += inputCreator.create(RagiumFluids.NITROGEN_DIOXIDE, 3000)
            ingredients += inputCreator.water()
            fluidResults += resultCreator.create(RagiumFluids.NITRIC_ACID, 2000)
        }
    }

    @JvmStatic
    private fun explosive() {
        // Carbon Compound + Saltpeter -> Gunpowder
        HTShapelessRecipeBuilder.create(output) {
            ingredients += RagiumItems.CARBON_COMPOUND
            ingredients += CommonTagPrefixes.DUST to CommonMaterialKeys.SALTPETER
            resultStack += Items.GUNPOWDER
            recipeId suffix "_from_carbon_compound"
        }

        // HNO3 + Glycerol -> Nitroglycerin
        HTItemOrFluidRecipeBuilder.chemicalWashing(output) {
            ingredient += inputCreator.create(RagiumItems.GLYCEROL_DROP)
            ingredient += inputCreator.create(RagiumFluids.NITRIC_ACID, 250)
            result += resultCreator.create(RagiumItems.NITROGLYCERIN)
        }
        // Nitroglycerin -> Dynamite
        HTShapelessRecipeBuilder.create(output) {
            ingredients += Items.PAPER
            ingredients += Tags.Items.STRINGS
            ingredients += Tags.Items.SANDS
            ingredients += RagiumItems.NITROGLYCERIN
            resultStack += RagiumItems.DYNAMITE to 2
        }
        // HNO3 + Paper -> Nitrocellulose
        HTItemOrFluidRecipeBuilder.chemicalWashing(output) {
            ingredient += inputCreator.create(Items.PAPER)
            ingredient += inputCreator.create(RagiumFluids.NITRIC_ACID, 250)
            result += resultCreator.create(RagiumItems.NITROCELLULOSE)
        }
        // Nitrocellulose + Nitroglycerin -> Smokeless Powder
        HTShapelessRecipeBuilder.create(output) {
            ingredients += RagiumItems.NITROCELLULOSE
            ingredients += RagiumItems.NITROGLYCERIN
            resultStack += RagiumItems.SMOKELESS_POWDER
        }
        // Smokeless -> 4x Gunpowder
        HTShapelessRecipeBuilder.create(output) {
            ingredients += RagiumItems.SMOKELESS_POWDER
            resultStack += Items.GUNPOWDER to 4
            recipeId suffix "_from_smokeless"
        }
        // Smokeless -> Industrial TNT
        HTShapedRecipeBuilder.create(output) {
            mosaic9()
            define('A') += RagiumItems.SMOKELESS_POWDER
            define('B') += Tags.Items.SANDS
            resultStack += RagiumBlocks.INDUSTRIAL_TNT
        }
    }

    @JvmStatic
    private fun blaze() {
        // S -> SO2
        HTItemOrFluidRecipeBuilder.pyrolyzing(output) {
            ingredient += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR)
            result += resultCreator.create(RagiumFluids.SULFUR_DIOXIDE)
        }
        // 2x SO2 + O2 -> 2x SO3
        HTChemicalReactingRecipeBuilder.create(output) {
            ingredients += inputCreator.create(RagiumFluids.SULFUR_DIOXIDE)
            ingredients += inputCreator.create(RagiumFluids.OXYGEN, 500)
            catalyst += catalyst(VanillaMaterialKeys.IRON)
            fluidResults += resultCreator.create(RagiumFluids.SULFUR_TRIOXIDE)
        }
        // Blaze Powder -> SO3
        HTItemOrFluidRecipeBuilder.pyrolyzing(output) {
            ingredient += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.BLAZE)
            result += resultCreator.create(RagiumFluids.SULFUR_TRIOXIDE)
        }
        // SO3 + H2O -> H2SO4
        HTChemicalReactingRecipeBuilder.create(output) {
            ingredients += inputCreator.create(RagiumFluids.SULFUR_TRIOXIDE)
            ingredients += inputCreator.water()
            fluidResults += resultCreator.create(RagiumFluids.SULFURIC_ACID)
        }
    }

    @JvmStatic
    private fun silicon() {
        // Quartz + Coal -> Crude Silicon
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.create(RagiumItems.CRUDE_SILICON)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.QUARTZ))
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.COAL), 2)
        }
        // Quartz + Coal Coke -> Crude Silicon
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.create(RagiumItems.CRUDE_SILICON)
            ingredients += inputCreator.create(baseOrDust(VanillaMaterialKeys.QUARTZ))
            ingredients += inputCreator.create(baseOrDust(CommonMaterialKeys.COAL_COKE))
            recipeId suffix "_with_coal_coke"
        }
        // Crude Silicon + Sulfuric Acid -> Refined Silicon
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(HiiragiCoreTags.Items.SILICON)
            ingredient += inputCreator.create(RagiumFluids.SULFURIC_ACID, 500)
            result += resultCreator.material(CommonParts.DUST, CommonMaterialKeys.SILICON)
        }

        // Quartz Dust + Gold Plate + Plastic -> Circuit Board
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.create(RagiumItems.CIRCUIT_BOARD)
            ingredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.QUARTZ)
            ingredients += inputCreator.create(CommonTagPrefixes.PLATE, VanillaMaterialKeys.GOLD)
            ingredients += inputCreator.create(HiiragiCoreTags.Items.PLASTICS)
        }
        // Silicon -> Silicon Wafer
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.create(RagiumItems.SILICON_WAFER)
            ingredients += inputCreator.create(HiiragiCoreTags.Items.SILICON, 4)
            ingredients += inputCreator.create(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGI_CRYSTAL)
            recipeId suffix "_from_crude_silicon"
            time *= 3
        }
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.create(RagiumItems.SILICON_WAFER)
            ingredients += inputCreator.create(CommonTagPrefixes.PLATE, CommonMaterialKeys.SILICON)
            ingredients += inputCreator.create(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGI_CRYSTAL)
            recipeId suffix "_from_refined_silicon"
            time *= 3
        }
        // Silicon Wafer -> Circuit Chip
        RagiumRecipeBuilder.cutting(output) {
            ingredient = inputCreator.create(RagiumItems.SILICON_WAFER)
            results += resultCreator.create(RagiumItems.CIRCUIT_CHIP, 4)
        }
        // Circuit Board + Circuit chip -> Electric Circuit
        HTAssemblingRecipeBuilder.create(output) {
            result = resultCreator.create(RagiumItems.ELECTRIC_CIRCUIT)
            ingredients += inputCreator.create(RagiumItems.CIRCUIT_BOARD)
            ingredients += inputCreator.create(RagiumItems.CIRCUIT_CHIP, 2)
        }
    }

    //    The End    //

    @JvmStatic
    private fun end() {
        eldritch()
        helium()
    }

    @JvmStatic
    private fun eldritch() {
        // Eldritch Flux
        /*HTChemicalReactingRecipeBuilder.create(output) {
            itemIngredient = inputCreator.create(HiiragiCoreTags.Items.ELDRITCH_PEARL_BINDER)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.CRIMSON_CRYSTAL)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.WARPED_CRYSTAL)
            results += resultCreator.molten(HCMaterialKeys.ELDRITCH)
        }*/

        // Artificial Artifact
        HTShapedRecipeBuilder.create(output) {
            cross8()
            define('A') += CommonTagPrefixes.NUGGET to VanillaMaterialKeys.NETHERITE
            define('B') += RagiumItems.ELECTRIC_CIRCUIT
            define('C') += CommonTagPrefixes.PEARL to HCMaterialKeys.ELDRITCH
            resultStack += RagiumItems.ARTIFICIAL_ARTIFACT
        }
        // Reinforced Deepslate
        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') += Items.DEEPSLATE
            define('B') += RagiumItems.ARTIFICIAL_ARTIFACT
            resultStack += Items.REINFORCED_DEEPSLATE to 8
        }
        // Heavy Core
        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') += CommonTagPrefixes.INGOT to VanillaMaterialKeys.NETHERITE
            define('B') += RagiumItems.ARTIFICIAL_ARTIFACT
            resultStack += Items.HEAVY_CORE
        }
        // Elytra
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "ABA",
                "A A",
                "A A",
            )
            define('A') += HiiragiCoreTags.Items.PLASTICS
            define('B') += RagiumItems.ARTIFICIAL_ARTIFACT
            resultStack += Items.ELYTRA
        }
    }

    @JvmStatic
    private fun helium() {
        // End Stone -> Helium
        pyrolyzing {
            ingredient += inputCreator.create(Tags.Items.END_STONES, 4)
            result += resultCreator.create(RagiumFluids.HELIUM, 500)
        }
    }

    //    End Game    //

    @JvmStatic
    private fun endGame() {
        iridescent()
    }

    @JvmStatic
    private fun iridescent() {
        // Iridescent Powder
        HTShapedRecipeBuilder.create(output) {
            pattern("ABC", "DEF", "GHI")
            define('A') += CommonTagPrefixes.DUST to CommonMaterialKeys.RUTHENIUM
            define('B') += CommonTagPrefixes.DUST to CommonMaterialKeys.RHODIUM
            define('C') += CommonTagPrefixes.DUST to CommonMaterialKeys.PALLADIUM
            define('D') += Items.CONDUIT
            define('E') += Tags.Items.NETHER_STARS
            define('F') += Items.HEAVY_CORE
            define('G') += CommonTagPrefixes.DUST to CommonMaterialKeys.OSMIUM
            define('H') += CommonTagPrefixes.DUST to CommonMaterialKeys.IRIDIUM
            define('I') += CommonTagPrefixes.DUST to CommonMaterialKeys.PLATINUM
            resultStack += HCItems.IRIDESCENT_POWDER
        }

        // Ambrosia
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.create(HCItems.AMBROSIA)
            ingredients += inputCreator.create(HCItems.IRIDESCENT_POWDER)
            ingredients += inputCreator.create(Items.HONEY_BLOCK, 64)
            ingredients += inputCreator.create(Items.ENCHANTED_GOLDEN_APPLE, 16)
        }
    }
}
