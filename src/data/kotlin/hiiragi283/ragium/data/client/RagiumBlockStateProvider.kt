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
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.function.Supplier

class RagiumBlockStateProvider(output: PackOutput, exFileHelper: ExistingFileHelper) :
    BlockStateProvider(output, RagiumAPI.MOD_ID, exFileHelper) {
    override fun registerStatesAndModels() {
        // Simple Blocks
        buildList {
            add(RagiumBlocks.SILT)

            add(RagiumBlocks.SPONGE_CAKE)

            add(RagiumBlocks.MACHINE_CASING)
            add(RagiumBlocks.ADVANCED_MACHINE_CASING)
            add(RagiumBlocks.DEVICE_CASING)

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

        // Machine

        // Device
        simpleBlock(
            RagiumBlocks.WATER_WELL.get(),
            models()
                .withExistingParent("block/water_well", RagiumAPI.id("block/fluid_well"))
                .texture("top", RagiumAPI.id("block/azure_tiles"))
                .texture("bottom", vanillaId("block/deepslate_tiles")),
        )

        simpleBlock(
            RagiumBlocks.LAVA_WELL.get(),
            models()
                .withExistingParent("block/lava_well", RagiumAPI.id("block/fluid_well"))
                .texture("top", RagiumAPI.id("block/ember_stone"))
                .texture("bottom", vanillaId("block/nether_bricks")),
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

    val machineCasing: ResourceLocation = RagiumAPI.id("block/machine_casing")
    val ragiAlloyCasing: ResourceLocation = RagiumAPI.id("block/ragi_alloy_casing")

    private fun Direction.getRotationY(): Int = ((this.toYRot() + 180) % 360).toInt()

    private fun <T : Any> ConfiguredModel.Builder<T>.rotationY(state: BlockState): ConfiguredModel.Builder<T> =
        rotationY(state.getValue(HTBlockStateProperties.HORIZONTAL).getRotationY())
}
