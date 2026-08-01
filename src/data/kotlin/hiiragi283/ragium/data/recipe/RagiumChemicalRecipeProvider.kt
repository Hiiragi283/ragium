package hiiragi283.ragium.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.FluidIngredientBuilder
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.data.recipe.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.core.support.data.recipe.HTItemOrFluidRecipeBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTagPrefixes
import hiiragi283.ragium.common.data.recipe.HTChemicalReactingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTCombiningRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTRefiningRecipeBuilder
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

class RagiumChemicalRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        overworld()
        nether()
        end()
        endGame()
    }

    override fun getName(): String = "Chemical Recipes"

    private inline fun pyrolyzing(builderAction: HTItemOrFluidRecipeBuilder<HTPyrolyzingRecipe>.() -> Unit) {
        // Without Nitrogen
        RagiumRecipeBuilder.pyrolyzing(builderAction).save(exporter)
        // With Nitrogen
        RagiumRecipeBuilder.pyrolyzing {
            builderAction()
            fluidIngredient { +RagiumFluids.NITROGEN }
            time /= 2
            recipeId suffix "_with_nitrogen"
        }.save(exporter)
    }

    private fun catalyst(key: HTMaterialKey): TagKey<Item> = tag(CommonTagPrefixes.DUST, key)

    //    Overworld    //

    private fun overworld() {
        coal()
        breeze()
        slime()
        rubber()

        // 2x H2O -> 2x H2 + O2
        HTChemicalReactingRecipeBuilder.create {
            ingredient { water() }
            catalyst { +Items.HEART_OF_THE_SEA }
            fluidResult { +RagiumFluids.HYDROGEN }
            fluidResult {
                +RagiumFluids.OXYGEN
                amount = 500
            }
            recipeId suffix "_from_water"
        }.save(exporter)
    }

    private fun coal() {
        // Log -> Charcoal
        pyrolyzing {
            itemIngredient {
                +ItemTags.LOGS_THAT_BURN
                count = 8
            }
            +HTItemResult.MaterialPart(CommonParts.FUEL, VanillaMaterialKeys.CHARCOAL, 8)
            fluidResult { +RagiumFluids.CREOSOTE }
            recipeId suffix "_from_log"
        }
        // Compressed Sawdust -> Charcoal
        pyrolyzing {
            itemIngredient {
                +tag(RagiumTagPrefixes.PELLET, VanillaMaterialKeys.WOOD)
                count = 8
            }
            +HTItemResult.MaterialPart(CommonParts.FUEL, VanillaMaterialKeys.CHARCOAL, 8)
            fluidResult {
                +RagiumFluids.CREOSOTE
                amount = 500
            }
            time /= 3
            recipeId suffix "_from_sawdust"
        }

        // Coal -> Coke + Creosote
        pyrolyzing {
            itemIngredient {
                +tag(CommonTagPrefixes.FUEL, VanillaMaterialKeys.COAL)
                count = 8
            }
            +HTItemResult.MaterialPart(CommonParts.FUEL, CommonMaterialKeys.COAL_COKE, 8)
            fluidResult {
                +RagiumFluids.CREOSOTE
                amount = 2000
            }
        }
        pyrolyzing {
            itemIngredient {
                +tag(CommonTagPrefixes.DUST, VanillaMaterialKeys.COAL)
                count = 8
            }
            +HTItemResult.MaterialPart(CommonParts.DUST, CommonMaterialKeys.COAL_COKE, 8)
            fluidResult {
                +RagiumFluids.CREOSOTE
                amount = 2000
            }
        }
        pyrolyzing {
            itemIngredient { +tag(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.COAL) }
            +HTItemResult.MaterialPart(CommonParts.BLOCK, CommonMaterialKeys.COAL_COKE)
            fluidResult {
                +RagiumFluids.CREOSOTE
                amount = 2000
            }
        }

        // Coal + Steam -> Synthetic Gas
        HTMixingRecipeBuilder.create {
            itemIngredient { +baseOrDust(VanillaMaterialKeys.COAL) }
            fluidIngredient {
                +RagiumFluids.STEAM
                amount = 250
            }
            fluidResult {
                +RagiumFluids.SYNTHETIC_GAS
                amount = 250
            }
            recipeId suffix "_from_coal"
        }.save(exporter)
        // Synthetic Gas + H2O -> CO2 + 2x H2
        HTChemicalReactingRecipeBuilder.create {
            ingredient { +RagiumFluids.SYNTHETIC_GAS }
            ingredient { water() }
            catalyst { +catalyst(CommonMaterialKeys.PLATINUM) }
            fluidResult {
                +RagiumFluids.HYDROGEN
                amount = 2000
            }
            recipeId replace RagiumAPI.id("water_gas_shift_reaction")
        }.save(exporter)
        // Coal -> Synthetic Oil
        RagiumRecipeBuilder.melting {
            ingredient { +baseOrDust(VanillaMaterialKeys.COAL) }
            result {
                +RagiumFluids.SYNTHETIC_OIL
                amount = 125
            }
        }.save(exporter)
        // Synthetic Oil -> Fuel
        HTRefiningRecipeBuilder.create {
            fluidIngredient {
                +RagiumFluids.SYNTHETIC_OIL
                amount = 500
            }
            fluidResult {
                +RagiumFluids.FUEL
                amount = 200
            }
            recipeId suffix "_from_synthetic_oil"
        }.save(exporter)
    }

    private fun breeze() {
        // Wind Charge -> N2
        RagiumRecipeBuilder.melting {
            ingredient { +Items.WIND_CHARGE }
            result {
                +RagiumFluids.NITROGEN
                amount = 125
            }
        }.save(exporter)
        // Cryo-Charge
        HTShapedRecipeBuilder.create {
            cross8()
            define('A') { +Items.PACKED_ICE }
            define('B') { +Items.BLUE_ICE }
            define('C') { +tag(CommonTagPrefixes.DUST, VanillaMaterialKeys.BREEZE) }
            +RagiumItems.CRYO_CHARGE.toStack()
        }.save(exporter)
        // Cryo-Charge -> liq N2
        RagiumRecipeBuilder.melting {
            ingredient { +RagiumItems.CRYO_CHARGE }
            result {
                +RagiumFluids.LIQUID_NITROGEN
                amount = 125
            }
        }.save(exporter)
    }

    private fun slime() {
        // H2SO4 + 2x NaOH aq -> Na2SO4 + 2x H2O
        HTMixingRecipeBuilder.create {
            itemIngredient { +Items.SLIME_BALL }
            fluidIngredient { +RagiumFluids.SULFURIC_ACID }
            itemResult { +Items.MAGMA_CREAM }
            fluidResult {
                water()
                amount = 2000
            }
            recipeId replace id("magma_cream_from_neutralization")
        }.save(exporter)

        // Slimeball + Water -> Glue
        HTMixingRecipeBuilder.create {
            itemIngredient { +Tags.Items.SLIME_BALLS }
            fluidIngredient { water() }
            fluidResult { +RagiumFluids.GLUE }
            recipeId suffix "_from_slime"
        }.save(exporter)
        // Polymer Resin + Water -> Glue
        HTMixingRecipeBuilder.create {
            itemIngredient { +HCItems.POLYMER_RESIN }
            fluidIngredient { water() }
            fluidResult { +RagiumFluids.GLUE }
            recipeId suffix "_from_polymer"
        }.save(exporter)
        // Borax + Glue -> Slimeball
        HTMixingRecipeBuilder.create {
            itemIngredient { +tag(CommonTagPrefixes.DUST, RagiumMaterialKeys.BORAX) }
            fluidIngredient { +RagiumFluids.GLUE }
            itemResult {
                +Items.SLIME_BALL
                count = 4
            }
            recipeId suffix "_from_glue"
        }.save(exporter)
        // Sawdust + Glue -> Particle Board
        RagiumRecipeBuilder.bathing {
            itemIngredient {
                +tag(CommonTagPrefixes.DUST, VanillaMaterialKeys.WOOD)
                count = 2
            }
            fluidIngredient {
                +RagiumFluids.GLUE
                amount = 250
            }
            result { +HCItems.PARTICLE_BOARD }
        }.save(exporter)
    }

    private fun rubber() {
        // Raw Rubber + Sulfur -> Rubber
        HTCombiningRecipeBuilder.alloying {
            result {
                +HCItems.CURED_RUBBER
                count = 2
            }
            ingredient { +HCItems.RAW_RUBBER }
            ingredient { +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR) }
        }.save(exporter)
        // Raw Rubber + Sulfur + Carbon -> Rubber
        HTCombiningRecipeBuilder.alloying {
            result {
                +HCItems.CURED_RUBBER
                count = 3
            }
            ingredient { +HCItems.RAW_RUBBER }
            ingredient { +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR) }
            ingredient { +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.CARBON) }
            recipeId suffix "_with_carbon"
        }.save(exporter)
    }

    //    Nether    //

    private fun nether() {
        crudeOil()
        crimson()
        warped()

        ghast()
        explosive()
        blaze()
        silicon()
    }

    private fun crudeOil() {
        // Oil Sand -> Sand + Crude Oil
        pyrolyzing {
            itemIngredient {
                +HCBlocks.OIL_SAND
                count = 4
            }
            itemResult {
                +Items.SAND
                count = 4
            }
            fluidResult {
                +RagiumFluids.CRUDE_OIL
                amount = 2000
            }
            recipeId replace id("crude_oil_from_sand")
        }
        // Oil Shale -> Clay + Crude Oil
        pyrolyzing {
            itemIngredient {
                +HCBlocks.OIL_SHALE
                count = 4
            }
            itemResult {
                +Items.CLAY_BALL
                count = 16
            }
            fluidResult {
                +RagiumFluids.CRUDE_OIL
                amount = 2000
            }
            recipeId replace id("crude_oil_from_shale")
        }
        // Soul Sand -> Sand + Crude Oil
        pyrolyzing {
            itemIngredient {
                +Items.SOUL_SAND
                count = 4
            }
            itemResult {
                +Items.SAND
                count = 4
            }
            fluidResult {
                +RagiumFluids.CRUDE_OIL
            }
            recipeId replace id("crude_oil_from_soul")
        }

        // Crude Oil -> Petroleum Coke + Naphtha
        HTRefiningRecipeBuilder.create {
            fluidIngredient {
                +RagiumFluids.CRUDE_OIL
                amount = 500
            }
            +HTItemResult.MaterialPart(CommonParts.FUEL, RagiumMaterialKeys.PETROLEUM_COKE)
            fluidResult {
                +RagiumFluids.NAPHTHA
                amount = 300
            }
            recipeId suffix "_from_crude_oil"
        }.save(exporter)
        // Naphtha -> Polymer Resin + Fuel
        HTRefiningRecipeBuilder.create {
            fluidIngredient {
                +RagiumFluids.NAPHTHA
                amount = 500
            }
            itemResult { +HCItems.POLYMER_RESIN }
            fluidResult {
                +RagiumFluids.FUEL
                amount = 300
            }
            recipeId suffix "_from_naphtha"
        }.save(exporter)
        // Polymer Resin + Oxygen -> Plastic
        RagiumRecipeBuilder.bathing {
            itemIngredient { +HCItems.POLYMER_RESIN }
            fluidIngredient {
                +RagiumFluids.OXYGEN
                amount = 250
            }
            +HTItemResult.MaterialPart(CommonParts.PLATE, CommonMaterialKeys.PLASTIC, 2)
        }.save(exporter)

        // CH4 + H2O -> Synthetic Gas
        HTChemicalReactingRecipeBuilder.create {
            ingredient { +RagiumFluids.METHANE }
            ingredient { water() }
            catalyst { +catalyst(CommonMaterialKeys.NICKEL) }
            fluidResult {
                +RagiumFluids.SYNTHETIC_GAS
                amount = 2000
            }
            recipeId suffix "_from_methane"
        }.save(exporter)
    }

    private fun crimson() {
        // Crimson Stem -> Crimson Blood
        pyrolyzing {
            itemIngredient {
                +ItemTags.CRIMSON_STEMS
                count = 8
            }
            fluidResult {
                +HCFluids.MOLTEN_CRIMSON_CRYSTAL
                amount = HTConst.INGOT_AMOUNT
            }
            recipeId suffix "_from_crimson_stem"
        }
        // Crimson Dust + Lava -> Blaze Powder
        RagiumRecipeBuilder.bathing {
            itemIngredient { +tag(CommonTagPrefixes.DUST, HCMaterialKeys.CRIMSON_CRYSTAL) }
            fluidIngredient {
                lava()
                amount = 250
            }
            result { +Items.BLAZE_POWDER }
            recipeId suffix "_from_crimson"
        }.save(exporter)
    }

    private fun warped() {
        // Warped Stem -> Dew of the Warp
        pyrolyzing {
            itemIngredient {
                +ItemTags.WARPED_STEMS
                count = 8
            }
            fluidResult {
                +HCFluids.MOLTEN_WARPED_CRYSTAL
                amount = HTConst.INGOT_AMOUNT
            }
            recipeId suffix "_from_warped_stem"
        }
    }

    private fun ghast() {
        // Soul Soil -> KNO3

        // 2x KNO3 + H2SO4 -> 2x HNO3 + K2SO4
        HTMixingRecipeBuilder.create {
            itemIngredient {
                +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.SALTPETER)
                count = 2
            }
            fluidIngredient { +RagiumFluids.SULFURIC_ACID }
            itemResult { +Items.MAGMA_CREAM }
            fluidResult {
                +RagiumFluids.NITRIC_ACID
                amount = 2000
            }
            recipeId suffix "_from_saltpeter"
        }.save(exporter)

        // 3x H2 + N2 -> 2x NH3
        HTChemicalReactingRecipeBuilder.create {
            ingredient {
                +RagiumFluids.HYDROGEN
                amount = 3000
            }
            ingredient { +RagiumFluids.NITROGEN }
            catalyst { +catalyst(VanillaMaterialKeys.IRON) }
            fluidResult {
                +RagiumFluids.AMMONIA
                amount = 2000
            }
        }.save(exporter)
        // 4x NH3 + 7x O2 -> 4x NO2 + 6x H2O
        HTChemicalReactingRecipeBuilder.create {
            ingredient {
                +RagiumFluids.AMMONIA
                amount = 4000
            }
            ingredient {
                +RagiumFluids.OXYGEN
                amount = 7000
            }
            catalyst { +catalyst(CommonMaterialKeys.PLATINUM) }
            fluidResult {
                +RagiumFluids.NITROGEN_DIOXIDE
                amount = 4000
            }
            fluidResult {
                water()
                amount = 6000
            }
        }.save(exporter)
        // Ghast Tear -> NO2
        RagiumRecipeBuilder.pyrolyzing {
            itemIngredient { +Items.GHAST_TEAR }
            fluidResult { +RagiumFluids.NITROGEN_DIOXIDE }
        }.save(exporter)
        // 3x NO2 + H2O -> 2x HNO3 + NO
        HTChemicalReactingRecipeBuilder.create {
            ingredient {
                +RagiumFluids.NITROGEN_DIOXIDE
                amount = 3000
            }
            ingredient { water() }
            fluidResult {
                +RagiumFluids.NITRIC_ACID
                amount = 2000
            }
        }.save(exporter)
    }

    private fun explosive() {
        // Carbon Compound + Saltpeter -> Gunpowder
        HTShapelessRecipeBuilder.create {
            val carbons: List<HTMaterialKey> = listOf(VanillaMaterialKeys.COAL, VanillaMaterialKeys.CHARCOAL, CommonMaterialKeys.CARBON)
            ingredient { +carbons.map(CommonTagPrefixes.DUST::itemTagKey) }
            ingredient { +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.SALTPETER) }
            ingredient { +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR) }
            result { +Items.GUNPOWDER }
            recipeId suffix "_from_carbon_compound"
        }.save(exporter)

        // HNO3 + Glycerol -> Nitroglycerin
        RagiumRecipeBuilder.bathing {
            itemIngredient { +RagiumItems.GLYCEROL_DROP }
            fluidIngredient {
                +RagiumFluids.NITRIC_ACID
                amount = 250
            }
            result { +RagiumItems.NITROGLYCERIN }
        }.save(exporter)
        // Nitroglycerin -> Dynamite
        HTShapelessRecipeBuilder.create {
            ingredient { +Items.PAPER }
            ingredient { +Tags.Items.STRINGS }
            ingredient { +Tags.Items.SANDS }
            ingredient { +RagiumItems.NITROGLYCERIN }
            +RagiumItems.DYNAMITE.toStack(2)
        }.save(exporter)
        // HNO3 + Paper -> Nitrocellulose
        RagiumRecipeBuilder.bathing {
            itemIngredient { +Items.PAPER }
            fluidIngredient {
                +RagiumFluids.NITRIC_ACID
                amount = 250
            }
            result { +RagiumItems.NITROCELLULOSE }
        }.save(exporter)
        // Nitrocellulose + Nitroglycerin -> Smokeless Powder
        HTShapelessRecipeBuilder.create {
            ingredient { +RagiumItems.NITROCELLULOSE }
            ingredient { +RagiumItems.NITROGLYCERIN }
            +RagiumItems.SMOKELESS_POWDER.toStack()
        }.save(exporter)
        // Smokeless -> 4x Gunpowder
        HTShapelessRecipeBuilder.create {
            ingredient { +RagiumItems.SMOKELESS_POWDER }
            result {
                +Items.GUNPOWDER
                count = 4
            }
            recipeId suffix "_from_smokeless"
        }.save(exporter)
        // Smokeless -> Industrial TNT
        HTShapedRecipeBuilder.create {
            mosaic9()
            define('A') { +RagiumItems.SMOKELESS_POWDER }
            define('B') { +Tags.Items.SANDS }
            +RagiumBlocks.INDUSTRIAL_TNT.toStack()
        }.save(exporter)
    }

    private fun blaze() {
        // S -> SO2
        RagiumRecipeBuilder.pyrolyzing {
            itemIngredient { +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR) }
            fluidResult { +RagiumFluids.SULFUR_DIOXIDE }
        }.save(exporter)
        // 2x SO2 + O2 -> 2x SO3
        HTChemicalReactingRecipeBuilder.create {
            ingredient { +RagiumFluids.SULFUR_DIOXIDE }
            ingredient {
                +RagiumFluids.OXYGEN
                amount = 500
            }
            catalyst { +catalyst(VanillaMaterialKeys.IRON) }
            fluidResult { +RagiumFluids.SULFUR_TRIOXIDE }
        }.save(exporter)
        // Blaze Powder -> SO3
        RagiumRecipeBuilder.pyrolyzing {
            itemIngredient { +tag(CommonTagPrefixes.DUST, VanillaMaterialKeys.BLAZE) }
            fluidResult { +RagiumFluids.SULFUR_TRIOXIDE }
        }.save(exporter)
        // SO3 + H2O -> H2SO4
        HTChemicalReactingRecipeBuilder.create {
            ingredient { +RagiumFluids.SULFUR_TRIOXIDE }
            ingredient { water() }
            fluidResult { +RagiumFluids.SULFURIC_ACID }
        }.save(exporter)
    }

    private fun silicon() {
        // Quartz + Coal -> Crude Silicon
        HTCombiningRecipeBuilder.alloying {
            result { +RagiumItems.CRUDE_SILICON }
            ingredient { +baseOrDust(VanillaMaterialKeys.QUARTZ) }
            ingredient {
                +baseOrDust(VanillaMaterialKeys.COAL)
                count = 2
            }
        }.save(exporter)
        // Quartz + Coal Coke -> Crude Silicon
        HTCombiningRecipeBuilder.alloying {
            result { +RagiumItems.CRUDE_SILICON }
            ingredient { +baseOrDust(VanillaMaterialKeys.QUARTZ) }
            ingredient { +baseOrDust(CommonMaterialKeys.COAL_COKE) }
            recipeId suffix "_with_coal_coke"
        }.save(exporter)
        // Crude Silicon + Sulfuric Acid -> Refined Silicon
        RagiumRecipeBuilder.bathing {
            itemIngredient { +HiiragiCoreTags.Items.SILICON }
            fluidIngredient {
                +RagiumFluids.SULFURIC_ACID
                amount = 500
            }
            +HTItemResult.MaterialPart(CommonParts.DUST, CommonMaterialKeys.SILICON)
        }.save(exporter)

        // Quartz Dust + Gold Plate + Plastic -> Circuit Board
        HTCombiningRecipeBuilder.alloying {
            result { +RagiumItems.CIRCUIT_BOARD }
            ingredient { +tag(CommonTagPrefixes.DUST, VanillaMaterialKeys.QUARTZ) }
            ingredient { +tag(CommonTagPrefixes.PLATE, VanillaMaterialKeys.GOLD) }
            ingredient { +HiiragiCoreTags.Items.PLASTICS }
        }.save(exporter)
        // Silicon -> Silicon Wafer
        HTCombiningRecipeBuilder.alloying {
            result { +RagiumItems.SILICON_WAFER }
            ingredient {
                +HiiragiCoreTags.Items.SILICON
                count = 4
            }
            ingredient { +tag(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGI_CRYSTAL) }
            recipeId suffix "_from_crude_silicon"
            time *= 3
        }.save(exporter)
        HTCombiningRecipeBuilder.alloying {
            result { +RagiumItems.SILICON_WAFER }
            ingredient { +tag(CommonTagPrefixes.PLATE, CommonMaterialKeys.SILICON) }
            ingredient { +tag(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGI_CRYSTAL) }
            recipeId suffix "_from_refined_silicon"
            time *= 3
        }.save(exporter)
        // Silicon Wafer -> Circuit Chip
        RagiumRecipeBuilder.cutting {
            ingredient { +RagiumItems.SILICON_WAFER }
            result {
                +RagiumItems.CIRCUIT_CHIP
                count = 4
            }
        }.save(exporter)
        // Circuit Board + Circuit chip -> Electric Circuit
        HTCombiningRecipeBuilder.assembling {
            result { +RagiumItems.ELECTRIC_CIRCUIT }
            ingredient { +RagiumItems.CIRCUIT_BOARD }
            ingredient {
                +RagiumItems.CIRCUIT_CHIP
                count = 2
            }
        }.save(exporter)

        // Laser Emitter
        HTShapedRecipeBuilder.create {
            +" AB"
            +"ACA"
            +"CA "
            define('A') { +tag(CommonTagPrefixes.PLATE, RagiumMaterialKeys.STAINLESS_STEEL) }
            define('B') { +tag(CommonTagPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL) }
            define('C') { +Tags.Items.GLASS_BLOCKS_COLORLESS }
            +RagiumItems.LASER_EMITTER.toStack()
        }.save(exporter)
    }

    //    The End    //

    private fun end() {
        chorus()
        ender()

        eldritch()
    }

    private fun chorus() {
        // Chorus Fruit -> Chorus Gas
        pyrolyzing {
            itemIngredient { +Items.CHORUS_FRUIT }
            fluidResult {
                +RagiumFluids.CHORUS_GAS
                amount = 250
            }
        }
        // Chorus Gas + Phantom Membrane + Shulker Shell -> ???
        /*HTMixingRecipeBuilder.create) {
            itemIngredients += inputCreator.create(Items.PHANTOM_MEMBRANE)
            itemIngredients += inputCreator.create(Items.SHULKER_SHELL)
            fluidIngredient = inputCreator.create(RagiumFluids.CHORUS_GAS)
            result += resultCreator.material(CommonParts.GEM, RagiumMaterialKeys.LEVITATINE)
        }*/
    }

    private fun ender() {
        val moltenEnder: FluidIngredientBuilder.() -> Unit = {
            +HCFluids.MOLTEN_ENDER
            amount = HTConst.INGOT_AMOUNT / 3
        }
        // Warped Crystal -> Ender Pearl
        RagiumRecipeBuilder.bathing {
            itemIngredient { +baseOrDust(HCMaterialKeys.WARPED_CRYSTAL) }
            fluidIngredient(moltenEnder)
            result { +Items.ENDER_PEARL }
            recipeId suffix "_from_warped"
        }.save(exporter)
        // Fruit -> Chorus Fruit
        RagiumRecipeBuilder.bathing {
            itemIngredient { +Tags.Items.FOODS_FRUIT }
            fluidIngredient(moltenEnder)
            result { +Items.CHORUS_FRUIT }
            time /= 2
            recipeId suffix "_with_ender"
        }.save(exporter)
        // Stone -> End Stone
        RagiumRecipeBuilder.bathing {
            itemIngredient { +Tags.Items.STONES }
            fluidIngredient(moltenEnder)
            result { +Items.END_STONE }
            time /= 2
            recipeId suffix "_with_ender"
        }.save(exporter)
    }

    private fun eldritch() {
        // Eldritch Flux
        HTMixingRecipeBuilder.create {
            itemIngredient { +tag(CommonTagPrefixes.DUST, HCMaterialKeys.CRIMSON_CRYSTAL) }
            itemIngredient { +HiiragiCoreTags.Items.ELDRITCH_PEARL_BINDER }
            fluidIngredient {
                +HCFluids.MOLTEN_WARPED_CRYSTAL
                amount = HTConst.INGOT_AMOUNT
            }
            fluidResult {
                +HCFluids.MOLTEN_ELDRITCH
                amount = HTConst.INGOT_AMOUNT
            }
            recipeId suffix "_from_molten_warped_crystal"
        }.save(exporter)
        HTMixingRecipeBuilder.create {
            itemIngredient { +tag(CommonTagPrefixes.DUST, HCMaterialKeys.WARPED_CRYSTAL) }
            itemIngredient { +HiiragiCoreTags.Items.ELDRITCH_PEARL_BINDER }
            fluidIngredient {
                +HCFluids.MOLTEN_CRIMSON_CRYSTAL
                amount = HTConst.INGOT_AMOUNT
            }
            fluidResult {
                +HCFluids.MOLTEN_ELDRITCH
                amount = HTConst.INGOT_AMOUNT
            }
            recipeId suffix "_from_molten_crimson_crystal"
        }.save(exporter)

        // Artificial Artifact
        HTShapedRecipeBuilder.create {
            cross8()
            define('A') { +tag(CommonTagPrefixes.PLATE, VanillaMaterialKeys.NETHERITE) }
            define('B') { +RagiumItems.ELECTRIC_CIRCUIT }
            define('C') { +tag(CommonTagPrefixes.PEARL, HCMaterialKeys.ELDRITCH) }
            +RagiumItems.ARTIFICIAL_ARTIFACT.toStack()
        }.save(exporter)
        // Reinforced Deepslate
        HTShapedRecipeBuilder.create {
            hollow8()
            define('A') { +Items.DEEPSLATE }
            define('B') { +RagiumItems.ARTIFICIAL_ARTIFACT }
            result {
                +Items.REINFORCED_DEEPSLATE
                count = 8
            }
        }.save(exporter)
        // Heavy Core
        HTShapedRecipeBuilder.create {
            hollow8()
            define('A') { +tag(CommonTagPrefixes.INGOT, VanillaMaterialKeys.NETHERITE) }
            define('B') { +RagiumItems.ARTIFICIAL_ARTIFACT }
            result { +Items.HEAVY_CORE }
        }.save(exporter)
        // Elytra
        HTShapedRecipeBuilder.create {
            +"ABA"
            +"A A"
            +"A A"
            define('A') { +HiiragiCoreTags.Items.PLASTICS }
            define('B') { +RagiumItems.ARTIFICIAL_ARTIFACT }
            result { +Items.ELYTRA }
        }.save(exporter)
    }

    //    End Game    //

    private fun endGame() {
        iridescent()
    }

    private fun iridescent() {
        // Iridescent Powder
        HTShapedRecipeBuilder.create {
            +"ABC"
            +"DEF"
            +"GHI"
            define('A') { +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.RUTHENIUM) }
            define('B') { +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.RHODIUM) }
            define('C') { +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.PALLADIUM) }
            define('D') { +Items.CONDUIT }
            define('E') { +Tags.Items.NETHER_STARS }
            define('F') { +Items.HEAVY_CORE }
            define('G') { +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.OSMIUM) }
            define('H') { +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.IRIDIUM) }
            define('I') { +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.PLATINUM) }
            +HCItems.IRIDESCENT_POWDER.toStack()
        }.save(exporter)

        // Ambrosia
        HTCombiningRecipeBuilder.alloying {
            result { +HCItems.AMBROSIA }
            ingredient { +HCItems.IRIDESCENT_POWDER }
            ingredient {
                +Items.HONEY_BLOCK
                count = 64
            }
            ingredient {
                +Items.ENCHANTED_GOLDEN_APPLE
                count = 16
            }
        }.save(exporter)
    }
}
