package hiiragi283.ragium.data.recipe

import hiiragi283.lib.collection.buildTable
import hiiragi283.lib.collection.forEach
import hiiragi283.lib.collection.nelOf
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.VanillaRecipeBuilders
import hiiragi283.lib.data.recipe.builder.ingredient
import hiiragi283.lib.data.recipe.builder.result
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTMaterialTagsIngredient
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTCommonTags
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.HTMachineType
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
        electronics()
    }

    override fun getName(): String = "Common Recipes"

    //    Mechanical    //

    private fun mechanical() {
        ore()
        rawMaterial()
    }

    private fun ore() {
        // Crushing
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Fuel.COAL) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Fuel.COAL)
                count = 2
            }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Mineral.REDSTONE) }
            result {
                +Items.REDSTONE
                count = 6
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.CINNABAR) }
            recipeId suffix "_from_ore"
        }.save(exporter)

        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Gem.LAPIS) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.LAPIS)
                count = 6
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.FLUORITE) }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Gem.QUARTZ) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.QUARTZ)
                count = 4
            }
            result { +Items.GOLD_NUGGET }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Gem.DIAMOND) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.DIAMOND)
                count = 2
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemicals.CARBON) }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Gem.EMERALD) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.EMERALD)
                count = 2
            }
            recipeId suffix "_from_ore"
        }.save(exporter)

        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Metal.COPPER) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Metal.COPPER)
                count = 3
            }
            result {
                +Items.GOLD_NUGGET
                count = 3
            }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Metal.IRON) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Metal.IRON)
                count = 2
            }
            result { +Items.FLINT }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Metal.GOLD) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Metal.GOLD)
                count = 2
            }
            recipeId suffix "_from_ore"
        }.save(exporter)
        // Alloying
        buildTable {
            put(RagiumMaterial.Fuel.COAL, Items.COAL, 3)
            put(RagiumMaterial.Mineral.REDSTONE, Items.REDSTONE, 9)

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
                extra { +holderSet(RagiumTags.Items.SMELTING_FLUXES) }
                result {
                    +result
                    this.count = count
                }
                recipeId suffix "_from_ore"
            }.save(exporter)
        }
    }

    private fun rawMaterial() {
        // Crushing
        for (metal: RagiumMaterial.Metal in RagiumMaterial.Metal.entries) {
            val dust: HTSimpleDeferredItem = RagiumItems.MATERIAL_ITEMS[HTItemPart.DUST, metal] ?: continue
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

        // Wood Tar -> Alcohol + Aromatic Compound
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(RagiumFluids.WOOD_TAR) }
            fluidResult {
                +RagiumFluids.ALCOHOL
                amount = 500
            }
            recipeId replace RagiumFluids.WOOD_TAR.idOrThrow
        }.save(exporter)
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
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemicals.CARBON) }
            fluidResult {
                +RagiumFluids.COAL_TAR
                amount = 500
            }
            time /= 2
        }.save(exporter)
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { +holderSet(CommonTagPrefixes.STORAGE_BLOCK, RagiumMaterial.Fuel.COAL) }
            itemResult { +RagiumBlocks.getStorageOrThrow(RagiumMaterial.Fuel.COAL_COKE) }
            fluidResult {
                +RagiumFluids.COAL_TAR
                amount = 500 * 9
            }
            time = time / 2 * 9
        }.save(exporter)

        // Coal Tar -> Aromatic Compound + Pitch Coke
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(RagiumFluids.COAL_TAR) }
            itemResult { +RagiumItems.PITCH_COKE }
            fluidResult {
                +RagiumFluids.AROMATIC_COMPOUND
                amount = 250
            }
            recipeId replace RagiumFluids.COAL_TAR.idOrThrow
        }.save(exporter)
    }

    private fun oilRefining() {
        plastic()

        // Soul Sand -> Sand + Crude Oil
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { items { +Items.SOUL_SAND } }
            itemResult { +Items.SAND }
            fluidResult {
                +RagiumFluids.CRUDE_OIL
                amount = 250
            }
            recipeId replace id("crude_oil_from_soul_sand")
        }.save(exporter)
        // Soul Soil -> Clay + Crude Oil
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { items { +Items.SOUL_SOIL } }
            itemResult { +Items.CLAY }
            fluidResult {
                +RagiumFluids.CRUDE_OIL
                amount = 250
            }
            recipeId replace id("crude_oil_from_soul_soil")
        }.save(exporter)
        // Crude Oil -> Naphtha + Residue Oil
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(RagiumFluids.CRUDE_OIL) }
            fluidResult {
                +RagiumFluids.NAPHTHA
                amount = 750
            }
            itemResult { +RagiumItems.TAR }
            recipeId replace RagiumFluids.CRUDE_OIL.idOrThrow
        }.save(exporter)

        // Naphtha -> Fuel + Sulfur
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(RagiumFluids.NAPHTHA) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Mineral.SULFUR) }
            fluidResult {
                +RagiumFluids.FUEL
                amount = 500
            }
            recipeId replace RagiumFluids.NAPHTHA.idOrThrow
        }.save(exporter)
        // Naphtha + Redstone -> Anti-rust Oil
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.REDSTONE) }
            fluidIngredient { +holderSet(RagiumFluids.NAPHTHA) }
            fluidResult { +RagiumFluids.ANTI_RUST_OIL }
        }.save(exporter)

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
            fluidResult {
                +RagiumFluids.FUEL
                amount = 500
            }
            recipeId suffix "_from_tar"
        }.save(exporter)
    }

    private fun plastic() {
        // Naphtha + O2 -> Molten Plastic
        RagiumRecipeBuilders.reacting {
            ingredient {
                +holderSet(RagiumFluids.NAPHTHA)
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
        val moltenPlastic = HTFluidIngredient(FluidIngredient.of(holderSet(RagiumFluids.MOLTEN_PLASTIC)), 90)
        RagiumRecipeBuilders.freezing {
            ingredient = moltenPlastic
            catalyst { items { +RagiumItems.PLATE_MOLD } }
            result { +RagiumItems.PLASTIC_PLATE }
        }.save(exporter)
        // Plastic -> Synthetic XX
        RagiumRecipeBuilders.freezing {
            ingredient = moltenPlastic
            catalyst { +holderSet(Tags.Items.FEATHERS) }
            result { +RagiumItems.SYNTHETIC_FEATHER }
        }.save(exporter)
        RagiumRecipeBuilders.freezing {
            ingredient = moltenPlastic
            catalyst { +holderSet(Tags.Items.LEATHERS) }
            result { +RagiumItems.SYNTHETIC_LEATHER }
        }.save(exporter)
        RagiumRecipeBuilders.freezing {
            ingredient = moltenPlastic
            catalyst { +holderSet(Tags.Items.STRINGS) }
            result { +RagiumItems.SYNTHETIC_FIBER }
        }.save(exporter)

        // Alcohol -> Aldehyde
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(RagiumTags.Fluids.ALCOHOLS) }
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

        // Synthetic Fiber + Nitrogen -> Carbon Fiber
        // Carbon Fiber + Resin -> CFRP Plate
        RagiumRecipeBuilders.bathing {
            itemIngredient { items { +RagiumItems.CARBON_FIBER } }
            fluidIngredient {
                +holderSet(RagiumTags.Fluids.RESINS)
                amount /= 4
            }
            result { +RagiumItems.CFRP_PLATE }
        }.save(exporter)
    }

    //    Chemical    //

    private fun chemical() {
        nitrogen()
        explosive()
        fluorine()

        sodium()
        // aluminum()
        silica()
        sulfur()
        chlorine()

        calcium()
    }

    private fun nitrogen() {
        // 1/2 H2SO4 + KNO3 -> HNO3
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.NITER) }
            fluidIngredient {
                +holderSet(RagiumFluids.SULFURIC_ACID)
                amount /= 2
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
            itemIngredient { +dustOrGem(RagiumMaterial.Gem.FLUORITE) }
            fluidIngredient { +holderSet(RagiumFluids.SULFURIC_ACID) }
            fluidResult {
                +RagiumFluids.HYDROGEN_FLUORIDE
                amount *= 2
            }
        }.save(exporter)
        // HF + H2O -> HF(aq)
        RagiumRecipeBuilders.reacting {
            ingredient { +holderSet(RagiumFluids.HYDROGEN_FLUORIDE) }
            ingredient { +waterSet() }
            fluidResult { +RagiumFluids.HYDROFLUORIC_ACID }
        }.save(exporter)
    }

    private fun sodium() {
        // Dried Kelp -> Na2CO3
        VanillaRecipeBuilders.smeltingAndBlasting(exporter) {
            ingredient { items { +Items.DRIED_KELP } }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemicals.SODA_ASH) }
            exp = 0.15f
        }
        // 2x NaOH (aq) + CO2 -> Na2CO3 + H2O
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(RagiumFluids.NAOH_SOLUTION) }
            fluidResult {
                water()
                amount *= 2
            }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemicals.SODA_ASH) }
            recipeId replace RagiumFluids.NAOH_SOLUTION.idOrThrow
        }.save(exporter)
        // SiO2 + Na2CO3 -> Glass
        RagiumRecipeBuilders.alloying {
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Gem.QUARTZ)
                count = 2
            }
            extra { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemicals.SODA_ASH) }
            result {
                +Items.GLASS
                count = 6
            }
        }.save(exporter)

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
    }

    /*private fun aluminum() {
        // Bauxite + NaOH aq -> Al(OH)3 (aq)
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.BAUXITE) }
            fluidIngredient {
                +holderSet(RagiumFluids.NAOH_SOLUTION)
                amount /= 2
            }
            result { +RagiumFluids.ALUMINA_SOLUTION }
        }.save(exporter)
        // Al(OH)3 (aq) -> Al2O3 + Water
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(RagiumFluids.ALUMINA_SOLUTION) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemicals.ALUMINA) }
            fluidResult { water() }
            recipeId replace RagiumFluids.ALUMINA_SOLUTION.idOrThrow
        }.save(exporter)
        // Al(OH)3 (aq) + HF (aq) -> AlF3 (aq)
        RagiumRecipeBuilders.reacting {
            primaryIngredient { +holderSet(RagiumFluids.ALUMINA_SOLUTION) }
            secondaryIngredient {
                +holderSet(RagiumFluids.HYDROFLUORIC_ACID)
                amount *= 3
            }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemicals.ALUMINUM_FLUORIDE) }
            fluidResult { water() }
        }.save(exporter)
        // 2x AlF3 + 3x Na2CO3 -> Na3AlF6 + 3x CO2
        RagiumRecipeBuilders.alloying {
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemicals.ALUMINUM_FLUORIDE)
                count = 2
            }
            extra {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemicals.SODIUM_CARBONATE)
                count = 3
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.CRYOLITE) }
        }.save(exporter)
        // Al2O3 + Cokes + Na3AlF6 -> Al
        RagiumRecipeBuilders.alloying {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemicals.ALUMINA) }
            extra { +holderSet(RagiumTags.Items.COKES) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.INGOT, RagiumMaterial.Metal.ALUMINUM)
                count = 2
            }
            time *= 4
        }.save(exporter)
        RagiumRecipeBuilders.alloying {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemicals.ALUMINA) }
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
            ingredient {
                +holderSet(Tags.Items.GLASS_BLOCKS_CHEAP, CommonTagPrefixes.DUST.itemTagKey(RagiumMaterial.Other.GLASS))
            }
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
            ingredient { +dustOrGem(RagiumMaterial.Gem.QUARTZ) }
            extra { +holderSet(Tags.Items.SANDS) }
            result { +RagiumBlocks.QUARTZ_GLASS }
            recipeId suffix "_from_quartz"
        }.save(exporter)
        RagiumRecipeBuilders.alloying {
            ingredient {
                +dustOrGem(RagiumMaterial.Gem.AMETHYST)
                count = 4
            }
            extra { +holderSet(Tags.Items.SANDS) }
            result { +RagiumBlocks.QUARTZ_GLASS }
            recipeId suffix "_from_amethyst"
        }.save(exporter)
        RagiumRecipeBuilders.alloying {
            ingredient { items { +Items.AMETHYST_BLOCK } }
            extra { +holderSet(Tags.Items.SANDS) }
            result { +RagiumBlocks.QUARTZ_GLASS }
            recipeId suffix "_from_amethyst_block"
        }.save(exporter)
    }

    private fun sulfur() {
        // S -> SO2
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.SULFUR) }
            fluidResult { +RagiumFluids.SULFUR_DIOXIDE }
        }.save(exporter)
        // SO2 + 1/2 O2 -> SO3
        RagiumRecipeBuilders.reacting {
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
        RagiumRecipeBuilders.reacting {
            ingredient { +holderSet(RagiumFluids.SULFUR_TRIOXIDE) }
            ingredient { +waterSet() }
            fluidResult { +RagiumFluids.SULFURIC_ACID }
        }.save(exporter)
    }

    private fun chlorine() {
        // NaCl(aq) -> H2O + NaCl
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(RagiumFluids.SALT_WATER) }
            fluidResult { water() }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Mineral.SALT) }
            recipeId replace RagiumFluids.SALT_WATER.idOrThrow
        }.save(exporter)

        // 2x NaCl + H2SO4 -> 2x HCl + Na2SO4
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.SALT) }
            fluidIngredient {
                +holderSet(RagiumFluids.SULFURIC_ACID)
                amount /= 2
            }
            fluidResult { +RagiumFluids.HYDROGEN_CHLORIDE }
        }.save(exporter)

        // 2x NaCl(aq) -> H2 + Cl2 + 2x NaOH(aq)
        RagiumRecipeBuilders.electrolyzing {
            ingredient { +holderSet(RagiumFluids.SALT_WATER) }
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

        // H2 + Cl2 -> 2x HCl
        RagiumRecipeBuilders.reacting {
            ingredient { +holderSet(RagiumFluids.HYDROGEN) }
            ingredient { +holderSet(RagiumFluids.CHLORINE) }
            fluidResult {
                +RagiumFluids.HYDROGEN_CHLORIDE
                amount *= 2
            }
        }.save(exporter)
        // HCl + H2O -> HCl(aq)
        RagiumRecipeBuilders.reacting {
            ingredient { +holderSet(RagiumFluids.HYDROGEN_CHLORIDE) }
            ingredient { +waterSet() }
            fluidResult { +RagiumFluids.HYDROCHLORIC_ACID }
        }.save(exporter)

        // Cl2 + 2x NaOH(aq) -> 2x NaClO(aq) + H2
    }

    private fun calcium() {
        // Calcite / Dripstone -> CaCO3
        RagiumRecipeBuilders.crushing {
            ingredient {
                items {
                    +Items.CALCITE
                    +Items.DRIPSTONE_BLOCK
                }
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemicals.LIME) }
        }.save(exporter)
        // CaCO3 -> CaO + CO2
        VanillaRecipeBuilders.smeltingAndBlasting(exporter) {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemicals.LIME) }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemicals.QUICK_LIME) }
            exp = 0.15f
        }

        // CaSO4 -> CaO + SO3
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.GYPSUM) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemicals.QUICK_LIME) }
            fluidResult { +RagiumFluids.SULFUR_TRIOXIDE }
            time *= 4
        }.save(exporter)
    }

    //    Electronics    //

    private fun electronics() {
        silicon()
    }

    private fun silicon() {
        val dustOrCoke: HTMaterialTagsIngredient = materialTags(
            nelOf(
                CommonTagPrefixes.DUST.itemTagKey(RagiumMaterial.Fuel.COAL_COKE),
                CommonTagPrefixes.DUST.itemTagKey(RagiumMaterial.Fuel.PITCH_COKE),
                RagiumTags.Items.COKES
            )
        )
        // Quartz + Coal Coke -> Crude Si
        RagiumRecipeBuilders.alloying {
            ingredient { +dustOrGem(RagiumMaterial.Gem.QUARTZ) }
            extra { +dustOrCoke }
            result { +RagiumItems.CRUDE_SILICON }
            recipeId suffix "_from_quartz"
        }.save(exporter)
        RagiumRecipeBuilders.alloying {
            ingredient { +holderSet(RagiumTags.BlockItems.QUARTZ_BLOCKS.item) }
            extra {
                +dustOrCoke
                count = 4
            }
            result {
                +RagiumItems.CRUDE_SILICON
                count = 4
            }
            time *= 4
            recipeId suffix "_from_quartz_block"
        }.save(exporter)
        // Amethyst + Coal Coke -> Crude Si
        RagiumRecipeBuilders.alloying {
            ingredient {
                +dustOrGem(RagiumMaterial.Gem.AMETHYST)
                count = 4
            }
            extra { +dustOrCoke }
            result { +RagiumItems.CRUDE_SILICON }
            recipeId suffix "_from_amethyst"
        }.save(exporter)
        RagiumRecipeBuilders.alloying {
            ingredient { items { +Items.AMETHYST_BLOCK } }
            extra { +dustOrCoke }
            result { +RagiumItems.CRUDE_SILICON }
            recipeId suffix "_from_amethyst_block"
        }.save(exporter)

        // Crude Si + HCl -> Si Dust
        RagiumRecipeBuilders.bathing {
            itemIngredient { +holderSet(HTCommonTags.Items.SILICON) }
            fluidIngredient {
                +holderSet(RagiumFluids.HYDROCHLORIC_ACID)
                amount = 125
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemicals.SILICON) }
        }.save(exporter)
        // Si Dust + Redstone -> Silicon Wafer
        RagiumRecipeBuilders.alloying {
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemicals.SILICON)
                count = 8
            }
            extra { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.RAGINITE) }
            result { +RagiumItems.SILICON_WAFER }
        }.save(exporter)
        // Silicon Wafer -> Circuit Chip
        RagiumRecipeBuilders.cutting {
            ingredient { items { +RagiumItems.SILICON_WAFER } }
            result {
                +RagiumItems.SILICON_CHIP
                count = 8
            }
        }.save(exporter)

        // Plastic + Gold Dust -> Circuit Board
        RagiumRecipeBuilders.alloying {
            ingredient { +holderSet(HTCommonTags.Items.PLASTICS) }
            extra { +dustOrIngot(RagiumMaterial.Metal.GOLD) }
            result { +RagiumItems.CIRCUIT_BOARD }
        }.save(exporter)
        // Silicon Chip + Circuit Board -> Electric Circuit
        RagiumRecipeBuilders.assembling {
            ingredient {
                items { +RagiumItems.SILICON_CHIP }
                count = 2
            }
            extra { items { +RagiumItems.CIRCUIT_BOARD } }
            result { +RagiumItems.ELECTRIC_CIRCUIT }
            recipeId suffix "_with_board"
        }.save(exporter)
        // Plastic + Silicon Chip + Gold Nugget -> Electric Circuit
        RagiumRecipeBuilders.assembling {
            ingredient { +holderSet(HTCommonTags.Items.PLASTICS) }
            extra {
                items { +RagiumItems.SILICON_CHIP }
                count = 2
            }
            extra {
                +holderSet(CommonTagPrefixes.NUGGET, RagiumMaterial.Metal.GOLD)
                count = 3
            }
            result { +RagiumItems.ELECTRIC_CIRCUIT }
        }.save(exporter)
        // Machine Parts
        RagiumRecipeBuilders.assembling {
            ingredient {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.BLACK_STEEL)
                count = 2
            }
            extra { items { +RagiumItems.ELECTRIC_CIRCUIT } }
            result { +RagiumItems.getParts(HTMachineType.ELECTRONICS) }
            recipeId suffix "_by_black_metal"
        }.save(exporter)
        RagiumRecipeBuilders.assembling {
            ingredient {
                items { +RagiumItems.CFRP_PLATE }
            }
            extra {
                items { +RagiumItems.SILICON_CHIP }
                count = 2
            }
            extra {
                +holderSet(CommonTagPrefixes.NUGGET, RagiumMaterial.Metal.GOLD)
                count = 3
            }
            result { +RagiumItems.getParts(HTMachineType.ELECTRONICS) }
        }.save(exporter)
    }
}
