package hiiragi283.lib.material.property

import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.property.HTPropertyKey
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.HTPart

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
data object HTMaterialPropertyKeys {
    /**
     * デフォルトの部品を管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val DEFAULT_PART: HTPropertyKey.Simple<HTDefaultPart> = HTPropertyKey.Simple(RagiumAPI.id("default_part"))

    @JvmField
    val ORIGIN_MOD_ID: HTPropertyKey.Simple<String> = HTPropertyKey.Simple(RagiumAPI.id("origin_mod_id"))

    /**
     * ブロックの必要素材数を管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val STORAGE_BLOCK: HTPropertyKey.Defaulted<HTStorageBlockProperty> = HTPropertyKey.Defaulted(RagiumAPI.id("storage_block"), HTStorageBlockProperty.THREE_BY_THREE)

    /**
     * 鉱石粉砕の主産物の個数の倍率を管理する[プロパティキー][HTPropertyKey]
     * @since 0.8.0
     */
    @JvmField
    val ORE_RESULT_MULTIPLIER: HTPropertyKey.Defaulted<Float> = HTPropertyKey.Defaulted(RagiumAPI.id("ore_result_multiplier"), 1f)

    //    Resource    //

    // Data Map
    /**
     * かまど燃料としての時間を管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val FUEL_TIME: HTPropertyKey.Simple<Int> = HTPropertyKey.Simple(RagiumAPI.id("fuel_time"))

    // Lang
    /**
     * 素材の[翻訳名][HTLangName]を管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val LANG_NAME: HTPropertyKey.Simple<HTLangName> = HTPropertyKey.Simple(RagiumAPI.id("lang_name"))

    /**
     * [部品][HTPart]に依存する[翻訳名][HTLangName]を管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val CUSTOM_LANG_NAME: HTPropertyKey.Defaulted<Map<HTPart, HTLangName>> = HTPropertyKey.Defaulted(RagiumAPI.id("custom_lang_name"), mapOf())

    // Texture
    @JvmField
    val COLOR: HTPropertyKey.Simple<Int> = HTPropertyKey.Simple(RagiumAPI.id("color"))
}
