package hiiragi283.ragium.api.tag

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.createTagKey
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid

object RagiumTags {
    //    Blocks    //

    object Blocks {
        @JvmStatic
        private fun common(vararg path: String): TagKey<Block> = Registries.BLOCK.createTagKey(HTConst.COMMON.toId(*path))

        @JvmStatic
        private fun mod(vararg path: String): TagKey<Block> = Registries.BLOCK.createTagKey(RagiumAPI.id(*path))
    }

    //    Fluids    //

    object Fluids {
        // Chemical
        @JvmField
        val ALCOHOL: TagKey<Fluid> = common("alcohol")

        // Other
        @JvmField
        val BIODIESEL: TagKey<Fluid> = common("biodiesel")

        @JvmField
        val DIESEL: TagKey<Fluid> = common("diesel")

        @JvmStatic
        private fun common(vararg path: String): TagKey<Fluid> = Registries.FLUID.createTagKey(HTConst.COMMON.toId(*path))

        @JvmStatic
        private fun mod(vararg path: String): TagKey<Fluid> = Registries.FLUID.createTagKey(RagiumAPI.id(*path))
    }

    //    Items    //

    object Items {
        @JvmField
        val FOODS_CAN: TagKey<Item> = common("foods", "can")

        // Upgrade Target
        @JvmField
        val GENERATOR_UPGRADABLE: TagKey<Item> = mod("upgradable", "generator")

        @JvmField
        val PROCESSOR_UPGRADABLE: TagKey<Item> = mod("upgradable", "processor")

        @JvmField
        val MACHINE_UPGRADABLE: TagKey<Item> = mod("upgradable", "machine")

        @JvmField
        val DEVICE_UPGRADABLE: TagKey<Item> = mod("upgradable", "device")

        @JvmField
        val EXTRA_VOIDING_UPGRADABLE: TagKey<Item> = mod("upgradable", "extra_voiding")

        @JvmField
        val EFFICIENT_CRUSHING_UPGRADABLE: TagKey<Item> = mod("upgradable", "efficient_crushing")

        @JvmField
        val ENERGY_CAPACITY_UPGRADABLE: TagKey<Item> = mod("upgradable", "energy_capacity")

        @JvmField
        val FLUID_CAPACITY_UPGRADABLE: TagKey<Item> = mod("upgradable", "fluid_capacity")

        @JvmField
        val ITEM_CAPACITY_UPGRADABLE: TagKey<Item> = mod("upgradable", "item_capacity")

        @JvmField
        val SMELTING_UPGRADABLE: TagKey<Item> = mod("upgradable", "smelting")

        // Upgrade Exclusive
        @JvmField
        val EXTRACTOR_EXCLUSIVE: TagKey<Item> = mod("exclusive", "extractor")

        @JvmField
        val SMELTER_EXCLUSIVE: TagKey<Item> = mod("exclusive", "smelter")

        @JvmStatic
        private fun common(vararg path: String): TagKey<Item> = Registries.ITEM.createTagKey(HTConst.COMMON.toId(*path))

        @JvmStatic
        private fun mod(vararg path: String): TagKey<Item> = Registries.ITEM.createTagKey(RagiumAPI.id(*path))
    }
}
