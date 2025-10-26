package hiiragi283.ragium.api.variant

import hiiragi283.ragium.api.data.lang.HTTranslationProvider

/**
 * さまざまな要素のキーとなるインターフェース
 */
interface HTVariantKey : HTTranslationProvider {
    fun variantName(): String
}
