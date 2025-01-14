package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

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

        // Drum
        RagiumBlocks.Drums.entries.forEach { drum: RagiumBlocks.Drums ->
            val id: ResourceLocation = drum.blockId
            simpleBlock(drum.get(), models().cubeTop(id.path, id.withSuffix("_side"), id.withSuffix("_top")))
        }
    }
}
