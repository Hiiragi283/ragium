package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.VanillaRecipeBuilders
import hiiragi283.lib.data.recipe.builder.ingredient
import hiiragi283.lib.data.recipe.builder.result
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTCommonTags
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import java.util.concurrent.CompletableFuture

class RagiumCommonRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun exportValues() {
        mechanical()
        heat()
        chemical()
        resource()
    }

    override fun getName(): String = "Common Recipes"

    //    Mechanical    //

    private fun mechanical() {}

    //    Heat    //

    private fun heat() {
        charcoal()
        coal()
        oilRefining()
    }

    private fun charcoal() {
        // Log -> Charcoal + Creosote
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { +holderSet(ItemTags.LOGS_THAT_BURN) }
            itemResult { +Items.CHARCOAL }
            fluidResult {
                +RagiumFluids.CREOSOTE
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
                +RagiumFluids.CREOSOTE
                amount = 250
            }
            time /= 2
        }.save(exporter)

        // Creosote -> Aromatic Compound
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(RagiumFluids.CREOSOTE) }
            fluidResult {
                +RagiumFluids.AROMATIC_COMPOUND
                amount = 500
            }
            recipeId replace RagiumFluids.CREOSOTE.id()
        }.save(exporter)
    }

    private fun coal() {
        // Coal -> Coal Coke + Tar + Creosote
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { items { +Items.COAL } }
            itemResult { +RagiumItems.COAL_COKE }
            itemResult { +RagiumItems.TAR }
            fluidResult {
                +RagiumFluids.CREOSOTE
                amount = 500
            }
            time /= 2
        }.save(exporter)
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Fuel.COAL) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.CARBON) }
            itemResult { +RagiumItems.TAR }
            fluidResult {
                +RagiumFluids.CREOSOTE
                amount = 500
            }
            time /= 2
        }.save(exporter)
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { +holderSet(CommonTagPrefixes.STORAGE_BLOCK, RagiumMaterial.Fuel.COAL) }
            itemResult { +RagiumBlocks.getStorageOrThrow(RagiumMaterial.Fuel.COAL_COKE) }
            itemResult {
                +RagiumItems.TAR
                count = 9
            }
            fluidResult {
                +RagiumFluids.CREOSOTE
                amount = 500 * 9
            }
            time = time / 2 * 9
        }.save(exporter)
    }

    private fun oilRefining() {
        plastic()
        carbonyl()

        // Soul Sand -> Sand + Crude Oil
        RagiumRecipeBuilders.centrifuging {
            ingredient { items { +Items.SOUL_SAND } }
            itemResult { +Items.SAND }
            fluidResult {
                +RagiumFluids.CRUDE_OIL
                amount = 250
            }
            recipeId replace id("crude_oil_from_soul_sand")
        }.save(exporter)
        // Soul Soil -> Clay + Crude Oil
        RagiumRecipeBuilders.centrifuging {
            ingredient { items { +Items.SOUL_SOIL } }
            itemResult { +Items.CLAY }
            fluidResult {
                +RagiumFluids.CRUDE_OIL
                amount = 250
            }
            recipeId replace id("crude_oil_from_soul_soil")
        }.save(exporter)
        // Crude Oil -> Sulfuric Naphtha + Sulfuric Fuel + Residue Oil
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(RagiumFluids.CRUDE_OIL) }
            fluidResult {
                +RagiumFluids.SULFURIC_NAPHTHA
                amount = 300
            }
            fluidResult {
                +RagiumFluids.SULFURIC_FUEL
                amount = 450
            }
            itemResult { +RagiumItems.TAR }
            recipeId replace RagiumFluids.CRUDE_OIL.id()
        }.save(exporter)

        // Sulfuric Naphtha + H2 -> Naphtha + H2S
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.SULFURIC_NAPHTHA) }
            ingredient {
                +holderSet(RagiumFluids.HYDROGEN)
                amount /= 4
            }
            fluidResult { +RagiumFluids.NAPHTHA }
            fluidResult {
                +RagiumFluids.HYDROGEN_SULFIDE
                amount /= 4
            }
        }.save(exporter)
        // Sulfuric Fuel + H2 -> Fuel + H2S
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.SULFURIC_FUEL) }
            ingredient {
                +holderSet(RagiumFluids.HYDROGEN)
                amount /= 4
            }
            fluidResult { +RagiumFluids.FUEL }
            fluidResult {
                +RagiumFluids.HYDROGEN_SULFIDE
                amount /= 4
            }
        }.save(exporter)
        // 2x H2S + 3x O2 -> 2x SO2 + 2x H2O
        RagiumRecipeBuilders.fluidReacting {
            ingredient {
                +holderSet(RagiumFluids.HYDROGEN_SULFIDE)
                amount *= 2
            }
            ingredient {
                +holderSet(RagiumFluids.OXYGEN)
                amount *= 3
            }
            fluidResult {
                +RagiumFluids.SULFUR_DIOXIDE
                amount *= 2
            }
            fluidResult {
                water()
                amount *= 2
            }
            recipeId suffix "_from_hydrogen_sulfide"
        }.save(exporter)

        // Naphtha -> Fuel
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(RagiumFluids.NAPHTHA) }
            fluidResult {
                +RagiumFluids.FUEL
                amount = 750
            }
            recipeId replace id("fuel_from_naphtha")
        }.save(exporter)

        // Tar -> Pitch Coke + Naphtha
        RagiumRecipeBuilders.pyrolyzing {
            ingredient {
                items { +RagiumItems.TAR }
                count = 4
            }
            itemResult { +RagiumItems.PITCH_COKE }
            fluidResult {
                +RagiumFluids.NAPHTHA
                amount = 250
            }
        }.save(exporter)
        // Tar + H2 -> Fuel
        RagiumRecipeBuilders.itemReacting {
            itemIngredient { items { +RagiumItems.TAR } }
            fluidIngredient {
                +holderSet(RagiumFluids.HYDROGEN)
                amount /= 2
            }
            fluidResult {
                +RagiumFluids.FUEL
                amount = 200
            }
            recipeId suffix "_from_tar"
        }.save(exporter)
    }

    private fun plastic() {
        // TODO: Molten Plastic
        // Molten Plastic <-> Plastic
        RagiumRecipeBuilders.melting {
            ingredient { +holderSet(HTCommonTags.Items.PLASTICS) }
            result {
                +RagiumFluids.MOLTEN_PLASTIC
                amount = 90
            }
        }.save(exporter)
        RagiumRecipeBuilders.melting {
            ingredient {
                items {
                    +RagiumItems.SYNTHETIC_FEATHER
                    +RagiumItems.SYNTHETIC_LEATHER
                    +RagiumItems.SYNTHETIC_FIBER
                }
            }
            result {
                +RagiumFluids.MOLTEN_PLASTIC
                amount = 90
            }
            recipeId suffix "_from_synthetic_parts"
        }.save(exporter)

        val moltenPlastic = HTFluidIngredient(FluidIngredient.of(holderSet(RagiumFluids.MOLTEN_PLASTIC)), 90)
        RagiumRecipeBuilders.freezing {
            +moltenPlastic
            catalyst { items { +RagiumItems.PLATE_MOLD } }
            result { +RagiumItems.PLASTIC_PLATE }
        }.save(exporter)
        // Plastic -> Synthetic XX
        RagiumRecipeBuilders.freezing {
            +moltenPlastic
            catalyst { +holderSet(Tags.Items.FEATHERS) }
            result { +RagiumItems.SYNTHETIC_FEATHER }
        }.save(exporter)
        RagiumRecipeBuilders.freezing {
            +moltenPlastic
            catalyst { +holderSet(Tags.Items.LEATHERS) }
            result { +RagiumItems.SYNTHETIC_LEATHER }
        }.save(exporter)
        RagiumRecipeBuilders.freezing {
            +moltenPlastic
            catalyst { +holderSet(Tags.Items.STRINGS) }
            result { +RagiumItems.SYNTHETIC_FIBER }
        }.save(exporter)
    }

    private fun carbonyl() {
        // Cokes + Water -> Synthetic Gas
        RagiumRecipeBuilders.itemReacting {
            itemIngredient { +holderSet(RagiumTags.Items.COKES) }
            fluidIngredient { +waterSet() }
            fluidResult { +RagiumFluids.SYNTHETIC_GAS }
        }.save(exporter)

        // Synthetic Gas -> Methanol
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.SYNTHETIC_GAS) }
            catalyst { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Metal.COPPER) }
            fluidResult { +RagiumFluids.ALCOHOL }
            recipeId replace id("methanol")
        }.save(exporter)
        // Alcohol + (Ag) -> Aldehyde
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumTags.Fluids.ALCOHOLS) }
            catalyst { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Metal.SILVER) }
            fluidResult { +RagiumFluids.ALDEHYDE }
        }.save(exporter)
        // Aromatic Compound + Aldehyde -> Synthetic Resin
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.AROMATIC_COMPOUND) }
            ingredient { +holderSet(RagiumTags.Fluids.ALDEHYDES) }
            fluidResult {
                +RagiumFluids.SYNTHETIC_RESIN
                amount *= 2
            }
        }.save(exporter)

        // Synthetic Gas -> Fuel + Water + Wax
        RagiumRecipeBuilders.refining {
            ingredient {
                +holderSet(RagiumFluids.SYNTHETIC_GAS)
                amount *= 2
            }
            fluidResult {
                +RagiumFluids.FUEL
                amount /= 2
            }
            fluidResult { water() }
            recipeId replace id("fischer_tropsch_process")
        }.save(exporter)
    }

    //    Chemical    //

    private fun chemical() {
        nitrogen()
        explosive()
        fluorine()

        alkali()
        // aluminum()
        silica()
        phosphor()
        sulfur()
        chlorine()

        calcium()

        // 2x H2O -> 2x H2 + O2
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +waterSet() }
            catalyst { +holderSet(RagiumTags.Items.CATALYSTS_ELECTROLYZE) }
            fluidResult { +RagiumFluids.HYDROGEN }
            fluidResult {
                +RagiumFluids.OXYGEN
                amount /= 2
            }
            recipeId replace id("water")
        }.save(exporter)
    }

    private fun saltAndSolution(fluid: HTFluidContent, item: HTSimpleDeferredItem) {
        // Salt + Water -> Salt (aq)
        RagiumRecipeBuilders.mixing {
            itemIngredient { items { +item } }
            fluidIngredient { +waterSet() }
            fluidResult { +fluid }
        }.save(exporter)
        // Salt (aq) -> Salt + Water
        RagiumRecipeBuilders.drying {
            fluidIngredient { +holderSet(fluid) }
            fluidResult { water() }
            itemResult { +item }
            recipeId replace item.id.withSuffix("_from_solution")
        }.save(exporter)
    }

    private fun saltAndSolution(fluid: HTFluidContent, material: RagiumMaterial) {
        // Salt + Water -> Salt (aq)
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, material) }
            fluidIngredient { +waterSet() }
            fluidResult { +fluid }
        }.save(exporter)
        // Salt (aq) -> Salt + Water
        val item: HTSimpleDeferredItem = RagiumItems.getOrThrow(HTItemPart.DUST, material)
        RagiumRecipeBuilders.drying {
            fluidIngredient { +holderSet(fluid) }
            fluidResult { water() }
            itemResult { +item }
            recipeId replace item.id.withSuffix("_from_solution")
        }.save(exporter)
    }

    private fun nitrogen() {
        // Gunpowder -> C + S + KNO3
        RagiumRecipeBuilders.centrifuging {
            ingredient {
                +holderSet(Tags.Items.GUNPOWDERS)
                count = 3
            }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.CARBON) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.SULFUR) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.NITER) }
            recipeId suffix "_from_gunpowder"
        }.save(exporter)
        // H2SO4 + 2x KNO3 -> 2x HNO3 + K2SO4
        RagiumRecipeBuilders.mixing {
            itemIngredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.NITER)
                count = 2
            }
            fluidIngredient { +holderSet(RagiumFluids.SULFURIC_ACID) }
            itemResult {
                +RagiumItems.ALKALI_SULFATE
                count = 2
            }
            fluidResult { +RagiumFluids.NITRIC_ACID }
            recipeId suffix "_from_niter"
        }.save(exporter)
    }

    private fun explosive() {
        // Aromatic Compound + HNO3 -> Liquid Explosive
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.AROMATIC_COMPOUND) }
            ingredient { +holderSet(RagiumFluids.NITRIC_ACID) }
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

    private fun fluorine() {
        // CaF2 + H2SO4 -> 2x HF + CaSO4
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Gem.FLUORITE) }
            fluidIngredient { +holderSet(RagiumFluids.SULFURIC_ACID) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.GYPSUM) }
            fluidResult {
                +RagiumFluids.HYDROGEN_FLUORIDE
                amount *= 2
            }
            recipeId suffix "_from_fluorite"
        }.save(exporter)
        // HF + H2O -> HF(aq)
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.HYDROGEN_FLUORIDE) }
            ingredient { +waterSet() }
            fluidResult { +RagiumFluids.HYDROFLUORIC_ACID }
        }.save(exporter)
    }

    private fun alkali() {
        // NaOH (aq)
        saltAndSolution(RagiumFluids.HYDROXIDE_SOLUTION, RagiumItems.ALKALI_HYDROXIDE)
        // Na2SO4 (aq)
        saltAndSolution(RagiumFluids.SULFATE_SOLUTION, RagiumItems.ALKALI_SULFATE)

        // Dried Kelp -> Na2CO3
        VanillaRecipeBuilders.smeltingAndBlasting(exporter) {
            ingredient { items { +Items.DRIED_KELP } }
            result { +RagiumItems.ALKALI_CARBONATE }
            exp = 0.15f
        }
        // 2x NaOH (aq) + CO2 -> Na2CO3 + H2O
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.HYDROXIDE_SOLUTION) }
            ingredient { +holderSet(RagiumFluids.CARBON_DIOXIDE) }
            itemResult { +RagiumItems.ALKALI_CARBONATE }
            fluidResult { water() }
            recipeId replace RagiumItems.ALKALI_CARBONATE.id
        }.save(exporter)
        // SiO2 + Na2CO3 -> Glass
        RagiumRecipeBuilders.alloying {
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SILICA)
                count = 2
            }
            ingredient { items { +RagiumItems.ALKALI_CARBONATE } }
            result {
                +Items.GLASS
                count = 6
            }
        }.save(exporter)

        // KOH (aq)
        RagiumRecipeBuilders.mixing {
            itemIngredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.ASH)
                count = 4
            }
            fluidIngredient { +waterSet() }
            fluidResult { +RagiumFluids.HYDROXIDE_SOLUTION }
            recipeId suffix "_from_ash"
        }.save(exporter)

        // Wood Pulp + Alkali aq -> Paper Pulp + Black Liquor
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.WOOD) }
            fluidIngredient {
                +holderSet(setOf(RagiumFluids.HYDROXIDE_SOLUTION, RagiumFluids.SULFATE_SOLUTION))
                amount = 250
            }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Other.PAPER) }
            fluidResult {
                +RagiumFluids.BLACK_LIQUOR
                amount = 250
            }
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
    }

    /*private fun aluminum() {
        // Bauxite + NaOH aq -> Al(OH)3 (aq)
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.BAUXITE) }
            fluidIngredient {
                +holderSet(RagiumFluids.NAOH_SOLUTION)
                amount /= 2
            }
            result { +RagiumFluids.ALUMINA_SOLUTION }
        }.save(exporter)
        // Al(OH)3 (aq) -> Al2O3 + Water
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(RagiumFluids.ALUMINA_SOLUTION) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Other.ALUMINA) }
            fluidResult { water() }
            recipeId replace RagiumFluids.ALUMINA_SOLUTION.id()
        }.save(exporter)
        // Al(OH)3 (aq) + HF (aq) -> AlF3 (aq)
        RagiumRecipeBuilders.mixing {
            primaryIngredient { +holderSet(RagiumFluids.ALUMINA_SOLUTION) }
            secondaryIngredient {
                +holderSet(RagiumFluids.HYDROFLUORIC_ACID)
                amount *= 3
            }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Other.ALUMINUM_FLUORIDE) }
            fluidResult { water() }
        }.save(exporter)
        // 2x AlF3 + 3x Na2CO3 -> Na3AlF6 + 3x CO2
        RagiumRecipeBuilders.alloying {
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.ALUMINUM_FLUORIDE)
                count = 2
            }
            extra {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.SODIUM_CARBONATE)
                count = 3
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.CRYOLITE) }
        }.save(exporter)
        // Al2O3 + Cokes + Na3AlF6 -> Al
        RagiumRecipeBuilders.alloying {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.ALUMINA) }
            extra { +holderSet(RagiumTags.Items.COKES) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.INGOT, RagiumMaterial.Metal.ALUMINUM)
                count = 2
            }
            time *= 4
        }.save(exporter)
        RagiumRecipeBuilders.alloying {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.ALUMINA) }
            extra { +holderSet(RagiumTags.Items.COKES) }
            extra { +dustOrGem(RagiumMaterial.Gem.CRYOLITE) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.INGOT, RagiumMaterial.Metal.ALUMINUM)
                count = 3
            }
            recipeId suffix "_with_cryolite"
        }.save(exporter)
        // Al + Cu -> Alclad Plate
        RagiumRecipeBuilders.assembling {
            ingredient {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.ALUMINUM)
                count = 3
            }
            extra { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.COPPER) }
            result {
                +RagiumItems.ALCLAD_PLATE
                count = 4
            }
        }.save(exporter)
    }*/

    private fun silica() {
        // Glass -> Molten Glass
        RagiumRecipeBuilders.melting {
            ingredient { +holderSet(Tags.Items.GLASS_BLOCKS_CHEAP) }
            result { +RagiumFluids.MOLTEN_GLASS }
            recipeId suffix "_from_block"
        }.save(exporter)
        RagiumRecipeBuilders.melting {
            ingredient { +holderSet(Tags.Items.GLASS_PANES) }
            result {
                +RagiumFluids.MOLTEN_GLASS
                amount = 375
            }
            recipeId suffix "_from_pane"
        }.save(exporter)
        RagiumRecipeBuilders.melting {
            ingredient {
                +holderSet(Tags.Items.GLASS_PANES)
                count = 8
            }
            result {
                +RagiumFluids.MOLTEN_GLASS
                amount *= 3
            }
            time *= 8
            recipeId suffix "_from_panes"
        }.save(exporter)
        // Molten Glass -> Glass
        RagiumRecipeBuilders.freezing {
            ingredient { +holderSet(RagiumFluids.MOLTEN_GLASS) }
            catalyst { items { +RagiumItems.BLOCK_MOLD } }
            result { +Items.GLASS }
        }.save(exporter)
        RagiumRecipeBuilders.freezing {
            ingredient {
                +holderSet(RagiumFluids.MOLTEN_GLASS)
                amount = 375
            }
            catalyst { items { +RagiumItems.PLATE_MOLD } }
            result { +Items.GLASS_PANE }
        }.save(exporter)
        // SiO2 + Sand -> Quartz Glass
        RagiumRecipeBuilders.alloying {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SILICA) }
            ingredient { +holderSet(Tags.Items.SANDS) }
            result {
                +RagiumBlocks.QUARTZ_GLASS
                count = 2
            }
        }.save(exporter)
    }

    private fun phosphor() {
        // Ca3(PO4)2 + 3x H2SO4 -> H3PO4 + 3x CaSO4
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Gem.APATITE) }
            fluidIngredient {
                +holderSet(RagiumFluids.SULFURIC_ACID)
                amount *= 3
            }
            itemResult {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.GYPSUM)
                count = 3
            }
            fluidResult { +RagiumFluids.PHOSPHORIC_ACID }
            recipeId suffix "_from_apatite"
        }.save(exporter)
        // H3PO4 + Ca(OH2) aq -> Bone
        RagiumRecipeBuilders.fluidReacting {
            ingredient {
                +holderSet(RagiumFluids.PHOSPHORIC_ACID)
                amount = 600
            }
            ingredient { +holderSet(RagiumFluids.HYDROXIDE_SOLUTION) }
            itemResult {
                +Items.BONE
                count = 12
            }
            recipeId replace id("hydroxyapatite")
        }.save(exporter)
    }

    private fun sulfur() {
        // S -> SO2
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SULFUR) }
            fluidResult { +RagiumFluids.SULFUR_DIOXIDE }
        }.save(exporter)
        // SO2 + 1/2 O2 -> SO3
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.SULFUR_DIOXIDE) }
            ingredient {
                +holderSet(RagiumFluids.OXYGEN)
                amount /= 2
            }
            fluidResult { +RagiumFluids.SULFUR_TRIOXIDE }
        }.save(exporter)
        // Blaze Powder -> SO3
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { items { +Items.BLAZE_POWDER } }
            fluidResult { +RagiumFluids.SULFUR_TRIOXIDE }
        }.save(exporter)
        // SO3 + H2O -> H2SO4
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.SULFUR_TRIOXIDE) }
            ingredient { +waterSet() }
            fluidResult { +RagiumFluids.SULFURIC_ACID }
        }.save(exporter)
    }

    private fun chlorine() {
        // NaCl(aq)
        saltAndSolution(RagiumFluids.SALT_WATER, RagiumMaterial.Chemical.SALT)

        // 2x NaCl + H2SO4 -> 2x HCl + Na2SO4
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SALT) }
            fluidIngredient {
                +holderSet(RagiumFluids.SULFURIC_ACID)
                amount /= 2
            }
            itemResult { +RagiumItems.ALKALI_SULFATE }
            fluidResult { +RagiumFluids.HYDROGEN_CHLORIDE }
            recipeId suffix "_from_salt"
        }.save(exporter)

        // 2x NaCl(aq) -> H2 + Cl2 + 2x NaOH(aq)
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.SALT_WATER) }
            catalyst { +holderSet(RagiumTags.Items.CATALYSTS_ELECTROLYZE) }
            fluidResult {
                +RagiumFluids.HYDROGEN
                amount /= 2
            }
            fluidResult {
                +RagiumFluids.CHLORINE
                amount /= 2
            }
            itemResult { +RagiumItems.ALKALI_HYDROXIDE }
            recipeId replace id("salt_water")
        }.save(exporter)

        // H2 + Cl2 -> 2x HCl
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.HYDROGEN) }
            ingredient { +holderSet(RagiumFluids.CHLORINE) }
            fluidResult {
                +RagiumFluids.HYDROGEN_CHLORIDE
                amount *= 2
            }
        }.save(exporter)
        // HCl + H2O -> HCl(aq)
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.HYDROGEN_CHLORIDE) }
            ingredient { +waterSet() }
            fluidResult { +RagiumFluids.HYDROCHLORIC_ACID }
        }.save(exporter)
    }

    private fun calcium() {
        // CaCO3 -> CaO + CO2
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.LIME) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.QUICK_LIME) }
            fluidResult { +RagiumFluids.CARBON_DIOXIDE }
            recipeId suffix "_from_lime"
        }.save(exporter)
        // CaO + H2O -> Ca(OH)2
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.QUICK_LIME) }
            fluidIngredient { +waterSet() }
            fluidResult { +RagiumFluids.LIMEWATER }
        }.save(exporter)
        // Ca(OH)2 + CO2 -> CaCO3 + H2O
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.LIMEWATER) }
            ingredient { +holderSet(RagiumFluids.CARBON_DIOXIDE) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.LIME) }
            fluidResult { water() }
            recipeId replace id("lime_dust")
        }.save(exporter)

        // (Na,K)2SO4 + Ca(OH)2 -> CaSO4 + 2x KOH
        RagiumRecipeBuilders.mixing {
            itemIngredient { items { +RagiumItems.ALKALI_SULFATE } }
            fluidIngredient { +holderSet(RagiumFluids.LIMEWATER) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.GYPSUM) }
            fluidResult {
                +RagiumFluids.HYDROXIDE_SOLUTION
                amount *= 2
            }
            recipeId suffix "_from_limewater"
        }.save(exporter)
        // CaSO4 -> CaO + SO3
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.GYPSUM) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.QUICK_LIME) }
            fluidResult { +RagiumFluids.SULFUR_TRIOXIDE }
            time *= 4
            recipeId suffix "_from_gypsum"
        }.save(exporter)
    }

    //    Resource    //

    private fun resource() {
        bioFuel()
    }

    private fun bioFuel() {
        // Seed -> Plant Oil + Bio Chaff
        RagiumRecipeBuilders.centrifuging {
            ingredient {
                +holderSet(Tags.Items.SEEDS)
                count *= 8
            }
            itemResult { +RagiumItems.BIO_CHAFF }
            fluidResult { +RagiumFluids.PLANT_OIL }
        }.save(exporter)
        // Bio Chaff -> Dirt
        RagiumRecipeBuilders.compressing {
            ingredient {
                items { +RagiumItems.BIO_CHAFF }
                count *= 4
            }
            result { +Items.DIRT }
            recipeId suffix "_from_chaff"
        }.save(exporter)

        // Water + Potato / Sugarcane -> Alcohol
        RagiumRecipeBuilders.brewing {
            itemIngredient { +holderSet(setOf(Tags.Items.CROPS_POTATO, Tags.Items.CROPS_SUGAR_CANE)) }
            fluidIngredient {
                +waterSet()
                amount /= 4
            }
            result {
                +RagiumFluids.ALCOHOL
                amount /= 4
            }
            recipeId prefix "bio_"
        }.save(exporter)

        // Plant Oil + R-OH + NaOH -> Fuel + Glycerol
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.PLANT_OIL) }
            ingredient {
                +holderSet(RagiumTags.Fluids.ALCOHOLS)
                amount *= 6
            }
            fluidResult {
                +RagiumFluids.FUEL
                amount *= 6
            }
            recipeId prefix "bio_"
        }.save(exporter)
    }
}
