package hiiragi283.ragium.data

import hiiragi283.core.api.data.map.HTDataMapProvider
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.createCommonTag
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.conditions.ICondition
import java.util.concurrent.CompletableFuture

class RagiumDataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    HTDataMapProvider(packOutput, lookupProvider) {
    override fun gatherInternal() {
        mobHeads()

        coolants()
        magmaticFuels()
        combustionFuels()

        matterPoint()
    }

    //    Vanilla    //

    //    Ragium    //

    // Block

    // Entity Type
    private fun mobHeads() {
        builder(RagiumDataMapTypes.MOB_HEAD)
            .add(EntityType.SKELETON, Items.SKELETON_SKULL.toLike())
            .add(EntityType.WITHER_SKELETON, Items.WITHER_SKELETON_SKULL.toLike())
            .add(EntityType.ZOMBIE, Items.ZOMBIE_HEAD.toLike())
            .add(EntityType.CREEPER, Items.CREEPER_HEAD.toLike())
            .add(EntityType.ENDER_DRAGON, Items.DRAGON_HEAD.toLike())
            .add(EntityType.PIGLIN, Items.PIGLIN_HEAD.toLike())
    }

    // Fluid
    private fun coolants() {
        builder(RagiumDataMapTypes.COOLANT)
            .add(Tags.Fluids.WATER, 100, false)
            .add(RagiumFluids.LIQUID_NITROGEN, 5)
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
            .add("lpg", medium)
            .add("ethene", medium)
            .add(RagiumFluids.METHANE, medium)
            // high
            .add(RagiumFluids.FUEL, high)
            .add(RagiumTags.Fluids.BIODIESEL, high, false)
            .add(RagiumTags.Fluids.DIESEL, high, false)
            // highest
            .add("high_power_biodiesel", highest)
    }

    // Item
    private fun matterPoint() {
        builder(RagiumDataMapTypes.MATTER_POINT)
            .add(CommonTagPrefixes.DUST, VanillaMaterialKeys.GLOWSTONE, 64)
            .add(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE, 16)
            .add(CommonTagPrefixes.GEM, VanillaMaterialKeys.AMETHYST, 16)
            .add(CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND, 128)
            .add(CommonTagPrefixes.GEM, VanillaMaterialKeys.ECHO, 128)
            .add(CommonTagPrefixes.GEM, VanillaMaterialKeys.ENDER, 128)
            .add(CommonTagPrefixes.GEM, VanillaMaterialKeys.LAPIS, 16)
            .add(CommonTagPrefixes.GEM, VanillaMaterialKeys.PRISMARINE, 32)
            .add(CommonTagPrefixes.GEM, VanillaMaterialKeys.QUARTZ, 32)
            .add(CommonTagPrefixes.INGOT, VanillaMaterialKeys.COPPER, 8)
            .add(CommonTagPrefixes.INGOT, VanillaMaterialKeys.GOLD, 64)
            .add(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON, 16)
            .add(CommonTagPrefixes.INGOT, VanillaMaterialKeys.NETHERITE, 256)
            .add(CommonTagPrefixes.ROD, VanillaMaterialKeys.BLAZE, 64)
            .add(CommonTagPrefixes.ROD, VanillaMaterialKeys.BREEZE, 64)
            .add(ItemTags.DIRT, 1, false)
            .add(ItemTags.LOGS, 4, false)
            .add(ItemTags.PLANKS, 1, false)
            .add(ItemTags.WOOL, 4, false)
            .add(Tags.Items.BONES, 4, false)
            .add(Tags.Items.BRICKS, 2, false)
            .add(Tags.Items.COBBLESTONES, 1, false)
            .add(Tags.Items.END_STONES, 4, false)
            .add(Tags.Items.GLASS_BLOCKS, 2, false)
            .add(Tags.Items.GRAVELS, 1, false)
            .add(Tags.Items.LEATHERS, 4, false)
            .add(Tags.Items.NETHER_STARS, 512, false)
            .add(Tags.Items.NETHERRACKS, 1, false)
            .add(Tags.Items.OBSIDIANS_CRYING, 64, false)
            .add(Tags.Items.OBSIDIANS_NORMAL, 32, false)
            .add(Tags.Items.SANDS, 1, false)
            .add(Tags.Items.SANDSTONE_BLOCKS, 4, false)
            .add(Tags.Items.SLIME_BALLS, 8, false)
            .add(Tags.Items.STONES, 1, false)
            .add(Tags.Items.STRINGS, 1, false)
            .addItem(Items.CAKE, 64)
            .addItem(Items.CHORUS_FLOWER, 128)
            .addItem(Items.DRAGON_BREATH, 128)
            .addItem(Items.ENCHANTED_GOLDEN_APPLE, 256)
            .addItem(Items.GHAST_TEAR, 64)
            .addItem(Items.HEART_OF_THE_SEA, 128)
            .addItem(Items.HEAVY_CORE, 512)
            .addItem(Items.HONEYCOMB, 16)
            .addItem(Items.MAGMA_CREAM, 16)
            .addItem(Items.NAUTILUS_SHELL, 32)
            .addItem(Items.PHANTOM_MEMBRANE, 64)
            .addItem(Items.REINFORCED_DEEPSLATE, 128)
            .addItem(Items.SCULK_CATALYST, 32)
            .addItem(Items.SCULK_SENSOR, 16)
            .addItem(Items.SCULK_SHRIEKER, 128)
            .addItem(Items.SHULKER_SHELL, 64)
            .addItem(Items.SPONGE, 64)
            .addItem(Items.TOTEM_OF_UNDYING, 256)
    }

    //    Extensions    //

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
