package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.blockTagKey
import hiiragi283.ragium.api.extension.fluidTagKey
import hiiragi283.ragium.api.extension.itemTagKey
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid

object RagiumModTags {
    //    Blocks    //

    object Blocks {
        @JvmField
        val LED_BLOCKS: TagKey<Block> = create("led_blocks")

        @JvmField
        val RESONANT_DEBRIS_REPLACEABLES: TagKey<Block> = create("deepslate_ore_replaceables")

        @JvmField
        val WIP: TagKey<Block> = create("work_in_progress")

        @JvmStatic
        private fun create(path: String): TagKey<Block> = blockTagKey(RagiumAPI.id(path))
    }

    //    EntityTypes    //

    object EntityTypes {
        @JvmField
        val CAPTURE_BLACKLIST: TagKey<EntityType<*>> = create("capture_blacklist")

        @JvmField
        val GENERATE_RESONANT_DEBRIS: TagKey<EntityType<*>> = create("generate_resonant_debris")

        @JvmField
        val SENSITIVE_TO_NOISE_CANCELLING: TagKey<EntityType<*>> = create("sensitive_to_noise_cancelling")

        @JvmStatic
        private fun create(path: String): TagKey<EntityType<*>> = TagKey.create(Registries.ENTITY_TYPE, RagiumAPI.id(path))
    }

    //    Fluids    //

    object Fluids {
        @JvmField
        val FUELS_NITRO: TagKey<Fluid> = fluidTagKey(RagiumAPI.id("fuels/nitro"))

        @JvmField
        val FUELS_NON_NITRO: TagKey<Fluid> = fluidTagKey(RagiumAPI.id("fuels/non_nitro"))

        @JvmField
        val FUELS_THERMAL: TagKey<Fluid> = fluidTagKey(RagiumAPI.id("fuels/thermal"))
    }

    //    Items    //

    object Items {
        @JvmField
        val ELDRITCH_PEARL_BINDER: TagKey<Item> = create("eldritch_pearl_binder")

        @JvmField
        val LED_BLOCKS: TagKey<Item> = create("led_blocks")

        @JvmField
        val POLYMER_RESIN: TagKey<Item> = create("polymer_resin")

        @JvmField
        val WIP: TagKey<Item> = create("work_in_progress")

        // ENI Upgrades
        @JvmField
        val ENI_UPGRADES: TagKey<Item> = create("eni_upgrades")

        @JvmField
        val ENI_UPGRADES_BASIC: TagKey<Item> = create("eni_upgrades/basic")

        @JvmField
        val ENI_UPGRADES_ADVANCED: TagKey<Item> = create("eni_upgrades/advanced")

        @JvmField
        val ENI_UPGRADES_ELITE: TagKey<Item> = create("eni_upgrades/elite")

        @JvmField
        val ENI_UPGRADES_ULTIMATE: TagKey<Item> = create("eni_upgrades/ultimate")

        // Flux
        @JvmField
        val ALLOY_SMELTER_FLUXES_BASIC: TagKey<Item> = create("alloy_smelter_fluxes/basic")

        @JvmField
        val ALLOY_SMELTER_FLUXES_ADVANCED: TagKey<Item> = create("alloy_smelter_fluxes/advanced")

        @JvmStatic
        private fun create(path: String): TagKey<Item> = itemTagKey(RagiumAPI.id(path))
    }
}
