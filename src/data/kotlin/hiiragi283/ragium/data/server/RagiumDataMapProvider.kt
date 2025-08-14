package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumDataMaps
import hiiragi283.ragium.api.data.HTFluidFuelData
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import java.util.concurrent.CompletableFuture

@Suppress("DEPRECATION")
class RagiumDataMapProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>) :
    DataMapProvider(output, provider) {
    private lateinit var provider: HolderLookup.Provider

    override fun gather(provider: HolderLookup.Provider) {
        this.provider = provider

        compostables()
        fuels()

        burningFuels()
        thermalFuels()
    }

    private fun compostables() {
        val builder: Builder<Compostable, Item> = builder(NeoForgeDataMaps.COMPOSTABLES)

        builder.add(RagiumCommonTags.Items.CROPS_WARPED_WART, Compostable(0.65f), false)
    }

    private fun fuels() {
        val builder: Builder<FurnaceFuel, Item> = builder(NeoForgeDataMaps.FURNACE_FUELS)

        builder.add(RagiumItems.RAGI_COKE, FurnaceFuel(200 * 8 * 4), false)
    }

    private fun burningFuels() {
        val builder: Builder<HTFluidFuelData, Fluid> = builder(RagiumDataMaps.BURNING_FUEL)

        builder.add(RagiumFluidContents.CRUDE_OIL, HTFluidFuelData(20 * 1, 100))
    }

    private fun thermalFuels() {
        val builder: Builder<HTFluidFuelData, Fluid> = builder(RagiumDataMaps.THERMAL_FUEL)

        builder.add(HTFluidContent.LAVA, HTFluidFuelData(20 * 10, 100))
    }

    private fun <T : Any> Builder<T, Fluid>.add(content: HTFluidContent<*, *, *>, value: T): Builder<T, Fluid> =
        add(content.commonTag, value, false)
}
