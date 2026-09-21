package hiiragi283.lib.recipe.handler

import hiiragi283.lib.recipe.ingredient.HTIngredient
import net.minecraft.core.TypedInstance
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * レシピの入力となるスロットを表すクラスです。
 *
 * 参考 : [Mekanism - IInputHandler](https://github.com/mekanism/Mekanism/blob/26.3/src/api/java/mekanism/api/recipes/inputs/IInputHandler.java)
 * @param TYPE 材料の種類のクラス
 * @param INSTANCE 材料の種類を保持するインスタンスのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
interface HTInputSlot<TYPE : Any, INSTANCE : TypedInstance<TYPE>> {
    /**
     * 現在保持している入力を取得します。
     */
    fun getStoredInput(): INSTANCE

    /**
     * 指定した入力が空かどうか判定します。
     * @return 空の場合は`true`
     */
    fun isEmpty(instance: INSTANCE): Boolean

    /**
     * 消費する入力を取得します。
     * @param ingredient 判定に用いる材料
     * @return 消費する入力
     */
    fun getRequiredInput(ingredient: HTIngredient<TYPE, INSTANCE>): INSTANCE

    /**
     * 入力を消費します。
     * @param input 消費する入力
     * @param transaction 現在のトランザクション
     */
    fun use(input: INSTANCE?, transaction: TransactionContext): UseResult

    /**
     * [HTInputSlot.use]の結果を表すクラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.7
     */
    enum class UseResult {
        /**
         * 消費する入力が空の場合（触媒など）
         */
        EMPTY,

        /**
         * 入力を消費できない場合
         */
        NOT_CONSUMED,

        /**
         * 入力を消費できる場合
         */
        CONSUMED
        ;

        /**
         * 入力の消費に成功した場合は`true`
         */
        val succeeded: Boolean get() = !failed

        /**
         * 入力の消費に失敗した場合は`true`
         */
        val failed: Boolean get() = this == NOT_CONSUMED
    }
}
