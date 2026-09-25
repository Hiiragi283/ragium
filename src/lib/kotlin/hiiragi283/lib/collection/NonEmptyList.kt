@file:Suppress("JavaDefaultMethodsNotOverriddenByDelegation")

package hiiragi283.lib.collection

/**
 * [NonEmptyList]を省略したエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.6
 */
typealias Nel<E> = NonEmptyList<E>

/**
 * 常に一つ以上の値を保持する[List]の実装クラスです。
 * @param E リストの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.6
 */
class NonEmptyList<out E> @PublishedApi internal constructor(val head: E, val tail: List<E>) : AbstractList<E>() {
    constructor(head: E) : this(head, listOf())

    val all: List<E> get() = this

    //    List    //

    override val size: Int get() = 1 + tail.size

    override fun get(index: Int): E = when (index) {
        0 -> head
        in 1 until size -> tail[index - 1]
        else -> throw IndexOutOfBoundsException("index: $index, size: $size")
    }

    //    Extensions    //

    fun first(): E = head

    fun firstOrNull(): E = head

    inline fun <R> map(transform: (E) -> R): Nel<R> = Nel(transform(this.head), this.tail.map(transform))

    inline fun <R> mapIndexed(transform: (index: Int, E) -> R): Nel<R> = this.all.mapIndexed(transform).toNel()

    fun distinct(): Nel<E> = this.toMutableSet().toNel()

    operator fun plus(element: @UnsafeVariance E): Nel<E> = Nel(this.head, this.tail + element)

    operator fun plus(elements: Array<out @UnsafeVariance E>): Nel<E> = Nel(this.head, this.tail + elements)

    operator fun plus(elements: Iterable<@UnsafeVariance E>): Nel<E> = Nel(this.head, this.tail + elements)

    operator fun plus(elements: Sequence<@UnsafeVariance E>): Nel<E> = Nel(this.head, this.tail + elements)
}
