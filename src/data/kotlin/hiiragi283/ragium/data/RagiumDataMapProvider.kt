package hiiragi283.ragium.data

import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.ragium.api.tag.HTItemPart
import hiiragi283.ragium.api.tag.HTMaterial
import hiiragi283.ragium.item.RagiumItems
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
        builder.add(RagiumItems.COAL_COKE, FurnaceFuel(200 * 16), false)
        builder.add(RagiumItems.BAMBOO_CHARCOAL, FurnaceFuel(200 * 6), false)

        for (fuel: HTMaterial.Fuel in HTMaterial.Fuel.entries) {
            val item: HTSimpleDeferredItem = RagiumItems.MATERIAL_ITEMS[HTItemPart.TINY, fuel] ?: continue
            val time: Int = when (fuel) {
                HTMaterial.Fuel.COAL -> 1
                HTMaterial.Fuel.CHARCOAL -> 1
                HTMaterial.Fuel.COAL_COKE -> 2
            } * 200
            builder.add(item, FurnaceFuel(time), false)
        }
    }
}
