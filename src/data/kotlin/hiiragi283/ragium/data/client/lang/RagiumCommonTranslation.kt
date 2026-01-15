package hiiragi283.ragium.data.client.lang

import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.HTMaterialTable
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.common.data.lang.HTMaterialTranslationHelper
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.ragium.common.item.HTFoodCanType
import hiiragi283.ragium.common.item.HTMoldType
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.upgrade.RagiumUpgradeType
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems

object RagiumCommonTranslation {
    @JvmStatic
    val MATERIAL_MAP: HTMaterialTable<HTMaterialPrefix, HTLangName> = buildTable {
        fun register(
            prefix: HTPrefixLike,
            material: HTMaterialLike,
            enName: String,
            jaName: String,
        ) {
            this[prefix.asMaterialPrefix(), material.asMaterialKey()] = HTLangName { type: HTLanguageType ->
                when (type) {
                    HTLanguageType.EN_US -> enName
                    HTLanguageType.JA_JP -> jaName
                }
            }
        }

        register(HCMaterialPrefixes.DUST, RagiumMaterialKeys.MEAT, "Minced Meat", "ひき肉")
    }.let(::HTMaterialTable)

    @JvmStatic
    fun addTranslations(provider: HTLangProvider) {
        val langType: HTLanguageType = provider.langType

        // Material
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in HTMaterialManager.INSTANCE.entries) {
            // Block
            for ((prefix: HTMaterialPrefix, block: HTHasTranslationKey) in RagiumBlocks.MATERIALS.column(key)) {
                val name: String = HTMaterialTranslationHelper.translate(langType, prefix, key, propertyMap, MATERIAL_MAP::get)
                    ?: continue
                provider.add(block, name)
            }
            // Item
            for ((prefix: HTMaterialPrefix, item: HTHasTranslationKey) in RagiumItems.MATERIALS.column(key)) {
                val name: String = HTMaterialTranslationHelper.translate(langType, prefix, key, propertyMap, MATERIAL_MAP::get)
                    ?: continue
                provider.add(item, name)
            }
        }

        // Food Can
        for ((canType: HTFoodCanType, item: HTHasTranslationKey) in RagiumItems.FOOD_CANS) {
            provider.add(item, LangPattern("%s Paste Can", "%sのペースト缶詰").translate(langType, canType))
        }
        // Mold
        for ((moldType: HTMoldType, item: HTHasTranslationKey) in RagiumItems.MOLDS) {
            provider.add(item, LangPattern("%s Mold", "%sの鋳型").translate(langType, moldType))
        }
        // Upgrade
        for ((upgradeType: RagiumUpgradeType, item: HTHasTranslationKey) in RagiumItems.UPGRADES) {
            provider.add(item, LangPattern("%s Upgrade", "%sアップグレード").translate(langType, upgradeType))
        }
    }

    @JvmRecord
    private data class LangPattern(private val enPattern: String, private val jaPattern: String) : HTLangPatternProvider {
        override fun translate(type: HTLanguageType, value: String): String = when (type) {
            HTLanguageType.EN_US -> enPattern
            HTLanguageType.JA_JP -> jaPattern
        }.replace("%s", value)
    }
}
