package hiiragi283.ragium.api.text

import hiiragi283.ragium.api.RagiumAPI
import me.desht.pneumaticcraft.common.item.ICustomTooltipName
import net.minecraft.ChatFormatting
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.util.ItemStackMap

/**
 * @see [mekanism.api.text.APILang]
 */
enum class RagiumTranslation(type: String, vararg path: String) : HTTranslation {
    // Azure Upgrade
    AZURE_STEEL_UPGRADE("upgrade", "azure_steel_upgrade"),
    AZURE_STEEL_UPGRADE_APPLIES_TO("upgrade", "azure_steel_upgrade", "applies_to"),
    AZURE_STEEL_UPGRADE_INGREDIENTS("upgrade", "azure_steel_upgrade", "ingredients"),
    AZURE_STEEL_UPGRADE_BASE_SLOT_DESCRIPTION("upgrade", "azure_steel_upgrade", "base_slot_description"),
    AZURE_STEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION("upgrade", "azure_steel_upgrade", "additions_slot_description"),

    // Deep Steel Upgrade
    DEEP_STEEL_UPGRADE("upgrade", "deep_steel_upgrade"),
    DEEP_STEEL_UPGRADE_APPLIES_TO("upgrade", "deep_steel_upgrade", "applies_to"),
    DEEP_STEEL_UPGRADE_INGREDIENTS("upgrade", "deep_steel_upgrade", "ingredients"),
    DEEP_STEEL_UPGRADE_BASE_SLOT_DESCRIPTION("upgrade", "deep_steel_upgrade", "base_slot_description"),
    DEEP_STEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION("upgrade", "deep_steel_upgrade", "additions_slot_description"),

    // Item Name
    ITEM_POTION("item", RagiumAPI.MOD_ID, "potion", "effect"),

    // Item Description
    TOOLTIP_EFFECT_RANGE("tooltip", RagiumAPI.MOD_ID, "effect_range"),
    TOOLTIP_ENERGY_PERCENTAGE("tooltip", RagiumAPI.MOD_ID, "energy_percentage"),
    TOOLTIP_FLUID_NAME("tooltip", RagiumAPI.MOD_ID, "fluid_name"),
    TOOLTIP_FLUID_NAME_EMPTY("tooltip", RagiumAPI.MOD_ID, "fluid_name", "empty"),
    TOOLTIP_INTRINSIC_ENCHANTMENT("tooltip", RagiumAPI.MOD_ID, "intrinsic_enchantment"),
    TOOLTIP_LOOT_TABLE_ID("tooltip", RagiumAPI.MOD_ID, "loot_table_id"),
    TOOLTIP_MISSING_ANCHOR("tooltip", RagiumAPI.MOD_ID, "missing_anchor"),
    TOOLTIP_MISSING_POS("tooltip", RagiumAPI.MOD_ID, "missing_pos"),
    TOOLTIP_SHOW_INFO("tooltip", RagiumAPI.MOD_ID, "show_info"),
    TOOLTIP_WIP("tooltip", RagiumAPI.MOD_ID, "work_in_progress"),

    // Key Mapping
    KEY_CATEGORY("key", RagiumAPI.MOD_ID, "category"),

    // Recipe
    RECIPE_CUTTING("recipe_type", RagiumAPI.MOD_ID, "cutting"),

    // Jade
    JADE_OUTPUT_SIDE("tooltip", RagiumAPI.MOD_ID, "output_side"),
    ;

    override val translationKey: String = type + "." + path.joinToString(separator = ".")

    companion object {
        @JvmStatic
        private val tooltipCache: MutableMap<ItemStack, String> = ItemStackMap.createTypeAndTagMap<String>()

        /**
         * @see [ICustomTooltipName.getTranslationKey]
         */
        @JvmStatic
        fun getTooltipKey(stack: ItemStack): String =
            tooltipCache.computeIfAbsent(stack) { stack1: ItemStack -> "${stack1.descriptionId}.tooltip" }

        @JvmStatic
        fun getTooltipText(stack: ItemStack): Component? {
            val descKey: String = getTooltipKey(stack)
            if (!I18n.exists(descKey)) return null
            return Component.translatable(descKey).withStyle(ChatFormatting.GREEN)
        }
    }
}
