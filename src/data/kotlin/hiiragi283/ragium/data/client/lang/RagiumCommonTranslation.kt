package hiiragi283.ragium.data.client.lang

import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.data.lang.HTLangType
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.ragium.common.item.HTFoodCanType
import hiiragi283.ragium.common.item.HTMoldType
import hiiragi283.ragium.common.upgrade.RagiumUpgradeType
import hiiragi283.ragium.setup.RagiumItems

object RagiumCommonTranslation {
    @JvmStatic
    fun addTranslations(provider: HTLangProvider) {
        val langType: HTLangType = provider.langType
        // Food Can
        for ((canType: HTFoodCanType, item: HTHasTranslationKey) in RagiumItems.FOOD_CANS) {
            provider.add(item, HTLangPatternProvider.create("%s Paste Can", "%sのペースト缶詰").translate(langType, canType))
        }
        // Mold
        for ((moldType: HTMoldType, item: HTHasTranslationKey) in RagiumItems.MOLDS) {
            provider.add(item, HTLangPatternProvider.create("%s Mold", "%sの鋳型").translate(langType, moldType))
        }
        // Upgrade
        for ((upgradeType: RagiumUpgradeType, item: HTHasTranslationKey) in RagiumItems.UPGRADES) {
            provider.add(item, HTLangPatternProvider.create("%s Upgrade", "%sアップグレード").translate(langType, upgradeType))
        }
    }
}
