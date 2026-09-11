package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.HTStorageBlockPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class RagiumCommonRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun exportValues() {
        heat()
        chemical()
    }

    override fun getName(): String = "Common Recipes"

    //    Heat    //

    private fun heat() {
        charcoal()
        coal()
        oilRefining()
    }

    private fun charcoal() {
        // Log -> Charcoal + Wood Tar
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { +holderSet(ItemTags.LOGS_THAT_BURN) }
            itemResult { +Items.CHARCOAL }
            fluidResult {
                +RagiumFluids.WOOD_TAR
                amount = 250
            }
            time /= 2
            recipeId suffix "_from_logs"
        }.save(exporter)
        RagiumRecipeBuilders.pyrolyzing {
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.WOOD)
                count = 4
            }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Fuel.CHARCOAL) }
            fluidResult {
                +RagiumFluids.WOOD_TAR
                amount = 250
            }
            time /= 2
        }.save(exporter)

        // Wood Tar -> Alcohol + Aromatic Compound TODO
    }

    private fun coal() {
        // Coal -> Coal Coke + Coal Tar
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { items { +Items.COAL } }
            itemResult { +RagiumItems.COAL_COKE }
            fluidResult {
                +RagiumFluids.COAL_TAR
                amount = 500
            }
            time /= 2
        }.save(exporter)
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Fuel.COAL) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Fuel.COAL_COKE) }
            fluidResult {
                +RagiumFluids.COAL_TAR
                amount = 500
            }
            time /= 2
        }.save(exporter)
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { +holderSet(CommonTagPrefixes.STORAGE_BLOCK, RagiumMaterial.Fuel.COAL) }
            itemResult { +RagiumBlocks.getOrThrow(HTStorageBlockPart.DEFAULT, RagiumMaterial.Fuel.COAL_COKE) }
            fluidResult {
                +RagiumFluids.COAL_TAR
                amount = 500 * 9
            }
            time /= 2
            time *= 9
        }.save(exporter)

        // Coal Tar -> Aromatic Compound + Pitch Coke
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(RagiumFluids.COAL_TAR) }
            itemResult { +RagiumItems.PITCH_COKE }
            fluidResult {
                +RagiumFluids.AROMATIC_COMPOUND
                amount = 250
            }
            recipeId suffix "_from_coal_tar"
        }.save(exporter)
    }

    private fun oilRefining() {
        // Soul Sand -> Sand + Crude Oil
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { items { +Items.SOUL_SAND } }
            itemResult { +Items.SAND }
            fluidResult {
                +RagiumFluids.CRUDE_OIL
                amount = 250
            }
            recipeId replace "crude_oil_from_soul_sand"
        }.save(exporter)
        // Soul Soil -> Clay + Crude Oil
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { items { +Items.SOUL_SOIL } }
            itemResult { +Items.CLAY }
            fluidResult {
                +RagiumFluids.CRUDE_OIL
                amount = 250
            }
            recipeId replace "crude_oil_from_soul_soil"
        }.save(exporter)
        // Crude Oil -> Naphtha + Residue Oil
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(RagiumFluids.CRUDE_OIL) }
            fluidResult {
                +RagiumFluids.NAPHTHA
                amount = 750
            }
            itemResult { +RagiumItems.TAR }
        }.save(exporter)

        // Naphtha -> Fuel + Sulfur
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(RagiumFluids.NAPHTHA) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Mineral.SULFUR) }
            fluidResult {
                +RagiumFluids.FUEL
                amount = 500
            }
            recipeId suffix "_from_naphtha"
        }.save(exporter)
        // Naphtha + O2 -> Plastic TODO

        // Tar -> Aromatic Compound + Pitch Coke
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { items { +RagiumItems.TAR } }
            itemResult { +RagiumItems.PITCH_COKE }
            fluidResult { +RagiumFluids.AROMATIC_COMPOUND }
            recipeId suffix "_from_tar"
        }.save(exporter)
        // Tar + H2 -> Fuel
        RagiumRecipeBuilders.mixing {
            itemIngredient { items { +RagiumItems.TAR } }
            fluidIngredient { +holderSet(RagiumFluids.HYDROGEN) }
            result {
                +RagiumFluids.FUEL
                amount = 500
            }
            recipeId suffix "_from_tar"
        }.save(exporter)
    }

    //    Chemical    //

    private fun chemical() {
        hydroChloricAcid()
        sulfuricAcid()
        nitricAcid()
        explosive()
    }

    private fun hydroChloricAcid() {
        // H2 + Cl2 -> 2 HCl
        RagiumRecipeBuilders.reacting {
            primaryIngredient { +holderSet(RagiumFluids.HYDROGEN) }
            secondaryIngredient { +holderSet(RagiumFluids.CHLORINE) }
            fluidResult { +RagiumFluids.HYDROGEN_CHLORIDE }
        }.save(exporter)
        // HCl + H2O -> Hcl(aq)
        RagiumRecipeBuilders.reacting {
            primaryIngredient { +holderSet(RagiumFluids.HYDROGEN_CHLORIDE) }
            secondaryIngredient { +waterSet() }
            fluidResult { +RagiumFluids.HYDROCHLORIC_ACID }
        }.save(exporter)
    }

    private fun sulfuricAcid() {
        // S + O2 -> SO2
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.SULFUR) }
            fluidIngredient { +holderSet(RagiumFluids.OXYGEN) }
            result { +RagiumFluids.SULFUR_DIOXIDE }
        }.save(exporter)
        // SO2 + 1/2 O2 -> SO3
        RagiumRecipeBuilders.reacting {
            primaryIngredient { +holderSet(RagiumFluids.SULFUR_DIOXIDE) }
            secondaryIngredient {
                +holderSet(RagiumFluids.OXYGEN)
                amount /= 2
            }
            fluidResult { +RagiumFluids.SULFUR_TRIOXIDE }
        }.save(exporter)
        // Blaze Powder + O2 -> SO3
        RagiumRecipeBuilders.mixing {
            itemIngredient { items { +Items.BLAZE_POWDER } }
            fluidIngredient { +holderSet(RagiumFluids.OXYGEN) }
            result { +RagiumFluids.SULFUR_TRIOXIDE }
        }.save(exporter)
        // SO3 + H2O -> H2SO4
        RagiumRecipeBuilders.reacting {
            primaryIngredient { +holderSet(RagiumFluids.SULFUR_TRIOXIDE) }
            secondaryIngredient { +waterSet() }
            fluidResult { +RagiumFluids.SULFURIC_ACID }
        }.save(exporter)
    }

    private fun nitricAcid() {
        // 1/2 H2SO4 + KNO3 -> HNO3
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.NITER) }
            fluidIngredient {
                +holderSet(RagiumFluids.SULFURIC_ACID)
                amount /= 2
            }
            result { +RagiumFluids.NITRIC_ACID }
            recipeId suffix "_from_niter"
        }.save(exporter)
    }

    private fun explosive() {
        // Aromatic Compound + HNO3 -> Liquid Explosive
        RagiumRecipeBuilders.reacting {
            primaryIngredient { +holderSet(RagiumFluids.AROMATIC_COMPOUND) }
            secondaryIngredient { +holderSet(RagiumFluids.NITRIC_ACID) }
            fluidResult { +RagiumFluids.LIQUID_EXPLOSIVE }
        }.save(exporter)

        // Sand + Liquid Explosive -> TNT
        RagiumRecipeBuilders.bathing {
            itemIngredient {
                +holderSet(Tags.Items.SANDS)
                count = 8
            }
            fluidIngredient { +holderSet(RagiumFluids.LIQUID_EXPLOSIVE) }
            result {
                +Items.TNT
                count = 8
            }
        }.save(exporter)
        // Clay + Liquid Explosive -> Plastic Explosive TODO
    }
}
