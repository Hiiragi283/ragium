package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.blockTagKey
import hiiragi283.ragium.api.extension.itemTagKey
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object RagiumModTags {
    //    Blocks    //

    object Blocks {
        @JvmField
        val INCORRECT_FOR_DESTRUCTION_TOOL: TagKey<Block> = create("incorrect_for_destruction_tool")

        @JvmField
        val LED_BLOCKS: TagKey<Block> = create("led_blocks")

        @JvmField
        val MINEABLE_WITH_DRILL: TagKey<Block> = create("mineable/drill")

        @JvmField
        val MINEABLE_WITH_HAMMER: TagKey<Block> = create("mineable/hammer")

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

    //    Items    //

    object Items {
        @JvmField
        val ELDRITCH_PEARL_BINDER: TagKey<Item> = create("eldritch_pearl_binder")

        @JvmField
        val IGNORED_IN_RECIPES: TagKey<Item> = create("ignored_in_recipes")

        @JvmField
        val LED_BLOCKS: TagKey<Item> = create("led_blocks")

        @JvmField
        val PLASTICS: TagKey<Item> = create("plastics")

        @JvmField
        val POLYMER_RESIN: TagKey<Item> = create("polymer_resin")

        @JvmField
        val WIP: TagKey<Item> = create("work_in_progress")

        // Flux
        @JvmField
        val ALLOY_SMELTER_FLUXES_BASIC: TagKey<Item> = create("alloy_smelter_fluxes", "basic")

        @JvmField
        val ALLOY_SMELTER_FLUXES_ADVANCED: TagKey<Item> = create("alloy_smelter_fluxes", "advanced")

        // Tools
        @JvmField
        val TOOLS_DRILL: TagKey<Item> = create("tools", "drill")

        @JvmField
        val TOOLS_HAMMER: TagKey<Item> = create("tools", "hammer")

        @JvmStatic
        private fun create(path: String): TagKey<Item> = itemTagKey(RagiumAPI.id(path))

        @JvmStatic
        private fun create(prefix: String, suffix: String): TagKey<Item> = itemTagKey(RagiumAPI.id(prefix, suffix))
    }
}
