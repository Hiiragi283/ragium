package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.data.HTDefoliant
import hiiragi283.ragium.api.data.RagiumDataMaps
import hiiragi283.ragium.api.extension.asHolder
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import java.util.concurrent.CompletableFuture

class RagiumDataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    DataMapProvider(packOutput, lookupProvider) {
    private fun <T : Any> Builder<T, Item>.addItem(item: ItemLike, value: T, replace: Boolean = false): Builder<T, Item> =
        add(item.asHolder(), value, replace)

    private fun <T : Any> Builder<T, Item>.addItem(
        prefix: HTTagPrefix,
        key: HTMaterialKey,
        value: T,
        replace: Boolean = false,
    ): Builder<T, Item> = add(prefix.createTag(key), value, replace)

    private fun <T : Any> Builder<T, Block>.addBlock(block: Block, value: T, replace: Boolean = false): Builder<T, Block> =
        add(block.defaultBlockState().blockHolder, value, replace)

    override fun gather() {
        // Furnace Fuel
        builder(NeoForgeDataMaps.FURNACE_FUELS)
            .addItem(HTTagPrefix.GEM, RagiumMaterials.FIERY_COAL, FurnaceFuel(64 * 200))
            .addItem(HTTagPrefix.STORAGE_BLOCK, RagiumMaterials.FIERY_COAL, FurnaceFuel(640 * 200))

        // Defoliant
        builder(RagiumDataMaps.DEFOLIANT)
            .add(BlockTags.CROPS, HTDefoliant(Blocks.AIR), false)
            .add(BlockTags.DIRT, HTDefoliant(Blocks.COARSE_DIRT), false)
            .add(BlockTags.LEAVES, HTDefoliant(Blocks.AIR), false)
            .add(BlockTags.REPLACEABLE_BY_TREES, HTDefoliant(Blocks.AIR), false)
            .add(BlockTags.SAPLINGS, HTDefoliant(Blocks.DEAD_BUSH), false)
            .add(BlockTags.WART_BLOCKS, HTDefoliant(Blocks.AIR), false)
            .addBlock(Blocks.BROWN_MUSHROOM_BLOCK, HTDefoliant(Blocks.AIR), false)
            .addBlock(Blocks.CRIMSON_NYLIUM, HTDefoliant(Blocks.NETHERRACK), false)
            .addBlock(Blocks.MOSS_CARPET, HTDefoliant(Blocks.AIR), false)
            .addBlock(Blocks.RED_MUSHROOM_BLOCK, HTDefoliant(Blocks.AIR), false)
            .addBlock(Blocks.WARPED_NYLIUM, HTDefoliant(Blocks.NETHERRACK), false)
    }
}
