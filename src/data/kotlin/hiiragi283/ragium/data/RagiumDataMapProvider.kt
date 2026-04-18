package hiiragi283.ragium.data

import hiiragi283.core.api.data.map.HTDataMapProvider
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.tag.createCommonTag
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.conditions.ICondition
import java.util.concurrent.CompletableFuture

class RagiumDataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    HTDataMapProvider(packOutput, lookupProvider) {
    override fun gatherInternal() {
        fermentSources()
        mobHeads()

        coolants()
        magmaticFuels()
        combustionFuels()
    }

    //    Vanilla    //

    //    Ragium    //

    // Block
    private fun fermentSources() {
        builder(RagiumDataMapTypes.FERMENT_SOURCE)
            .addHolder(Blocks.BROWN_MUSHROOM.toLike(), 1)
            .addHolder(Blocks.MYCELIUM.toLike(), 1)
            .addHolder(Blocks.RED_MUSHROOM.toLike(), 1)
    }

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
