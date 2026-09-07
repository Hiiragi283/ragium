package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class RagiumChemicalRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun exportValues() {
        bathing()
        electrolyzing()
        mixing()
        reacting()
    }

    private fun bathing() {
        // Wood Pulp + NaOH aq -> Paper Pulp
        RagiumRecipeBuilders.bathing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.WOOD) }
            fluidIngredient {
                +holderSet(RagiumFluids.NAOH_SOLUTION)
                amount = 250
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Other.PAPER) }
            recipeId suffix "_from_wood"
        }.save(exporter)
        // Paper Pulp + Water -> Paper
        RagiumRecipeBuilders.bathing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.PAPER) }
            fluidIngredient {
                +waterSet()
                amount = 250
            }
            result { +Items.PAPER }
            recipeId suffix "_from_pulp"
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
        // Clay + Liquid Explosive -> Plastic Explosive?
    }

    private fun electrolyzing() {
        // 2x H2O -> 2x H2 + O2
        RagiumRecipeBuilders.electrolyzing {
            fluidIngredient { +waterSet() }
            result { +RagiumFluids.HYDROGEN }
            result {
                +RagiumFluids.OXYGEN
                amount /= 2
            }
            recipeId suffix "_from_water"
        }.save(exporter)
        // 2x NaCl(aq) -> H2 + Cl2 + 2x NaOH(aq)
        RagiumRecipeBuilders.electrolyzing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.SALT) }
            fluidIngredient { +waterSet() }
            result {
                +RagiumFluids.HYDROGEN
                amount /= 2
            }
            result {
                +RagiumFluids.CHLORINE
                amount /= 2
            }
            result { +RagiumFluids.NAOH_SOLUTION }
            recipeId suffix "_from_salt_water"
        }.save(exporter)
    }

    private fun mixing() {
        // Naphtha + Petrolatum -> Anti-rust Oil
        RagiumRecipeBuilders.mixing {
            itemIngredient { items { +RagiumItems.PETROLATUM } }
            fluidIngredient { +holderSet(RagiumFluids.NAPHTHA) }
            result { +RagiumFluids.ANTI_RUST_OIL }
        }.save(exporter)

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

        // S + O2 -> SO2
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.SULFUR) }
            fluidIngredient { +holderSet(RagiumFluids.OXYGEN) }
            result { +RagiumFluids.SULFUR_DIOXIDE }
        }.save(exporter)
        // Blaze Powder + O2 -> SO3
        RagiumRecipeBuilders.mixing {
            itemIngredient { items { +Items.BLAZE_POWDER } }
            fluidIngredient { +holderSet(RagiumFluids.OXYGEN) }
            result { +RagiumFluids.SULFUR_TRIOXIDE }
        }.save(exporter)
    }

    private fun reacting() {
        // Aromatic Compound + HNO3 -> Liquid Explosive
        RagiumRecipeBuilders.reacting {
            primaryIngredient { +holderSet(RagiumFluids.AROMATIC_COMPOUND) }
            secondaryIngredient { +holderSet(RagiumFluids.NITRIC_ACID) }
            fluidResult { +RagiumFluids.LIQUID_EXPLOSIVE }
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
        // SO3 + H2O -> H2SO4
        RagiumRecipeBuilders.reacting {
            primaryIngredient { +holderSet(RagiumFluids.SULFUR_TRIOXIDE) }
            secondaryIngredient { +waterSet() }
            fluidResult { +RagiumFluids.SULFURIC_ACID }
        }.save(exporter)

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

    override fun getName(): String = "Chemical Recipes"
}
