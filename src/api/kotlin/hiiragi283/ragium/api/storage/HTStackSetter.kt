package hiiragi283.ragium.api.storage

import hiiragi283.ragium.api.stack.ImmutableStack

/**
 * 指定された[STACK]を受け取る関数型インターフェース
 * @param STACK 受け取るスタックのクラス
 */
fun interface HTStackSetter<STACK : ImmutableStack<*, STACK>> {
    fun setStack(stack: STACK?)
}
