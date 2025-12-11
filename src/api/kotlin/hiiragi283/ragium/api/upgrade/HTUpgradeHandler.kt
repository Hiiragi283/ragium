package hiiragi283.ragium.api.upgrade

import hiiragi283.ragium.api.math.fraction
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

    fun getMaxMultiplier(key: HTUpgradeKey): Fraction? = getUpgrades().maxOfOrNull { HTUpgradeHelper.getUpgrade(it, key) }

    fun hasUpgrade(key: HTUpgradeKey): Boolean {
        for (stack: ImmutableItemStack in getUpgrades()) {
            val fraction: Fraction = HTUpgradeHelper.getUpgrade(stack, key)
            if (fraction > Fraction.ZERO) {
                return true
            }
        }
        return false
    }

    fun collectMultiplier(key: HTUpgradeKey, base: Fraction = fraction(1)): Fraction {
        var isEmpty = true
        var sum: Fraction = base
        for (stack: ImmutableItemStack in getUpgrades()) {
            val fraction: Fraction = HTUpgradeHelper.getUpgrade(stack, key)
            if (fraction > Fraction.ZERO) {
                sum *= fraction
                isEmpty = false
            }
        }
        return when (isEmpty) {
            true -> Fraction.ZERO
            false -> sum
        }
    }

    fun modifyValue(key: HTUpgradeKey, base: Fraction = fraction(1), operator: UnaryOperator<Fraction>): Int =
        collectMultiplier(key, base).let(operator::apply).toInt()

    fun getBaseMultiplier(): Fraction = getMaxMultiplier(RagiumUpgradeKeys.BASE_MULTIPLIER) ?: Fraction.ONE

    fun isCreative(): Boolean = hasUpgrade(RagiumUpgradeKeys.IS_CREATIVE)
}
