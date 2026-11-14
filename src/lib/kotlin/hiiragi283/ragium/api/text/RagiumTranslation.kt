package hiiragi283.ragium.api.text

import hiiragi283.lib.HTConstants
import hiiragi283.lib.text.HTTranslation
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants

/**
 * Ragiumで使用される[翻訳][HTTranslation]を集めたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
enum class RagiumTranslation(type: String, vararg path: String) : HTTranslation {
    // Constants
    RAGIUM(HTConstants.CONSTANTS, "name"),

    // Config
    CONFIG_ENERGY_CAPACITY("config", "energy_capacity"),
    CONFIG_ENERGY_RATE("config", "energy_rate"),

    // Recipe Category
    RECIPE_CATEGORY_ASSEMBLING(HTConstants.RECIPE_CATEGORY, RagiumConstants.ASSEMBLING),
    RECIPE_CATEGORY_CENTRIFUGING(HTConstants.RECIPE_CATEGORY, RagiumConstants.CENTRIFUGING),
    RECIPE_CATEGORY_COMPRESSING(HTConstants.RECIPE_CATEGORY, RagiumConstants.COMPRESSING),
    RECIPE_CATEGORY_CRUSHING(HTConstants.RECIPE_CATEGORY, RagiumConstants.CRUSHING),
    RECIPE_CATEGORY_CUTTING(HTConstants.RECIPE_CATEGORY, RagiumConstants.CUTTING),
    RECIPE_CATEGORY_EXTRACTING(HTConstants.RECIPE_CATEGORY, RagiumConstants.EXTRACTING),
    RECIPE_CATEGORY_FILLING(HTConstants.RECIPE_CATEGORY, RagiumConstants.FILLING),
    RECIPE_CATEGORY_PRESSING(HTConstants.RECIPE_CATEGORY, RagiumConstants.PRESSING),

    RECIPE_CATEGORY_ALLOYING(HTConstants.RECIPE_CATEGORY, RagiumConstants.ALLOYING),
    RECIPE_CATEGORY_DRYING(HTConstants.RECIPE_CATEGORY, RagiumConstants.DRYING),
    RECIPE_CATEGORY_FREEZING(HTConstants.RECIPE_CATEGORY, RagiumConstants.FREEZING),
    RECIPE_CATEGORY_MELTING(HTConstants.RECIPE_CATEGORY, RagiumConstants.MELTING),
    RECIPE_CATEGORY_PYROLYZING(HTConstants.RECIPE_CATEGORY, RagiumConstants.PYROLYZING),
    RECIPE_CATEGORY_REFINING(HTConstants.RECIPE_CATEGORY, RagiumConstants.REFINING),

    RECIPE_CATEGORY_AUTOCLAVING(HTConstants.RECIPE_CATEGORY, RagiumConstants.AUTOCLAVING),
    RECIPE_CATEGORY_BATHING(HTConstants.RECIPE_CATEGORY, RagiumConstants.BATHING),
    RECIPE_CATEGORY_MIXING(HTConstants.RECIPE_CATEGORY, RagiumConstants.MIXING),
    RECIPE_CATEGORY_FLUID_REACTING(HTConstants.RECIPE_CATEGORY, RagiumConstants.FLUID_REACTING),
    RECIPE_CATEGORY_ITEM_REACTING(HTConstants.RECIPE_CATEGORY, RagiumConstants.ITEM_REACTING),

    RECIPE_CATEGORY_BREWING(HTConstants.RECIPE_CATEGORY, RagiumConstants.BREWING),
    RECIPE_CATEGORY_PLANTING(HTConstants.RECIPE_CATEGORY, RagiumConstants.PLANTING),
    RECIPE_CATEGORY_RESOURCE_PUMPING(HTConstants.RECIPE_CATEGORY, RagiumConstants.RESOURCE_PUMPING),

    RECIPE_CATEGORY_ELECTROLYZING(HTConstants.RECIPE_CATEGORY, RagiumConstants.ELECTROLYZING),
    RECIPE_CATEGORY_ENGRAVING(HTConstants.RECIPE_CATEGORY, RagiumConstants.ENGRAVING),
    RECIPE_CATEGORY_PRINTING(HTConstants.RECIPE_CATEGORY, RagiumConstants.PRINTING),

    RECIPE_CATEGORY_ENCHANTING(HTConstants.RECIPE_CATEGORY, RagiumConstants.ENCHANTING),
    RECIPE_CATEGORY_TRANSFORMING(HTConstants.RECIPE_CATEGORY, RagiumConstants.TRANSFORMING),

    // Tooltips
    TOOLTIPS_SULFUR(HTConstants.TOOLTIP, "sulfur"),
    TOOLTIPS_CINNABAR(HTConstants.TOOLTIP, "cinnabar"),
    TOOLTIPS_APATITE(HTConstants.TOOLTIP, "apatite"),
    TOOLTIPS_CARBON(HTConstants.TOOLTIP, "carbon"),
    TOOLTIPS_FLUORITE(HTConstants.TOOLTIP, "fluorite"),

    TOOLTIPS_MEMORY_DISC_DATA(HTConstants.TOOLTIP, "memory_disc_data"),
    TOOLTIPS_UPGRADE_SPEED(HTConstants.TOOLTIP, HTConstants.UPGRADE, HTConstants.SPEED),
    TOOLTIPS_UPGRADE_EFFICIENCY(HTConstants.TOOLTIP, HTConstants.UPGRADE, HTConstants.EFFICIENCY),
    TOOLTIPS_UPGRADE_CAPACITY(HTConstants.TOOLTIP, HTConstants.UPGRADE, HTConstants.CAPACITY),
    ;

    override val translationKey: String = RagiumAPI.id(path.joinToString(separator = ".", transform = { it.replace('/', '.') })).toLanguageKey(type)
}
