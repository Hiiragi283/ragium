package hiiragi283.ragium.api.util

import hiiragi283.ragium.api.RagiumAPI
import me.desht.pneumaticcraft.common.item.ICustomTooltipName
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

    //    Tooltip    //

    private const val TOOLTIP_PREFIX = "gui.tooltip."
    private const val TOOLTIP_PREFIX_RAGIUM = "${TOOLTIP_PREFIX}${RagiumAPI.MOD_ID}."

    /**
     * @see [ICustomTooltipName.getTranslationKey]
     */
    @JvmStatic
    fun getTooltipKey(stack: ItemStack): String = "${TOOLTIP_PREFIX}${stack.descriptionId}"

    const val TOOLTIP_EFFECT_RANGE = "${TOOLTIP_PREFIX_RAGIUM}effect_range"
    const val TOOLTIP_ENERGY_PERCENTAGE = "${TOOLTIP_PREFIX_RAGIUM}energy_percentage"
    const val TOOLTIP_FLUID_NAME = "${TOOLTIP_PREFIX_RAGIUM}fluid_name"
    const val TOOLTIP_FLUID_NAME_EMPTY = "${TOOLTIP_PREFIX_RAGIUM}fluid_name.empty"
    const val TOOLTIP_INTRINSIC_ENCHANTMENT = "${TOOLTIP_PREFIX_RAGIUM}intrinsic_enchantment"
    const val TOOLTIP_LOOT_TABLE_ID = "${TOOLTIP_PREFIX_RAGIUM}loot_table_id"
    const val TOOLTIP_MISSING_ANCHOR = "${TOOLTIP_PREFIX_RAGIUM}missing_anchor"
    const val TOOLTIP_MISSING_POS = "${TOOLTIP_PREFIX_RAGIUM}missing_pos"
    const val TOOLTIP_SHOW_INFO = "${TOOLTIP_PREFIX_RAGIUM}show_info"
    const val TOOLTIP_WIP = "${TOOLTIP_PREFIX_RAGIUM}work_in_progress"

    //    Jade    //

    const val JADE_OUTPUT_SIDE = "${TOOLTIP_PREFIX_RAGIUM}output_side"
}
