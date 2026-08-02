package hiiragi283.ragium.data

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.map.HTDataMapProvider
import hiiragi283.core.api.registry.HTSimpleDeferredItem
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.createTagKey
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.setup.RagiumFluids
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags

class RagiumDataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : HTDataMapProvider(packOutput, lookupProvider) {
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
            .add(EntityType.SKELETON.toLike(), HTSimpleDeferredItem(vanillaId("skeleton_skull")))
            .add(EntityType.WITHER_SKELETON.toLike(), HTSimpleDeferredItem(vanillaId("wither_skeleton_skull")))
            .add(EntityType.ZOMBIE.toLike(), HTSimpleDeferredItem(vanillaId("zombie_head")))
            .add(EntityType.CREEPER.toLike(), HTSimpleDeferredItem(vanillaId("creeper_head")))
            .add(EntityType.ENDER_DRAGON.toLike(), HTSimpleDeferredItem(vanillaId("dragon_head")))
            .add(EntityType.PIGLIN.toLike(), HTSimpleDeferredItem(vanillaId("piglin_head")))
    }

    // Fluid
    private fun coolants() {
        builder(RagiumDataMapTypes.COOLANT)
            .add(Tags.Fluids.WATER, 100, false)
            .addTag(RagiumFluids.LIQUID_NITROGEN, 5)
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
            .addTag(RagiumFluids.CREOSOTE, lowest)
            // low
            .add("oil", low)
            .addTag(RagiumFluids.CRUDE_OIL, low)
            .addTag(RagiumFluids.SYNTHETIC_OIL, low)
            .add(RagiumTags.Fluids.ALCOHOL, medium, false)
            // medium
            .add("lpg", medium)
            .add("ethene", medium)
            .addTag(RagiumFluids.METHANE, medium)
            // high
            .addTag(RagiumFluids.FUEL, high)
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
            .add(Items.CAKE.toLike(), 64)
            .add(Items.CHORUS_FLOWER.toLike(), 128)
            .add(Items.DRAGON_BREATH.toLike(), 128)
            .add(Items.ENCHANTED_GOLDEN_APPLE.toLike(), 256)
            .add(Items.GHAST_TEAR.toLike(), 64)
            .add(Items.HEART_OF_THE_SEA.toLike(), 128)
            .add(Items.HEAVY_CORE.toLike(), 512)
            .add(Items.HONEYCOMB.toLike(), 16)
            .add(Items.MAGMA_CREAM.toLike(), 16)
            .add(Items.NAUTILUS_SHELL.toLike(), 32)
            .add(Items.PHANTOM_MEMBRANE.toLike(), 64)
            .add(Items.REINFORCED_DEEPSLATE.toLike(), 128)
            .add(Items.SCULK_CATALYST.toLike(), 32)
            .add(Items.SCULK_SENSOR.toLike(), 16)
            .add(Items.SCULK_SHRIEKER.toLike(), 128)
            .add(Items.SHULKER_SHELL.toLike(), 64)
            .add(Items.SPONGE.toLike(), 64)
            .add(Items.TOTEM_OF_UNDYING.toLike(), 256)
    }

    //    Extensions    //

    private fun <T : Any> Builder<T, Fluid>.add(path: String, value: T): Builder<T, Fluid> = add(Registries.FLUID.createTagKey(HTConst.COMMON.toId(path)), value, false)
}
