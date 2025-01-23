package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.extension.asHolder
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.material.HTMaterialDefinition
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import java.util.concurrent.CompletableFuture

class RagiumDataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    DataMapProvider(packOutput, lookupProvider) {
    private fun <T : Any> Builder<T, Item>.add(item: ItemLike, value: T): Builder<T, Item> = add(item.asHolder(), value, false)

    private fun <T : Any> Builder<T, Item>.addContent(content: HTContent<out ItemLike>, value: T): Builder<T, Item> =
        add(content.id, value, false)

    private fun Builder<FurnaceFuel, Item>.addFuel(item: ItemLike, second: Int) = add(item, FurnaceFuel(second * 200))

    override fun gather() {
        // Fuel
        builder(NeoForgeDataMaps.FURNACE_FUELS)
            .addFuel(RagiumItems.COAL_CHIP, 1)
            .addFuel(RagiumItems.RESIDUAL_COKE, 8)

        // Machine
        val machineBuilder: Builder<HTMachineKey, Item> = builder(HTMachineKey.DATA_MAP_TYPE)

        RagiumAPI.machineRegistry.entryMap.forEach { (key: HTMachineKey, entry: HTMachineRegistry.Entry) ->
            machineBuilder.addContent(entry, key)
        }
        // Material
        val materialBuilder: Builder<HTMaterialDefinition, Item> = builder(HTMaterialDefinition.DATA_MAP_TYPE)

        HTTagPrefix.entries.forEach { prefix: HTTagPrefix ->
            RagiumAPI.materialRegistry.keys.forEach { key: HTMaterialKey ->
                materialBuilder.add(prefix.createTag(key), HTMaterialDefinition(prefix, key), false)
            }
        }

        materialBuilder
            .add(Items.NETHERITE_SCRAP, HTMaterialDefinition(HTTagPrefix.GEM, RagiumMaterialKeys.NETHERITE_SCRAP))
    }
}
