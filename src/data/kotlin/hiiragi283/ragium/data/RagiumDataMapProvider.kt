package hiiragi283.ragium.data

import hiiragi283.ragium.common.item.RagiumItems
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps

class RagiumDataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : DataMapProvider(packOutput, lookupProvider) {
    override fun gather(provider: HolderLookup.Provider) {
        furnaceFuel()
    }

    private fun furnaceFuel() {
        val builder: Builder<FurnaceFuel, Item> = builder(NeoForgeDataMaps.FURNACE_FUELS)
        builder.add(RagiumItems.BAMBOO_CHARCOAL, FurnaceFuel(200 * 6), false)
        builder.add(RagiumItems.COAL_COKE, FurnaceFuel(200 * 16), false)
        builder.add(RagiumItems.TAR, FurnaceFuel(200 * 4), false)
    }
}
