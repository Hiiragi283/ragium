package hiiragi283.lib.transfer

import net.neoforged.neoforge.transfer.resource.Resource

/**
 * スタックに基づいた[HTResourceSlot]の抽象クラスです。
 * @param S リソースと量を束ねたクラス
 * @param T 保持するリソースのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTStackResourceSlot<S : Any, T : Resource> : HTResourceSlot<T> {
    /**
     * 保持しているスタックのコピーを取得します。
     */
    fun getStackCopy(): S

    /**
     * 保持しているスタックを置き換えます。
     */
    fun setStack(stack: S)

    /**
     * スタックをリソースに変換します。
     */
    fun getResourceFrom(stack: S): T

    /**
     * スタックから数量を取得します。
     */
    fun getAmountFrom(stack: S): Int

    /**
     * 指定したスタックとリソースが等しいかどうか判定します。
     */
    fun isSame(stack: S, resource: T): Boolean

    /**
     * リソースと数量からスタックを作成します。
     */
    fun createStack(resource: T, amount: Int): S
}
