package hiiragi283.ragium.data

import hiiragi283.lib.material.CommonMaterials
import hiiragi283.lib.material.HTMaterial
import hiiragi283.lib.material.HTMaterialCategory
import hiiragi283.lib.material.VanillaMaterials
import hiiragi283.ragium.api.tag.HTBlockPart
import hiiragi283.ragium.api.tag.HTItemPart
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.material.RagiumMaterialHelper
import java.util.concurrent.CompletableFuture
import net.minecraft.core.Holder
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

        for (fuel: HTMaterial in RagiumMaterialHelper.MANAGER[HTMaterialCategory.FUEL]) {
            val time: Int = when (fuel) {
                VanillaMaterials.COAL -> 1
                VanillaMaterials.CHARCOAL -> 1
                CommonMaterials.COAL_COKE -> 2
                else -> continue
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
