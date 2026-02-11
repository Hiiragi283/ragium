package hiiragi283.ragium.data.server

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.createCommonTag
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.data.map.HTUpgradeData
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.api.upgrade.HTUpgradeKeys
import hiiragi283.ragium.common.upgrade.RagiumUpgradeKeys
import hiiragi283.ragium.common.upgrade.RagiumUpgradeType
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.data.DataMapProvider

class RagiumDataMapProvider(context: HTDataGenContext) : DataMapProvider(context.output, context.registries) {
    private lateinit var provider: HolderLookup.Provider
    private val inputCreator: HTIngredientCreator = HTIngredientCreator

    override fun gather(provider: HolderLookup.Provider) {
        this.provider = provider

        fermentSources()
        mobHeads()

        coolants()
        magmaticFuels()
        combustionFuels()

        explosive()
        upgrade()
    }

    //    Vanilla    //

    //    Ragium    //

    private fun fermentSources() {
        builder(RagiumDataMapTypes.FERMENT_SOURCE)
            .addHolder(HTBlockHolderLike.of(Blocks.BROWN_MUSHROOM), 1)
            .addHolder(HTBlockHolderLike.of(Blocks.MYCELIUM), 1)
            .addHolder(HTBlockHolderLike.of(Blocks.RED_MUSHROOM), 1)
    }

    private fun mobHeads() {
        builder(RagiumDataMapTypes.MOB_HEAD)
            .add(EntityType.SKELETON, HTItemHolderLike.of(Items.SKELETON_SKULL))
            .add(EntityType.WITHER_SKELETON, HTItemHolderLike.of(Items.WITHER_SKELETON_SKULL))
            .add(EntityType.ZOMBIE, HTItemHolderLike.of(Items.ZOMBIE_HEAD))
            .add(EntityType.CREEPER, HTItemHolderLike.of(Items.CREEPER_HEAD))
            .add(EntityType.ENDER_DRAGON, HTItemHolderLike.of(Items.DRAGON_HEAD))
            .add(EntityType.PIGLIN, HTItemHolderLike.of(Items.PIGLIN_HEAD))
    }

    private fun coolants() {
        builder(RagiumDataMapTypes.COOLANT)
            .add(Tags.Fluids.WATER, 100, false)
            .add(RagiumFluids.COOLANT, 25)
    }

    private fun magmaticFuels() {
        val lowest = 40
        val low = 60
        val medium = 120
        val high = 180
        val highest = 240

        builder(RagiumDataMapTypes.MAGMATIC_FUEL)
            // lowest
            .add("steam", lowest)
            // low
            // medium
            .add(Tags.Fluids.LAVA, medium, false)
            // high
            .add("blaze_blood", high)
        // highest
    }

    private fun combustionFuels() {
        val lowest = 80
        val low = 120
        val medium = 240
        val high = 360
        val highest = 480

        builder(RagiumDataMapTypes.COMBUSTION_FUEL)
            // lowest
            .add(RagiumFluids.CREOSOTE, lowest)
            // low
            .add("oil", low)
            .add(RagiumFluids.CRUDE_OIL, low)
            .add(RagiumFluids.SYNTHETIC_OIL, low)
            .add(RagiumTags.Fluids.ALCOHOL, medium, false)
            // medium
            .add("bioethanol", medium)
            .add("lpg", medium)
            .add(RagiumFluids.ETHYLENE, medium)
            .add(RagiumFluids.METHANE, medium)
            .add(RagiumFluids.SYNTHETIC_GAS, medium)
            // high
            .add(RagiumFluids.FUEL, high)
            .add(RagiumTags.Fluids.BIODIESEL, high, false)
            .add(RagiumTags.Fluids.DIESEL, high, false)
            // highest
            .add("high_power_biodiesel", highest)
    }

    private fun explosive() {
        builder(RagiumDataMapTypes.EXPLOSIVE)
            .addHolder(HTItemHolderLike.of(Items.FIREWORK_ROCKET), 1)
            .addHolder(HTItemHolderLike.of(Items.TNT), 4)
            .addHolder(HTItemHolderLike.of(Items.END_CRYSTAL), 8)
        // Dynamite - 4
        // Industrial TNT - 8
    }

    private fun upgrade() {
        val builder: Builder<HTUpgradeData, Item> = builder(RagiumDataMapTypes.UPGRADE)
        // components
        // upgrades
        val processor: HTItemIngredient = inputCreator.create(RagiumTags.Items.PROCESSOR_UPGRADABLE)

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
                    targetSet(inputCreator.create(RagiumTags.Items.SMELTING_UPGRADABLE))
                    exclusiveSet(inputCreator.create(RagiumTags.Items.SMELTER_EXCLUSIVE))
                }
                RagiumUpgradeType.EFFICIENT_CRUSHING -> HTUpgradeData.create {
                    set(RagiumUpgradeKeys.USE_LUBRICANT, 1)
                    targetSet(inputCreator.create(RagiumTags.Items.EFFICIENT_CRUSHING_UPGRADABLE))
                }
                RagiumUpgradeType.EXTRA_VOIDING -> HTUpgradeData.create {
                    set(RagiumUpgradeKeys.VOID_EXTRA, 1)
                    targetSet(inputCreator.create(RagiumTags.Items.EXTRA_VOIDING_UPGRADABLE))
                }
                RagiumUpgradeType.SMOKING -> HTUpgradeData.create {
                    set(RagiumUpgradeKeys.SMOKING, 1)
                    targetSet(inputCreator.create(RagiumTags.Items.SMELTING_UPGRADABLE))
                    exclusiveSet(inputCreator.create(RagiumTags.Items.SMELTER_EXCLUSIVE))
                }
                // Device

                // Storage
                RagiumUpgradeType.ENERGY_CAPACITY -> HTUpgradeData.create {
                    set(HTUpgradeKeys.ENERGY_CAPACITY, 4)
                    targetSet(inputCreator.create(RagiumTags.Items.ENERGY_CAPACITY_UPGRADABLE))
                }
                RagiumUpgradeType.FLUID_CAPACITY -> HTUpgradeData.create {
                    set(HTUpgradeKeys.FLUID_CAPACITY, 4)
                    targetSet(inputCreator.create(RagiumTags.Items.FLUID_CAPACITY_UPGRADABLE))
                }
                RagiumUpgradeType.ITEM_CAPACITY -> HTUpgradeData.create {
                    set(HTUpgradeKeys.ENERGY_CAPACITY, 4)
                    targetSet(inputCreator.create(RagiumTags.Items.ITEM_CAPACITY_UPGRADABLE))
                }
            }
            builder.addHolder(type, upgradeData)
        }
    }

    //    Extensions    //

    private fun <T : Any, R : Any> Builder<T, R>.addHolder(holder: HTIdLike, value: T, vararg conditions: ICondition): Builder<T, R> =
        add(holder.getId(), value, false, *conditions)

    // Item
    private fun <T : Any> Builder<T, Item>.add(prefix: HTTagPrefix, key: HTMaterialLike, value: T): Builder<T, Item> =
        add(prefix.itemTagKey(key), value, false)

    // Fluid
    private fun <T : Any> Builder<T, Fluid>.add(content: HTFluidContent, value: T): Builder<T, Fluid> = add(content.fluidTag, value, false)

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
