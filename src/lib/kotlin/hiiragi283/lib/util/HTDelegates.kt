package hiiragi283.lib.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Ragiumで使用される委譲プロパティをまとめたクラスです。
 *
 * 参照 : [Kotlin - Delegates][kotlin.properties.Delegates]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTDelegates {
    /**
     * 一度だけ値を代入可能なプロパティを作成します。
     * @param T 値のクラス
     */
    fun <T : Any> onceInitialize(): ReadWriteProperty<Any?, T> = OnceInitialize()

    private class OnceInitialize<T : Any> : ReadWriteProperty<Any?, T> {
        private var value: T? = null

        override fun getValue(thisRef: Any?, property: KProperty<*>): T = value ?: error("Property ${property.name} has not initialized")

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            check(this.value == null) { "Property ${property.name} has already initialized" }
            this.value = value
        }
    }

    /**
     * 一度だけ値を代入可能なプロパティを返します。
     * @param defaultValue 値が一度も代入されていない場合の値を提供するブロック
     */
    fun <T : Any> onceInitialize(defaultValue: () -> T): ReadWriteProperty<Any?, T> = OnceInitializeOr(defaultValue)

    private class OnceInitializeOr<T : Any>(private val defaultValue: () -> T) : ReadWriteProperty<Any?, T> {
        private var value: T? = null

        override fun getValue(thisRef: Any?, property: KProperty<*>): T = value ?: defaultValue()

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            check(this.value == null) { "Property ${property.name} has already initialized" }
            this.value = value
        }
    }

    /**
     * 一度だけ値を代入可能なプロパティを返します。
     */
    fun <T : Any> optionalOnceInitialize(): ReadWriteProperty<Any?, Option<T>> = OptionalOnceInitialize()

    private class OptionalOnceInitialize<T : Any> : ReadWriteProperty<Any?, Option<T>> {
        private var value: Option<T> = Option.none()
        private var initialized: Boolean = false

        override fun getValue(thisRef: Any?, property: KProperty<*>): Option<T> = value

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Option<T>) {
            check(!initialized) { "Property ${property.name} has already initialized" }
            this.value = value
        }
    }
}
