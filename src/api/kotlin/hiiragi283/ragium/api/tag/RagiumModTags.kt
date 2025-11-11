package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageType
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
        private fun create(path: String): TagKey<Block> = Registries.BLOCK.createTagKey(RagiumAPI.id(path))
    }

    //    DamageTypes    //

    object DamageTypes {
        @JvmField
        val IS_SONIC: TagKey<DamageType> = create("is_sonic")

        @JvmStatic
        private fun create(path: String): TagKey<DamageType> = Registries.DAMAGE_TYPE.createTagKey(RagiumAPI.id(path))
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
        private fun create(path: String): TagKey<EntityType<*>> = Registries.ENTITY_TYPE.createTagKey(RagiumAPI.id(path))
    }

    //    Fluids    //

    //    Items    //

    object Items {
        @JvmField
        val BUDDING_AZURE_ACTIVATOR: TagKey<Item> = create("budding_azure_activator")

        @JvmField
        val BYPASS_MENU_VALIDATION: TagKey<Item> = create("bypass_menu_validation")

        @JvmField
        val ELDRITCH_PEARL_BINDER: TagKey<Item> = create("eldritch_pearl_binder")

        @JvmField
        val IGNORED_IN_RECIPES: TagKey<Item> = create("ignored_in_recipes")

        @JvmField
        val IS_NUCLEAR_FUEL: TagKey<Item> = create("is_nuclear_fuel")

        @JvmField
        val LED_BLOCKS: TagKey<Item> = create("led_blocks")

        @JvmField
        val PLASTICS: TagKey<Item> = create("plastics")

        @JvmField
        val POLYMER_RESIN: TagKey<Item> = create("polymer_resin")

        @JvmField
        val RAW_MEAT: TagKey<Item> = create("raw_meat")

        @JvmField
        val WIP: TagKey<Item> = create("work_in_progress")

        // Enchantments
        @JvmField
        val CAPACITY_ENCHANTABLE: TagKey<Item> = create("enchantable/capacity")

        @JvmField
        val RANGE_ENCHANTABLE: TagKey<Item> = create("enchantable/range")

        @JvmField
        val STRIKE_ENCHANTABLE: TagKey<Item> = create("enchantable/strike")

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
        private fun create(path: String): TagKey<Item> = Registries.ITEM.createTagKey(RagiumAPI.id(path))

        @JvmStatic
        private fun create(prefix: String, suffix: String): TagKey<Item> = create("$prefix/$suffix")
    }
}
