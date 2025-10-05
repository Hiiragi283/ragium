package hiiragi283.ragium.data.server

import de.ellpeck.actuallyadditions.mod.fluids.InitFluids
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.data.map.HTBrewingEffect
import hiiragi283.ragium.api.data.map.HTFluidFuelData
import hiiragi283.ragium.api.data.map.HTSolarPower
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.extension.createCommonTag
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.common.material.HTBlockMaterialVariant
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
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
        solarPower()

        brewingEffect()
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
        builder(RagiumDataMaps.INSTANCE.thermalFuelType)
            .add("steam", 100)
            .add(HTFluidContent.LAVA, 10)
            .add("blaze_blood", 5)
    }

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

    private fun nuclearFuels() {
        builder(RagiumDataMaps.INSTANCE.nuclearFuelType)
            .add(RagiumFluidContents.GREEN_FUEL, 5)
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

    //    Brewing    //

    private fun brewingEffect() {
        // Potions
        val builder: Builder<HTBrewingEffect, Item> = builder(RagiumDataMaps.INSTANCE.brewingEffectType)
            .add(Items.GOLDEN_CARROT, HTBrewingEffect(Potions.NIGHT_VISION))
            .add(Tags.Items.GEMS_AMETHYST, HTBrewingEffect(Potions.INVISIBILITY), false)
            .add(Items.RABBIT_FOOT, HTBrewingEffect(Potions.LEAPING))
            .add(Items.MAGMA_CREAM, HTBrewingEffect(Potions.FIRE_RESISTANCE))
            .add(Items.SUGAR, HTBrewingEffect(Potions.SWIFTNESS))
            .add(Tags.Items.INGOTS_IRON, HTBrewingEffect(Potions.SLOWNESS), false)
            .add(Items.TURTLE_SCUTE, HTBrewingEffect(Potions.TURTLE_MASTER))
            .add(Items.PUFFERFISH, HTBrewingEffect(Potions.WATER_BREATHING))
            .add(Items.GLISTERING_MELON_SLICE, HTBrewingEffect(Potions.HEALING))
            .add(Items.FERMENTED_SPIDER_EYE, HTBrewingEffect(Potions.HARMING))
            .add(Items.SPIDER_EYE, HTBrewingEffect(Potions.POISON))
            .add(Items.GHAST_TEAR, HTBrewingEffect(Potions.REGENERATION))
            .add(Items.BLAZE_POWDER, HTBrewingEffect(Potions.STRENGTH))
            .add(Items.BONE, HTBrewingEffect(Potions.WEAKNESS))
            .add(Tags.Items.GEMS_EMERALD, HTBrewingEffect(Potions.LUCK), false)
            .add(Items.PHANTOM_MEMBRANE, HTBrewingEffect(Potions.SLOWNESS))
            .add(Items.WIND_CHARGE, HTBrewingEffect(Potions.WIND_CHARGED))
            .add(Items.COBWEB, HTBrewingEffect(Potions.WEAVING))
            .add(Tags.Items.STORAGE_BLOCKS_SLIME, HTBrewingEffect(Potions.OOZING), false)
            .add(Items.STONE, HTBrewingEffect(Potions.INFESTED))
        // Custom - Vanilla
        builder
            .add(
                Items.GOLDEN_PICKAXE,
                HTBrewingEffect { addEffect(MobEffects.DIG_SPEED, 3 * 60 * 20, 0) },
            )

        mapOf(
            Items.POISONOUS_POTATO to MobEffects.CONFUSION,
            Items.ROTTEN_FLESH to MobEffects.HUNGER,
            Items.WITHER_ROSE to MobEffects.WITHER,
            Items.SHULKER_SHELL to MobEffects.LEVITATION,
        ).forEach { (item: Item, effect: Holder<MobEffect>) ->
            builder.add(item, HTBrewingEffect { addEffect(effect, 45 * 20, 0) })
        }
        // Custom - Ragium
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
        add(Registries.FLUID.createCommonTag(path), HTFluidFuelData(amount), false)
}
