package hiiragi283.ragium.common.internal

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.content.HTItemContent
import hiiragi283.ragium.api.data.HTMachineRecipeJsonBuilder
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
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object RagiumHardModePlugin : RagiumPlugin {
    override val priority: Int = -90

    private val hardMode: Boolean by lazy(RagiumAPI.getInstance()::isHardMode)

    override fun registerRuntimeRecipe(exporter: RecipeExporter, helper: RagiumPlugin.RecipeHelper) {
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
        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ASSEMBLER)
            .itemInput(RagiumItems.LUMINESCENCE_DUST)
            .itemInput(ConventionalItemTags.GLASS_BLOCKS_COLORLESS)
            .itemInput(RagiumHardModeContents.COPPER.getPrefixedTag(hardMode))
            .itemOutput(RagiumItems.LED, 4)
            .offerTo(exporter, RagiumItems.LED)
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

        Registries.FLUID.streamEntries().forEach { entry: RegistryEntry<Fluid> ->
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

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ASSEMBLER)
            .itemInput(RagiumHardModeContents.STEEL.getPrefixedTag(hardMode), 8)
            .itemInput(RagiumHardModeContents.RAGI_STEEL.getPrefixedTag(hardMode), 8)
            .itemOutput(RagiumItems.ENGINE)
            .offerTo(exporter, RagiumItems.ENGINE)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.ASSEMBLER, HTMachineTier.ADVANCED)
            .itemInput(RagiumItems.Circuits.ADVANCED, 2)
            .itemInput(RagiumHardModeContents.DEEP_STEEL.getPrefixedTag(hardMode), 4)
            .itemInput(RagiumItems.ENGINEERING_PLASTIC_PLATE, 4)
            .fluidInput(RagiumFluids.NOBLE_GAS)
            .itemOutput(RagiumItems.LASER_EMITTER)
            .offerTo(exporter, RagiumItems.LASER_EMITTER)

        HTMachineRecipeJsonBuilder
            .create(RagiumMachineKeys.COMPRESSOR, HTMachineTier.ADVANCED)
            .itemInput(RagiumHardModeContents.ALUMINUM.getPrefixedTag(hardMode))
            .itemInput(RagiumItems.ENGINEERING_PLASTIC_PLATE)
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
        createMechanics(
            exporter,
            RagiumBlocks.OPEN_CRATE,
            RagiumItems.Gems.FLUORITE,
            Items.HOPPER,
        )
        createMechanics(
            exporter,
            RagiumBlocks.TRASH_BOX,
            RagiumHardModeContents.IRON.getContent(hardMode),
            Items.LAVA_BUCKET,
        )
        // consumers
        createProcessor(
            exporter,
            RagiumMachineKeys.BEDROCK_MINER,
            RagiumItems.Gears.DIAMOND,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.BIOMASS_FERMENTER,
            Items.COMPOSTER,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.DRAIN,
            Items.BUCKET,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.FLUID_DRILL,
            RagiumBlocks.SHAFT,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.ROCK_GENERATOR,
            Items.LAVA_BUCKET,
            Items.WATER_BUCKET,
        )
        // generators
        HTMachineTier.entries.forEach { tier: HTMachineTier ->
            // combustion generator
            HTShapedRecipeJsonBuilder
                .create(RagiumMachineKeys.COMBUSTION_GENERATOR.createItemStack(tier))
                .patterns(
                    "AAA",
                    "ABA",
                    "ACA",
                ).input('A', tier.getSteelMetal().getPrefixedTag(hardMode))
                .input('B', RagiumItems.ENGINE)
                .input('C', tier.getCircuit())
                .offerTo(exporter, tier.createId(RagiumMachineKeys.COMBUSTION_GENERATOR))
        }
        createProcessor(
            exporter,
            RagiumMachineKeys.NUCLEAR_REACTOR,
            RagiumItems.STELLA_PLATE,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.SOLAR_GENERATOR,
            RagiumItems.SOLAR_PANEL,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.STEAM_GENERATOR,
            Items.FURNACE,
            Items.BUCKET,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.THERMAL_GENERATOR,
            Items.MAGMA_BLOCK,
        )
        // processors
        /*createProcessor(
            exporter,
            RagiumMachineKeys.ALLOY_FURNACE,
            Items.FURNACE
        )*/
        createProcessor(
            exporter,
            RagiumMachineKeys.ASSEMBLER,
            RagiumItems.Circuits.PRIMITIVE,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.BLAST_FURNACE,
            Items.BLAST_FURNACE,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.CHEMICAL_REACTOR,
            Items.GLASS,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.COMPRESSOR,
            Items.PISTON,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.CUTTING_MACHINE,
            Items.STONECUTTER,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.DISTILLATION_TOWER,
            RagiumBlocks.Drums.BASIC,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.ELECTROLYZER,
            RagiumItems.CHARGED_CARBON_ELECTRODE,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.EXTRACTOR,
            Items.HOPPER,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.GRINDER,
            Items.FLINT,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.GROWTH_CHAMBER,
            Items.IRON_HOE,
            Items.IRON_AXE,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.INFUSER,
            Items.GLASS_BOTTLE,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.LASER_TRANSFORMER,
            RagiumItems.LASER_EMITTER,
        )
        /*createProcessor(
            exporter,
            RagiumMachineKeys.METAL_FORMER,
            RagiumBlocks.MANUAL_FORGE,
            RagiumItemsNew.Ingredients.FORGE_HAMMER
        )*/
        createProcessor(
            exporter,
            RagiumMachineKeys.MIXER,
            Items.CAULDRON,
        )
        createProcessor(
            exporter,
            RagiumMachineKeys.MULTI_SMELTER,
            RagiumItems.BLAZING_CARBON_ELECTRODE,
        )
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
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.ASSEMBLER)
                .itemInput(exporter1.tier.getCoil())
                .itemInput(Items.PISTON)
                .itemOutput(exporter1)
                .offerTo(exporter, exporter1)
        }
    }

    private fun craftPipes(exporter: RecipeExporter) {
        RagiumBlocks.Pipes.entries.forEach { pipe: RagiumBlocks.Pipes ->
            val input: TagKey<Item> = when (pipe) {
                RagiumBlocks.Pipes.STONE -> RagiumHardModeContents.STONE
                RagiumBlocks.Pipes.WOODEN -> RagiumHardModeContents.WOOD
                RagiumBlocks.Pipes.IRON -> RagiumHardModeContents.IRON
                RagiumBlocks.Pipes.COPPER -> RagiumHardModeContents.COPPER
                RagiumBlocks.Pipes.UNIVERSAL -> RagiumHardModeContents.REFINED_RAGI_STEEL
            }.getPrefixedTag(hardMode)
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
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.ASSEMBLER)
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
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.ASSEMBLER)
                .itemInput(crate.tier.getSubMetal().getPrefixedTag(hardMode), 4)
                .itemInput(Items.BARREL)
                .itemOutput(crate)
                .offerTo(exporter, crate)
        }
    }

    private fun craftCircuits(exporter: RecipeExporter) {
        val boardMap: Map<HTMachineTier, TagKey<Item>> =
            mapOf(
                HTMachineTier.PRIMITIVE to RagiumItemTags.SILICON,
                HTMachineTier.BASIC to RagiumItemTags.SILICON_PLATES,
                HTMachineTier.ADVANCED to RagiumItemTags.REFINED_SILICON_PLATES,
            )
        val circuitMap: Map<HTMachineTier, TagKey<Item>> =
            mapOf(
                HTMachineTier.PRIMITIVE to ConventionalItemTags.REDSTONE_DUSTS,
                HTMachineTier.BASIC to ConventionalItemTags.GLOWSTONE_DUSTS,
                HTMachineTier.ADVANCED to RagiumItems.Dusts.RAGI_CRYSTAL.prefixedTagKey,
            )

        RagiumItems.CircuitBoards.entries.forEach { board: RagiumItems.CircuitBoards ->
            val tier: HTMachineTier = board.tier
            val plate: TagKey<Item> = boardMap[tier] ?: return@forEach
            val dope: TagKey<Item> = circuitMap[tier] ?: return@forEach
            // board
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.ASSEMBLER, tier)
                .itemInput(plate)
                .itemInput(tier.getSubMetal().getPrefixedTag(hardMode))
                .itemOutput(board)
                .offerTo(exporter, board)
            // circuit
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.ASSEMBLER, tier)
                .itemInput(board)
                .itemInput(dope)
                .itemOutput(board.getCircuit())
                .offerTo(exporter, board.getCircuit())
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
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.ASSEMBLER)
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
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.ASSEMBLER)
                .itemInput(tier.getMainMetal().getPrefixedTag(hardMode), 3)
                .itemInput(tier.getCasing())
                .itemOutput(hull)
                .offerTo(exporter, hull)
        }
    }

    private fun craftCoils(exporter: RecipeExporter) {
        RagiumBlocks.Coils.entries.forEach { coil: RagiumBlocks.Coils ->
            HTMachineRecipeJsonBuilder
                .create(RagiumMachineKeys.ASSEMBLER)
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

    private fun createProcessor(
        exporter: RecipeExporter,
        key: HTMachineKey,
        left: ItemConvertible,
        right: ItemConvertible = left,
    ) {
        HTMachineTier.entries.forEach { tier: HTMachineTier ->
            HTShapedRecipeJsonBuilder
                .create(key.createItemStack(tier))
                .patterns(
                    "AEA",
                    "BCD",
                ).input('A', tier.getMainMetal().getPrefixedTag(hardMode))
                .input('B', left)
                .input('C', tier.getCasing())
                .input('D', right)
                .input('E', tier.getCircuit())
                .offerTo(exporter, tier.createId(key))
        }
    }
}
