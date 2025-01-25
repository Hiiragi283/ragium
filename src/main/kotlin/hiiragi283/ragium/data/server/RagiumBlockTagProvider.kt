package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.data.addElement
import hiiragi283.ragium.data.addElements
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagBuilder
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.ExistingFileHelper
import vectorwing.farmersdelight.common.tag.ModTags
import java.util.concurrent.CompletableFuture

class RagiumBlockTagProvider(
    output: PackOutput,
    provider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper,
) : TagsProvider<Block>(output, Registries.BLOCK, provider, RagiumAPI.MOD_ID, existingFileHelper) {
    override fun addTags(provider: HolderLookup.Provider) {
        // Mineable
        val pickaxe: TagBuilder = getOrCreateRawBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
        buildList {
            addAll(RagiumBlocks.Ores.entries)
            addAll(RagiumBlocks.StorageBlocks.entries)

            addAll(RagiumBlocks.Grates.entries)
            addAll(RagiumBlocks.Casings.entries)
            addAll(RagiumBlocks.Hulls.entries)
            addAll(RagiumBlocks.Coils.entries)
            addAll(RagiumBlocks.Burners.entries)

            addAll(RagiumBlocks.Drums.entries)

            addAll(RagiumAPI.machineRegistry.blockMap.values)
        }.forEach(pickaxe::addElement)

        buildList {
            add(RagiumBlocks.SOUL_MAGMA_BLOCK)

            add(RagiumBlocks.SHAFT)
            add(RagiumBlocks.CHEMICAL_GLASS)

            add(RagiumBlocks.PLASTIC_BLOCK)

            add(RagiumBlocks.MANUAL_GRINDER)

            addAll(RagiumBlocks.ADDONS)
        }.forEach(pickaxe::addElement)

        getOrCreateRawBuilder(BlockTags.MINEABLE_WITH_HOE)
            .addElement(RagiumBlocks.SPONGE_CAKE)
            .addElement(RagiumBlocks.SWEET_BERRIES_CAKE)

        // Farmer's Delight
        getOrCreateRawBuilder(ModTags.HEAT_SOURCES).addElements(RagiumBlocks.Burners.entries)
    }
}
