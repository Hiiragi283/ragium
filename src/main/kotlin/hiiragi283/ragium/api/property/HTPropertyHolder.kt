package hiiragi283.ragium.api.property

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.getOrNull
import hiiragi283.ragium.api.extension.orElse

/**
 * さまざまな型の値を保持するインターフェース
 *
 * シリアライズ不可能な[net.minecraft.core.component.DataComponentHolder]
 *
 * @see [HTMutablePropertyHolder]
 */
interface HTPropertyHolder {
    fun <T : Any> getResult(key: HTPropertyKey<T>): DataResult<T>
}

/**
 * 指定された[key]から[T]を返します。
 * @return [key]に紐づいた値がない場合は`null`
 */
operator fun <T : Any> HTPropertyHolder.get(key: HTPropertyKey<T>): T? = getResult(key).getOrNull()

/**
 * 指定された[key]から[T]を返します。
 * @return [key]に紐づいた値がない場合は[HTPropertyKey.getDefaultValue]
 */
fun <T : Any> HTPropertyHolder.getOrDefault(key: HTPropertyKey<T>): T = getResult(key).orElse(key.getDefaultValue())

/**
 * 指定された[key]から[T]を返します。
 * @throws IllegalStateException [key]に紐づいた値がない場合
 */
fun <T : Any> HTPropertyHolder.getOrThrow(key: HTPropertyKey<T>): T = getResult(key).orThrow

/**
 * 指定された[key]が含まれているか判定します。
 */
operator fun <T : Any> HTPropertyHolder.contains(key: HTPropertyKey<T>): Boolean = getResult(key).isSuccess

/**
 * 指定された[key]に紐づいた値を[transform]で変換します。
 * @return [key]に紐づいた値がない場合は`null`
 */
fun <T : Any, R : Any> HTPropertyHolder.map(key: HTPropertyKey<T>, transform: (T) -> R): R? = get(key)?.let(transform)

/**
 * 指定された[key]に紐づいた値を[action]に渡します。
 * @return [key]に紐づいた値がない場合は実行されない
 */
fun <T : Any> HTPropertyHolder.ifPresent(key: HTPropertyKey<T>, action: (T) -> Unit) {
    map(key, action)
}
