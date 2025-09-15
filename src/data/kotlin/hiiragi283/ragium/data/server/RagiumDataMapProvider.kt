package hiiragi283.ragium.data.server

import de.ellpeck.actuallyadditions.mod.fluids.InitFluids
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.HTFluidFuelData
import hiiragi283.ragium.api.data.HTSolarPower
import hiiragi283.ragium.api.data.RagiumDataMaps
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.fluidTagKey
import hiiragi283.ragium.api.material.HTBlockMaterialVariant
import hiiragi283.ragium.api.material.HTItemMaterialVariant
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.conditions.ModLoadedCondition
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import java.util.concurrent.CompletableFuture
import java.util.function.Supplier

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
        solarPower()
    }

    //    Vanilla    //

    private fun compostables() {
        builder(NeoForgeDataMaps.COMPOSTABLES)
            .add(RagiumCommonTags.Items.CROPS_WARPED_WART, Compostable(0.65f), false)
    }

    private fun furnaceFuels() {
        builder(NeoForgeDataMaps.FURNACE_FUELS)
            .add(HTBlockMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.CRIMSON_CRYSTAL, FurnaceFuel(200 * 24 * 9))
            .add(HTItemMaterialVariant.FUEL, RagiumMaterialType.BAMBOO_CHARCOAL, FurnaceFuel(200 * 6))
            .add(HTItemMaterialVariant.GEM, RagiumMaterialType.CRIMSON_CRYSTAL, FurnaceFuel(200 * 24))
            .add(RagiumItems.COMPRESSED_SAWDUST, FurnaceFuel(200 * 6), false)
            .add(RagiumItems.RAGI_COKE, FurnaceFuel(200 * 16), false)
            .add(RagiumItems.RESIN, FurnaceFuel(200 * 4), false)
            .add(RagiumItems.TAR, FurnaceFuel(200 * 4), false)
    }

    //    Ragium    //

    private fun combustionFuels() {
        builder(RagiumDataMaps.INSTANCE.combustionFuelType)
            // lowest
            .add(RagiumFluidContents.CRUDE_OIL, 100)
            .add("oil", 100)
            .add("creosote", 100)
            // low
            .add(InitFluids.CANOLA_OIL, HTFluidFuelData(50))
            // medium
            .add(RagiumFluidContents.NATURAL_GAS, 20)
            .add("ethanol", 20)
            .add("bioethanol", 20)
            .add("lpg", 20)
            .add(InitFluids.REFINED_CANOLA_OIL, HTFluidFuelData(20))
            // high
            .add(RagiumFluidContents.FUEL, 10)
            .add("diesel", 10)
            .add("biodiesel", 10)
            .add(InitFluids.CRYSTALLIZED_OIL, HTFluidFuelData(10))
            // highest
            .add(RagiumFluidContents.CRIMSON_FUEL, 5)
            .add("high_power_biodiesel", 5)
            .add(InitFluids.EMPOWERED_OIL, HTFluidFuelData(5))
    }

    private fun thermalFuels() {
        builder(RagiumDataMaps.INSTANCE.thermalFuelType)
            .add("steam", 100)
            .add(HTFluidContent.LAVA, 10)
            .add("blaze_blood", 5)
    }

    private fun solarPower() {
        builder(RagiumDataMaps.INSTANCE.solarPowerType)
            // low
            .add(Tags.Blocks.PUMPKINS_JACK_O_LANTERNS, HTSolarPower(0.5f), false)
            // medium
            .add(HTBlockMaterialVariant.STORAGE_BLOCK, HTVanillaMaterialType.GLOWSTONE, HTSolarPower(1f))
            .add(Blocks.SHROOMLIGHT, HTSolarPower(1f))
            // high
            .add(Tags.Blocks.OBSIDIANS_CRYING, HTSolarPower(1.5f), false)
            .add(Blocks.AMETHYST_CLUSTER, HTSolarPower(2f))
            // highest
            .add(Blocks.BEACON, HTSolarPower(4f))
            .add(HTBlockMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.GILDIUM, HTSolarPower(4f))
    }

    //    Extensions    //

    private fun <T : Any, R : Any> Builder<T, R>.add(holder: HTHolderLike, value: T): Builder<T, R> {
        val id: ResourceLocation = holder.getId()
        val conditions: Array<ModLoadedCondition> = id.namespace
            .takeUnless(RagiumConst.BUILTIN_IDS::contains)
            ?.let(::ModLoadedCondition)
            .let(::listOfNotNull)
            .toTypedArray()
        return add(id, value, false, *conditions)
    }

    // Block
    private fun <T : Any> Builder<T, Block>.add(block: Block, value: T): Builder<T, Block> = add(HTHolderLike.fromBlock(block), value)

    private fun <T : Any> Builder<T, Block>.add(
        variant: HTMaterialVariant.BlockTag,
        material: HTMaterialType,
        value: T,
    ): Builder<T, Block> = add(variant.blockTagKey(material), value, false)

    // Item
    private fun <T : Any> Builder<T, Item>.add(item: ItemLike, value: T): Builder<T, Item> = add(HTHolderLike.fromItem(item), value)

    private fun <T : Any> Builder<T, Item>.add(variant: HTMaterialVariant.ItemTag, material: HTMaterialType, value: T): Builder<T, Item> =
        add(variant.itemTagKey(material), value, false)

    // fluid
    private fun <T : Any> Builder<T, Fluid>.add(fluid: Supplier<out Fluid>, value: T): Builder<T, Fluid> =
        add(HTHolderLike.fromFluid(fluid.get()), value)

    private fun Builder<HTFluidFuelData, Fluid>.add(content: HTFluidContent<*, *, *>, amount: Int): Builder<HTFluidFuelData, Fluid> =
        add(content.commonTag, HTFluidFuelData(amount), false)

    private fun Builder<HTFluidFuelData, Fluid>.add(path: String, amount: Int): Builder<HTFluidFuelData, Fluid> =
        add(fluidTagKey(commonId(path)), HTFluidFuelData(amount), false)
}
