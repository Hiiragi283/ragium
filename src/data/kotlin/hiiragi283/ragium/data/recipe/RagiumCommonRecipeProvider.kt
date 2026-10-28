package hiiragi283.ragium.data.recipe

import hiiragi283.lib.collection.buildTable
import hiiragi283.lib.collection.forEach
import hiiragi283.lib.collection.nelOf
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.VanillaRecipeBuilders
import hiiragi283.lib.data.recipe.builder.ingredient
import hiiragi283.lib.data.recipe.builder.result
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTCommonTags
import hiiragi283.lib.tag.HTMaterialLike
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
import net.minecraft.world.item.Item
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
        electronics()
    }

    override fun getName(): String = "Common Recipes"

    //    Mechanical    //

    private fun mechanical() {
        ore()
        rawMaterial()
    }

    private fun ore() {
        val oreProcessing = "ore_processing/"
        // Crushing
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Fuel.COAL) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Fuel.COAL)
                count = 2
            }
            recipeId prefix oreProcessing
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Other.REDSTONE) }
            result {
                +Items.REDSTONE
                count = 6
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.CINNABAR) }
            recipeId prefix oreProcessing
        }.save(exporter)

        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Gem.LAPIS) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.LAPIS)
                count = 6
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.FLUORITE) }
            recipeId prefix oreProcessing
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Gem.QUARTZ) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.SILICA)
                count = 4
            }
            result { +Items.GOLD_NUGGET }
            recipeId prefix oreProcessing
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Gem.DIAMOND) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.DIAMOND)
                count = 2
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.CARBON) }
            recipeId prefix oreProcessing
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Gem.EMERALD) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.EMERALD)
                count = 2
            }
            recipeId prefix oreProcessing
        }.save(exporter)

        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Metal.COPPER) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Metal.COPPER)
                count = 3
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Metal.NICKEL) }
            recipeId prefix oreProcessing
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Metal.IRON) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Metal.IRON)
                count = 2
            }
            result { +Items.FLINT }
            recipeId prefix oreProcessing
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Metal.GOLD) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Metal.GOLD)
                count = 2
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Metal.SILVER) }
            recipeId prefix oreProcessing
        }.save(exporter)
        // Alloying
        buildTable {
            put(RagiumMaterial.Fuel.COAL, Items.COAL, 3)
            put(RagiumMaterial.Other.REDSTONE, Items.REDSTONE, 9)

            put(RagiumMaterial.Gem.LAPIS, Items.LAPIS_LAZULI, 9)
            put(RagiumMaterial.Gem.QUARTZ, Items.QUARTZ, 6)
            put(RagiumMaterial.Gem.DIAMOND, Items.DIAMOND, 3)
            put(RagiumMaterial.Gem.EMERALD, Items.EMERALD, 3)

            put(RagiumMaterial.Metal.COPPER, Items.COPPER_INGOT, 3)
            put(RagiumMaterial.Metal.IRON, Items.IRON_INGOT, 3)
            put(RagiumMaterial.Metal.GOLD, Items.GOLD_INGOT, 3)
        }.forEach { (material: HTMaterialLike, result: Item, count: Int) ->
            RagiumRecipeBuilders.alloying {
                ingredient { +holderSet(CommonTagPrefixes.ORE, material) }
                ingredient { +holderSet(RagiumTags.Items.SMELTING_FLUXES) }
                result {
                    +result
                    this.count = count
                }
                recipeId prefix oreProcessing
            }.save(exporter)
        }
    }

    private fun rawMaterial() {
        // Crushing
        for (metal: RagiumMaterial.Metal in RagiumMaterial.Metal.entries) {
            val dust: HTSimpleDeferredItem = RagiumItems.MATERIAL_ITEMS[HTItemPart.DUST, metal] ?: continue
            val extraDust: HTSimpleDeferredItem? = when (metal) {
                RagiumMaterial.Metal.COPPER -> RagiumMaterial.Metal.NICKEL
                RagiumMaterial.Metal.GOLD -> RagiumMaterial.Metal.SILVER
                else -> null
            }?.let { RagiumItems.MATERIAL_ITEMS[HTItemPart.DUST, it] }
            // 3x Raw -> 4x Dust
            RagiumRecipeBuilders.crushing {
                ingredient {
                    +holderSet(CommonTagPrefixes.RAW_MATERIALS, metal)
                    count = 3
                }
                result {
                    +dust
                    count = 4
                }
                extraDust?.asTemplate()?.ifSuccess { result { from(it) } }
                recipeId suffix "_from_raw"
                condition { itemTagPresent(CommonTagPrefixes.RAW_MATERIALS, metal) }
            }.save(exporter)
            // 1x Raw Block -> 12x Dust
            RagiumRecipeBuilders.crushing {
                ingredient { +holderSet(CommonTagPrefixes.RAW_STORAGE_BLOCK, metal) }
                result {
                    +dust
                    count = 12
                }
                extraDust?.asTemplate(3)?.ifSuccess { result { from(it) } }
                recipeId suffix "_from_raw_block"
                condition { itemTagPresent(CommonTagPrefixes.RAW_STORAGE_BLOCK, metal) }
            }.save(exporter)
        }
    }

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
        resin()

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
        // Crude Oil -> Natural Gas + Naphtha + Residue Oil
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(RagiumFluids.CRUDE_OIL) }
            fluidResult {
                +RagiumFluids.NATURAL_GAS
                amount = 250
            }
            fluidResult {
                +RagiumFluids.NAPHTHA
                amount = 750
            }
            itemResult { +RagiumItems.TAR }
            recipeId replace RagiumFluids.CRUDE_OIL.id()
        }.save(exporter)

        // Naphtha -> Fuel + Sulfur
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(RagiumFluids.NAPHTHA) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.SULFUR) }
            fluidResult {
                +RagiumFluids.FUEL
                amount = 500
            }
            recipeId suffix "_from_naphtha"
        }.save(exporter)
        // Naphtha + Redstone -> Anti-rust Oil
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.REDSTONE) }
            fluidIngredient { +holderSet(RagiumFluids.NAPHTHA) }
            fluidResult { +RagiumFluids.ANTI_RUST_OIL }
        }.save(exporter)

        // Tar -> Pitch Coke
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { items { +RagiumItems.TAR } }
            itemResult { +RagiumItems.PITCH_COKE }
            recipeId suffix "_from_tar"
        }.save(exporter)
        // Tar + H2 -> Fuel
        RagiumRecipeBuilders.mixing {
            itemIngredient { items { +RagiumItems.TAR } }
            fluidIngredient { +holderSet(RagiumFluids.HYDROGEN) }
            fluidResult {
                +RagiumFluids.FUEL
                amount = 500
            }
            recipeId suffix "_from_tar"
        }.save(exporter)
    }

    private fun plastic() {
        // Natural Gas + O2 -> Molten Plastic
        RagiumRecipeBuilders.reacting {
            ingredient {
                +holderSet(RagiumFluids.NATURAL_GAS)
                amount /= 2
            }
            ingredient {
                +holderSet(RagiumFluids.OXYGEN)
                amount /= 4
            }
            fluidResult {
                +RagiumFluids.MOLTEN_PLASTIC
                amount = 180
            }
        }.save(exporter)
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

    private fun resin() {
        // Natural Gas + H2O -> Alcohol
        RagiumRecipeBuilders.mixing {
            fluidIngredient { +holderSet(RagiumFluids.NATURAL_GAS) }
            fluidIngredient {
                +waterSet()
                amount /= 2
            }
            fluidResult { +RagiumFluids.ALCOHOL }
        }.save(exporter)
        // Alcohol + (Ag) -> Aldehyde
        RagiumRecipeBuilders.reacting {
            ingredient { +holderSet(RagiumTags.Fluids.ALCOHOLS) }
            catalyst { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Metal.SILVER) }
            fluidResult { +RagiumFluids.ALDEHYDE }
        }.save(exporter)
        // Aromatic Compound + Aldehyde -> Synthetic Resin
        RagiumRecipeBuilders.reacting {
            ingredient { +holderSet(RagiumFluids.AROMATIC_COMPOUND) }
            ingredient { +holderSet(RagiumTags.Fluids.ALDEHYDES) }
            fluidResult {
                +RagiumFluids.SYNTHETIC_RESIN
                amount *= 2
            }
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
    }

    private fun saltAndSolution(fluid: HTFluidContent, item: HTSimpleDeferredItem) {
        // Salt + Water -> Salt (aq)
        RagiumRecipeBuilders.mixing {
            itemIngredient { items { +item } }
            fluidIngredient { +waterSet() }
            fluidResult { +fluid }
        }.save(exporter)
        // Salt (aq) -> Salt + Water
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(fluid) }
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
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(fluid) }
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
        RagiumRecipeBuilders.reacting {
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
        RagiumRecipeBuilders.mixing {
            fluidIngredient { +holderSet(RagiumFluids.HYDROGEN_FLUORIDE) }
            fluidIngredient { +waterSet() }
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
        RagiumRecipeBuilders.mixing {
            fluidIngredient { +holderSet(RagiumFluids.HYDROXIDE_SOLUTION) }
            fluidResult {
                water()
                amount *= 2
            }
            itemResult { +RagiumItems.ALKALI_CARBONATE }
            recipeId replace id("soda_ash_dust")
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
        RagiumRecipeBuilders.mixing {
            fluidIngredient {
                +holderSet(RagiumFluids.PHOSPHORIC_ACID)
                amount = 600
            }
            fluidIngredient { +holderSet(RagiumFluids.HYDROXIDE_SOLUTION) }
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
        RagiumRecipeBuilders.mixing {
            fluidIngredient { +holderSet(RagiumFluids.SULFUR_DIOXIDE) }
            fluidIngredient {
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
        RagiumRecipeBuilders.mixing {
            fluidIngredient { +holderSet(RagiumFluids.SULFUR_TRIOXIDE) }
            fluidIngredient { +waterSet() }
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
        RagiumRecipeBuilders.electrolyzing {
            ingredient { +holderSet(RagiumFluids.SALT_WATER) }
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
        RagiumRecipeBuilders.mixing {
            fluidIngredient { +holderSet(RagiumFluids.HYDROGEN) }
            fluidIngredient { +holderSet(RagiumFluids.CHLORINE) }
            fluidResult {
                +RagiumFluids.HYDROGEN_CHLORIDE
                amount *= 2
            }
        }.save(exporter)
        // HCl + H2O -> HCl(aq)
        RagiumRecipeBuilders.mixing {
            fluidIngredient { +holderSet(RagiumFluids.HYDROGEN_CHLORIDE) }
            fluidIngredient { +waterSet() }
            fluidResult { +RagiumFluids.HYDROCHLORIC_ACID }
        }.save(exporter)
    }

    private fun calcium() {
        // CaCO3 -> CaO + CO2
        VanillaRecipeBuilders.smeltingAndBlasting(exporter) {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.LIME) }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.QUICK_LIME) }
            exp = 0.15f
        }

        // CaSO4 -> CaO + SO3
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.GYPSUM) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.QUICK_LIME) }
            fluidResult { +RagiumFluids.SULFUR_TRIOXIDE }
            time *= 4
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
        RagiumRecipeBuilders.mixing {
            itemIngredient { items { +RagiumItems.ALKALI_HYDROXIDE } }
            fluidIngredient { +holderSet(RagiumFluids.PLANT_OIL) }
            fluidIngredient {
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

    //    Electronics    //

    private fun electronics() {
        silicon()
        diamond()
    }

    private fun silicon() {
        // SiO2 + C -> Si + CO2
        RagiumRecipeBuilders.alloying {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SILICA) }
            ingredient {
                +materialTags(
                    nelOf(
                        CommonTagPrefixes.DUST.itemTagKey(RagiumMaterial.Fuel.COAL_COKE),
                        CommonTagPrefixes.DUST.itemTagKey(RagiumMaterial.Fuel.PITCH_COKE),
                        CommonTagPrefixes.DUST.itemTagKey(RagiumMaterial.Chemical.CARBON),
                        RagiumTags.Items.COKES,
                    ),
                )
            }
            result { +RagiumItems.CRUDE_SILICON }
        }.save(exporter)
        // Crude Si + HCl -> Si Dust
        RagiumRecipeBuilders.bathing {
            itemIngredient { +holderSet(HTCommonTags.Items.SILICON) }
            fluidIngredient {
                +holderSet(RagiumFluids.HYDROCHLORIC_ACID)
                amount = 125
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.SILICON) }
        }.save(exporter)
        // Si Dust + Redstone -> Silicon Wafer
        RagiumRecipeBuilders.alloying {
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SILICON)
                count = 8
            }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.RAGINITE) }
            result { +RagiumItems.SILICON_WAFER }
        }.save(exporter)
        // Silicon Wafer -> Silicon Chip
        RagiumRecipeBuilders.cutting {
            ingredient { items { +RagiumItems.SILICON_WAFER } }
            result {
                +RagiumItems.SILICON_CHIP
                count = 8
            }
        }.save(exporter)

        // Plastic + SiO2 Dust -> Circuit Board
        RagiumRecipeBuilders.alloying {
            ingredient { +holderSet(HTCommonTags.Items.PLASTICS) }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SILICA) }
            result { +RagiumItems.CIRCUIT_BOARD }
        }.save(exporter)
        // Circuit Board + Wiring Metal + Silicon Chip -> Electric Circuit
        RagiumRecipeBuilders.assembling {
            ingredient { items { +RagiumItems.CIRCUIT_BOARD } }
            ingredient {
                +holderSet(CommonTagPrefixes.NUGGET, RagiumMaterial.Metal.COPPER)
                count = 9
            }
            ingredient { items { +RagiumItems.SILICON_CHIP } }
            result { +RagiumItems.ELECTRIC_CIRCUIT }
            recipeId prefix "silicon/"
            recipeId suffix "_with_copper"
        }.save(exporter)
        RagiumRecipeBuilders.assembling {
            ingredient { items { +RagiumItems.CIRCUIT_BOARD } }
            ingredient {
                +holderSet(CommonTagPrefixes.NUGGET, RagiumMaterial.Metal.GOLD)
                count = 6
            }
            ingredient { items { +RagiumItems.SILICON_CHIP } }
            result { +RagiumItems.ELECTRIC_CIRCUIT }
            recipeId prefix "silicon/"
            recipeId suffix "_with_gold"
        }.save(exporter)
        RagiumRecipeBuilders.assembling {
            ingredient { items { +RagiumItems.CIRCUIT_BOARD } }
            ingredient {
                +holderSet(CommonTagPrefixes.NUGGET, RagiumMaterial.Metal.SILVER)
                count = 3
            }
            ingredient { items { +RagiumItems.SILICON_CHIP } }
            result { +RagiumItems.ELECTRIC_CIRCUIT }
            recipeId prefix "silicon/"
            recipeId suffix "_with_silver"
        }.save(exporter)
        RagiumRecipeBuilders.assembling {
            ingredient { items { +RagiumItems.CIRCUIT_BOARD } }
            ingredient {
                +holderSet(CommonTagPrefixes.NUGGET, RagiumMaterial.Metal.ALUMINUM)
                count = 3
            }
            ingredient { items { +RagiumItems.SILICON_CHIP } }
            result { +RagiumItems.ELECTRIC_CIRCUIT }
            recipeId prefix "silicon/"
            recipeId suffix "_with_aluminum"
        }.save(exporter)
    }

    private fun diamond() {
        // Carbon -> Diamond Wafer
        // TODO: CVD法
        RagiumRecipeBuilders.engraving {
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.CARBON)
                count = 8
            }
            catalyst { items { +RagiumItems.DIAMOND_LENS } }
            result { +RagiumItems.DIAMOND_WAFER }
        }.save(exporter)
        // Diamond Wafer -> Diamond Chip
        RagiumRecipeBuilders.cutting {
            ingredient { items { +RagiumItems.DIAMOND_WAFER } }
            result {
                +RagiumItems.DIAMOND_CHIP
                count = 8
            }
        }.save(exporter)
        // Circuit Board + Wiring Metal + Silicon Chip -> Electric Circuit
        RagiumRecipeBuilders.assembling {
            ingredient { items { +RagiumItems.CIRCUIT_BOARD } }
            ingredient {
                +holderSet(CommonTagPrefixes.NUGGET, RagiumMaterial.Metal.COPPER)
                count = 9
            }
            ingredient { items { +RagiumItems.DIAMOND_CHIP } }
            result {
                +RagiumItems.ELECTRIC_CIRCUIT
                count = 2
            }
            recipeId prefix "diamond/"
            recipeId suffix "_with_copper"
        }.save(exporter)
        RagiumRecipeBuilders.assembling {
            ingredient { items { +RagiumItems.CIRCUIT_BOARD } }
            ingredient {
                +holderSet(CommonTagPrefixes.NUGGET, RagiumMaterial.Metal.GOLD)
                count = 6
            }
            ingredient { items { +RagiumItems.DIAMOND_CHIP } }
            result {
                +RagiumItems.ELECTRIC_CIRCUIT
                count = 2
            }
            recipeId prefix "diamond/"
            recipeId suffix "_with_gold"
        }.save(exporter)
        RagiumRecipeBuilders.assembling {
            ingredient { items { +RagiumItems.CIRCUIT_BOARD } }
            ingredient {
                +holderSet(CommonTagPrefixes.NUGGET, RagiumMaterial.Metal.SILVER)
                count = 3
            }
            ingredient { items { +RagiumItems.DIAMOND_CHIP } }
            result {
                +RagiumItems.ELECTRIC_CIRCUIT
                count = 2
            }
            recipeId prefix "diamond/"
            recipeId suffix "_with_silver"
        }.save(exporter)
        RagiumRecipeBuilders.assembling {
            ingredient { items { +RagiumItems.CIRCUIT_BOARD } }
            ingredient {
                +holderSet(CommonTagPrefixes.NUGGET, RagiumMaterial.Metal.ALUMINUM)
                count = 3
            }
            ingredient { items { +RagiumItems.DIAMOND_CHIP } }
            result {
                +RagiumItems.ELECTRIC_CIRCUIT
                count = 2
            }
            recipeId prefix "diamond/"
            recipeId suffix "_with_aluminum"
        }.save(exporter)
    }
}
