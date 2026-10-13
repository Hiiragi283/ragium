package hiiragi283.lib.transfer

import net.neoforged.neoforge.common.util.ValueIOSerializable
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * 外部との搬入/搬出が可能な[HTResourceView]の拡張インターフェースです。
 *
 * 参照 : [Mekanism - IResourceContainer](https://github.com/mekanism/Mekanism/blob/26.3/src/api/java/mekanism/api/resource/IResourceContainer.java)
 * @param T 保持するリソースのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTResourceSlot<T : Resource> :
    HTResourceView<T>,
    ValueIOSerializable {
    /**
     * 指定したリソースを保持できるかどうか判定します。
     */
    fun isValid(resource: T): Boolean

    /**
     * リソースを搬入します。
     * @param resource 搬入するリソース
     * @param amount 搬入する数量
     * @param transaction 現在のトランザクション
     * @param access このスロットに対するアクセスの種類
     * @return 搬入される数量
     */
    fun insert(resource: T, amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int

    /**
     * リソースを搬出します。
     * @param resource 搬出するリソース
     * @param amount 搬出する数量
     * @param transaction 現在のトランザクション
     * @param access このスロットに対するアクセスの種類
     * @return 搬出される数量
     */
    fun extract(resource: T, amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int

    fun extractSelf(amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int {
        val resourceIn: T = this.resource
        return when {
            resourceIn.isEmpty -> 0
            else -> extract(resourceIn, amount, transaction, access)
        }
    }
}
