package hiiragi283.ragium.data.client

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.property.getOrDefault
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.common.block.storage.HTDrumBlock
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.core.Direction
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.TransparentBlock
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

            add(RagiumBlocks.SLAG_BLOCK)

            addAll(RagiumBlocks.STORAGE_BLOCKS.values)

            add(RagiumBlocks.PLASTIC_BLOCK)
            addAll(RagiumBlocks.LED_BLOCKS.values)
        }.map(Supplier<out Block>::get)
            .forEach(::simpleBlock)

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

        // Burner
        RagiumBlocks.BURNERS_NEW.forEach { burner ->
            val core: ResourceLocation = when (burner) {
                RagiumBlocks.MAGMA_BURNER -> ResourceLocation.withDefaultNamespace("magma")
                RagiumBlocks.SOUL_BURNER -> RagiumAPI.id("soul_magma_block")
                RagiumBlocks.FIERY_BURNER -> RagiumAPI.id("ultimate_burner")
                else -> return
            }
            val base: ResourceLocation = when (burner) {
                RagiumBlocks.MAGMA_BURNER -> "polished_blackstone_bricks"
                RagiumBlocks.SOUL_BURNER -> "end_stone_bricks"
                RagiumBlocks.FIERY_BURNER -> "red_nether_bricks"
                else -> return
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

        // Drum
        RagiumBlocks.COPPER_DRUM.let { drum: DeferredBlock<HTDrumBlock> ->
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

        horizontalBlock(
            RagiumBlocks.PRIMITIVE_BLAST_FURNACE.get(),
            ModelFile.UncheckedModelFile(RagiumAPI.id("block/primitive_blast_furnace")),
        )

        // Utility
        RagiumBlocks.SHAFT.let { holder: DeferredBlock<RotatedPillarBlock> ->
            val model = ModelFile.UncheckedModelFile(holder.blockId)
            axisBlock(holder.get(), model, model)
        }

        RagiumBlocks.GLASSES.forEach { glass: DeferredBlock<TransparentBlock> ->
            simpleBlock(
                glass.get(),
                models().cutoutSimpleBlock(glass.blockId.path, glass.blockId),
            )
        }

        buildList {
            addAll(RagiumBlocks.ADDONS)
        }.map(Supplier<out Block>::get)
            .forEach(::simpleBlock)

        // Machine
        RagiumAPI.getInstance().getMachineRegistry().forEachEntries {
            key: HTMachineKey,
            holder: DeferredBlock<*>?,
            property: HTPropertyHolder,
            ->
            val block: Block = holder?.get() ?: return@forEachEntries
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
    }

    private fun Direction.getRotationY(): Int = ((this.toYRot() + 180) % 360).toInt()
}
