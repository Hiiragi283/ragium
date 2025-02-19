package hiiragi283.ragium.api.capability

/**
 * 値を保持するだけのインターフェース
 * @param T 保持する値のクラス
 * @see [HTHandlerSerializer]
 */
interface HTSlotHandler<T : Any> {
    var stack: T
}
