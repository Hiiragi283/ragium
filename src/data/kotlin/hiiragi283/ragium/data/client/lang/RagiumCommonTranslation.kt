package hiiragi283.ragium.data.client.lang

import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.common.data.lang.HTMaterialTranslationHelper
import hiiragi283.ragium.common.item.HTFoodCanType
import hiiragi283.ragium.common.item.HTMoldType
import hiiragi283.ragium.common.upgrade.RagiumUpgradeType
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems

object RagiumCommonTranslation {
    @JvmStatic
    fun addTranslations(provider: HTLangProvider) {
        val langType: HTLanguageType = provider.langType

        // Material
        HTMaterialTranslationHelper.translateAll(provider, RagiumBlocks.MATERIALS::column)
        HTMaterialTranslationHelper.translateAll(provider, RagiumItems.MATERIALS::column)
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
