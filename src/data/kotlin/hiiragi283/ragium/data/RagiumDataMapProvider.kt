package hiiragi283.ragium.data

import hiiragi283.ragium.api.material.HTBlockPart
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import java.util.concurrent.CompletableFuture

class RagiumDataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    DataMapProvider(packOutput, lookupProvider) {
    override fun gather(provider: HolderLookup.Provider) {
        furnaceFuel()
    }

    private fun furnaceFuel() {
        val builder: Builder<FurnaceFuel, Item> = builder(NeoForgeDataMaps.FURNACE_FUELS)
        builder.add(RagiumItems.BAMBOO_CHARCOAL, FurnaceFuel(200 * 6), false)
        builder.add(RagiumItems.COAL_COKE, FurnaceFuel(200 * 16), false)
        builder.add(RagiumItems.TAR, FurnaceFuel(200 * 4), false)

        for (fuel: RagiumMaterial.Fuel in RagiumMaterial.Fuel.entries) {
            val time: Int = when (fuel) {
                RagiumMaterial.Fuel.COAL -> 1
                RagiumMaterial.Fuel.CHARCOAL -> 1
                RagiumMaterial.Fuel.COAL_COKE -> 2
            } * 200
            RagiumBlocks.MATERIAL_BLOCKS[HTBlockPart.STORAGE_BLOCK, fuel]?.item?.let { storage: Holder<Item> ->
                builder.add(storage, FurnaceFuel(time * 80), false)
            }
            RagiumItems.MATERIAL_ITEMS[HTItemPart.TINY, fuel]?.let { tiny: Holder<Item> ->
                builder.add(tiny, FurnaceFuel(time), false)
            }
        }
    }
}
