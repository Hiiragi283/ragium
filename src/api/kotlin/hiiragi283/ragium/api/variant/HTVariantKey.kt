package hiiragi283.ragium.api.variant

import hiiragi283.ragium.api.data.lang.HTLangPatternProvider

/**
 * さまざまな要素のキーとなるインターフェース
 */
interface HTVariantKey : HTLangPatternProvider {
    fun variantName(): String
}
