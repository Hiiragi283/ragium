package hiiragi283.ragium.common.internal

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.content.HTItemContent
import hiiragi283.ragium.api.data.HTMachineRecipeJsonBuilder
import hiiragi283.ragium.api.data.HTMachineRecipeJsonBuilderNew
import hiiragi283.ragium.api.data.HTShapedRecipeJsonBuilder
import hiiragi283.ragium.api.data.HTShapelessRecipeJsonBuilder
import hiiragi283.ragium.api.extension.id
import hiiragi283.ragium.api.extension.isAir
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.common.init.*
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object RagiumHardModePlugin : RagiumPlugin {
    override val priority: Int = -90

    private val hardMode: Boolean by lazy(RagiumAPI.getInstance()::isHardMode)

    override fun registerRuntimeRecipe(exporter: RecipeExporter, lookup: RegistryWrapper.WrapperLookup, helper: RagiumPlugin.RecipeHelper) {
        // solar panel
        HTShapedRecipeJsonBuilder
            .create(RagiumItems.SOLAR_PANEL)
            .patterns(
                "AAA",
                "BBB",
                "CCC",
            ).input('A', ConventionalItemTags.GLASS_PANES)
            .input('B', RagiumItemTags.SILICON_PLATES)
            .input('C', RagiumHardModeContents.ALUMINUM.getPrefixedTag(hardMode))
            .offerTo(exporter)
        // primitive circuit
        HTShapedRecipeJsonBuilder
            .create(RagiumItems.Circuits.PRIMITIVE)
            .patterns(
                "ABA",
                "CDC",
                "ABA",
            ).input('A', ConventionalItemTags.REDSTONE_DUSTS)
            .input('B', RagiumHardModeContents.IRON.getPrefixedTag(hardMode))
            .input('C', RagiumHardModeContents.COPPER.getPrefixedTag(hardMode))
            .input('D', ItemTags.PLANKS)
            .unlockedBy(ConventionalItemTags.REDSTONE_DUSTS)
            .offerTo(exporter)
        // basic circuit
        HTShapedRecipeJsonBuilder
            .create(RagiumItems.Circuits.BASIC)
            .patterns(
                "ABA",
                "CDC",
                "ABA",
            ).input('A', RagiumItems.Dusts.RAGINITE)
            .input('B', RagiumHardModeContents.STEEL.getPrefixedTag(hardMode))
            .input('C', RagiumHardModeContents.GOLD.getPrefixedTag(hardMode))
            .input('D', RagiumItems.Circuits.PRIMITIVE)
            .unlockedBy(RagiumItems.Dusts.RAGINITE)
            .offerTo(exporter)
        // led
        HTMachineRecipeJsonBuilderNew
            .create(RagiumRecipeTypes.ASSEMBLER)
            .itemInput(RagiumItems.LUMINESCENCE_DUST)
            .itemInput(ConventionalItemTags.GLASS_BLOCKS_COLORLESS)
            .itemInput(RagiumHardModeContents.COPPER.getPrefixedTag(hardMode))
            .itemOutput(RagiumItems.LED, 4)
            .offerTo(exporter, "led")
        // glasses
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE)
            .itemInput(RagiumHardModeContents.STEEL.getPrefixedTag(hardMode))
            .itemInput(ConventionalItemTags.GLASS_BLOCKS_COLORLESS, 4)
            .itemOutput(RagiumBlocks.Glasses.STEEL, 4)
            .offerTo(exporter, RagiumBlocks.Glasses.STEEL, "_from_steel")
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE)
            .itemInput(RagiumHardModeContents.DEEP_STEEL.getPrefixedTag(hardMode))
            .itemInput(ConventionalItemTags.GLASS_BLOCKS_COLORLESS, 8)
            .itemOutput(RagiumBlocks.Glasses.STEEL, 8)
            .offerTo(exporter, RagiumBlocks.Glasses.STEEL, "_from_deep_steel")
        // trader catalog
        HTShapelessRecipeJsonBuilder
            .create(RagiumItems.TRADER_CATALOG)
            .input(Items.BOOK)
            .input(RagiumHardModeContents.EMERALD.getPrefixedTag(hardMode))
            .input(ConventionalItemTags.CHESTS)
            .unlockedBy(Items.BOOK)
            .offerTo(exporter)
        // press molds
        mapOf(
            RagiumItems.PressMolds.GEAR to HTTagPrefix.GEAR.commonTagKey,
            RagiumItems.PressMolds.PIPE to ConventionalItemTags.CHESTS,
            RagiumItems.PressMolds.PLATE to HTTagPrefix.PLATE.commonTagKey,
            RagiumItems.PressMolds.ROD to HTTagPrefix.ROD.commonTagKey,
            RagiumItems.PressMolds.WIRE to HTTagPrefix.WIRE.commonTagKey,
        ).forEach { (pressMold: RagiumItems.PressMolds, input: TagKey<Item>) ->
            HTShapedRecipeJsonBuilder
                .create(pressMold)
                .patterns(
                    "AA",
                    "AA",
                    "BC",
                ).input('A', RagiumHardModeContents.STEEL.getPrefixedTag(hardMode))
                .input('B', RagiumItems.FORGE_HAMMER)
                .input('C', input)
                .unlockedBy(input)
                .offerTo(exporter)
        }

        lookup.getWrapperOrThrow(RegistryKeys.FLUID).streamEntries().forEach { entry: RegistryEntry<Fluid> ->
            val id: Identifier = entry.id ?: return@forEach
            val fluid: Fluid = entry.value()
            if (!fluid.isStill(fluid.defaultState)) return@forEach
            val bucket: Item = fluid.bucketItem
            if (bucket.isAir) return@forEach
            // insert to cube
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.INFUSER)
                .itemInput(RagiumItems.EMPTY_FLUID_CUBE)
                .fluidInput(fluid)
                .itemOutput(RagiumAPI.getInstance().createFilledCube(fluid))
                .offerTo(exporter, id.withPrefixedPath("filling_cube/"))
            // insert to bucket
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.INFUSER)
                .itemInput(Items.BUCKET)
                .fluidInput(fluid)
                .itemOutput(bucket)
                .offerTo(exporter, id.withPrefixedPath("filling_bucket/"))
            // extract from bucket
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.EXTRACTOR)
                .itemInput(bucket)
                .itemOutput(Items.BUCKET)
                .fluidOutput(fluid)
                .offerTo(exporter, id.withPrefixedPath("extract_bucket/"))
        }

        craftingMachines(exporter)
    }

    //    Crafting - Machines    //

    private fun craftingMachines(exporter: RecipeExporter) {
        craftExporters(exporter)
        craftPipes(exporter)
        craftDrums(exporter)
        craftCrates(exporter)
        craftCircuits(exporter)
        craftHulls(exporter)
        craftCoils(exporter)
        craftCasing(exporter)

        HTMachineRecipeJsonBuilderNew
            .create(RagiumRecipeTypes.ASSEMBLER)
            .itemInput(RagiumHardModeContents.STEEL.getPrefixedTag(hardMode), 8)
            .itemInput(RagiumHardModeContents.RAGI_STEEL.getPrefixedTag(hardMode), 8)
            .itemOutput(RagiumItems.ENGINE)
            .offerTo(exporter, "engine")

        HTMachineRecipeJsonBuilderNew
            .create(RagiumRecipeTypes.ASSEMBLER, HTMachineTier.ADVANCED)
            .itemInput(RagiumItems.Circuits.ADVANCED, 2)
            .itemInput(RagiumHardModeContents.DEEP_STEEL.getPrefixedTag(hardMode), 4)
            .itemInput(RagiumItems.Plastics.ADVANCED.tagKey, 4)
            // .fluidInput(RagiumFluids.NOBLE_GAS)
            .itemOutput(RagiumItems.LASER_EMITTER)
            .offerTo(exporter, "laser_emitter")

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.COMPRESSOR, HTMachineTier.ADVANCED)
            .itemInput(RagiumHardModeContents.ALUMINUM.getPrefixedTag(hardMode))
            .itemInput(RagiumItems.Plastics.ADVANCED.tagKey)
            .itemInput(RagiumHardModeContents.DEEP_STEEL.getPrefixedTag(hardMode), 2)
            .itemOutput(RagiumItems.STELLA_PLATE)
            .offerTo(exporter, RagiumItems.STELLA_PLATE)

        // machines
        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.MANUAL_FORGE)
            .patterns(
                "AAA",
                " B ",
                "BBB",
            ).input('A', RagiumBlocks.StorageBlocks.RAGI_ALLOY)
            .input('B', Items.BRICKS)
            .unlockedBy(RagiumBlocks.StorageBlocks.RAGI_ALLOY)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.MANUAL_GRINDER)
            .patterns(
                "A  ",
                "BBB",
                "CCC",
            ).input('A', ConventionalItemTags.WOODEN_RODS)
            .input('B', HTMachineTier.PRIMITIVE.getMainMetal().getPrefixedTag(hardMode))
            .input('C', Items.BRICKS)
            .offerTo(exporter)

        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.MANUAL_MIXER)
            .patterns(
                "A A",
                "A A",
                "BBB",
            ).input('A', HTMachineTier.PRIMITIVE.getMainMetal().getPrefixedTag(hardMode))
            .input('B', Items.BRICKS)
            .offerTo(exporter)

        createMechanics(
            exporter,
            RagiumBlocks.NETWORK_INTERFACE,
            RagiumHardModeContents.STEEL.getContent(hardMode),
            RagiumItems.Processors.DIAMOND,
        )
        createMechanics(
            exporter,
            RagiumBlocks.EXTENDED_PROCESSOR,
            RagiumHardModeContents.RAGI_ALLOY.getContent(hardMode),
            RagiumItems.Processors.RAGIUM,
        )
        createMechanics(
            exporter,
            RagiumBlocks.AUTO_ILLUMINATOR,
            RagiumHardModeContents.GOLD.getContent(hardMode),
            RagiumItems.CRIMSON_CRYSTAL,
        )
        createMechanics(
            exporter,
            RagiumBlocks.TELEPORT_ANCHOR,
            RagiumHardModeContents.ALUMINUM.getContent(hardMode),
            RagiumItems.WARPED_CRYSTAL,
        )

        mapOf(
            // consumers
            RagiumMachineKeys.BEDROCK_MINER to RagiumItems.Gears.DIAMOND,
            RagiumMachineKeys.BIOMASS_FERMENTER to Items.COMPOSTER,
            RagiumMachineKeys.DRAIN to Items.BUCKET,
            RagiumMachineKeys.FLUID_DRILL to RagiumBlocks.SHAFT,
            RagiumMachineKeys.GAS_PLANT to Items.GLASS_BOTTLE,
            RagiumMachineKeys.ROCK_GENERATOR to Items.OBSIDIAN,
            // generators
            RagiumMachineKeys.COMBUSTION_GENERATOR to RagiumItems.ENGINE,
            RagiumMachineKeys.NUCLEAR_REACTOR to RagiumItems.STELLA_PLATE,
            RagiumMachineKeys.SOLAR_GENERATOR to RagiumItems.SOLAR_PANEL,
            RagiumMachineKeys.STEAM_GENERATOR to Items.FURNACE,
            RagiumMachineKeys.THERMAL_GENERATOR to Items.MAGMA_BLOCK,
            // processors
            RagiumMachineKeys.ASSEMBLER to Items.CRAFTER,
            RagiumMachineKeys.BLAST_FURNACE to Items.BLAST_FURNACE,
            RagiumMachineKeys.CHEMICAL_REACTOR to Items.GLASS,
            RagiumMachineKeys.CONDENSER to Items.SNOWBALL,
            RagiumMachineKeys.COMPRESSOR to Items.PISTON,
            RagiumMachineKeys.CUTTING_MACHINE to Items.STONECUTTER,
            RagiumMachineKeys.DISTILLATION_TOWER to RagiumBlocks.Drums.BASIC,
            RagiumMachineKeys.ELECTROLYZER to RagiumItems.CHARGED_CARBON_ELECTRODE,
            RagiumMachineKeys.EXTRACTOR to Items.DROPPER,
            RagiumMachineKeys.GRINDER to Items.FLINT,
            RagiumMachineKeys.GROWTH_CHAMBER to Items.IRON_HOE,
            RagiumMachineKeys.INFUSER to Items.HOPPER,
            RagiumMachineKeys.LARGE_CHEMICAL_REACTOR to RagiumBlocks.Glasses.STEEL,
            RagiumMachineKeys.LASER_TRANSFORMER to RagiumItems.LASER_EMITTER,
            RagiumMachineKeys.MIXER to Items.CAULDRON,
            RagiumMachineKeys.MULTI_SMELTER to RagiumItems.BLAZING_CARBON_ELECTRODE,
        ).forEach { (key: HTMachineKey, input: ItemConvertible) ->
            HTMachineTier.entries.forEach { tier: HTMachineTier ->
                HTShapedRecipeJsonBuilder
                    .create(key.createItemStack(tier))
                    .patterns(
                        "ABA",
                        "CDC",
                    ).input('A', tier.getMainMetal().getPrefixedTag(hardMode))
                    .input('B', tier.getCircuit())
                    .input('C', input)
                    .input('D', tier.getCasing())
                    .offerTo(exporter, tier.createId(key))
            }
        }
    }

    private fun craftExporters(exporter: RecipeExporter) {
        RagiumBlocks.Exporters.entries.forEach { exporter1: RagiumBlocks.Exporters ->
            // shaped crafting
            HTShapedRecipeJsonBuilder
                .create(exporter1)
                .patterns(
                    "AAA",
                    " B ",
                    "CDC",
                ).input('A', exporter1.tier.getMainMetal().getPrefixedTag(hardMode))
                .input('B', ConventionalItemTags.GLASS_BLOCKS)
                .input('C', exporter1.tier.getCoil())
                .input('D', Items.PISTON)
                .offerTo(exporter)
            // assembler
            HTMachineRecipeJsonBuilderNew
                .create(RagiumRecipeTypes.ASSEMBLER)
                .itemInput(exporter1.tier.getCoil())
                .itemInput(Items.PISTON)
                .itemOutput(exporter1)
                .offerTo(exporter, exporter1)
        }
    }

    private fun craftPipes(exporter: RecipeExporter) {
        RagiumBlocks.Pipes.entries.forEach { pipe: RagiumBlocks.Pipes ->
            val input: TagKey<Item> = when (pipe) {
                RagiumBlocks.Pipes.STONE -> ConventionalItemTags.STONES
                RagiumBlocks.Pipes.WOODEN -> ItemTags.PLANKS
                RagiumBlocks.Pipes.IRON -> RagiumHardModeContents.IRON.getPrefixedTag(hardMode)
                RagiumBlocks.Pipes.COPPER -> RagiumHardModeContents.COPPER.getPrefixedTag(hardMode)
                RagiumBlocks.Pipes.UNIVERSAL -> RagiumHardModeContents.REFINED_RAGI_STEEL.getPrefixedTag(hardMode)
            }
            // shaped crafting
            HTShapedRecipeJsonBuilder
                .create(pipe)
                .patterns("ABA")
                .input('A', input)
                .input('B', ConventionalItemTags.GLASS_BLOCKS)
                .offerTo(exporter)
            // compressor
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.COMPRESSOR)
                .itemInput(input, 2)
                .catalyst(RagiumItems.PressMolds.PIPE)
                .itemOutput(pipe, 2)
                .offerTo(exporter, pipe)
        }
        RagiumBlocks.CrossPipes.entries.forEach { crossPipe: RagiumBlocks.CrossPipes ->
            val input: TagKey<Item> = when (crossPipe) {
                RagiumBlocks.CrossPipes.STEEL -> RagiumHardModeContents.STEEL
                RagiumBlocks.CrossPipes.GOLD -> RagiumHardModeContents.GOLD
            }.getPrefixedTag(hardMode)
            // shaped crafting
            HTShapedRecipeJsonBuilder
                .create(crossPipe)
                .patterns(
                    " A ",
                    "ABA",
                    " A ",
                ).input('A', input)
                .input('B', ConventionalItemTags.GLASS_BLOCKS)
                .offerTo(exporter)
            // compressor
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.COMPRESSOR)
                .itemInput(input, 4)
                .catalyst(RagiumItems.PressMolds.PIPE)
                .itemOutput(crossPipe, 2)
                .offerTo(exporter, crossPipe)
        }
        RagiumBlocks.PipeStations.entries.forEach { station: RagiumBlocks.PipeStations ->
            val input: TagKey<Item> = when (station) {
                RagiumBlocks.PipeStations.ITEM -> ConventionalItemTags.REDSTONE_DUSTS
                RagiumBlocks.PipeStations.FLUID -> ConventionalItemTags.LAPIS_GEMS
            }
            // shaped crafting
            HTShapedRecipeJsonBuilder
                .create(station, 2)
                .patterns(
                    "AAA",
                    "BCB",
                    "AAA",
                ).input('A', input)
                .input('B', RagiumItems.Circuits.BASIC)
                .input('C', ConventionalItemTags.GLASS_BLOCKS)
                .offerTo(exporter)
        }
        RagiumBlocks.FilteringPipes.entries.forEach { filtering: RagiumBlocks.FilteringPipes ->
            val input: TagKey<Item> = when (filtering) {
                RagiumBlocks.FilteringPipes.ITEM -> RagiumHardModeContents.DIAMOND
                RagiumBlocks.FilteringPipes.FLUID -> RagiumHardModeContents.EMERALD
            }.getPrefixedTag(hardMode)
            // shaped crafting
            HTShapedRecipeJsonBuilder
                .create(filtering, 2)
                .patterns(
                    "AAA",
                    "BCB",
                    "AAA",
                ).input('A', input)
                .input('B', RagiumItems.Circuits.ADVANCED)
                .input('C', RagiumBlocks.Glasses.STEEL)
                .offerTo(exporter)
        }
    }

    private fun craftDrums(exporter: RecipeExporter) {
        RagiumBlocks.Drums.entries.forEach { drum: RagiumBlocks.Drums ->
            // shaped crafting
            HTShapedRecipeJsonBuilder
                .create(drum)
                .patterns(
                    "ABA",
                    "ACA",
                    "ABA",
                ).input('A', drum.tier.getSubMetal().getPrefixedTag(hardMode))
                .input('B', drum.tier.getMainMetal().getPrefixedTag(hardMode))
                .input('C', Items.BUCKET)
                .offerTo(exporter)
            // assembler
            HTMachineRecipeJsonBuilderNew
                .create(RagiumRecipeTypes.ASSEMBLER)
                .itemInput(drum.tier.getSubMetal().getPrefixedTag(hardMode), 4)
                .itemInput(Items.BUCKET)
                .itemOutput(drum)
                .offerTo(exporter, drum)
        }
    }

    private fun craftCrates(exporter: RecipeExporter) {
        RagiumBlocks.Crates.entries.forEach { crate: RagiumBlocks.Crates ->
            // shaped crafting
            HTShapedRecipeJsonBuilder
                .create(crate)
                .patterns(
                    "ABA",
                    "ACA",
                    "ABA",
                ).input('A', crate.tier.getSubMetal().getPrefixedTag(hardMode))
                .input('B', crate.tier.getMainMetal().getPrefixedTag(hardMode))
                .input('C', Items.BARREL)
                .offerTo(exporter)
            // assembler
            HTMachineRecipeJsonBuilderNew
                .create(RagiumRecipeTypes.ASSEMBLER)
                .itemInput(crate.tier.getSubMetal().getPrefixedTag(hardMode), 4)
                .itemInput(Items.BARREL)
                .itemOutput(crate)
                .offerTo(exporter, crate)
        }
        // open crate
        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.OPEN_CRATE)
            .patterns(
                "ABA",
                "ACA",
                "ABA",
            ).input('A', RagiumHardModeContents.EMERALD.getPrefixedTag(hardMode))
            .input('B', RagiumHardModeContents.STEEL.getPrefixedTag(hardMode))
            .input('C', Items.HOPPER)
            .offerTo(exporter)
        // void crate
        HTShapedRecipeJsonBuilder
            .create(RagiumBlocks.VOID_CRATE)
            .patterns(
                "ABA",
                "ACA",
                "ABA",
            ).input('A', ConventionalItemTags.OBSIDIANS)
            .input('B', RagiumHardModeContents.STEEL.getPrefixedTag(hardMode))
            .input('C', Items.LAVA_BUCKET)
            .offerTo(exporter)
    }

    private fun craftCircuits(exporter: RecipeExporter) {
        val boardMap: Map<HTMachineTier, TagKey<Item>> =
            mapOf(
                HTMachineTier.PRIMITIVE to RagiumItemTags.SILICON,
                HTMachineTier.BASIC to RagiumItemTags.SILICON_PLATES,
                HTMachineTier.ADVANCED to RagiumItemTags.REFINED_SILICON_PLATES,
            )
        val circuitMap: Map<HTMachineTier, ItemConvertible> =
            mapOf(
                HTMachineTier.PRIMITIVE to Items.REDSTONE,
                HTMachineTier.BASIC to Items.GLOWSTONE_DUST,
                HTMachineTier.ADVANCED to RagiumItems.LUMINESCENCE_DUST,
            )

        RagiumItems.CircuitBoards.entries.forEach { board: RagiumItems.CircuitBoards ->
            val tier: HTMachineTier = board.tier
            val plate: TagKey<Item> = boardMap[tier] ?: return@forEach
            val dope: ItemConvertible = circuitMap[tier] ?: return@forEach
            if (hardMode) {
                // board
                HTMachineRecipeJsonBuilderNew
                    .create(RagiumRecipeTypes.ASSEMBLER, tier)
                    .itemInput(plate)
                    .itemInput(tier.getSubMetal().getPrefixedTag(hardMode))
                    .itemOutput(board)
                    .offerTo(exporter, board)
                // circuit
                HTMachineRecipeJsonBuilderNew
                    .create(RagiumRecipeTypes.ASSEMBLER, tier)
                    .itemInput(board)
                    .itemInput(dope)
                    .itemOutput(board.getCircuit())
                    .offerTo(exporter, board.getCircuit())
            } else {
                HTMachineRecipeJsonBuilderNew
                    .create(RagiumRecipeTypes.ASSEMBLER, tier)
                    .itemInput(plate)
                    .itemInput(tier.getSubMetal().getPrefixedTag(hardMode))
                    .itemInput(dope)
                    .itemOutput(board.getCircuit())
                    .offerTo(exporter, board.getCircuit())
            }
        }
    }

    private fun craftHulls(exporter: RecipeExporter) {
        HTMachineTier.entries.forEach { tier: HTMachineTier ->
            val grate: RagiumBlocks.Grates = tier.getGrate()
            val hull: RagiumBlocks.Hulls = tier.getHull()
            // shaped crafting
            HTShapedRecipeJsonBuilder
                .create(grate, 8)
                .patterns(
                    " A ",
                    "A A",
                    " A ",
                ).input('A', tier.getSteelMetal().getPrefixedTag(hardMode))
                .offerTo(exporter)
            // assembler
            HTMachineRecipeJsonBuilderNew
                .create(RagiumRecipeTypes.ASSEMBLER)
                .itemInput(grate.tier.getSteelMetal().getPrefixedTag(hardMode), 4)
                .catalyst(grate)
                .itemOutput(grate, 16)
                .offerTo(exporter, grate)
            // shaped crafting
            HTShapedRecipeJsonBuilder
                .create(hull, 3)
                .patterns(
                    "AAA",
                    "ACA",
                    "BBB",
                ).input('A', tier.getMainMetal().getPrefixedTag(hardMode))
                .input('B', tier.getCasing())
                .input('C', tier.getCircuit())
                .offerTo(exporter)
            // assembler
            HTMachineRecipeJsonBuilderNew
                .create(RagiumRecipeTypes.ASSEMBLER)
                .itemInput(tier.getMainMetal().getPrefixedTag(hardMode), 3)
                .itemInput(tier.getCasing())
                .itemOutput(hull)
                .offerTo(exporter, hull)
        }
    }

    private fun craftCoils(exporter: RecipeExporter) {
        RagiumBlocks.Coils.entries.forEach { coil: RagiumBlocks.Coils ->
            HTMachineRecipeJsonBuilderNew
                .create(RagiumRecipeTypes.ASSEMBLER)
                .itemInput(coil.tier.getSubMetal().getPrefixedTag(hardMode), 8)
                .itemInput(RagiumBlocks.SHAFT)
                .itemOutput(coil, 2)
                .offerTo(exporter, coil)
        }
    }

    private fun craftCasing(exporter: RecipeExporter) {
        RagiumBlocks.Casings.entries.forEach { casing: RagiumBlocks.Casings ->
            val stone: Item =
                when (casing) {
                    RagiumBlocks.Casings.PRIMITIVE -> Items.STONE
                    RagiumBlocks.Casings.BASIC -> Items.QUARTZ_BLOCK
                    RagiumBlocks.Casings.ADVANCED -> Items.POLISHED_DEEPSLATE
                }
            HTShapedRecipeJsonBuilder
                .create(casing, 3)
                .patterns(
                    "ABA",
                    "BCB",
                    "ABA",
                ).input('A', stone)
                .input('B', casing.tier.getGrate())
                .input('C', HTTagPrefix.GEAR, casing.tier.getSteelMetal().material)
                .offerTo(exporter)
        }
    }

    private fun createMechanics(
        exporter: RecipeExporter,
        output: ItemConvertible,
        side: HTItemContent,
        core: ItemConvertible,
    ) {
        HTShapedRecipeJsonBuilder
            .create(output)
            .patterns(
                "ABA",
                "BCB",
                "ABA",
            ).input('A', RagiumHardModeContents.DEEP_STEEL.getPrefixedTag(hardMode))
            .input('B', side)
            .input('C', core)
            .offerTo(exporter)
    }
}
