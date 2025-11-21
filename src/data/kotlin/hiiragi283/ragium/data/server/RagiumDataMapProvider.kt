package hiiragi283.ragium.data.server

import com.enderio.base.common.init.EIOBlocks
import com.enderio.base.common.init.EIOItems
import de.ellpeck.actuallyadditions.mod.fluids.InitFluids
import dev.shadowsoffire.hostilenetworks.Hostile
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.data.map.HTFluidFuelData
import hiiragi283.ragium.api.data.map.HTMobHead
import hiiragi283.ragium.api.data.map.HTSubEntityTypeIngredient
import hiiragi283.ragium.api.data.map.MapDataMapValueRemover
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.data.map.equip.HTMobEffectEquipAction
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.tag.createCommonTag
import hiiragi283.ragium.common.data.map.HTBlockCrushingMaterialRecipe
import hiiragi283.ragium.common.data.map.HTCompressingMaterialRecipe
import hiiragi283.ragium.common.data.map.HTCrushingMaterialRecipe
import hiiragi283.ragium.common.data.map.HTDataModelEntityIngredient
import hiiragi283.ragium.common.data.map.HTRawSmeltingMaterialRecipe
import hiiragi283.ragium.common.data.map.HTSoulVialEntityIngredient
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.ModLoadedCondition
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps

@Suppress("DEPRECATION")
class RagiumDataMapProvider(context: HTDataGenContext) : DataMapProvider(context.output, context.registries) {
    private lateinit var provider: HolderLookup.Provider

    override fun gather(provider: HolderLookup.Provider) {
        this.provider = provider

        compostables()
        furnaceFuels()

        mobHead()

        thermalFuels()
        combustionFuels()
        nuclearFuels()

        armorEquip()
        subEntityIngredient()

        materialRecipe()
    }

    //    Vanilla    //

