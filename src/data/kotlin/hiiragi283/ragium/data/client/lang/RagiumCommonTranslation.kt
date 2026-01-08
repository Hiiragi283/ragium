package hiiragi283.ragium.data.client.lang

import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.material.HTMaterialDefinition
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.attribute.HTLangNameMaterialAttribute
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.common.data.lang.HTMaterialTranslationHelper
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.ragium.common.item.HTMoldType
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.upgrade.RagiumUpgradeType
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems

object RagiumCommonTranslation {
    @JvmStatic
    fun addTranslations(provider: HTLangProvider) {
        val langType: HTLanguageType = provider.langType

        // Material
        for ((key: HTMaterialKey, definition: HTMaterialDefinition) in HTMaterialManager.INSTANCE.entries) {
            // Block
            for ((prefix: HTMaterialPrefix, block: HTHasTranslationKey) in RagiumBlocks.MATERIALS.column(key)) {
                val name: String = HTMaterialTranslationHelper.translate(langType, prefix, key, definition) { _, _ -> null }
                    ?: continue
                provider.add(block, name)
            }
            // Item
            for ((prefix: HTMaterialPrefix, item: HTHasTranslationKey) in RagiumItems.MATERIALS.column(key)) {
                val name: String =
                    HTMaterialTranslationHelper.translate(
                        langType,
                        prefix,
                        key,
                        definition,
                    ) { prefixIn: HTMaterialPrefix, keyIn: HTMaterialKey ->
                        if (prefixIn == HCMaterialPrefixes.DUST && keyIn == RagiumMaterialKeys.MEAT) {
                            HTLangNameMaterialAttribute("Minced Meat", "ひき肉")
                        } else {
                            null
                        }
                    }
                        ?: continue
                provider.add(item, name)
            }
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
