package hiiragi283.lib.recipe.handler

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
         * 出力の受け取りに失敗した場合は`true`
         */
        val noneTaken: Boolean get() = this == NONE

        /**
         * 出力の受け取りに成功した場合は`true`
         */
        val anyTaken: Boolean get() = !noneTaken
    }
}
