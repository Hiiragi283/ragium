package hiiragi283.ragium.data.server

import de.ellpeck.actuallyadditions.mod.fluids.InitFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.data.map.HTFluidFuelData
import hiiragi283.ragium.api.data.map.HTMobHead
import hiiragi283.ragium.api.data.map.MapDataMapValueRemover
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.tag.createCommonTag
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.impl.data.map.HTCrushingMaterialRecipeData
import hiiragi283.ragium.impl.data.map.HTRawSmeltingMaterialRecipeData
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.Holder
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

        materialRecipe()
    }

    //    Vanilla    //

    private fun compostables() {
        builder(NeoForgeDataMaps.COMPOSTABLES)
            .add(RagiumCommonTags.Items.CROPS_WARPED_WART, Compostable(0.65f), false)
    }

    private fun furnaceFuels() {
        builder(NeoForgeDataMaps.FURNACE_FUELS)
            .add(CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.CRIMSON_CRYSTAL, FurnaceFuel(200 * 24 * 9))
            .add(CommonMaterialPrefixes.FUEL, RagiumMaterialKeys.BAMBOO_CHARCOAL, FurnaceFuel(200 * 6))
            .add(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.CRIMSON_CRYSTAL, FurnaceFuel(200 * 24))
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

    //    Material Recipe    //

    private fun materialRecipe() {
        MapDataMapBuilder(builder(RagiumDataMaps.MATERIAL_RECIPE))
            .getOrCreateMap(RagiumRecipeTypes.ALLOYING) {
                put(
                    RagiumAPI.id("raw_to_ingot_with_basic"),
                    HTRawSmeltingMaterialRecipeData(
                        CommonMaterialPrefixes.RAW_MATERIAL,
                        2,
                        3,
                        RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC,
                        1,
                    ),
                )
                put(
                    RagiumAPI.id("raw_to_ingot_with_advanced"),
                    HTRawSmeltingMaterialRecipeData(
                        CommonMaterialPrefixes.RAW_MATERIAL,
                        1,
                        2,
                        RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED,
                        1,
                    ),
                )

                put(
                    RagiumAPI.id("raw_block_to_ingot_with_basic"),
                    HTRawSmeltingMaterialRecipeData(
                        CommonMaterialPrefixes.RAW_STORAGE_BLOCK,
                        2,
                        27,
                        RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC,
                        6,
                    ),
                )
                put(
                    RagiumAPI.id("raw_block_to_ingot_with_advanced"),
                    HTRawSmeltingMaterialRecipeData(
                        CommonMaterialPrefixes.RAW_STORAGE_BLOCK,
                        1,
                        18,
                        RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED,
                        6,
                    ),
                )
            }.getOrCreateMap(RagiumRecipeTypes.CRUSHING) {
                put(
                    RagiumAPI.id("ingot_to_dust"),
                    HTCrushingMaterialRecipeData(CommonMaterialPrefixes.INGOT, 1, 1),
                )
                put(
                    RagiumAPI.id("gem_to_dust"),
                    HTCrushingMaterialRecipeData(CommonMaterialPrefixes.GEM, 1, 1),
                )
                put(
                    RagiumAPI.id("plate_to_dust"),
                    HTCrushingMaterialRecipeData(CommonMaterialPrefixes.PLATE, 1, 1),
                )
                put(
                    RagiumAPI.id("raw_to_dust"),
                    HTCrushingMaterialRecipeData(CommonMaterialPrefixes.RAW_MATERIAL, 3, 4),
                )
                put(
                    RagiumAPI.id("rod_to_dust"),
                    HTCrushingMaterialRecipeData(CommonMaterialPrefixes.ROD, 2, 1),
                )
                put(
                    RagiumAPI.id("fuel_to_dust"),
                    HTCrushingMaterialRecipeData(CommonMaterialPrefixes.FUEL, 1, 1),
                )
            }
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
    private fun <T : Any> Builder<T, Item>.add(prefix: HTPrefixLike, key: HTMaterialLike, value: T): Builder<T, Item> =
        add(prefix.itemTagKey(key), value, false)

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

    private class MapDataMapBuilder<R : Any, V : Any>(
        private val builder: AdvancedBuilder<Map<ResourceLocation, V>, R, MapDataMapValueRemover<R, V>>,
    ) {
        inline fun getOrCreateMap(holder: Holder<R>, builderAction: MutableMap<ResourceLocation, V>.() -> Unit): MapDataMapBuilder<R, V> {
            builder.add(holder, buildMap(builderAction), false)
            return this
        }
    }
}
