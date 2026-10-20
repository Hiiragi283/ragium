package hiiragi283.lib.recipe.handler.output

import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * レシピの出力となるスロットを表すクラスです。
 * @param STACK 搬入するスタックのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTOutputSlot<STACK : Any> {
    /**
     * 出力を受け取ります。
     * @param stack 受け取る出力
     * @param transaction 現在のトランザクション
     */
    fun take(stack: STACK, transaction: TransactionContext): TakeResult

    /**
     * すべての出力を受け取ります。
     * @param stacks 受け取る出力の一覧
     * @param transaction 現在のトランザクション
     * @since 26.1.11
     */
    fun takeAll(stacks: List<STACK>, transaction: TransactionContext): TakeResult = when (stacks.size) {
        0 -> TakeResult.NONE
        1 -> this.take(stacks.first(), transaction)
        else -> if (stacks.all { this.take(it, transaction).fullOrNoneTaken }) TakeResult.FULL else TakeResult.PARTIALLY
    }

    //    TakeResult    //

    /**
     * [HTOutputSlot.take]の結果を表すクラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.7
     */
    enum class TakeResult {
        /**
         * 出力を受け取れない場合
         */
        NONE,

        /**
         * 出力を一部だけ受け取れる場合
         */
        PARTIALLY,

        /**
         * 出力をすべて受け取れる場合
         */
        FULL;

        /**
         * 出力をすべて受け取れた場合は`true`
         */
        val fullOrNoneTaken: Boolean get() = this == NONE || this == FULL
    }
}
