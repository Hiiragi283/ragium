package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.RotatedPillarBlock
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredBlock

class RagiumBlockStateProvider(output: PackOutput, exFileHelper: ExistingFileHelper) :
    BlockStateProvider(output, RagiumAPI.MOD_ID, exFileHelper) {
    override fun registerStatesAndModels() {
        // Simple Blocks
        buildList {
            addAll(RagiumBlocks.StorageBlocks.entries)
            addAll(RagiumBlocks.Grates.entries)
            addAll(RagiumBlocks.Casings.entries)
        }.map(HTBlockContent::get)
            .forEach(::simpleBlock)

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
        }
    }
}
