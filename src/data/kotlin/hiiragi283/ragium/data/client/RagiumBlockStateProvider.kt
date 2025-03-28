package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTBlockStateProperties
import hiiragi283.ragium.api.extension.cutoutSimpleBlock
import hiiragi283.ragium.api.extension.simpleAltBlock
import hiiragi283.ragium.api.extension.slabBlock
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.core.Direction
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredBlock
import java.util.function.Supplier

class RagiumBlockStateProvider(output: PackOutput, exFileHelper: ExistingFileHelper) :
    BlockStateProvider(output, RagiumAPI.MOD_ID, exFileHelper) {
    override fun registerStatesAndModels() {
        // Simple Blocks
        buildList {
            add(RagiumBlocks.SILT)

            add(RagiumBlocks.SPONGE_CAKE)

            add(RagiumBlocks.DEVICE_CASING)

            add(RagiumBlocks.ITEM_COLLECTOR)
            add(RagiumBlocks.SPRINKLER)
            add(RagiumBlocks.ENI)

            addAll(RagiumBlocks.LED_BLOCKS.values)
            addAll(RagiumBlocks.StorageBlocks.blocks)
        }.map(Supplier<out Block>::get)
            .forEach(::simpleBlock)

        slabBlock(RagiumBlocks.SPONGE_CAKE_SLAB, RagiumBlocks.SPONGE_CAKE)

        RagiumBlocks.GLASSES.forEach(::cutoutSimpleBlock)

        RagiumBlocks.RAGI_BRICK_SETS.addBlockStates(this)
        RagiumBlocks.AZURE_TILE_SETS.addBlockStates(this)
        RagiumBlocks.EMBER_STONE_SETS.addBlockStates(this)
        RagiumBlocks.PLASTIC_SETS.addBlockStates(this)
        RagiumBlocks.BLUE_NETHER_BRICK_SETS.addBlockStates(this)

        // Ore
        RagiumBlocks.RAGINITE_ORES.addBlockStates(this)
        RagiumBlocks.RAGI_CRYSTAL_ORES.addBlockStates(this)

        // Log
        logBlockWithRenderType(RagiumBlocks.ASH_LOG.get(), "cutout")

        // Flower
        simpleBlock(
            RagiumBlocks.LILY_OF_THE_ENDER.get(),
            models().cross("block/lily_of_the_ender", RagiumAPI.id("block/lily_of_the_ender")).renderType("cutout"),
        )

        // Bush
        val expBerryModel: BlockModelBuilder = models()
            .leaves(
                RagiumAPI.id("block/exp_berry_bush").toString(),
                vanillaId("block/oak_leaves"),
            ).renderType("cutout")

        getVariantBuilder(RagiumBlocks.EXP_BERRY_BUSH.get())
            .forAllStates { state: BlockState ->

                val modelId: BlockModelBuilder = when (state.getValue(BlockStateProperties.AGE_3)) {
                    3 -> models()
                        .withExistingParent(
                            RagiumAPI.id("block/exp_berry_bush_mature").toString(),
                            RagiumAPI.id("block/layered"),
                        ).texture("layer0", vanillaId("block/oak_leaves"))
                        .texture("layer1", RagiumAPI.id("item/exp_berries"))
                        .renderType("cutout")

                    else -> expBerryModel
                }
                ConfiguredModel.builder().modelFile(modelId).build()
            }

        // Food
        simpleAltBlock(RagiumBlocks.SWEET_BERRIES_CAKE)

        // Machine Frame
        simpleAltBlock(RagiumBlocks.WOODEN_CASING, "block/note_block")

        simpleBlock(
            RagiumBlocks.STONE_CASING.get(),
            models().cubeColumn(
                "block/stone_casing",
                vanillaId("block/furnace_side"),
                vanillaId("block/furnace_top"),
            ),
        )

        simpleBlock(
            RagiumBlocks.MACHINE_CASING.get(),
            models()
                .withExistingParent("block/machine_casing", RagiumAPI.id("block/casing_base"))
                .texture("top", RagiumAPI.id("block/ragi_alloy_block"))
                .texture("bottom", vanillaId("block/deepslate_tiles")),
        )

        simpleBlock(
            RagiumBlocks.ADVANCED_MACHINE_CASING.get(),
            models()
                .withExistingParent("block/advanced_machine_casing", RagiumAPI.id("block/casing_base"))
                .texture("top", RagiumAPI.id("block/advanced_ragi_alloy_block"))
                .texture("bottom", RagiumAPI.id("block/azure_tiles")),
        )

        // Machine
        fun basicMachine(holder: DeferredBlock<*>) {
            horizontalBlock(
                holder.get(),
                models()
                    .withExistingParent("block/" + holder.id.path, RagiumAPI.id("block/machine_base"))
                    .texture("top", RagiumAPI.id("block/ragi_alloy_block"))
                    .texture("bottom", vanillaId("block/deepslate_tiles"))
                    .texture("front", holder.id.withPath { "block/${it}_front" }),
            )
        }

        basicMachine(RagiumBlocks.CRUSHER)
        basicMachine(RagiumBlocks.EXTRACTOR)

        fun advMachine(holder: DeferredBlock<*>) {
            horizontalBlock(
                holder.get(),
                models()
                    .withExistingParent("block/" + holder.id.path, RagiumAPI.id("block/machine_base"))
                    .texture("top", RagiumAPI.id("block/advanced_ragi_alloy_block"))
                    .texture("bottom", RagiumAPI.id("block/azure_tiles"))
                    .texture("front", holder.id.withPath { "block/${it}_front" }),
            )
        }

        advMachine(RagiumBlocks.CENTRIFUGE)
        advMachine(RagiumBlocks.INFUSER)

        // Device
        simpleBlock(
            RagiumBlocks.WATER_COLLECTOR.get(),
            ConfiguredModel(
                models()
                    .withExistingParent("block/water_collector", RagiumAPI.id("block/layered"))
                    .texture("layer0", "minecraft:block/water_still")
                    .texture("layer1", RagiumAPI.id("block/device_overlay"))
                    .renderType("cutout"),
            ),
        )

        simpleBlock(
            RagiumBlocks.LAVA_COLLECTOR.get(),
            ConfiguredModel(
                models()
                    .withExistingParent("block/lava_collector", RagiumAPI.id("block/layered"))
                    .texture("layer0", "minecraft:block/lava_still")
                    .texture("layer1", RagiumAPI.id("block/device_overlay"))
                    .renderType("cutout"),
            ),
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
    }

    private fun Direction.getRotationY(): Int = ((this.toYRot() + 180) % 360).toInt()

    private fun <T : Any> ConfiguredModel.Builder<T>.rotationY(state: BlockState): ConfiguredModel.Builder<T> =
        rotationY(state.getValue(HTBlockStateProperties.HORIZONTAL).getRotationY())
}
