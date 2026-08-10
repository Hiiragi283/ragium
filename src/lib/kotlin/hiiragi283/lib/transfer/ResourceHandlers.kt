@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.transfer

import com.google.common.primitives.Ints
import hiiragi283.lib.math.fixedFraction
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.neoforged.neoforge.transfer.RangedResourceHandler
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.ResourceHandlerUtil
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.Transaction
import net.neoforged.neoforge.transfer.transaction.TransactionContext
import org.apache.commons.lang3.math.Fraction

/**
 * この[ResourceHandler][this]の有効なインデックスの範囲を返します。
 *
 * 参照 : [Kotlin - Collection.indices][Collection.indices]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val ResourceHandler<*>.indices: IntRange get() = (0..<size())

/**
 * この[ResourceHandler][this]が空かどうか判定します。
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
val ResourceHandler<out Resource>.isEmpty: Boolean get() = ResourceHandlerUtil.isEmpty(this)

/**
 * この[ResourceHandler][this]の空き容量を取得します。
 * @param index スロットのインデックス
 * @param resource 容量の対象となるリソース
 * @return [Long]型での空き容量の値
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun <T : Resource> ResourceHandler<T>.getNeededAsLong(index: Int, resource: T = getResource(index)): Long = maxOf(0, getCapacityAsLong(index, resource) - getAmountAsLong(index))

/**
 * この[ResourceHandler][this]の空き容量を取得します。
 * @param index スロットのインデックス
 * @param resource 容量の対象となるリソース
 * @return [Int]型での空き容量の値
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun <T : Resource> ResourceHandler<T>.getNeededAsInt(index: Int, resource: T = getResource(index)): Int = Ints.saturatedCast(this.getNeededAsLong(index, resource))

/**
 * この[ResourceHandler][this]の充填率を取得します。
 * @param index スロットのインデックス
 * @param resource 容量の対象となるリソース
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun <T : Resource> ResourceHandler<T>.getFilledLevel(index: Int, resource: T = getResource(index)): Fraction = fixedFraction(getAmountAsLong(index), getCapacityAsLong(index, resource))

/**
 * この[ResourceHandler][this]からリソースを搬出します。
 * @param index スロットのインデックス
 * @param amount 搬出する量
 * @param transaction 現在のトランザクション
 * @return 搬出される量
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun <T : Resource> ResourceHandler<T>.extractSelf(index: Int, transaction: TransactionContext, amount: Int = this.getAmountAsInt(index)): Int = this.extract(index, this.getResource(index), amount, transaction)

// Ranged
/**
 * この[ResourceHandler][this]をスロットが制限された[ResourceHandler]に変換します。
 * @param start スロットのインデックスの範囲の下限，含まれる
 * @param end スロットのインデックスの範囲の上限，含まれない
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun <T : Resource> ResourceHandler<T>.ranged(start: Int, end: Int): ResourceHandler<T> = RangedResourceHandler.of(this, start, end)

/**
 * この[ResourceHandler][this]をスロットが制限された[ResourceHandler]に変換します。
 * @param range スロットのインデックスの範囲，上限が含まれる
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
infix fun <T : Resource> ResourceHandler<T>.ranged(range: IntRange): ResourceHandler<T> = this.ranged(range.first, range.endInclusive)

//    Transaction    //

/**
 * [Transaction]を安全に使用します。
 * @param T 戻り値のクラス
 * @param parent 現在開いている親のトランザクション
 * @param action 現在のトランザクションを使用するブロック
 * @return [action]の戻り値
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <T> useTransaction(parent: TransactionContext? = null, action: (Transaction) -> T): T {
    contract {
        callsInPlace(action, InvocationKind.EXACTLY_ONCE)
    }
    return Transaction.open(parent).use(action)
}
