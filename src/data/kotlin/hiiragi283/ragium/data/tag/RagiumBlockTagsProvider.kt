package hiiragi283.ragium.data.tag

import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class RagiumBlockTagsProvider(
    fileHelper: ExistingFileHelper,
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
) : HTTagsProvider.DataGen<Block>(fileHelper, output, Registries.BLOCK, lookupProvider, RagiumAPI.MOD_ID) {
    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Block>) {
        // Mineable
        val hoe: HTTagBuilder<Block> = factory.apply(BlockTags.MINEABLE_WITH_HOE)
        sequence {
            yield(RagiumBlocks.MEAT_BLOCK)
            yield(RagiumBlocks.COOKED_MEAT_BLOCK)
        }.forEach(hoe::add)

        val pickaxe: HTTagBuilder<Block> = factory.apply(BlockTags.MINEABLE_WITH_PICKAXE)
        sequence {
            // Machine
            yield(RagiumBlocks.ALLOY_SMELTER)
            yield(RagiumBlocks.ASSEMBLER)
            yield(RagiumBlocks.AUTO_CHISEL)
            yield(RagiumBlocks.CRUSHER)
            yield(RagiumBlocks.CUTTING_MACHINE)
            yield(RagiumBlocks.ELECTRIC_FURNACE)
            yield(RagiumBlocks.PLANTER)

            yield(RagiumBlocks.FREEZER)
            yield(RagiumBlocks.MELTER)
            yield(RagiumBlocks.PYROLYZER)
            yield(RagiumBlocks.REFINERY)
            yield(RagiumBlocks.WASHER)

            yield(RagiumBlocks.BREWERY)
            yield(RagiumBlocks.CHEMICAL_WASHER)
            yield(RagiumBlocks.FLUID_MIXER)
            yield(RagiumBlocks.MIXER)

            yield(RagiumBlocks.FLUID_DUPLICATOR)
            // Device
            yield(RagiumBlocks.ENCHANTER)
            // Storage
            yield(RagiumBlocks.UNIVERSAL_CHEST)

            yield(RagiumBlocks.BATTERY)
            yield(RagiumBlocks.CRATE)
            yield(RagiumBlocks.TANK)

            yield(RagiumBlocks.VOID_TANK)

            yield(RagiumBlocks.IMITATION_SPAWNER)

            yield(RagiumBlocks.CREATIVE_BATTERY)
            yield(RagiumBlocks.CREATIVE_CRATE)
            yield(RagiumBlocks.CREATIVE_TANK)
        }.forEach(pickaxe::add)
        // Other
    }
}
