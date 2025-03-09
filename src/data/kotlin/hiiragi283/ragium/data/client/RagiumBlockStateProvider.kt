package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.util.HTCrateVariant
import hiiragi283.ragium.api.util.HTDrumVariant
import hiiragi283.ragium.common.block.storage.HTCrateBlock
import hiiragi283.ragium.common.block.storage.HTDrumBlock
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
import java.util.function.Supplier

class RagiumBlockStateProvider(output: PackOutput, exFileHelper: ExistingFileHelper) :
    BlockStateProvider(output, RagiumAPI.MOD_ID, exFileHelper) {
    override fun registerStatesAndModels() {
        // Simple Blocks
        buildList {
            add(RagiumBlocks.SOUL_MAGMA_BLOCK)

            add(RagiumBlocks.SLAG_BLOCK)

            addAll(RagiumBlocks.STORAGE_BLOCKS.values)

            addAll(RagiumBlocks.LED_BLOCKS.values)
        }.map(Supplier<out Block>::get)
            .forEach(::simpleBlock)

        RagiumBlocks.RAGI_BRICK_SETS.generateStates(this)
        RagiumBlocks.PLASTIC_SETS.generateStates(this)
        RagiumBlocks.BLUE_NETHER_BRICK_SETS.generateStates(this)

        // Ore
        RagiumBlocks.RAGINITE_ORES.generateStates(this)
        RagiumBlocks.RAGI_CRYSTAL_ORES.generateStates(this)

        // Log
        logBlockWithRenderType(RagiumBlocks.ASH_LOG.get(), "cutout")

        // Burner
        for (burner: DeferredBlock<Block> in RagiumBlocks.BURNERS) {
            val core: ResourceLocation = when (burner) {
                RagiumBlocks.MAGMA_BURNER -> ResourceLocation.withDefaultNamespace("magma")
                RagiumBlocks.SOUL_BURNER -> RagiumAPI.id("soul_magma_block")
                RagiumBlocks.FIERY_BURNER -> RagiumAPI.id("ultimate_burner")
                else -> continue
            }
            val base: ResourceLocation = when (burner) {
                RagiumBlocks.MAGMA_BURNER -> "polished_blackstone_bricks"
                RagiumBlocks.SOUL_BURNER -> "end_stone_bricks"
                RagiumBlocks.FIERY_BURNER -> "red_nether_bricks"
                else -> continue
            }.let(ResourceLocation::withDefaultNamespace)
            simpleBlock(
                burner.get(),
                models()
                    .withExistingParent(burner.id.path, RagiumAPI.id("block/burner"))
                    .texture("bars", "minecraft:block/iron_bars")
                    .blockTexture("core", core)
                    .blockTexture("side", base)
                    .blockTexture("top", base)
                    .cutout(),
            )
        }

        // Crate
        for ((variant: HTCrateVariant, crate: DeferredBlock<HTCrateBlock>) in RagiumBlocks.CRATES) {
            val baseTexId: ResourceLocation = when (variant) {
                HTCrateVariant.WOODEN -> ResourceLocation.withDefaultNamespace("oak_log")
                HTCrateVariant.IRON -> ResourceLocation.withDefaultNamespace("iron_block")
                HTCrateVariant.STEEL -> RagiumAPI.id("steel_block")
                HTCrateVariant.DEEP_STEEL -> RagiumAPI.id("deep_steel_block")
                HTCrateVariant.DIAMOND -> ResourceLocation.withDefaultNamespace("diamond_block")
                HTCrateVariant.NETHERITE -> ResourceLocation.withDefaultNamespace("netherite_block")
            }
            simpleBlock(
                crate.get(),
                ConfiguredModel(
                    models()
                        .withExistingParent(crate.id.path, RagiumAPI.id("block/layered"))
                        .blockTexture("layer0", baseTexId)
                        .blockTexture("layer1", RagiumAPI.id("crate_overlay"))
                        .renderType("cutout"),
                ),
            )
        }

        // Drum
        for ((variant: HTDrumVariant, drum: DeferredBlock<HTDrumBlock>) in RagiumBlocks.DRUMS) {
            val baseTexId: ResourceLocation = when (variant) {
                HTDrumVariant.COPPER -> ResourceLocation.withDefaultNamespace("copper_block")
                HTDrumVariant.GOLD -> ResourceLocation.withDefaultNamespace("gold_block")
                HTDrumVariant.ALUMINUM -> RagiumAPI.id("aluminum_block")
                HTDrumVariant.EMERALD -> ResourceLocation.withDefaultNamespace("emerald_block")
                HTDrumVariant.RAGIUM -> RagiumAPI.id("ragium_block")
            }
            simpleBlock(
                drum.get(),
                ConfiguredModel(
                    models()
                        .withExistingParent(drum.id.path, RagiumAPI.id("block/drum"))
                        .blockTexture("side", baseTexId)
                        .renderType("cutout"),
                ),
            )
        }

        // Food
        simpleBlock(RagiumBlocks.SPONGE_CAKE.get())
        uncheckedSimpleBlock(RagiumBlocks.SWEET_BERRIES_CAKE)

        // Machine Frame
        simpleBlock(RagiumBlocks.WOODEN_CASING, ResourceLocation.withDefaultNamespace("block/note_block"))
        simpleBlock(RagiumBlocks.COBBLESTONE_CASING, ResourceLocation.withDefaultNamespace("block/piston_bottom"))

        for (frame: DeferredBlock<Block> in listOf(
            RagiumBlocks.MACHINE_FRAME,
            RagiumBlocks.CHEMICAL_MACHINE_FRAME,
            RagiumBlocks.PRECISION_MACHINE_FRAME,
        )) {
            val id: ResourceLocation = frame.id
            simpleBlock(
                frame.get(),
                models()
                    .withExistingParent(id.path, RagiumAPI.id("block/machine_casing"))
                    .blockTexture("all", id),
            )
        }

        // Manual Machine
        getMultipartBuilder(RagiumBlocks.MANUAL_GRINDER.get()).part().apply {
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
        }

        uncheckedSimpleBlock(RagiumBlocks.DISENCHANTING_TABLE)

        // Utility
        RagiumBlocks.SHAFT.let { holder: DeferredBlock<RotatedPillarBlock> ->
            val model = ModelFile.UncheckedModelFile(holder.blockId)
            axisBlock(holder.get(), model, model)
        }

        RagiumBlocks.GLASSES.forEach(::cutoutSimpleBlock)

        buildList {
            addAll(RagiumBlocks.ADDONS)
        }.map(Supplier<out Block>::get)
            .forEach(::simpleBlock)

        // Machine
        for (type: HTMachineType in HTMachineType.entries) {
            val block: Block = type.holder.get()
            getVariantBuilder(block)
                .forAllStates { state: BlockState ->
                    ConfiguredModel
                        .builder()
                        .modelFile(ModelFile.UncheckedModelFile(RagiumAPI.id("block/${type.serializedName}")))
                        .rotationY(state.getValue(BlockStateProperties.HORIZONTAL_FACING).getRotationY())
                        .build()
                }
        }
    }

    private fun Direction.getRotationY(): Int = ((this.toYRot() + 180) % 360).toInt()
}
