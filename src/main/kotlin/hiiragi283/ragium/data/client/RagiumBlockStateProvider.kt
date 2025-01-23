package hiiragi283.ragium.data.client

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.init.RagiumBlockProperties
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.data.blockTexture
import hiiragi283.ragium.data.withExistingParent
import net.minecraft.core.Direction
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredBlock
import org.slf4j.Logger
import java.util.function.Supplier

class RagiumBlockStateProvider(output: PackOutput, exFileHelper: ExistingFileHelper) :
    BlockStateProvider(output, RagiumAPI.MOD_ID, exFileHelper) {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    override fun registerStatesAndModels() {
        // Simple Blocks
        buildList {
            addAll(RagiumBlocks.StorageBlocks.entries)
            addAll(RagiumBlocks.Casings.entries)

            add(RagiumBlocks.PLASTIC_BLOCK)
            addAll(RagiumBlocks.LEDBlocks.entries)
        }.map(Supplier<out Block>::get)
            .forEach(::simpleBlock)

        // Ore
        RagiumBlocks.Ores.entries.forEach { ore: RagiumBlocks.Ores ->
            simpleBlock(
                ore.get(),
                ConfiguredModel(
                    models()
                        .withExistingParent(ore.id, RagiumAPI.id("block/layered"))
                        .blockTexture("layer0", ore.oreVariant.baseStoneName)
                        .blockTexture("layer1", RagiumAPI.id(ore.material.name))
                        .renderType("cutout"),
                ),
            )
        }

        // Grate
        RagiumBlocks.Grates.entries.forEach { grate: RagiumBlocks.Grates ->
            simpleBlock(
                grate.get(),
                ConfiguredModel(
                    models()
                        .singleTexture(
                            grate.id.path,
                            ResourceLocation.withDefaultNamespace("block/cube_all"),
                            "all",
                            grate.blockId,
                        ).renderType("cutout"),
                ),
            )
        }

        // Hull
        RagiumBlocks.Hulls.entries.forEach { hull: RagiumBlocks.Hulls ->
            val id: ResourceLocation = hull.blockId
            getVariantBuilder(hull.get())
                .partialState()
                .setModels(
                    ConfiguredModel(
                        models()
                            .withExistingParent(id, RagiumAPI.id("block/hull"))
                            .blockTexture("top", hull.machineTier.getStorageBlock().id)
                            .blockTexture("inside", hull.machineTier.getCasing().id)
                            .texture("side", id)
                            .renderType("cutout"),
                    ),
                )
        }

        // Coil
        RagiumBlocks.Coils.entries.forEach { coil: RagiumBlocks.Coils ->
            val block: RotatedPillarBlock = coil.get() as RotatedPillarBlock
            axisBlock(
                block,
                coil.blockId.withSuffix("_side"),
                coil.blockId.withSuffix("_top"),
            )
        }

        // Drum
        RagiumBlocks.Drums.entries.forEach { drum: RagiumBlocks.Drums ->
            val id: ResourceLocation = drum.blockId
            simpleBlock(drum.get(), models().cubeTop(id.path, id.withSuffix("_side"), id.withSuffix("_top")))
        }

        // Food
        simpleBlock(RagiumBlocks.SPONGE_CAKE.get())

        simpleBlock(
            RagiumBlocks.SWEET_BERRIES_CAKE.get(),
            ConfiguredModel(ModelFile.UncheckedModelFile(RagiumAPI.id("block/sweet_berries_cake"))),
        )

        // Manual Machine
        getMultipartBuilder(RagiumBlocks.MANUAL_GRINDER.get()).part().apply {
            RagiumBlockProperties.LEVEL_7.possibleValues.forEach { step: Int ->
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
                    .condition(RagiumBlockProperties.LEVEL_7, step)
            }
        }

        // Utility
        RagiumBlocks.SHAFT.let { holder: DeferredBlock<RotatedPillarBlock> ->
            val model = ModelFile.UncheckedModelFile(holder.id.withPrefix("block/"))
            axisBlock(holder.get(), model, model)
        }

        buildList {
            add(RagiumBlocks.ENERGY_NETWORK_INTERFACE)
        }.map(Supplier<out Block>::get).forEach(::simpleBlock)

        // Machine
        RagiumAPI.machineRegistry.blocks.forEach { content: HTBlockContent ->
            val builder: ConfiguredModel.Builder<MultiPartBlockStateBuilder.PartBuilder> =
                getMultipartBuilder(content.get()).part()

            HTMachineTier.entries.forEach { tier: HTMachineTier ->
                builder
                    .modelFile(ModelFile.UncheckedModelFile(tier.getHull().blockId))
                    .addModel()
                    .condition(HTMachineTier.PROPERTY, tier)
            }

            val inactiveId: ResourceLocation = content.id.withPrefix("block/machine/")
            val activeId: ResourceLocation = content.id.withPath { "block/machine/${it}_active" }

            BlockStateProperties.HORIZONTAL_FACING.possibleValues.forEach { front: Direction ->
                // inactive front
                builder
                    .rotationY(front.getRotationY())
                    .modelFile(
                        models()
                            .withExistingParent(inactiveId, RagiumAPI.id("block/machine_front"))
                            .texture("front", inactiveId)
                            .renderType("cutout"),
                    ).addModel()
                    .condition(BlockStateProperties.HORIZONTAL_FACING, front)
                    .condition(RagiumBlockProperties.ACTIVE, false)
                // active front
                builder
                    .rotationY(front.getRotationY())
                    .modelFile(
                        models()
                            .withExistingParent(activeId, RagiumAPI.id("block/machine_front"))
                            .texture("front", activeId)
                            .renderType("cutout"),
                    ).addModel()
                    .condition(BlockStateProperties.HORIZONTAL_FACING, front)
                    .condition(RagiumBlockProperties.ACTIVE, true)
            }
        }

        // Fluid Block
        /*RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            getVariantBuilder(fluid.blockHolder.get())
                .partialState()
                .setModels(
                    ConfiguredModel(
                        models()
                            .getBuilder(fluid.blockHolder.id.withPrefix("block/"))
                            .texture("particle", fluid.stillTexture),
                    ),
                )
        }*/
    }

    private fun Direction.getRotationY(): Int = ((this.toYRot() + 180) % 360).toInt()
}
