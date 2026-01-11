package hiiragi283.ragium.data.server

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.recipe.ingredient.HTIngredientAccess
import hiiragi283.core.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.math.fraction
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.createCommonTag
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumTags
import hiiragi283.ragium.api.data.map.HTFluidCoolantData
import hiiragi283.ragium.api.data.map.HTFluidFertilizerData
import hiiragi283.ragium.api.data.map.HTFluidFuelData
import hiiragi283.ragium.api.data.map.HTMobHead
import hiiragi283.ragium.api.data.map.HTUpgradeData
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.upgrade.HTUpgradeKeys
import hiiragi283.ragium.common.upgrade.RagiumUpgradeKeys
import hiiragi283.ragium.common.upgrade.RagiumUpgradeType
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps

class RagiumDataMapProvider(context: HTDataGenContext) : DataMapProvider(context.output, context.registries) {
    private lateinit var provider: HolderLookup.Provider
    private val itemCreator: HTItemIngredientCreator = HTIngredientAccess.INSTANCE.itemCreator()

    override fun gather(provider: HolderLookup.Provider) {
        this.provider = provider

        furnaceFuels()

        mobHead()

        coolants()
        magmaticFuels()
        combustionFuels()
        fertilizer()

        upgrade()
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
            .add(Tags.Fluids.WATER, HTFluidCoolantData(100), false)
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
            .add(Tags.Fluids.LAVA, medium, false)
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
            .add(Tags.Fluids.WATER, HTFluidFertilizerData(1f), false)
            .add(RagiumFluids.FERTILIZER, HTFluidFertilizerData(1.5f))
    }

    private fun upgrade() {
        val builder: Builder<HTUpgradeData, Item> = builder(RagiumDataMapTypes.UPGRADE)
        // components
        // upgrades
        val processor: HTItemIngredient = itemCreator.fromTagKey(RagiumTags.Items.PROCESSOR_UPGRADABLE)

        for (type: RagiumUpgradeType in RagiumUpgradeType.entries) {
            val upgradeData: HTUpgradeData = when (type) {
                // Creative
                RagiumUpgradeType.CREATIVE -> HTUpgradeData.create {
                    set(HTUpgradeKeys.IS_CREATIVE, 1)
                }
                // Generator
                // Processor
                RagiumUpgradeType.EFFICIENCY -> HTUpgradeData.create {
                    set(HTUpgradeKeys.ENERGY_EFFICIENCY, fraction(5, 4))
                    targetSet(processor)
                }
                RagiumUpgradeType.SPEED -> HTUpgradeData.create {
                    set(HTUpgradeKeys.ENERGY_EFFICIENCY, fraction(4, 5))
                    set(HTUpgradeKeys.SPEED, fraction(5, 4))
                    targetSet(processor)
                }
                RagiumUpgradeType.HIGH_SPEED -> HTUpgradeData.create {
                    set(HTUpgradeKeys.ENERGY_EFFICIENCY, fraction(2, 5))
                    set(HTUpgradeKeys.SPEED, fraction(3, 2))
                    targetSet(processor)
                }
                // Processor
                RagiumUpgradeType.BLASTING -> HTUpgradeData.create {
                    set(RagiumUpgradeKeys.BLASTING, 1)
                    targetSet(itemCreator.fromTagKey(RagiumTags.Items.SMELTING_UPGRADABLE))
                    exclusiveSet(itemCreator.fromTagKey(RagiumTags.Items.SMELTER_EXCLUSIVE))
                }
                RagiumUpgradeType.EFFICIENT_CRUSHING -> HTUpgradeData.create {
                    set(RagiumUpgradeKeys.USE_LUBRICANT, 1)
                    targetSet(itemCreator.fromTagKey(RagiumTags.Items.EFFICIENT_CRUSHING_UPGRADABLE))
                }
                RagiumUpgradeType.EXTRA_VOIDING -> HTUpgradeData.create {
                    set(RagiumUpgradeKeys.VOID_EXTRA, 1)
                    targetSet(itemCreator.fromTagKey(RagiumTags.Items.EXTRA_VOIDING_UPGRADABLE))
                }
                RagiumUpgradeType.SMOKING -> HTUpgradeData.create {
                    set(RagiumUpgradeKeys.SMOKING, 1)
                    targetSet(itemCreator.fromTagKey(RagiumTags.Items.SMELTING_UPGRADABLE))
                    exclusiveSet(itemCreator.fromTagKey(RagiumTags.Items.SMELTER_EXCLUSIVE))
                }
                // Device

                // Storage
                RagiumUpgradeType.ENERGY_CAPACITY -> HTUpgradeData.create {
                    set(HTUpgradeKeys.ENERGY_CAPACITY, 4)
                    targetSet(itemCreator.fromTagKey(RagiumTags.Items.ENERGY_CAPACITY_UPGRADABLE))
                }
                RagiumUpgradeType.FLUID_CAPACITY -> HTUpgradeData.create {
                    set(HTUpgradeKeys.FLUID_CAPACITY, 4)
                    targetSet(itemCreator.fromTagKey(RagiumTags.Items.FLUID_CAPACITY_UPGRADABLE))
                }
                RagiumUpgradeType.ITEM_CAPACITY -> HTUpgradeData.create {
                    set(HTUpgradeKeys.ENERGY_CAPACITY, 4)
                    targetSet(itemCreator.fromTagKey(RagiumTags.Items.ITEM_CAPACITY_UPGRADABLE))
                }
            }
            builder.addHolder(type, upgradeData)
        }
    }

    //    Extensions    //

    private fun <T : Any, R : Any> Builder<T, R>.addHolder(holder: HTIdLike, value: T, vararg conditions: ICondition): Builder<T, R> =
        add(holder.getId(), value, false, *conditions)

    // Item
    private fun <T : Any> Builder<T, Item>.add(prefix: HTPrefixLike, key: HTMaterialLike, value: T): Builder<T, Item> =
        add(prefix.itemTagKey(key), value, false)

    // Fluid
    private fun <T : Any> Builder<T, Fluid>.add(content: HTFluidContent<*, *, *>, value: T): Builder<T, Fluid> =
        add(content.fluidTag, value, false)

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
