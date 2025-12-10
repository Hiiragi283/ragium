package hiiragi283.ragium.data.server

import com.enderio.base.common.init.EIOBlocks
import de.ellpeck.actuallyadditions.mod.fluids.InitFluids
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.data.map.HTFluidCoolantData
import hiiragi283.ragium.api.data.map.HTFluidFuelData
import hiiragi283.ragium.api.data.map.HTMobHead
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.data.map.equip.HTMobEffectEquipAction
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.registry.HTFluidHolderLike
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.createCommonTag
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
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
import plus.dragons.createenchantmentindustry.common.registry.CEIDataMaps

@Suppress("DEPRECATION")
class RagiumDataMapProvider(context: HTDataGenContext) : DataMapProvider(context.output, context.registries) {
    private lateinit var provider: HolderLookup.Provider

    override fun gather(provider: HolderLookup.Provider) {
        this.provider = provider

        compostables()
        furnaceFuels()

        mobHead()

        coolants()
        magmaticFuels()
        combustionFuels()

        armorEquip()

        createEnchIndustry()
    }

    //    Vanilla    //

    private fun compostables() {
        builder(NeoForgeDataMaps.COMPOSTABLES)
            .add(CommonMaterialPrefixes.CROP, FoodMaterialKeys.WARPED_WART, Compostable(0.65f))
    }

    private fun furnaceFuels() {
        builder(NeoForgeDataMaps.FURNACE_FUELS)
            .add(CommonMaterialPrefixes.FUEL, CommonMaterialKeys.COAL_COKE, FurnaceFuel(200 * 16))
            .add(CommonMaterialPrefixes.FUEL, RagiumMaterialKeys.BAMBOO_CHARCOAL, FurnaceFuel(200 * 6))
            .add(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.CRIMSON_CRYSTAL, FurnaceFuel(200 * 24))
            .add(CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.CRIMSON_CRYSTAL, FurnaceFuel(200 * 24 * 9))
            .add(RagiumItems.COMPRESSED_SAWDUST, FurnaceFuel(200 * 6), false)
            .add(RagiumItems.TAR, FurnaceFuel(200 * 4), false)
    }

    //    Ragium    //

    private fun mobHead() {
        builder(RagiumDataMapTypes.MOB_HEAD)
            .add(EntityType.SKELETON, HTMobHead(Items.SKELETON_SKULL))
            .add(EntityType.WITHER_SKELETON, HTMobHead(Items.WITHER_SKELETON_SKULL))
            .add(EntityType.ZOMBIE, HTMobHead(Items.ZOMBIE_HEAD))
            .add(EntityType.CREEPER, HTMobHead(Items.CREEPER_HEAD))
            .add(EntityType.ENDER_DRAGON, HTMobHead(Items.DRAGON_HEAD))
            .add(EntityType.PIGLIN, HTMobHead(Items.PIGLIN_HEAD))
            // EIO Integration
            .add(EntityType.ENDERMAN, HTMobHead(EIOBlocks.ENDERMAN_HEAD), ModLoadedCondition(RagiumConst.EIO_BASE))
    }

    private fun coolants() {
        builder(RagiumDataMapTypes.COOLANT)
            .add(HTFluidHolderLike.WATER, HTFluidCoolantData(100))
            .add(RagiumFluidContents.COOLANT, HTFluidCoolantData(25))
    }

    private fun magmaticFuels() {
        val lowest = HTFluidFuelData(40)
        val low = HTFluidFuelData(60)
        val medium = HTFluidFuelData(120)
        val high = HTFluidFuelData(180)
        val highest = HTFluidFuelData(240)

        builder(RagiumDataMapTypes.MAGMATIC_FUEL)
            // lowest
            .add("steam", lowest)
            // low
            // medium
            .add(HTFluidHolderLike.LAVA, medium)
            // high
            .add(RagiumFluidContents.CRIMSON_BLOOD, high)
            .add("blaze_blood", high)
        // highest
    }

    private fun combustionFuels() {
        val lowest = HTFluidFuelData(80)
        val low = HTFluidFuelData(120)
        val medium = HTFluidFuelData(240)
        val high = HTFluidFuelData(360)
        val highest = HTFluidFuelData(480)

        val actually = ModLoadedCondition(RagiumConst.ACTUALLY)

        builder(RagiumDataMapTypes.COMBUSTION_FUEL)
            // lowest
            .add(RagiumFluidContents.CRUDE_OIL, lowest)
            .add("oil", lowest)
            .add(RagiumFluidContents.CREOSOTE, lowest)
            // low
            .add(InitFluids.CANOLA_OIL.get(), low, actually)
            // medium
            .add(RagiumFluidContents.NATURAL_GAS, medium)
            .add("ethanol", medium)
            .add("bioethanol", medium)
            .add("lpg", medium)
            .add(InitFluids.REFINED_CANOLA_OIL.get(), medium, actually)
            // high
            .add(RagiumFluidContents.FUEL, high)
            .add(RagiumFluidContents.BIOFUEL, high)
            .add(RagiumCommonTags.Fluids.DIESEL, high, false)
            .add(RagiumCommonTags.Fluids.BIODIESEL, high, false)
            .add(InitFluids.CRYSTALLIZED_OIL.get(), high, actually)
            // highest
            .add("high_power_biodiesel", highest)
            .add(InitFluids.EMPOWERED_OIL.get(), highest, actually)
    }

    private fun armorEquip() {
        builder(RagiumDataMapTypes.ARMOR_EQUIP)
            .addHolder(RagiumItems.NIGHT_VISION_GOGGLES, HTMobEffectEquipAction(MobEffects.NIGHT_VISION, -1))
    }

    //    Integration    //

    private fun createEnchIndustry() {
        builder(CEIDataMaps.FLUID_UNIT_EXPERIENCE).addHolder(RagiumFluidContents.EXPERIENCE, 20)
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

    private fun <T : Any> Builder<T, Fluid>.add(holder: HTFluidHolderLike, value: T): Builder<T, Fluid> =
        add(holder.getFluidTag(), value, false)

    private fun <T : Any> Builder<T, Fluid>.add(path: String, value: T): Builder<T, Fluid> =
        add(Registries.FLUID.createCommonTag(path), value, false)

    // Entity Type
    private fun <T : Any> Builder<T, EntityType<*>>.add(
        type: EntityType<*>,
        value: T,
        vararg conditions: ICondition,
    ): Builder<T, EntityType<*>> = addHolder(type.toHolderLike(), value, *conditions)
}
