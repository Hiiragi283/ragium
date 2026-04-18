package hiiragi283.ragium.data.lang

import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.data.lang.HTLangType
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.ragium.common.item.HTFoodCanType
import hiiragi283.ragium.setup.RagiumItems
import kotlin.collections.iterator

internal object RagiumCommonTranslation {
    @JvmStatic
    fun addTranslations(provider: HTLangProvider) {
        val langType: HTLangType = provider.langType
        // Food Can
        for ((canType: HTFoodCanType, item: HTHasTranslationKey) in RagiumItems.FOOD_CANS) {
            provider.add(item, HTLangPatternProvider.create("%s Paste Can", "%sのペースト缶詰").translate(langType, canType))
        }
    }
}
