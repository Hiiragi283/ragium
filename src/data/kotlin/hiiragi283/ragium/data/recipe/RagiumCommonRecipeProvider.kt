package hiiragi283.ragium.data.recipe

import hiiragi283.lib.collection.nelOf
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.recipe.ingredient.HTMaterialTagsIngredient
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTCommonTags
import hiiragi283.lib.tag.HTMaterialKey
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
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
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
            itemResult { +RagiumItems.PLASTIC_PLATE }
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
    }

    //    Chemical    //

    private fun chemical() {
        chlorine()
        sulfuricAcid()
        nitricAcid()
        explosive()

        fluorine()
        aluminum()
    }

    private fun chlorine() {
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
                +RagiumFluids.FLUORINE
                amount *= 2
            }
        }.save(exporter)
        // H2 + F2 -> 2 HF
        RagiumRecipeBuilders.reacting {
            primaryIngredient { +holderSet(RagiumFluids.HYDROGEN) }
            secondaryIngredient { +holderSet(RagiumFluids.FLUORINE) }
            fluidResult {
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
        // Bauxite + NaOH aq -> Al2O3
        RagiumRecipeBuilders.bathing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.BAUXITE) }
            fluidIngredient {
                +holderSet(RagiumFluids.NAOH_SOLUTION)
                amount /= 2
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Other.ALUMINA) }
        }.save(exporter)
        // Al2O3 + NaOH aq -> Alumina Solution
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.ALUMINA) }
            fluidIngredient {
                +holderSet(RagiumFluids.NAOH_SOLUTION)
                amount /= 2
            }
            result {
                +RagiumFluids.ALUMINA_SOLUTION
                amount /= 2
            }
        }.save(exporter)
        // Alumina Solution + HF aq -> Na3AlF6
        RagiumRecipeBuilders.reacting {
            primaryIngredient { +holderSet(RagiumFluids.ALUMINA_SOLUTION) }
            secondaryIngredient {
                +holderSet(RagiumFluids.HYDROGEN_FLUORIDE)
                amount *= 6
            }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.GEM, RagiumMaterial.Gem.CRYOLITE) }
        }.save(exporter)
        // Al2O3 + Na3AlF6 -> Al
        RagiumRecipeBuilders.alloying {
            primary {
                +materialTags(
                    nelOf(CommonTagPrefixes.DUST, CommonTagPrefixes.GEM),
                    nelOf(
                        RagiumMaterial.Other.ALUMINA,
                        HTMaterialKey("ruby"),
                        HTMaterialKey("sapphire")
                    )
                )
            }
            secondary { +dustOrGem(RagiumMaterial.Gem.CRYOLITE) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.INGOT, RagiumMaterial.Metal.ALUMINUM)
                count = 3
            }
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
            primary { +dustOrGem(RagiumMaterial.Gem.QUARTZ) }
            secondary { +dustOrCoke }
            result { +RagiumItems.CRUDE_SILICON }
            recipeId suffix "_from_quartz"
        }.save(exporter)
        RagiumRecipeBuilders.alloying {
            primary { +holderSet(RagiumTags.BlockItem.QUARTZ_BLOCKS.item) }
            secondary {
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
            primary {
                +dustOrGem(RagiumMaterial.Gem.AMETHYST)
                count = 4
            }
            secondary { +dustOrCoke }
            result { +RagiumItems.CRUDE_SILICON }
            recipeId suffix "_from_amethyst"
        }.save(exporter)
        RagiumRecipeBuilders.alloying {
            primary { items { +Items.AMETHYST_BLOCK } }
            secondary { +dustOrCoke }
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
            primary {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.SILICON)
                count = 8
            }
            secondary { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.RAGINITE) }
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
            primary { +holderSet(HTCommonTags.Items.PLASTICS) }
            secondary { +dustOrIngot(RagiumMaterial.Metal.GOLD) }
            result { +RagiumItems.CIRCUIT_BOARD }
        }.save(exporter)
        // Circuit Chip + Circuit Board -> Electric Circuit
        RagiumRecipeBuilders.assembling {
            primary {
                items { +RagiumItems.CIRCUIT_CHIP }
                count = 2
            }
            secondary { items { +RagiumItems.CIRCUIT_BOARD } }
            result { +RagiumItems.ELECTRIC_CIRCUIT }
        }.save(exporter)
        // Machine Parts
        RagiumRecipeBuilders.assembling {
            primary {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.BLACK_STEEL)
                count = 4
            }
            secondary { items { +RagiumItems.ELECTRIC_CIRCUIT } }
            result { +RagiumItems.getParts(HTMachineType.ELECTRONICS) }
            recipeId suffix "_by_black_metal"
        }.save(exporter)
        RagiumRecipeBuilders.assembling {
            primary {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.VOID_METAL)
                count = 2
            }
            secondary { items { +RagiumItems.ELECTRIC_CIRCUIT } }
            result { +RagiumItems.getParts(HTMachineType.ELECTRONICS) }
        }.save(exporter)
    }
}
