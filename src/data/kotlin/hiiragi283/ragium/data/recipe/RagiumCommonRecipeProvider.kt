package hiiragi283.ragium.data.recipe

import hiiragi283.lib.collection.nelOf
import hiiragi283.lib.color.VanillaColoredCollections
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.ingredient
import hiiragi283.lib.data.recipe.builder.result
import hiiragi283.lib.recipe.ingredient.HTMaterialTagsIngredient
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTCommonTags
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
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.DifferenceIngredient
import java.util.concurrent.CompletableFuture

class RagiumCommonRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun exportValues() {
        heat()
        chemical()
        electronics()
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
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Other.CARBON) }
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
        // Naphtha -> Plastic
        RagiumRecipeBuilders.freezing {
            ingredient {
                +holderSet(RagiumFluids.NAPHTHA)
                amount /= 2
            }
            result { +RagiumItems.PLASTIC_PLATE }
        }.save(exporter)
        RagiumRecipeBuilders.reacting {
            primaryIngredient {
                +holderSet(RagiumFluids.NAPHTHA)
                amount /= 2
            }
            secondaryIngredient {
                +holderSet(RagiumFluids.OXYGEN)
                amount /= 4
            }
            itemResult {
                +RagiumItems.PLASTIC_PLATE
                count = 2
            }
        }.save(exporter)
        // Naphtha + Redstone -> Anti-rust Oil
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.REDSTONE) }
            fluidIngredient { +holderSet(RagiumFluids.NAPHTHA) }
            result { +RagiumFluids.ANTI_RUST_OIL }
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
            result {
                +RagiumFluids.FUEL
                amount = 500
            }
            recipeId suffix "_from_tar"
        }.save(exporter)

        // Aromatic Compound + Water -> Synthetic Resin
        RagiumRecipeBuilders.reacting {
            primaryIngredient { +holderSet(RagiumFluids.AROMATIC_COMPOUND) }
            secondaryIngredient { +waterSet() }
            fluidResult { +RagiumFluids.SYNTHETIC_RESIN }
        }.save(exporter)
        // Aluminum + ??? -> Black Steel
        RagiumRecipeBuilders.bathing {
            itemIngredient { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.ALUMINUM) }
            /*fluidIngredient {
                +holderSet(RagiumFluids.COLORED_RESINS.black)
                amount /= 4
            }*/
            result { +RagiumItems.getOrThrow(HTItemPart.INGOT, RagiumMaterial.Metal.BLACK_STEEL) }
        }
    }

    //    Chemical    //

    private fun chemical() {
        colored()

        chlorine()
        sulfuricAcid()
        nitricAcid()
        explosive()

        fluorine()
        aluminum()
    }

    private fun colored() {
        for (color: DyeColor in DyeColor.entries) {
            val dyeContent: HTFluidContent = RagiumFluids.DYES[color]
            // Water + Solid Dye -> Liquid Dye
            RagiumRecipeBuilders.mixing {
                itemIngredient { +holderSet(color.tag) }
                fluidIngredient {
                    +waterSet()
                    amount /= 4
                }
                result {
                    +dyeContent
                    amount /= 4
                }
            }.save(exporter)
            // Liquid Dye -> Solid Dye
            RagiumRecipeBuilders.freezing {
                ingredient {
                    +holderSet(dyeContent)
                    amount /= 4
                }
                result { +VanillaColoredCollections.DYE[color] }
                recipeId suffix "_from_liquid_dye"
            }.save(exporter)
        }

        // Lapis + Resin -> Blue Dye
        RagiumRecipeBuilders.bathing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Gem.LAPIS) }
            fluidIngredient {
                +holderSet(RagiumTags.Fluids.RESINS)
                amount /= 4
            }
            result { +Items.BLUE_DYE }
            recipeId suffix "_from_lapis"
        }.save(exporter)
        // Bauxite + Resin -> Blue Dye
        RagiumRecipeBuilders.bathing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.BAUXITE) }
            fluidIngredient {
                +holderSet(RagiumTags.Fluids.RESINS)
                amount /= 4
            }
            result { +Items.BROWN_DYE }
            recipeId suffix "_from_bauxite"
        }.save(exporter)
        // Carbon + Resin -> Black Dye
        RagiumRecipeBuilders.bathing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.CARBON) }
            fluidIngredient {
                +holderSet(RagiumTags.Fluids.RESINS)
                amount /= 4
            }
            result { +Items.BLACK_DYE }
            recipeId suffix "_from_carbon"
        }.save(exporter)
    }

    private fun chlorine() {
        // Dried Kelp + Water -> Salt
        RagiumRecipeBuilders.bathing {
            itemIngredient { +holderSet(Tags.Items.STORAGE_BLOCKS_DRIED_KELP) }
            fluidIngredient { +waterSet() }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Mineral.SALT)
                count = 6
            }
            recipeId suffix "_from_block"
        }.save(exporter)
        // NaCl(aq) -> H2O + NaCl
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(RagiumFluids.SALT_WATER) }
            fluidResult { +Fluids.WATER }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Mineral.SALT) }
            recipeId replace id("salt_dust_from_salt_water")
        }.save(exporter)

        // 2x NaCl + H2SO4 -> 2x HCl + Na2SO4
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.SALT) }
            fluidIngredient {
                +holderSet(RagiumFluids.SULFURIC_ACID)
                amount /= 2
            }
            result { +RagiumFluids.HYDROGEN_CHLORIDE }
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
            primaryIngredient { +holderSet(RagiumFluids.HYDROGEN) }
            secondaryIngredient { +holderSet(RagiumFluids.CHLORINE) }
            fluidResult {
                +RagiumFluids.HYDROGEN_CHLORIDE
                amount *= 2
            }
        }.save(exporter)
        // HCl + H2O -> HCl(aq)
        RagiumRecipeBuilders.reacting {
            primaryIngredient { +holderSet(RagiumFluids.HYDROGEN_CHLORIDE) }
            secondaryIngredient { +waterSet() }
            fluidResult { +RagiumFluids.HYDROCHLORIC_ACID }
        }.save(exporter)

        // Cl2 + 2x NaOH(aq) -> 2x NaClO(aq) + H2

        bleaching()
    }

    private fun bleaching() {
        setOf(
            ItemTags.BANNERS to Items.WHITE_BANNER,
            ItemTags.BEDS to Items.WHITE_BED,
            ItemTags.HARNESSES to Items.WHITE_HARNESS,
            ItemTags.SHULKER_BOXES to Items.WHITE_SHULKER_BOX,
            ItemTags.WOOL to Items.WHITE_WOOL,
            ItemTags.WOOL_CARPETS to Items.WHITE_CARPET
        ).forEach { (input: TagKey<Item>, bleached: Item) ->
            RagiumRecipeBuilders.bathing {
                itemIngredient { +DifferenceIngredient.of(Ingredient.of(holderSet(input)), Ingredient.of(bleached)) }
                fluidIngredient {
                    +holderSet(RagiumFluids.BLEACH)
                    amount /= 8
                }
                result { +bleached }
                recipeId prefix "bleaching/"
            }.save(exporter)
        }
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

    private fun fluorine() {
        // CaF2 + H2SO4 -> 2x HF + CaSO4
        RagiumRecipeBuilders.mixing {
            itemIngredient { +dustOrGem(RagiumMaterial.Gem.FLUORITE) }
            fluidIngredient { +holderSet(RagiumFluids.SULFURIC_ACID) }
            result {
                +RagiumFluids.HYDROGEN_FLUORIDE
                amount *= 2
            }
        }.save(exporter)
        // HF + H2O -> HF(aq)
        RagiumRecipeBuilders.reacting {
            primaryIngredient { +holderSet(RagiumFluids.HYDROGEN_FLUORIDE) }
            secondaryIngredient { +waterSet() }
            fluidResult { +RagiumFluids.HYDROFLUORIC_ACID }
        }.save(exporter)
    }

    private fun aluminum() {
        // Bauxite + NaOH aq -> Alumina Solution
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.BAUXITE) }
            fluidIngredient {
                +holderSet(RagiumFluids.NAOH_SOLUTION)
                amount /= 2
            }
            result { +RagiumFluids.ALUMINA_SOLUTION }
        }.save(exporter)
        // Alumina Solution -> Al2O3 + Water
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(RagiumFluids.ALUMINA_SOLUTION) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Other.ALUMINA) }
            fluidResult { +Fluids.WATER }
            recipeId replace id("alumina_dust_from_solution")
        }.save(exporter)
        // Alumina Solution + HF aq -> Na3AlF6
        RagiumRecipeBuilders.reacting {
            primaryIngredient { +holderSet(RagiumFluids.ALUMINA_SOLUTION) }
            secondaryIngredient {
                +holderSet(RagiumFluids.HYDROFLUORIC_ACID)
                amount *= 6
            }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.GEM, RagiumMaterial.Gem.CRYOLITE) }
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
            ingredient { +holderSet(RagiumTags.BlockItem.QUARTZ_BLOCKS.item) }
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
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Other.SILICON) }
        }.save(exporter)
        // Si Dust + Redstone -> Silicon Wafer
        RagiumRecipeBuilders.alloying {
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.SILICON)
                count = 8
            }
            extra { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.RAGINITE) }
            result { +RagiumItems.SILICON_WAFER }
        }.save(exporter)
        // Silicon Wafer -> Circuit Chip
        RagiumRecipeBuilders.cutting {
            ingredient { items { +RagiumItems.SILICON_WAFER } }
            result {
                +RagiumItems.CIRCUIT_CHIP
                count = 8
            }
        }.save(exporter)

        // Plastic + Gold Dust -> Circuit Board
        RagiumRecipeBuilders.alloying {
            ingredient { +holderSet(HTCommonTags.Items.PLASTICS) }
            extra { +dustOrIngot(RagiumMaterial.Metal.GOLD) }
            result { +RagiumItems.CIRCUIT_BOARD }
        }.save(exporter)
        // Circuit Chip + Circuit Board -> Electric Circuit
        RagiumRecipeBuilders.assembling {
            ingredient {
                items { +RagiumItems.CIRCUIT_CHIP }
                count = 2
            }
            extra { items { +RagiumItems.CIRCUIT_BOARD } }
            result { +RagiumItems.ELECTRIC_CIRCUIT }
        }.save(exporter)
        // Machine Parts
        RagiumRecipeBuilders.assembling {
            ingredient {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.BLACK_STEEL)
                count = 4
            }
            extra { items { +RagiumItems.ELECTRIC_CIRCUIT } }
            result { +RagiumItems.getParts(HTMachineType.ELECTRONICS) }
            recipeId suffix "_by_black_metal"
        }.save(exporter)
        RagiumRecipeBuilders.assembling {
            ingredient {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.VOID_METAL)
                count = 2
            }
            extra { items { +RagiumItems.ELECTRIC_CIRCUIT } }
            result { +RagiumItems.getParts(HTMachineType.ELECTRONICS) }
        }.save(exporter)
    }
}
