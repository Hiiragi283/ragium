package hiiragi283.ragium.api.upgrade

import org.apache.commons.lang3.math.Fraction

/**
 * アップグレードを提供するインターフェース
 */
fun interface HTUpgradeProvider {
    /**
     * 指定された引数から，アップグレードの倍率を返します。
     * @param key 対象の倍率に紐づいた[HTUpgradeKey]
     * @return [key]に紐づいたアップグレードの倍率
     */
    fun getUpgradeData(key: HTUpgradeKey): Fraction
}
