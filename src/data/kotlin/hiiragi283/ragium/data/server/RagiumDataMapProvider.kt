package hiiragi283.ragium.data.server

import de.ellpeck.actuallyadditions.mod.fluids.InitFluids
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.data.map.HTFluidFuelData
import hiiragi283.ragium.api.data.map.HTMobHead
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.createCommonTag
import hiiragi283.ragium.api.variant.HTMaterialVariant
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.variant.HTBlockMaterialVariant
import hiiragi283.ragium.common.variant.HTItemMaterialVariant
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.EnchantmentTags
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.LevelBasedValue
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.conditions.ModLoadedCondition
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import java.util.function.Supplier

@Suppress("DEPRECATION")
class RagiumDataMapProvider(context: HTDataGenContext) : DataMapProvider(context.output, context.registries) {
    private lateinit var provider: HolderLookup.Provider

    override fun gather(provider: HolderLookup.Provider) {
        this.provider = provider

        compostables()
        furnaceFuels()

        thermalFuels()
        combustionFuels()
        nuclearFuels()
        enchFactories()

        mobHead()
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

    private fun thermalFuels() {
        builder(RagiumDataMaps.THERMAL_FUEL)
            .add("steam", 100)
            .add(HTFluidContent.LAVA, 10)
            .add("blaze_blood", 5)
    }

    private fun combustionFuels() {
        builder(RagiumDataMaps.COMBUSTION_FUEL)
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

    private fun nuclearFuels() {
        builder(RagiumDataMaps.NUCLEAR_FUEL)
            .add(RagiumFluidContents.GREEN_FUEL, 5)
    }

    private fun enchFactories() {
        builder(RagiumDataMaps.ENCHANT_FUEL)
            .add(EnchantmentTags.TREASURE, LevelBasedValue.perLevel(3f), false)
            .add(EnchantmentTags.CURSE, LevelBasedValue.perLevel(-1f), false)
    }

    //    Mob Head    //

    private fun mobHead() {
        builder(RagiumDataMaps.MOB_HEAD)
            .add(EntityType.SKELETON, HTMobHead(Items.SKELETON_SKULL))
            .add(EntityType.WITHER_SKELETON, HTMobHead(Items.WITHER_SKELETON_SKULL))
            .add(EntityType.ZOMBIE, HTMobHead(Items.ZOMBIE_HEAD))
            .add(EntityType.CREEPER, HTMobHead(Items.CREEPER_HEAD))
            .add(EntityType.ENDER_DRAGON, HTMobHead(Items.DRAGON_HEAD))
            .add(EntityType.PIGLIN, HTMobHead(Items.PIGLIN_HEAD))
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

    // Item
    private fun <T : Any> Builder<T, Item>.add(variant: HTMaterialVariant.ItemTag, material: HTMaterialType, value: T): Builder<T, Item> =
        add(variant.itemTagKey(material), value, false)

    // Fluid
    private fun <T : Any> Builder<T, Fluid>.add(fluid: Supplier<out Fluid>, value: T): Builder<T, Fluid> =
        add(fluid.get().toHolderLike(), value)

    private fun Builder<HTFluidFuelData, Fluid>.add(content: HTFluidContent<*, *, *>, amount: Int): Builder<HTFluidFuelData, Fluid> =
        add(content.commonTag, HTFluidFuelData(amount), false)

    private fun Builder<HTFluidFuelData, Fluid>.add(path: String, amount: Int): Builder<HTFluidFuelData, Fluid> =
        add(Registries.FLUID.createCommonTag(path), HTFluidFuelData(amount), false)

    // Entity Type
    private fun <T : Any> Builder<T, EntityType<*>>.add(type: EntityType<*>, value: T): Builder<T, EntityType<*>> =
        add(type.toHolderLike(), value)
}
