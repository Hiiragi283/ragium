package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumDataMaps
import hiiragi283.ragium.api.data.HTFluidFuelData
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.fluidTagKey
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.api.util.material.HTBlockMaterialVariant
import hiiragi283.ragium.api.util.material.HTItemMaterialVariant
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.material.RagiumMaterialType
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
        builder(NeoForgeDataMaps.COMPOSTABLES)
            .add(RagiumCommonTags.Items.CROPS_WARPED_WART, Compostable(0.65f), false)
    }

    private fun furnaceFuels() {
        builder(NeoForgeDataMaps.FURNACE_FUELS)
            .add(HTItemMaterialVariant.GEM, RagiumMaterialType.CRIMSON_CRYSTAL, FurnaceFuel(200 * 24))
            .add(HTBlockMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.CRIMSON_CRYSTAL, FurnaceFuel(200 * 24 * 9))
            .add(RagiumItems.COMPRESSED_SAWDUST, FurnaceFuel(200 * 6), false)
            .add(RagiumItems.RAGI_COKE, FurnaceFuel(200 * 16), false)
            .add(RagiumItems.RESIN, FurnaceFuel(200 * 4), false)
            .add(RagiumItems.TAR, FurnaceFuel(200 * 4), false)
    }

    //    Ragium    //

    private fun combustionFuels() {
        builder(RagiumDataMaps.COMBUSTION_FUEL)
            // lowest
            .add(RagiumFluidContents.CRUDE_OIL, 100)
            .add("oil", 100)
            .add("creosote", 100)
            // low
            .add(RagiumConst.ACTUALLY, "canola_oil", 50)
            // medium
            .add(RagiumFluidContents.NATURAL_GAS, 20)
            .add("ethanol", 20)
            .add("bioethanol", 20)
            .add("lpg", 20)
            .add(RagiumConst.ACTUALLY, "refined_canola_oil", 20)
            // high
            .add(RagiumFluidContents.FUEL, 10)
            .add("diesel", 10)
            .add("biodiesel", 10)
            .add(RagiumConst.ACTUALLY, "crystallized_oil", 10)
            // highest
            .add(RagiumFluidContents.CRIMSON_FUEL, 5)
            .add("high_power_biodiesel", 5)
            .add(RagiumConst.ACTUALLY, "empowered_oil", 5)
    }

    private fun thermalFuels() {
        builder(RagiumDataMaps.THERMAL_FUEL)
            .add("steam", 100)
            .add(HTFluidContent.LAVA, 10)
            .add("blaze_blood", 5)
    }

    //    Extensions    //

    private fun <T : Any> Builder<T, Item>.add(variant: HTMaterialVariant.ItemTag, material: HTMaterialType, value: T): Builder<T, Item> =
        add(variant.itemTagKey(material), value, false)

    private fun Builder<HTFluidFuelData, Fluid>.add(content: HTFluidContent<*, *, *>, amount: Int): Builder<HTFluidFuelData, Fluid> =
        add(content.commonTag, HTFluidFuelData(amount), false)

    private fun Builder<HTFluidFuelData, Fluid>.add(path: String, amount: Int): Builder<HTFluidFuelData, Fluid> =
        add(fluidTagKey(commonId(path)), HTFluidFuelData(amount), false)

    private fun Builder<HTFluidFuelData, Fluid>.add(modId: String, path: String, amount: Int): Builder<HTFluidFuelData, Fluid> =
        add(ResourceLocation.fromNamespaceAndPath(modId, path), HTFluidFuelData(amount), false, ModLoadedCondition(modId))
}
