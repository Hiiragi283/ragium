package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.blockId
import hiiragi283.ragium.api.extension.cutoutSimpleBlock
import hiiragi283.ragium.api.extension.layeredBlock
import hiiragi283.ragium.api.extension.layeredModel
import hiiragi283.ragium.api.extension.modelFile
import hiiragi283.ragium.api.extension.simpleAltBlock
import hiiragi283.ragium.api.extension.simpleBlock
import hiiragi283.ragium.api.extension.slabBlock
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.registry.HTBlockSet
import hiiragi283.ragium.common.block.HTBlockStateProperties
import hiiragi283.ragium.common.block.HTFeastBlock
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.Direction
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredBlock

class RagiumBlockStateProvider(output: PackOutput, exFileHelper: ExistingFileHelper) :
    BlockStateProvider(output, RagiumAPI.MOD_ID, exFileHelper) {
    override fun registerStatesAndModels() {
        // Simple Blocks
        buildList {
            add(RagiumBlocks.ADVANCED_RAGI_ALLOY_BLOCK)
            add(RagiumBlocks.AZURE_STEEL_BLOCK)
            add(RagiumBlocks.CHOCOLATE_BLOCK)
            add(RagiumBlocks.COOKED_MEAT_BLOCK)
            add(RagiumBlocks.CRIMSON_CRYSTAL_BLOCK)
            add(RagiumBlocks.DEEP_STEEL_BLOCK)
            add(RagiumBlocks.ELDRITCH_PEARL_BLOCK)
            add(RagiumBlocks.MEAT_BLOCK)
            add(RagiumBlocks.RAGI_ALLOY_BLOCK)
            add(RagiumBlocks.RAGI_CRYSTAL_BLOCK)
            add(RagiumBlocks.WARPED_CRYSTAL_BLOCK)

            add(RagiumBlocks.CRIMSON_SOIL)
            add(RagiumBlocks.SILT)

            add(RagiumBlocks.SPONGE_CAKE)

            add(RagiumBlocks.BASIC_MACHINE_FRAME)
            add(RagiumBlocks.ADVANCED_MACHINE_FRAME)
            add(RagiumBlocks.DEVICE_CASING)

            add(RagiumBlocks.CEU)
            add(RagiumBlocks.ENI)
            add(RagiumBlocks.EXP_COLLECTOR)
            add(RagiumBlocks.ITEM_COLLECTOR)
            add(RagiumBlocks.SPRINKLER)
            add(RagiumBlocks.TELEPORT_ANCHOR)

            addAll(RagiumBlocks.LED_BLOCKS.values)
        }.forEach(::simpleBlock)

        layeredBlock(
            RagiumBlocks.MYSTERIOUS_OBSIDIAN,
            vanillaId("block/obsidian"),
            RagiumAPI.id("block/mysterious_obsidian"),
        )

        slabBlock(RagiumBlocks.SPONGE_CAKE_SLAB, RagiumBlocks.SPONGE_CAKE)

        RagiumBlocks.GLASSES.forEach(::cutoutSimpleBlock)

        for (sets: HTBlockSet in RagiumBlocks.DECORATIONS) {
            sets.addBlockStates(this)
        }

        // Ore
        RagiumBlocks.RAGINITE_ORES.addBlockStates(this)
        RagiumBlocks.RAGI_CRYSTAL_ORES.addBlockStates(this)

        simpleBlock(
            RagiumBlocks.RESONANT_DEBRIS.get(),
            models()
                .withExistingParent(RagiumBlocks.RESONANT_DEBRIS.id.path, "cube_column")
                .texture("end", RagiumBlocks.RESONANT_DEBRIS.blockId.withSuffix("_top"))
                .texture("side", RagiumBlocks.RESONANT_DEBRIS.blockId.withSuffix("_side")),
        )

        // Log
        logBlockWithRenderType(RagiumBlocks.ASH_LOG.get(), "cutout")

        // Bush
        val expBerryModel: BlockModelBuilder = models()
            .leaves(
                RagiumAPI.id("block/exp_berry_bush").toString(),
                vanillaId("block/oak_leaves"),
            ).renderType("cutout")

        getVariantBuilder(RagiumBlocks.EXP_BERRY_BUSH.get())
            .forAllStates { state: BlockState ->
                val modelId: BlockModelBuilder = when (state.getValue(BlockStateProperties.AGE_3)) {
                    3 -> models().layeredModel(
                        "block/exp_berry_bush_mature",
                        vanillaId("block/oak_leaves"),
                        RagiumAPI.id("item/exp_berries"),
                    )

                    else -> expBerryModel
                }
                ConfiguredModel.builder().modelFile(modelId).build()
            }

        // Food
        simpleAltBlock(RagiumBlocks.SWEET_BERRIES_CAKE)
        feastBlock(RagiumBlocks.COOKED_MEAT_ON_THE_BONE)

        // Machine Frame
        simpleAltBlock(RagiumBlocks.WOODEN_CASING, vanillaId("block/note_block"))

        simpleBlock(
            RagiumBlocks.STONE_CASING.get(),
            models().cubeColumn(
                "block/stone_casing",
                vanillaId("block/furnace_side"),
                vanillaId("block/furnace_top"),
            ),
        )

        // Machine
        fun machine(holder: DeferredBlock<*>, top: ResourceLocation, bottom: ResourceLocation) {
            horizontalBlock(
                holder.get(),
                models()
                    .withExistingParent("block/" + holder.id.path, RagiumAPI.id("block/machine_base"))
                    .texture("top", top)
                    .texture("bottom", bottom)
                    .texture("front", holder.id.withPath { "block/${it}_front" }),
            )
        }

        val basicMachine: ResourceLocation = RagiumAPI.id("block/basic_machine_casing")
        machine(RagiumBlocks.CRUSHER, basicMachine, vanillaId("block/bricks"))
        machine(RagiumBlocks.BLOCK_BREAKER, basicMachine, vanillaId("block/bricks"))
        machine(RagiumBlocks.EXTRACTOR, basicMachine, vanillaId("block/bricks"))

        val advancedMachine: ResourceLocation = RagiumAPI.id("block/advanced_machine_casing")
        machine(RagiumBlocks.ALLOY_SMELTER, advancedMachine, vanillaId("block/nether_bricks"))
        machine(RagiumBlocks.MELTER, advancedMachine, vanillaId("block/polished_blackstone_bricks"))
        machine(RagiumBlocks.REFINERY, advancedMachine, vanillaId("block/polished_blackstone_bricks"))
        machine(RagiumBlocks.SOLIDIFIER, advancedMachine, vanillaId("block/polished_blackstone_bricks"))

        // Device
        layeredBlock(
            RagiumBlocks.WATER_COLLECTOR,
            vanillaId("block/water_still"),
            RagiumAPI.id("block/device_overlay"),
        )

        layeredBlock(
            RagiumBlocks.LAVA_COLLECTOR,
            vanillaId("block/lava_still"),
            RagiumAPI.id("block/device_overlay"),
        )

        simpleAltBlock(RagiumBlocks.MILK_DRAIN)

        // Manual Machine
        /*getMultipartBuilder(RagiumBlocks.MANUAL_GRINDER.get()).part().apply {
            for (step: Int in BlockStateProperties.AGE_7.possibleValues) {
                val modelId: ResourceLocation = RagiumAPI.id(
                    when (step % 2 == 0) {
                        true -> "block/manual_grinder"
                        false -> "block/manual_grinder_diagonal"
                    },
                )
                val direction: Direction = when (step / 2) {
                    0 -> Direction.NORTH
                    1 -> Direction.EAST
                    2 -> Direction.SOUTH
                    3 -> Direction.WEST
                    else -> error("")
                }

                this
                    .modelFile(ModelFile.UncheckedModelFile(modelId))
                    .rotationY(direction.getRotationY())
                    .addModel()
                    .condition(BlockStateProperties.AGE_7, step)
            }
        }*/

        // uncheckedSimpleBlock(RagiumBlocks.DISENCHANTING_TABLE)

        // Storages
        for (drum: DeferredBlock<*> in RagiumBlocks.DRUMS) {
            val id: ResourceLocation = drum.blockId
            simpleBlock(
                drum.get(),
                models().cubeColumn(
                    id.path,
                    id.withSuffix("_side"),
                    id.withSuffix("_top"),
                ),
            )
        }
    }

    //    Extensions    //

    private fun Direction.getRotationY(): Int = ((this.toYRot() + 180) % 360).toInt()

    private fun <T : Any> ConfiguredModel.Builder<T>.rotationY(state: BlockState): ConfiguredModel.Builder<T> =
        rotationY(state.getValue(HTBlockStateProperties.HORIZONTAL).getRotationY())

    /*private fun cauldronBlock(holder: DeferredBlock<*>) {
        if (holder == RagiumBlocks.HONEY_CAULDRON) return
        getVariantBuilder(holder.get())
            .forAllStates { state: BlockState ->
                val level: Int = state.getValue(LayeredCauldronBlock.LEVEL)
                val suffix: String = when (level) {
                    3 -> "_full"
                    else -> "_level$level"
                }
                ConfiguredModel
                    .builder()
                    .modelFile(modelFile(vanillaId("block/water_cauldron").withSuffix(suffix)))
                    .build()
            }
    }*/

    private fun feastBlock(holder: DeferredBlock<out HTFeastBlock>) {
        getVariantBuilder(holder.get()).forAllStates { state: BlockState ->
            val block: HTFeastBlock = holder.get()
            val property: IntegerProperty = block.getServingsProperty()
            val servings: Int = state.getValue(property)
            val suffix: String =
                if (servings == 0) {
                    if (block.hasLeftovers) {
                        "_leftover"
                    } else {
                        "_stage${property.possibleValues.size - 2}"
                    }
                } else {
                    "_stage${block.getMaxServings() - servings}"
                }
            ConfiguredModel
                .builder()
                .modelFile(modelFile(holder.blockId.withSuffix(suffix)))
                .rotationY(state)
                .build()
        }
    }
}
