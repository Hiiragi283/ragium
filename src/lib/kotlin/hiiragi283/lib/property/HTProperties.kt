@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.property

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

//    HTPropertyGetter    //

/**
 * 指定した[key]に紐づいた値を返します。
 * @throws IllegalStateException 値がない場合
 */
fun <T : Any> HTPropertyGetter.getOrThrow(key: HTPropertyKey<T>): T = get(key) ?: error("Unbounded property: ${key.getId()}")

/**
 * 指定した[key]に紐づいた値を返します。
 * @return 値がない場合は[デフォルト値][HTPropertyKey.Defaulted.getDefaultOrNull]
 */
fun <T : Any> HTPropertyGetter?.getOrDefault(key: HTPropertyKey.Defaulted<T>): T = this?.get(key) ?: key.getDefaultOrNull()

//    Extensions    //

inline fun buildPropertyMap(builderAction: HTPropertyMap.Mutable.() -> Unit): HTPropertyMap {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return HTPropertyMap.Mutable().apply(builderAction).toImmutable()
}

/**
 * @since 0.9.0
 */
fun HTPropertyMap.Mutable.add(key: HTPropertyKey<Unit>) {
    this.put(key, Unit)
}

/**
 * @since 0.9.0
 */
operator fun HTPropertyMap.Mutable.plusAssign(key: HTPropertyKey<Unit>) {
    this.add(key)
}

/**
 * @see MutableMap.computeIfAbsent
 */
inline fun <T : Any> HTPropertyMap.Mutable.computeIfAbsent(key: HTPropertyKey<T>, mapping: () -> T): T {
    val oldValue: T? = get(key) ?: key.getDefaultOrNull()
    if (oldValue == null) {
        val newValue: T = mapping()
        put(key, newValue)
        return newValue
    } else {
        return oldValue
    }
}
