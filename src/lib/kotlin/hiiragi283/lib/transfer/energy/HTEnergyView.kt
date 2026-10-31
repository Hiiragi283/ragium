package hiiragi283.lib.transfer.energy

import hiiragi283.lib.util.fixedFraction

/**
 * エネルギーを保持するスロットを表すインターフェースです。
 *
 * 参照 : [Mekanism - IEnergyContainer](https://github.com/mekanism/Mekanism/blob/26.3/src/api/java/mekanism/api/energy/IEnergyContainer.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
interface HTEnergyView {
    /**
     * 保持しているエネルギー量
     */
    val amount: Int

    /**
     * エネルギー容量
     */
    val capacity: Int

    /**
     * このビューが空かどうか判定します。
     */
    val isEmpty: Boolean get() = amount <= 0

    /**
     * 空き容量を取得します。
     */
    val needed: Int get() = amount - capacity

    /**
     * 占有率を取得します。
     */
    val filledLevel: Float get() = fixedFraction(amount, capacity)
}
