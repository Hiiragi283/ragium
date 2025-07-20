package hiiragi283.ragium.api.util

import net.minecraft.world.item.ItemStack

object RagiumTranslationKeys {
    //    Item    //

    const val AZURE_STEEL_UPGRADE = "upgrade.azure_steel_upgrade"
    const val AZURE_STEEL_UPGRADE_APPLIES_TO = "items.smithing_template.azure_steel_upgrade.applies_to"
    const val AZURE_STEEL_UPGRADE_INGREDIENTS = "items.smithing_template.azure_steel_upgrade.ingredients"
    const val AZURE_STEEL_UPGRADE_BASE_SLOT_DESCRIPTION = "items.smithing_template.azure_steel_upgrade.base_slot_description"
    const val AZURE_STEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = "items.smithing_template.azure_steel_upgrade.additions_slot_description"

    const val DEEP_STEEL_UPGRADE = "upgrade.deep_steel_upgrade"
    const val DEEP_STEEL_UPGRADE_APPLIES_TO = "items.smithing_template.deep_steel_upgrade.applies_to"
    const val DEEP_STEEL_UPGRADE_INGREDIENTS = "items.smithing_template.deep_steel_upgrade.ingredients"
    const val DEEP_STEEL_UPGRADE_BASE_SLOT_DESCRIPTION = "items.smithing_template.deep_steel_upgrade.base_slot_description"
    const val DEEP_STEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = "items.smithing_template.deep_steel_upgrade.additions_slot_description"

    @JvmStatic
    fun getTooltipKey(stack: ItemStack): String = "text.ragium.${stack.descriptionId}"

    //    Text    //

    const val TEXT_ENERGY_PERCENTAGE = "text.ragium.energy_percentage"
    const val TEXT_FLUID_NAME = "text.ragium.fluid_name"
    const val TEXT_FLUID_NAME_EMPTY = "text.ragium.fluid_name.empty"

    const val TEXT_EFFECT_RANGE = "text.ragium.effect_range"

    const val TEXT_LOOT_TABLE_ID = "text.ragium.loot_table_id"

    const val TEXT_MISSING_ANCHOR = "text.ragium.missing_anchor"
    const val TEXT_MISSING_POS = "text.ragium.missing_pos"

    const val TEXT_SHOW_INFO = "text.ragium.show_info"

    const val TEXT_WIP = "text.ragium.work_in_progress"

    //    Jade    //

    const val JADE_OUTPUT_SIDE = "text.ragium.output_side"
}
