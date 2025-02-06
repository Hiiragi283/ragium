package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.asHolder
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.common.block.storage.HTDrumBlock
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import java.util.concurrent.CompletableFuture

class RagiumDataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    DataMapProvider(packOutput, lookupProvider) {
    private fun <T : Any> Builder<T, Item>.addItem(item: ItemLike, value: T): Builder<T, Item> = add(item.asHolder(), value, false)

    private fun Builder<FurnaceFuel, Item>.addFuel(item: ItemLike, second: Int): Builder<FurnaceFuel, Item> =
        addItem(item, FurnaceFuel(second * 200))

    override fun gather() {
        // item
        furnaceFuel(builder(NeoForgeDataMaps.FURNACE_FUELS))

        machineKey(builder(RagiumAPI.DataMapTypes.MACHINE_KEY))
        machineTier(builder(RagiumAPI.DataMapTypes.MACHINE_TIER))
    }

    //    Item    //

    private fun furnaceFuel(builder: Builder<FurnaceFuel, Item>) {
        builder.addFuel(RagiumItems.getMaterialItem(HTTagPrefix.GEM, RagiumMaterials.FIERY_COAL), 64)

        builder.addFuel(RagiumBlocks.STORAGE_BLOCKS[RagiumMaterials.FIERY_COAL]!!, 640)
    }

    private fun machineKey(builder: Builder<HTMachineKey, Item>) {
        RagiumAPI.machineRegistry.blockMap.forEach { (key: HTMachineKey, content: DeferredBlock<*>) ->
            builder.add(content.id, key, false)
        }
    }

    private fun machineTier(builder: Builder<HTMachineTier, Item>) {
        RagiumBlocks.BURNERS.forEach { (tier: HTMachineTier, burner: DeferredBlock<Block>) ->
            builder.addItem(burner, tier)
        }

        RagiumBlocks.DRUMS.forEach { (tier: HTMachineTier, drum: DeferredBlock<HTDrumBlock>) ->
            builder.addItem(drum, tier)
        }
    }
}
