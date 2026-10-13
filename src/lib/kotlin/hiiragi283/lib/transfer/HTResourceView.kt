package hiiragi283.lib.transfer

import hiiragi283.lib.util.fixedFraction
import net.neoforged.neoforge.transfer.resource.Resource

/**
 * 1種類のリソースを保持するスロットを表すインターフェースです。
 *
 * 参照 : [Mekanism - IResourceContainer](https://github.com/mekanism/Mekanism/blob/26.3/src/api/java/mekanism/api/resource/IResourceContainer.java)
 * @param T 保持するリソースのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTResourceView<T : Resource> {
    /**
     * 保持しているリソース
     */
    val resource: T

    /**
     * 保持しているリソースの数量
     */
    val amount: Int

    /**
     * このビューが空かどうか判定します。
     */
    val isEmpty: Boolean get() = resource.isEmpty || amount <= 0

    /**
     * 指定したリソースに対する容量を取得します。
     */
    fun getCapacity(resource: T): Int

    /**
     * 指定したリソースに対する空き容量を取得します。
     */
    fun getNeeded(resource: T): Int = getCapacity(resource) - amount

    /**
     * 指定したリソースに対する占有率を取得します。
     */
    fun getFilledLevel(resource: T): Float = fixedFraction(amount, getCapacity(resource))

    /**
     * 保持しているリソースに対する容量を取得します。
     */
    val currentCapacity: Int get() = getCapacity(this.resource)

    /**
     * 保持しているリソースに対する空き容量を取得します。
     */
    val currentNeeded: Int get() = getNeeded(this.resource)

    /**
     * 保持しているリソースに対する占有率を取得します。
     */
    val currentFilledLevel: Float get() = getFilledLevel(this.resource)
}
