package hiiragi283.ragium.data.client

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.common.init.RagiumBlockProperties
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.core.Direction
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredBlock
import org.slf4j.Logger

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
        }.map(HTBlockContent::get)
            .forEach(::simpleBlock)

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
                            .withExistingParent(id.path, "ragium:block/hull")
                            .texture("top", hull.machineTier.getStorageBlock().blockId)
                            .texture("inside", hull.machineTier.getCasing().blockId)
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

        // Machine
        RagiumAPI.getInstance().machineRegistry.blocks.forEach { holder: DeferredBlock<HTMachineBlock> ->
            val builder: ConfiguredModel.Builder<MultiPartBlockStateBuilder.PartBuilder> =
                getMultipartBuilder(holder.get()).part()

            HTMachineTier.entries.forEach { tier: HTMachineTier ->
                builder
                    .modelFile(ModelFile.UncheckedModelFile(tier.getHull().blockId))
                    .addModel()
                    .condition(HTMachineTier.PROPERTY, tier)
            }

            val inactiveId: ResourceLocation = holder.id.withPrefix("block/machine/")
            val activeId: ResourceLocation = holder.id.withPath { "block/machine/${it}_active" }

            BlockStateProperties.HORIZONTAL_FACING.possibleValues.forEach { front: Direction ->
                // inactive front
                builder
                    .rotationY(front.getRotationY())
                    .modelFile(
                        models()
                            .withExistingParent(inactiveId.toString(), RagiumAPI.id("block/machine_front"))
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
                            .withExistingParent(activeId.toString(), RagiumAPI.id("block/machine_front"))
                            .texture("front", activeId)
                            .renderType("cutout"),
                    ).addModel()
                    .condition(BlockStateProperties.HORIZONTAL_FACING, front)
                    .condition(RagiumBlockProperties.ACTIVE, true)
            }
        }
    }

    private fun Direction.getRotationY(): Int = ((this.toYRot() + 180) % 360).toInt()
}