    private fun compostables() {
        builder(NeoForgeDataMaps.COMPOSTABLES)
            .add(CommonMaterialPrefixes.CROP, FoodMaterialKeys.WARPED_WART, Compostable(0.65f))
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

    private fun mobHead() {
        builder(RagiumDataMaps.MOB_HEAD)
            .add(EntityType.SKELETON, HTMobHead(Items.SKELETON_SKULL))
            .add(EntityType.WITHER_SKELETON, HTMobHead(Items.WITHER_SKELETON_SKULL))
            .add(EntityType.ZOMBIE, HTMobHead(Items.ZOMBIE_HEAD))
            .add(EntityType.CREEPER, HTMobHead(Items.CREEPER_HEAD))
            .add(EntityType.ENDER_DRAGON, HTMobHead(Items.DRAGON_HEAD))
            .add(EntityType.PIGLIN, HTMobHead(Items.PIGLIN_HEAD))
            // EIO Integration
            .add(EntityType.ENDERMAN, HTMobHead(EIOBlocks.ENDERMAN_HEAD), ModLoadedCondition(RagiumConst.EIO_BASE))
    }

    private fun thermalFuels() {
        builder(RagiumDataMaps.THERMAL_FUEL)
            .add("steam", HTFluidFuelData(100))
            .add(HTFluidContent.LAVA, HTFluidFuelData(10))
            .add("blaze_blood", HTFluidFuelData(5))
    }

    private fun combustionFuels() {
        builder(RagiumDataMaps.COMBUSTION_FUEL)
            // lowest
            .add(RagiumFluidContents.CRUDE_OIL, HTFluidFuelData(100))
            .add("oil", HTFluidFuelData(100))
            .add("creosote", HTFluidFuelData(100))
            // low
            .add(InitFluids.CANOLA_OIL.get(), HTFluidFuelData(50), ModLoadedCondition(RagiumConst.ACTUALLY))
            // medium
            .add(RagiumFluidContents.NATURAL_GAS, HTFluidFuelData(20))
            .add("ethanol", HTFluidFuelData(20))
            .add("bioethanol", HTFluidFuelData(20))
            .add("lpg", HTFluidFuelData(20))
            .add(InitFluids.REFINED_CANOLA_OIL.get(), HTFluidFuelData(20), ModLoadedCondition(RagiumConst.ACTUALLY))
            // high
            .add(RagiumFluidContents.FUEL, HTFluidFuelData(10))
            .add("diesel", HTFluidFuelData(10))
            .add("biodiesel", HTFluidFuelData(10))
            .add(InitFluids.CRYSTALLIZED_OIL.get(), HTFluidFuelData(10), ModLoadedCondition(RagiumConst.ACTUALLY))
            // highest
            .add(RagiumFluidContents.CRIMSON_FUEL, HTFluidFuelData(5))
            .add("high_power_biodiesel", HTFluidFuelData(5))
            .add(InitFluids.EMPOWERED_OIL.get(), HTFluidFuelData(5), ModLoadedCondition(RagiumConst.ACTUALLY))
    }

    private fun nuclearFuels() {
        builder(RagiumDataMaps.NUCLEAR_FUEL)
            .add(RagiumFluidContents.GREEN_FUEL, HTFluidFuelData(5))
    }

    private fun armorEquip() {
        builder(RagiumDataMaps.ARMOR_EQUIP)
            .addHolder(RagiumItems.NIGHT_VISION_GOGGLES, HTMobEffectEquipAction(MobEffects.NIGHT_VISION, -1))
    }

    private fun subEntityIngredient() {
        builder(RagiumDataMaps.SUB_ENTITY_INGREDIENT)
            .addHolder(Items.DRAGON_EGG.toHolderLike(), HTSubEntityTypeIngredient.simple(EntityType.ENDER_DRAGON))
            .addHolder(Items.SNIFFER_EGG.toHolderLike(), HTSubEntityTypeIngredient.simple(EntityType.SNIFFER))
            // EIO
            .add(
                EIOItems.SOUL_VIAL,
                HTSoulVialEntityIngredient,
                false,
                ModLoadedCondition(RagiumConst.EIO_BASE),
            )
            // HNN
            .add(
                Hostile.Items.DATA_MODEL,
                HTDataModelEntityIngredient,
                false,
                ModLoadedCondition(RagiumConst.HOSTILE_NETWORKS),
            )
    }

    private fun materialRecipe() {
        MapDataMapBuilder(builder(RagiumDataMaps.MATERIAL_RECIPE))
            .getOrCreateMap(RagiumRecipeTypes.ALLOYING) {
                put(
                    RagiumAPI.id("raw_to_ingot_with_basic"),
                    HTRawSmeltingMaterialRecipe(
                        CommonMaterialPrefixes.RAW_MATERIAL,
                        2,
                        3,
                        RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC,
                        1,
                    ),
                )
                put(
                    RagiumAPI.id("raw_to_ingot_with_advanced"),
                    HTRawSmeltingMaterialRecipe(
                        CommonMaterialPrefixes.RAW_MATERIAL,
                        1,
                        2,
                        RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED,
                        1,
                    ),
                )

                put(
                    RagiumAPI.id("raw_block_to_ingot_with_basic"),
                    HTRawSmeltingMaterialRecipe(
                        CommonMaterialPrefixes.RAW_STORAGE_BLOCK,
                        2,
                        27,
                        RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC,
                        6,
                    ),
                )
                put(
                    RagiumAPI.id("raw_block_to_ingot_with_advanced"),
                    HTRawSmeltingMaterialRecipe(
                        CommonMaterialPrefixes.RAW_STORAGE_BLOCK,
                        1,
                        18,
                        RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED,
                        6,
                    ),
                )
            }.getOrCreateMap(RagiumRecipeTypes.COMPRESSING) {
                put(
                    RagiumAPI.id("dust_to_gem"),
                    HTCompressingMaterialRecipe.dust(CommonMaterialPrefixes.GEM),
                )
                put(
                    RagiumAPI.id("dust_to_fuel"),
                    HTCompressingMaterialRecipe.dust(CommonMaterialPrefixes.FUEL),
                )
            }.getOrCreateMap(RagiumRecipeTypes.CRUSHING) {
                put(
                    RagiumAPI.id("ingot_to_dust"),
                    HTCrushingMaterialRecipe.dust(CommonMaterialPrefixes.INGOT, 1, 1),
                )
                put(
                    RagiumAPI.id("gem_to_dust"),
                    HTCrushingMaterialRecipe.dust(CommonMaterialPrefixes.GEM, 1, 1),
                )
                put(
                    RagiumAPI.id("plate_to_dust"),
                    HTCrushingMaterialRecipe.dust(CommonMaterialPrefixes.PLATE, 1, 1),
                )
                put(
                    RagiumAPI.id("raw_to_dust"),
                    HTCrushingMaterialRecipe.dust(CommonMaterialPrefixes.RAW_MATERIAL, 3, 4),
                )
                put(
                    RagiumAPI.id("rod_to_dust"),
                    HTCrushingMaterialRecipe.dust(CommonMaterialPrefixes.ROD, 2, 1),
                )
                put(
                    RagiumAPI.id("fuel_to_dust"),
                    HTCrushingMaterialRecipe.dust(CommonMaterialPrefixes.FUEL, 1, 1),
                )

                put(
                    RagiumAPI.id("crop_to_flour"),
                    HTCrushingMaterialRecipe(CommonMaterialPrefixes.CROP, 1, CommonMaterialPrefixes.FLOUR, 1),
                )

                put(
                    RagiumAPI.id("storage_block_to_dust"),
                    HTBlockCrushingMaterialRecipe,
                )
            }
    }

    //    Extensions    //

    private fun <T : Any, R : Any> Builder<T, R>.addHolder(holder: HTHolderLike, value: T, vararg conditions: ICondition): Builder<T, R> =
        add(holder.getId(), value, false, *conditions)

    // Item
    private fun <T : Any> Builder<T, Item>.add(prefix: HTPrefixLike, key: HTMaterialLike, value: T): Builder<T, Item> =
        add(prefix.itemTagKey(key), value, false)

    // Fluid
    private fun <T : Any> Builder<T, Fluid>.add(fluid: Fluid, value: T, vararg conditions: ICondition): Builder<T, Fluid> =
        addHolder(fluid.toHolderLike(), value, *conditions)

    private fun <T : Any> Builder<T, Fluid>.add(content: HTFluidContent<*, *, *>, value: T): Builder<T, Fluid> =
        add(content.commonTag, value, false)

    private fun <T : Any> Builder<T, Fluid>.add(path: String, value: T): Builder<T, Fluid> =
        add(Registries.FLUID.createCommonTag(path), value, false)

    // Entity Type
    private fun <T : Any> Builder<T, EntityType<*>>.add(
        type: EntityType<*>,
        value: T,
        vararg conditions: ICondition,
    ): Builder<T, EntityType<*>> = addHolder(type.toHolderLike(), value, *conditions)

    private class MapDataMapBuilder<R : Any, V : Any>(
        private val builder: AdvancedBuilder<Map<ResourceLocation, V>, R, MapDataMapValueRemover<R, V>>,
    ) {
        inline fun getOrCreateMap(holder: Holder<R>, builderAction: MutableMap<ResourceLocation, V>.() -> Unit): MapDataMapBuilder<R, V> {
            builder.add(holder, buildMap(builderAction), false)
            return this
        }
    }
}
