@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.transfer

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.transaction.Transaction
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * この[ResourceHandler][this]の有効なインデックスの範囲を返します。
 *
 * 参照 : [Kotlin - Collection.indices][Collection.indices]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val ResourceHandler<*>.indices: IntRange get() = (0..<size())

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
