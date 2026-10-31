package hiiragi283.lib.transfer

import net.neoforged.neoforge.transfer.resource.Resource

/**
 * スタックに基づいた[HTResourceSlot]の拡張インターフェースです。
 * @param S リソースと量を束ねたクラス
 * @param T 保持するリソースのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
interface HTStackSlot<S : Any, T : Resource> : HTResourceSlot<T> {
    /**
     * 保持しているスタックのコピーを取得します。
     */
    fun getStackCopy(): S

    /**
     * 保持しているスタックを置き換えます。
     */
    fun setStack(stack: S)
}
