package hiiragi283.ragium.common.datagen.map

import hiiragi283.core.api.data.map.HTDataMapProvider
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.tag.createCommonTag
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags

/**
 * @see hiiragi283.core.common.datagen.map.HCDataMapProviders
 */
data object RagiumDataMapProviders {
    data object MobHead : HTDataMapProvider<HTItemHolderLike<*>, EntityType<*>>(RagiumDataMapTypes.MOB_HEAD) {
        override fun gather() {
            add(EntityType.SKELETON, Items.SKELETON_SKULL)
            add(EntityType.WITHER_SKELETON, Items.WITHER_SKELETON_SKULL)
            add(EntityType.ZOMBIE, Items.ZOMBIE_HEAD)
            add(EntityType.CREEPER, Items.CREEPER_HEAD)
            add(EntityType.ENDER_DRAGON, Items.DRAGON_HEAD)
            add(EntityType.PIGLIN, Items.PIGLIN_HEAD)
        }

        @Suppress("DEPRECATION")
        private fun add(type: EntityType<*>, item: ItemLike) {
            this.add(type.builtInRegistryHolder().toLike(), HTItemHolderLike.of(item))
        }
    }

    data object Coolant : HTDataMapProvider<Int, Fluid>(RagiumDataMapTypes.COOLANT) {
        override fun gather() {
            add(Tags.Fluids.WATER, 100)
            add(RagiumFluids.COOLANT, 25)
        }
    }

    data object MagmaticFuel : HTDataMapProvider<Int, Fluid>(RagiumDataMapTypes.MAGMATIC_FUEL) {
        override fun gather() {
            val lowest = 40
            val low = 60
            val medium = 120
            val high = 180
            val highest = 240

            // lowest
            add(Registries.FLUID.createCommonTag("steam"), lowest)
            // low
            // medium
            add(Tags.Fluids.LAVA, medium)
            // high
            add(HCFluids.MOLTEN_CRIMSON_CRYSTAL, high)
            add(Registries.FLUID.createCommonTag("blaze_blood"), high)
        }
    }

    data object CombustionFuel : HTDataMapProvider<Int, Fluid>(RagiumDataMapTypes.COMBUSTION_FUEL) {
        override fun gather() {
            val lowest = 80
            val low = 120
            val medium = 240
            val high = 360
            val highest = 480

            // lowest
            add(RagiumFluids.CREOSOTE, lowest)
            // low
            add(tag("oil"), low)
            add(RagiumFluids.CRUDE_OIL, low)
            add(RagiumFluids.SYNTHETIC_OIL, low)
            add(RagiumTags.Fluids.ALCOHOL, medium, false)
            // medium
            add(tag("bioethanol"), medium)
            add(tag("lpg"), medium)
            add(RagiumFluids.ETHYLENE, medium)
            add(RagiumFluids.METHANE, medium)
            add(RagiumFluids.SYNTHETIC_GAS, medium)
            // high
            add(RagiumFluids.FUEL, high)
            add(RagiumTags.Fluids.BIODIESEL, high, false)
            add(RagiumTags.Fluids.DIESEL, high, false)
            // highest
            add(tag("high_power_biodiesel"), highest)
        }

        @JvmStatic
        private fun tag(path: String): TagKey<Fluid> = Registries.FLUID.createCommonTag(path)
    }
}
