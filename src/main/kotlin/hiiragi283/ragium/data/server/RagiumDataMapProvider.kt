package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.component.HTRadioactivity
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.content.HTFluidContent
import hiiragi283.ragium.api.extension.asHolder
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import java.util.concurrent.CompletableFuture

class RagiumDataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    DataMapProvider(packOutput, lookupProvider) {
    private fun <T : Any> Builder<T, Item>.addItem(item: ItemLike, value: T): Builder<T, Item> = add(item.asHolder(), value, false)

    private fun <T : Any> Builder<T, Item>.addContent(content: HTBlockContent, value: T): Builder<T, Item> = add(content.id, value, false)

    private fun Builder<Map<HTMachineKey, Int>, Fluid>.addFuel(
        fluid: HTFluidContent,
        machine: HTMachineKey,
        value: Int,
    ): Builder<Map<HTMachineKey, Int>, Fluid> = add(fluid.commonTag, mapOf(machine to value), false)

    private fun Builder<FurnaceFuel, Item>.addFuel(item: ItemLike, second: Int): Builder<FurnaceFuel, Item> =
        addItem(item, FurnaceFuel(second * 200))

    override fun gather() {
        // fluid
        machineFuel(builder(RagiumAPI.DataMapTypes.MACHINE_FUEL))
        // item
        furnaceFuel(builder(NeoForgeDataMaps.FURNACE_FUELS))

        machineKey(builder(RagiumAPI.DataMapTypes.MACHINE_KEY))
        machineTier(builder(RagiumAPI.DataMapTypes.MACHINE_TIER))
        radioactivity(builder(RagiumAPI.DataMapTypes.RADIOACTIVES))
    }

    //    Block    //

    //    Fluid    //

    private fun machineFuel(builder: Builder<Map<HTMachineKey, Int>, Fluid>) {
        // Combustion
        builder.add(
            RagiumFluidTags.NON_NITRO_FUEL,
            mapOf(RagiumMachineKeys.COMBUSTION_GENERATOR to FluidType.BUCKET_VOLUME / 10),
            false,
        )
        builder.add(
            RagiumFluidTags.NITRO_FUEL,
            mapOf(RagiumMachineKeys.COMBUSTION_GENERATOR to FluidType.BUCKET_VOLUME / 100),
            false,
        )
        // Steam
        builder.addFuel(RagiumFluids.STEAM, RagiumMachineKeys.STEAM_TURBINE, FluidType.BUCKET_VOLUME / 10)
        // Thermal
        builder.add(
            RagiumFluidTags.THERMAL_FUEL,
            mapOf(RagiumMachineKeys.THERMAL_GENERATOR to FluidType.BUCKET_VOLUME / 10),
            false,
        )
    }

    //    Item    //

    private fun furnaceFuel(builder: Builder<FurnaceFuel, Item>) {
        builder.addFuel(RagiumItems.getMaterialItem(HTTagPrefix.GEM, RagiumMaterials.RESIDUAL_COKE), 8)
        builder.addFuel(RagiumItems.getMaterialItem(HTTagPrefix.GEM, RagiumMaterials.FIERY_COAL), 64)

        builder.addFuel(RagiumBlocks.STORAGE_BLOCKS[RagiumMaterials.RESIDUAL_COKE]!!, 80)
        builder.addFuel(RagiumBlocks.STORAGE_BLOCKS[RagiumMaterials.FIERY_COAL]!!, 640)
    }

    private fun machineKey(builder: Builder<HTMachineKey, Item>) {
        RagiumAPI.machineRegistry.blockMap.forEach { (key: HTMachineKey, content: HTBlockContent) ->
            builder.addContent(content, key)
        }
    }

    private fun machineTier(builder: Builder<HTMachineTier, Item>) {
        fun registerTier(contents: Iterable<HTBlockContent.Tier>) {
            for (content: HTBlockContent.Tier in contents) {
                builder.addContent(content, content.machineTier)
            }
        }

        registerTier(RagiumBlocks.Grates.entries)
        registerTier(RagiumBlocks.Casings.entries)
        registerTier(RagiumBlocks.Coils.entries)
        registerTier(RagiumBlocks.Burners.entries)

        registerTier(RagiumBlocks.Drums.entries)
    }

    private fun radioactivity(builder: Builder<HTRadioactivity, Item>) {
        builder
            .addItem(RagiumItems.URANIUM_FUEL, HTRadioactivity.MEDIUM)
            .addItem(RagiumItems.NUCLEAR_WASTE, HTRadioactivity.LOW)
            .addItem(RagiumItems.PLUTONIUM_FUEL, HTRadioactivity.HIGH)
    }
}
