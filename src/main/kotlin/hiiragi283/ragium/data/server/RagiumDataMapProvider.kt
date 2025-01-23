package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineRegistry
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.DataMapValueRemover
import java.util.concurrent.CompletableFuture

class RagiumDataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    DataMapProvider(packOutput, lookupProvider) {
    override fun gather() {
        // Machine
        val machineBuilder: AdvancedBuilder<HTMachineKey, Item, DataMapValueRemover.Default<HTMachineKey, Item>> =
            builder(HTMachineKey.DATA_MAP_TYPE)

        RagiumAPI.getInstance().machineRegistry.entryMap.forEach { (key: HTMachineKey, entry: HTMachineRegistry.Entry) ->
            machineBuilder.add(entry.id, key, false)
        }
    }
}
