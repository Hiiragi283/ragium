package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumDataMaps
import hiiragi283.ragium.api.data.HTFluidFuelData
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.fluidTagKey
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.conditions.ModLoadedCondition
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
        furnaceFuels()

        combustionFuels()
        thermalFuels()
    }

    //    Vanilla    //

    private fun compostables() {
        val builder: Builder<Compostable, Item> = builder(NeoForgeDataMaps.COMPOSTABLES)

        builder.add(RagiumCommonTags.Items.CROPS_WARPED_WART, Compostable(0.65f), false)
    }

    private fun furnaceFuels() {
        val builder: Builder<FurnaceFuel, Item> = builder(NeoForgeDataMaps.FURNACE_FUELS)

        builder.add(RagiumItems.RAGI_COKE, FurnaceFuel(200 * 8 * 4), false)
    }

    //    Ragium    //

    private fun combustionFuels() {
        val builder: Builder<HTFluidFuelData, Fluid> = builder(RagiumDataMaps.COMBUSTION_FUEL)

        builder.add(RagiumFluidContents.CRUDE_OIL, 100)
        builder.add("oil", 100)
        builder.add("creosote", 100)

        builder.add(RagiumConst.ACTUALLY, "canola_oil", 50)

        builder.add(RagiumFluidContents.LPG, 20)
        builder.add("ethanol", 10)
        builder.add("bioethanol", 10)
        builder.add(RagiumConst.ACTUALLY, "refined_canola_oil", 10)

        builder.add(RagiumFluidContents.DIESEL, 10)
        builder.add("biodiesel", 10)
        builder.add(RagiumConst.ACTUALLY, "crystallized_canola_oil", 10)

        builder.add(RagiumFluidContents.BLOOD_DIESEL, 5)
        builder.add("high_power_biodiesel", 5)
        builder.add(RagiumConst.ACTUALLY, "empowered_canola_oil", 10)
    }

    private fun thermalFuels() {
        val builder: Builder<HTFluidFuelData, Fluid> = builder(RagiumDataMaps.THERMAL_FUEL)

        builder.add("steam", 100)

        builder.add(HTFluidContent.LAVA, 10)

        builder.add("blaze_blood", 5)
    }

    private fun Builder<HTFluidFuelData, Fluid>.add(content: HTFluidContent<*, *, *>, amount: Int): Builder<HTFluidFuelData, Fluid> =
        add(content.commonTag, HTFluidFuelData(amount), false)

    private fun Builder<HTFluidFuelData, Fluid>.add(path: String, amount: Int): Builder<HTFluidFuelData, Fluid> =
        add(fluidTagKey(commonId(path)), HTFluidFuelData(amount), false)

    private fun Builder<HTFluidFuelData, Fluid>.add(modId: String, path: String, amount: Int): Builder<HTFluidFuelData, Fluid> =
        add(ResourceLocation.fromNamespaceAndPath(modId, path), HTFluidFuelData(amount), false, ModLoadedCondition(modId))
}
