package hiiragi283.ragium.data.server

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.registry.HTFluidWithTag
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.createCommonTag
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.data.map.HTFluidCoolantData
import hiiragi283.ragium.api.data.map.HTFluidFertilizerData
import hiiragi283.ragium.api.data.map.HTFluidFuelData
import hiiragi283.ragium.api.data.map.HTMobHead
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps

class RagiumDataMapProvider(context: HTDataGenContext) : DataMapProvider(context.output, context.registries) {
    private lateinit var provider: HolderLookup.Provider

    override fun gather(provider: HolderLookup.Provider) {
        this.provider = provider

        furnaceFuels()

        mobHead()

        coolants()
        magmaticFuels()
        combustionFuels()
        fertilizer()
    }

    //    Vanilla    //

    private fun furnaceFuels() {
        builder(NeoForgeDataMaps.FURNACE_FUELS)
            .add(RagiumItems.TAR, FurnaceFuel(20 * 10 * 4), false)
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
    }

    private fun coolants() {
        builder(RagiumDataMapTypes.COOLANT)
            .add(HTFluidWithTag.WATER, HTFluidCoolantData(100))
            .add(RagiumFluids.COOLANT, HTFluidCoolantData(25))
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
            .add(HTFluidWithTag.LAVA, medium)
            // high
            .add(HCFluids.MOLTEN_CRIMSON_CRYSTAL, high)
            .add("blaze_blood", high)
        // highest
    }

    private fun combustionFuels() {
        val lowest = HTFluidFuelData(80)
        val low = HTFluidFuelData(120)
        val medium = HTFluidFuelData(240)
        val high = HTFluidFuelData(360)
        val highest = HTFluidFuelData(480)

        builder(RagiumDataMapTypes.COMBUSTION_FUEL)
            // lowest
            .add(RagiumFluids.CREOSOTE, lowest)
            // low
            .add(RagiumFluids.CRUDE_OIL, low)
            .add("oil", low)
            .add(RagiumFluids.CRUDE_BIO, low)
            // medium
            .add(RagiumFluids.ETHANOL, medium)
            .add("bioethanol", medium)
            .add("lpg", medium)
            // high
            .add(RagiumFluids.FUEL, high)
            .add(RagiumFluids.BIOFUEL, high)
            .add("diesel", high)
            .add("biodiesel", high)
            // highest
            .add("high_power_biodiesel", highest)
    }

    private fun fertilizer() {
        builder(RagiumDataMapTypes.FERTILIZER)
            .add(HTFluidWithTag.WATER, HTFluidFertilizerData(1f))
            .add(RagiumFluids.FERTILIZER, HTFluidFertilizerData(1.5f))
    }

    //    Extensions    //

    private fun <T : Any, R : Any> Builder<T, R>.addHolder(holder: HTIdLike, value: T, vararg conditions: ICondition): Builder<T, R> =
        add(holder.getId(), value, false, *conditions)

    // Item
    private fun <T : Any> Builder<T, Item>.add(prefix: HTPrefixLike, key: HTMaterialLike, value: T): Builder<T, Item> =
        add(prefix.itemTagKey(key), value, false)

    // Fluid
    private fun <T : Any> Builder<T, Fluid>.add(holder: HTFluidWithTag<*>, value: T): Builder<T, Fluid> =
        add(holder.getFluidTag(), value, false)

    private fun <T : Any> Builder<T, Fluid>.add(path: String, value: T): Builder<T, Fluid> =
        add(Registries.FLUID.createCommonTag(path), value, false)

    // Entity Type
    @Suppress("DEPRECATION")
    private fun <T : Any> Builder<T, EntityType<*>>.add(
        type: EntityType<*>,
        value: T,
        vararg conditions: ICondition,
    ): Builder<T, EntityType<*>> = addHolder(type.builtInRegistryHolder().toLike(), value, *conditions)
}
