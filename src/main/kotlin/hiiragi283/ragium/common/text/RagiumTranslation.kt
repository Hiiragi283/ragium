package hiiragi283.ragium.common.text

import hiiragi283.core.api.text.HTTranslation
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.Util

enum class RagiumTranslation(type: String, vararg path: String) : HTTranslation {
    // Constants
    RAGIUM("constants", "mod_name"),

    // Blocks - Storages
    BATTERY("description", "battery"),
    CRATE("description", "crate"),
    TANK("description", "tank"),
    BUFFER("description", "buffer"),
    UNIVERSAL_CHEST("description", "universal_chest"),

    // Items - Utilities
    SLOT_COVER("description", "slot_cover"),
    TRADER_CATALOG("description", "trader_catalog"),

    // Tooltips
    TOOLTIP_BLOCK_POS("tooltip", "block_pos"),
    TOOLTIP_CHARGE_POWER("tooltip", "blast_power"),
    TOOLTIP_DIMENSION("tooltip", "dimension"),
    TOOLTIP_LOOT_TABLE_ID("tooltip", "loot_table_id"),
    ;

    override val translationKey: String = Util.makeDescriptionId(type, RagiumAPI.id(path.joinToString(separator = ".")))
}
