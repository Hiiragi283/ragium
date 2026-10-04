package hiiragi283.ragium.data

import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import java.util.concurrent.CompletableFuture

class RagiumDataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    DataMapProvider(packOutput, lookupProvider) {
    override fun gather(provider: HolderLookup.Provider) {
        furnaceFuel()

        mortarRepair()
    }

    private fun furnaceFuel() {
        val builder: Builder<FurnaceFuel, Item> = builder(NeoForgeDataMaps.FURNACE_FUELS)
            .add(RagiumItems.BAMBOO_CHARCOAL, FurnaceFuel(200 * 6), false)
            .add(RagiumItems.COAL_COKE, FurnaceFuel(200 * 16), false)
            .add(RagiumItems.TAR, FurnaceFuel(200 * 4), false)
            .add(RagiumItems.PITCH_COKE, FurnaceFuel(200 * 12), false)

        for (fuel: RagiumMaterial.Fuel in RagiumMaterial.Fuel.entries) {
            val time: Int = when (fuel) {
                RagiumMaterial.Fuel.COAL -> 200
                RagiumMaterial.Fuel.CHARCOAL -> 200
                RagiumMaterial.Fuel.COAL_COKE -> 400
                RagiumMaterial.Fuel.PITCH_COKE -> 300
            }
            RagiumBlocks.STORAGE_BLOCKS[fuel]?.item?.let { storage: Holder<Item> ->
                builder.add(storage, FurnaceFuel(time * 80), false)
            }
            RagiumItems.MATERIAL_ITEMS[HTItemPart.TINY, fuel]?.let { tiny: Holder<Item> ->
                builder.add(tiny, FurnaceFuel(time), false)
            }
        }
    }

    private fun mortarRepair() {
        builder(RagiumDataMaps.MORTAR_REPAIR)
            .add(Blocks.CRACKED_DEEPSLATE_BRICKS, Blocks.DEEPSLATE_BRICKS)
            .add(Blocks.CRACKED_DEEPSLATE_TILES, Blocks.DEEPSLATE_TILES)
            .add(Blocks.CRACKED_NETHER_BRICKS, Blocks.NETHER_BRICKS)
            .add(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS)
            .add(Blocks.CRACKED_STONE_BRICKS, Blocks.STONE_BRICKS)
            .add(Blocks.INFESTED_CRACKED_STONE_BRICKS, Blocks.INFESTED_STONE_BRICKS)
    }

    @Suppress("DEPRECATION")
    private fun <T : Any> Builder<T, Block>.add(
        block: Block,
        value: T,
        replace: Boolean = false,
        conditions: List<ICondition> = listOf()
    ): Builder<T, Block> = this.add(block.builtInRegistryHolder(), value, replace, *conditions.toTypedArray())
}
