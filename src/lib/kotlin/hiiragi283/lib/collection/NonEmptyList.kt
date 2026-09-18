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
class NonEmptyList<out E> @PublishedApi internal constructor(val head: E, val tail: List<E>) : List<E> {
    constructor(head: E) : this(head, listOf())

    val all: List<E> get() = listOf(head) + tail

    override fun equals(other: Any?): Boolean = when (other) {
        is Nel<*> -> this.all == other.all
        else -> this.all == other
    }

    override fun hashCode(): Int = all.hashCode()

    override fun toString(): String = all.toString()

    //    List    //

    override val size: Int get() = all.size

    override fun isEmpty(): Boolean = false

    override fun contains(element: @UnsafeVariance E): Boolean = all.contains(element)

    override fun iterator(): Iterator<E> = all.iterator()

    override fun containsAll(elements: Collection<@UnsafeVariance E>): Boolean = all.containsAll(elements)

    override fun get(index: Int): E = all[index]

    override fun indexOf(element: @UnsafeVariance E): Int = all.indexOf(element)

    override fun lastIndexOf(element: @UnsafeVariance E): Int = all.lastIndexOf(element)

    override fun listIterator(): ListIterator<E> = all.listIterator()

    override fun listIterator(index: Int): ListIterator<E> = all.listIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int): List<E> = all.subList(fromIndex, toIndex)

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
