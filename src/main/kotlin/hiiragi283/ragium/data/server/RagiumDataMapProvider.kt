package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.data.HTDefoliant
import hiiragi283.ragium.api.data.HTSoap
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
import net.neoforged.neoforge.common.Tags
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

        // Soap
        builder(RagiumDataMaps.SOAP)
            // Colored
            .add(BlockTags.CANDLES, HTSoap(Blocks.CANDLE), false)
            .add(BlockTags.CONCRETE_POWDER, HTSoap(Blocks.WHITE_CONCRETE_POWDER), false)
            .add(BlockTags.TERRACOTTA, HTSoap(Blocks.TERRACOTTA), false)
            .add(BlockTags.WOOL, HTSoap(Blocks.WHITE_WOOL), false)
            .add(BlockTags.WOOL_CARPETS, HTSoap(Blocks.WHITE_CARPET), false)
            .add(Tags.Blocks.CONCRETES, HTSoap(Blocks.WHITE_CONCRETE), false)
            .add(Tags.Blocks.GLASS_BLOCKS_CHEAP, HTSoap(Blocks.GLASS), false)
            .add(Tags.Blocks.GLASS_PANES, HTSoap(Blocks.GLASS_PANE), false)
            // Washing
            .add(Tags.Blocks.COBBLESTONES_MOSSY, HTSoap(Blocks.COBBLESTONE), false)
            .addBlock(Blocks.MOSSY_STONE_BRICKS, HTSoap(Blocks.STONE_BRICKS), false)
            .addBlock(Blocks.INFESTED_STONE, HTSoap(Blocks.STONE), false)
            .addBlock(Blocks.INFESTED_COBBLESTONE, HTSoap(Blocks.COBBLESTONE), false)
            .addBlock(Blocks.INFESTED_STONE_BRICKS, HTSoap(Blocks.STONE_BRICKS), false)
            .addBlock(Blocks.INFESTED_MOSSY_STONE_BRICKS, HTSoap(Blocks.STONE_BRICKS), false)
            .addBlock(Blocks.INFESTED_CRACKED_STONE_BRICKS, HTSoap(Blocks.CRACKED_STONE_BRICKS), false)
            .addBlock(Blocks.INFESTED_CHISELED_STONE_BRICKS, HTSoap(Blocks.CHISELED_STONE_BRICKS), false)
            .addBlock(Blocks.INFESTED_DEEPSLATE, HTSoap(Blocks.DEEPSLATE), false)
    }
}
