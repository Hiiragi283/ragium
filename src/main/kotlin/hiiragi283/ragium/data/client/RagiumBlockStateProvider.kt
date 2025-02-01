package hiiragi283.ragium.data.client

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.property.getOrDefault
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.core.Direction
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ModelFile
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
            add(RagiumBlocks.SOUL_MAGMA_BLOCK)

            addAll(RagiumBlocks.STORAGE_BLOCKS.values)
            addAll(RagiumBlocks.Casings.entries)

            add(RagiumBlocks.PLASTIC_BLOCK)
            addAll(RagiumBlocks.LEDBlocks.entries)
        }.map(Supplier<out Block>::get)
            .forEach(::simpleBlock)

        RagiumBlocks.CasingWalls.entries.forEach { wall: RagiumBlocks.CasingWalls ->
            wallBlock(wall.holder.get(), wall.machineTier.getCasing().blockId)
        }

        // Ore
        RagiumBlocks.ORES.forEach { (variant: HTOreVariant, key: HTMaterialKey, ore: DeferredBlock<out Block>) ->
            simpleBlock(
                ore.get(),
                ConfiguredModel(
                    models()
                        .withExistingParent(ore.id.path, RagiumAPI.id("block/layered"))
                        .blockTexture("layer0", variant.baseStoneName)
                        .blockTexture("layer1", RagiumAPI.id(key.name))
                        .cutout(),
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
                        ).cutout(),
                ),
            )
        }

        // Hull
        RagiumBlocks.Hulls.entries.forEach { hull: RagiumBlocks.Hulls ->
            getVariantBuilder(hull.get())
                .partialState()
                .setModels(
                    ConfiguredModel(
                        models()
                            .withExistingParent(hull, RagiumAPI.id("block/hull"))
                            .blockTexture("top", hull.machineTier.getStorageBlock().id)
                            .blockTexture("bottom", hull.machineTier.getCasing().id)
                            .cutout(),
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

        // Burner
        RagiumBlocks.Burners.entries.forEach { burner: RagiumBlocks.Burners ->
            val tier: HTMachineTier = burner.machineTier
            simpleBlock(
                burner.get(),
                models()
                    .withExistingParent(burner, RagiumAPI.id("block/burner"))
                    .blockTexture("bars", tier.getGrate().id)
                    .blockTexture(
                        "core",
                        when (tier) {
                            HTMachineTier.BASIC -> ResourceLocation.withDefaultNamespace("coal_block")
                            HTMachineTier.ADVANCED -> ResourceLocation.withDefaultNamespace("magma")
                            HTMachineTier.ELITE -> RagiumAPI.id("soul_magma_block")
                            HTMachineTier.ULTIMATE -> RagiumAPI.id("ultimate_burner")
                        },
                    ).blockTexture("side", tier.getCoil().id.withSuffix("_side"))
                    .blockTexture("top", tier.getCoil().id.withSuffix("_top"))
                    .cutout(),
            )
        }

        // Drum
        RagiumBlocks.Drums.entries.forEach { drum: RagiumBlocks.Drums ->
            val id: ResourceLocation = drum.blockId
            simpleBlock(drum.get(), models().cubeTop(id.path, id.withSuffix("_side"), id.withSuffix("_top")))
        }

        // Decoration
        RagiumBlocks.Decorations.entries.forEach { decoration: RagiumBlocks.Decorations ->
            simpleBlock(decoration.get(), ModelFile.UncheckedModelFile(decoration.parent.blockId))
        }

        // Food
        simpleBlock(RagiumBlocks.SPONGE_CAKE.get())

        simpleBlock(
            RagiumBlocks.SWEET_BERRIES_CAKE.get(),
            ConfiguredModel(ModelFile.UncheckedModelFile(RagiumAPI.id("block/sweet_berries_cake"))),
        )

        // Manual Machine
        getMultipartBuilder(RagiumBlocks.MANUAL_GRINDER.get()).part().apply {
            BlockStateProperties.AGE_7.possibleValues.forEach { step: Int ->
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
        }

        // Utility
        RagiumBlocks.SHAFT.let { holder: DeferredBlock<RotatedPillarBlock> ->
            val model = ModelFile.UncheckedModelFile(holder.id.withPrefix("block/"))
            axisBlock(holder.get(), model, model)
        }

        simpleBlock(
            RagiumBlocks.CHEMICAL_GLASS.get(),
            models().cutoutSimpleBlock("block/chemical_glass", RagiumAPI.id("block/chemical_glass")),
        )

        buildList {
            addAll(RagiumBlocks.ADDONS)
        }.map(Supplier<out Block>::get)
            .forEach(::simpleBlock)

        // Machine
        RagiumAPI.machineRegistry.forEachEntries { key: HTMachineKey, content: HTBlockContent?, property: HTPropertyHolder ->
            val block: Block = content?.get() ?: return@forEachEntries
            val properties: HTPropertyHolder = key.getProperty()
            getVariantBuilder(block)
                .forAllStates { state: BlockState ->
                    val modelId: ResourceLocation =
                        properties.getOrDefault(HTMachinePropertyKeys.MODEL_MAPPER)(key)
                    val rotation: Int = properties
                        .getOrDefault(HTMachinePropertyKeys.ROTATION_MAPPER)(state.getValue(BlockStateProperties.HORIZONTAL_FACING))
                        .getRotationY()

                    ConfiguredModel
                        .builder()
                        .modelFile(ModelFile.UncheckedModelFile(modelId))
                        .rotationY(rotation)
                        .build()
                }
        }

        /*RagiumAPI.machineRegistry.blocks.forEach { content: HTBlockContent ->
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
        }*/
    }

    private fun Direction.getRotationY(): Int = ((this.toYRot() + 180) % 360).toInt()
}
