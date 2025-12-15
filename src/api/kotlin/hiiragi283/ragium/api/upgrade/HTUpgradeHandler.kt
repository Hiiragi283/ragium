package hiiragi283.ragium.api.upgrade

import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.stack.ImmutableItemStack
import org.apache.commons.lang3.math.Fraction
import java.util.function.UnaryOperator

/**
 * アップグレードを保持するインターフェース
 */
interface HTUpgradeHandler {
    fun getUpgrades(): List<ImmutableItemStack>

    fun isValidUpgrade(upgrade: ImmutableItemStack, existing: List<ImmutableItemStack>): Boolean

    //    Extensions    //

    fun getMaxMultiplier(key: HTUpgradeKey): Fraction? = getUpgrades().mapNotNull { HTUpgradeHelper.getUpgrade(it, key) }.maxOrNull()

    fun hasUpgrade(key: HTUpgradeKey): Boolean {
        for (stack: ImmutableItemStack in getUpgrades()) {
            if (HTUpgradeHelper.getUpgrade(stack, key) != null) {
                return true
            }
        }
        return false
    }

    fun collectMultiplier(key: HTUpgradeKey, ignoreEmpty: Boolean = false): Fraction {
        var isEmpty = true
        var sum: Fraction = Fraction.ONE
        for (stack: ImmutableItemStack in getUpgrades()) {
            val fraction: Fraction = HTUpgradeHelper.getUpgrade(stack, key) ?: continue
            sum *= fraction
            isEmpty = false
        }
        return when {
            isEmpty && ignoreEmpty -> Fraction.ZERO
            else -> sum
        }
    }

    fun modifyValue(key: HTUpgradeKey, ignoreEmpty: Boolean = false, operator: UnaryOperator<Fraction>): Int =
        collectMultiplier(key, ignoreEmpty).let(operator::apply).toInt()

    fun getBaseMultiplier(): Fraction = getMaxMultiplier(HTUpgradeKeys.BASE_MULTIPLIER) ?: Fraction.ONE

    fun isCreative(): Boolean = hasUpgrade(HTUpgradeKeys.IS_CREATIVE)
}
