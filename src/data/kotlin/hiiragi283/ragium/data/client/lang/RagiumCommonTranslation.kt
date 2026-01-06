package hiiragi283.ragium.data.client.lang

import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.common.data.lang.HTMaterialTranslationHelper
import hiiragi283.ragium.common.item.HTMoldType
import hiiragi283.ragium.common.material.RagiumMaterial
import hiiragi283.ragium.common.upgrade.RagiumUpgradeType
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems

object RagiumCommonTranslation {
    @JvmStatic
    fun addTranslations(provider: HTLangProvider) {
        val langType: HTLanguageType = provider.langType

        // Material
        for (material: RagiumMaterial in RagiumMaterial.entries) {
            // Block
            for ((prefix: HTMaterialPrefix, block: HTHasTranslationKey) in RagiumBlocks.MATERIALS.column(material)) {
                val name: String = HTMaterialTranslationHelper.translate(langType, prefix, material) ?: continue
                provider.add(block, name)
            }
            // Item
            for ((prefix: HTMaterialPrefix, item: HTHasTranslationKey) in RagiumItems.MATERIALS.column(material)) {
                val name: String = HTMaterialTranslationHelper.translate(langType, prefix, material) ?: continue
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
