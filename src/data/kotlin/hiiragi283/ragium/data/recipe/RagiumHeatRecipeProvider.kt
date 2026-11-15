package hiiragi283.ragium.data.recipe

import hiiragi283.lib.collection.nelOf
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.VanillaRecipeBuilders
import hiiragi283.lib.data.recipe.builder.ingredient
import hiiragi283.lib.data.recipe.builder.result
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
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

class RagiumHeatRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun exportValues() {
        alloying()

        iceAndFire()

        charcoal()
        coal()
        oilRefining()
        plastic()
        carbonyl()

        // XX Mold
        VanillaRecipeBuilders.shaped {
            +"AA"
            +"AA"
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.GOLD) }
            result { +RagiumItems.BLANK_MOLD }
        }.save(exporter)
        for (mold: HTSimpleDeferredItem in RagiumItems.MOLDS) {
            VanillaRecipeBuilders.stonecutting {
                ingredient { +holderSet(RagiumTags.Items.MOLDS) }
                result { +mold }
            }.save(exporter)
        }
    }

    private fun alloying() {
        // Gold + Netherite Scrap -> Netherite
        RagiumRecipeBuilders.alloying {
            ingredient {
                +dustOrIngot(RagiumMaterial.Metal.GOLD)
                count = 2
            }
            ingredient {
                items { +Items.NETHERITE_SCRAP }
                count = 2
            }
            result { +Items.NETHERITE_INGOT }
        }.save(exporter)
        // Clay + Sand -> Brick
        RagiumRecipeBuilders.alloying {
            ingredient { items { +Items.CLAY_BALL } }
            ingredient { +holderSet(Tags.Items.SANDS) }
            result {
                +Items.BRICK
                count = 2
            }
        }.save(exporter)
        // Netherrack + Soul Sand -> Nether Brick
        RagiumRecipeBuilders.alloying {
            ingredient { +holderSet(Tags.Items.NETHERRACKS) }
            ingredient { +holderSet(ItemTags.SOUL_FIRE_BASE_BLOCKS) }
            result {
                +Items.NETHER_BRICK
                count = 2
            }
        }.save(exporter)

        // Copper + Nickel -> Constantan
        VanillaRecipeBuilders.shapeless {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Metal.COPPER) }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Metal.NICKEL) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Alloy.CONSTANTAN)
                count = 2
            }
        }.save(exporter)
        RagiumRecipeBuilders.alloying {
            ingredient { +dustOrIngot(RagiumMaterial.Metal.COPPER) }
            ingredient { +dustOrIngot(RagiumMaterial.Metal.NICKEL) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.INGOT, RagiumMaterial.Alloy.CONSTANTAN)
                count = 2
            }
        }.save(exporter)
        // Iron + Nickel -> Invar
        VanillaRecipeBuilders.shapeless {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Metal.IRON) }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Metal.IRON) }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Metal.NICKEL) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Alloy.INVAR)
                count = 3
            }
        }.save(exporter)
        RagiumRecipeBuilders.alloying {
            ingredient {
                +dustOrIngot(RagiumMaterial.Metal.IRON)
                count = 2
            }
            ingredient { +dustOrIngot(RagiumMaterial.Metal.NICKEL) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.INGOT, RagiumMaterial.Alloy.INVAR)
                count = 3
            }
        }.save(exporter)

        // Iron + Coals -> Sooty Iron
        RagiumRecipeBuilders.alloying {
            ingredient { +dustOrIngot(RagiumMaterial.Metal.IRON) }
            ingredient {
                +materialTags(
                    nelOf(
                        CommonTagPrefixes.DUST.itemTagKey(RagiumMaterial.Fuel.COAL),
                        CommonTagPrefixes.DUST.itemTagKey(RagiumMaterial.Fuel.CHARCOAL),
                        RagiumTags.Items.COALS,
                    ),
                )
            }
            result { +RagiumItems.getOrThrow(HTItemPart.INGOT, RagiumMaterial.Alloy.SOOTY_IRON) }
            recipeId suffix "_by_coals"
        }.save(exporter)
        // 2x Iron + Cokes -> 2x Sooty Iron
        RagiumRecipeBuilders.alloying {
            ingredient {
                +dustOrIngot(RagiumMaterial.Metal.IRON)
                count = 2
            }
            ingredient { +holderSet(RagiumTags.Items.COKES) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.INGOT, RagiumMaterial.Alloy.SOOTY_IRON)
                count = 2
            }
            recipeId suffix "_by_cokes"
        }.save(exporter)
        // Sooty Iron + Nickel + Obsidian Dust -> Black Steel
        RagiumRecipeBuilders.alloying {
            ingredient { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON) }
            ingredient { +dustOrIngot(RagiumMaterial.Metal.NICKEL) }
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.OBSIDIAN)
                count = 2
            }
            result { +RagiumItems.getOrThrow(HTItemPart.INGOT, RagiumMaterial.Alloy.BLACK_STEEL) }
        }.save(exporter)
    }

    private fun iceAndFire() {
        // Snow / Ice -> Water
        RagiumRecipeBuilders.melting {
            ingredient {
                items { +Items.SNOWBALL }
                count = 4
            }
            result { water() }
            time /= 2
            recipeId suffix "_from_snowball"
        }.save(exporter)
        RagiumRecipeBuilders.melting {
            ingredient {
                items {
                    +Items.SNOW_BLOCK
                    +Items.ICE
                }
            }
            result { water() }
        }.save(exporter)
        RagiumRecipeBuilders.melting {
            ingredient { items { +Items.PACKED_ICE } }
            result {
                water()
                amount *= 9
            }
            time *= 6
            recipeId suffix "_from_packed_ice"
        }.save(exporter)
        RagiumRecipeBuilders.melting {
            ingredient { items { +Items.BLUE_ICE } }
            result {
                water()
                amount *= 81
            }
            time *= 36
            recipeId suffix "_from_blue_ice"
        }.save(exporter)
        // Water -> Snowball
        RagiumRecipeBuilders.freezing {
            ingredient {
                +waterSet()
                amount /= 4
            }
            catalyst { items { +RagiumItems.BALL_MOLD } }
            result { +Items.SNOWBALL }
        }.save(exporter)
        // Water -> Snow Block
        RagiumRecipeBuilders.freezing {
            ingredient { +waterSet() }
            catalyst { items { +RagiumItems.BLOCK_MOLD } }
            result { +Items.SNOW_BLOCK }
        }.save(exporter)

        // Stone -> Lava
        RagiumRecipeBuilders.melting {
            ingredient { +holderSet(Tags.Items.STONES, Tags.Items.COBBLESTONES) }
            result {
                lava()
                amount /= 8
            }
            recipeId suffix "_from_stone"
        }
        // Magma block -> Lava
        RagiumRecipeBuilders.melting {
            ingredient { items { +Items.MAGMA_BLOCK } }
            result {
                lava()
                amount /= 2
            }
            recipeId suffix "_from_magma_block"
        }.save(exporter)
        // Lava -> Obsidian
        RagiumRecipeBuilders.freezing {
            ingredient { +lavaSet() }
            catalyst { items { +RagiumItems.BLOCK_MOLD } }
            result { +Items.OBSIDIAN }
        }.save(exporter)

        // Water + Lava -> Obsidian + Steam
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +waterSet() }
            ingredient { +lavaSet() }
            itemResult { +Items.OBSIDIAN }
            fluidResult {
                +RagiumFluids.STEAM
                amount *= 4
            }
            time /= 16
            recipeId suffix "_by_water"
        }.save(exporter)
        // Snow/Ice + Lava -> Obsidian + Water
        RagiumRecipeBuilders.itemReacting {
            itemIngredient {
                items {
                    +Items.SNOW_BLOCK
                    +Items.ICE
                }
            }
            fluidIngredient { +lavaSet() }
            itemResult { +Items.OBSIDIAN }
            fluidResult { water() }
            time /= 8
            recipeId suffix "_by_ice"
        }.save(exporter)
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

        // Bamboo Charcoal
        VanillaRecipeBuilders.smelting {
            ingredient { items { +Items.BAMBOO } }
            result { +RagiumItems.BAMBOO_CHARCOAL }
            exp = 0.5f
        }.save(exporter)
        VanillaRecipeBuilders.smelting {
            ingredient { +holderSet(ItemTags.BAMBOO_BLOCKS) }
            result {
                +RagiumItems.BAMBOO_CHARCOAL
                count = 9
            }
            exp = 0.5f * 9
            time *= 9
            recipeId suffix "_from_block"
        }.save(exporter)

        RagiumRecipeBuilders.pyrolyzing {
            ingredient { items { +Items.BAMBOO } }
            itemResult { +RagiumItems.BAMBOO_CHARCOAL }
            fluidResult {
                +RagiumFluids.CREOSOTE
                amount = 125
            }
            time /= 2
        }.save(exporter)
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { +holderSet(ItemTags.BAMBOO_BLOCKS) }
            itemResult {
                +RagiumItems.BAMBOO_CHARCOAL
                count = 9
            }
            fluidResult {
                +RagiumFluids.CREOSOTE
                amount = 125 * 9
            }
            time = time / 2 * 9
            recipeId suffix "_from_block"
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
        // Creosote -> Aromatic Compound
        RagiumRecipeBuilders.refining {
            ingredient { +holderSet(RagiumFluids.CREOSOTE) }
            fluidResult {
                +RagiumFluids.AROMATIC_COMPOUND
                amount = 500
            }
            recipeId replace RagiumFluids.CREOSOTE.id()
        }.save(exporter)

        // Cokes + Steam -> Synthetic Gas
        RagiumRecipeBuilders.itemReacting {
            itemIngredient { +holderSet(RagiumTags.Items.COKES) }
            fluidIngredient { +holderSet(RagiumFluids.STEAM) }
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
        // Aromatic Compound + Aldehyde -> Synthetic Resin Clump
        RagiumRecipeBuilders.fluidReacting {
            ingredient {
                +holderSet(RagiumFluids.AROMATIC_COMPOUND)
                amount /= 4
            }
            ingredient {
                +holderSet(RagiumTags.Fluids.ALDEHYDES)
                amount /= 4
            }
            itemResult { +RagiumItems.SYNTHETIC_RESIN_CLUMP }
        }.save(exporter)
        // Resin Clump + Alcohol -> Glue
        RagiumRecipeBuilders.itemReacting {
            itemIngredient { +holderSet(Tags.Items.CLUMPS_RESIN) }
            fluidIngredient {
                +holderSet(RagiumTags.Fluids.ALCOHOLS)
                amount /= 4
            }
            fluidResult {
                +RagiumFluids.GLUE
                amount /= 4
            }
        }.save(exporter)
        // Glue -> Sticky Ball
        RagiumRecipeBuilders.drying {
            ingredient {
                +holderSet(RagiumFluids.GLUE)
                amount /= 4
            }
            itemResult { +RagiumItems.STICKY_BALL }
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

    override fun getName(): String = "Heat Recipes"
}
